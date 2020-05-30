package co.dobot.magicain.client;

import android.content.Context;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import co.dobot.magicain.message.DobotMessage;
import co.dobot.magicain.message.base.MessageCallback;
import co.dobot.magicain.message.base.BaseMessage;
import co.dobot.magicain.message.cmd.CMDParams;
import co.dobot.magicain.message.cmd.CommandID;
import co.dobot.magicain.utils.TransformUtils;


/**
 * Created by x on 2018/1/12.
 */

public class DobotMessageClient extends BaseMessageClient {

    private static final String SERVICE_UUID = "0003cdd0-0000-1000-8000-00805f9b0131";
    private static final String READ_UUID = "0003cdd1-0000-1000-8000-00805f9b0131";
    private static final String WRITE_UUID = "0003cdd2-0000-1000-8000-00805f9b0131";
    private static final String TAG = "DobotMessageClient";
    private static final int MAX_BYTES_LENGTH = 20;
    private static DobotMessageClient Instance;
    private boolean isAllowSendMsg = true;
    private int queueLeftSpace = 0;
    private boolean isNeedResend = false;
    private volatile List<BaseMessage> addMsgLinkedListH = new LinkedList<>();
    private volatile List<BaseMessage> addMsgLinkedListM = new LinkedList<>();
    private volatile List<BaseMessage> addMsgLinkedListL = new LinkedList<>();
    private volatile Queue<BaseMessage> writeMsgQueue = new LinkedBlockingQueue<>();
    private Queue<BaseMessage> receiveMsgQueue = new LinkedBlockingQueue<>();
    private int writeQueueSizeLimit = 1;
    private volatile byte[] cache;
    private int delayTime = 20;
    private ExecutorService sendExecutor = Executors.newSingleThreadExecutor();
    private ScheduledExecutorService writeExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledExecutorService timeExecutor = Executors.newScheduledThreadPool(writeQueueSizeLimit + 1);

    private SendRunnable sendRunnable = new SendRunnable();
    private WriteRunnable writeRunnable = new WriteRunnable();
    private volatile boolean doSend = false;

    private Queue<byte[]> splitMessage = new LinkedBlockingQueue<>();


    private MessageCallback mMessageCallback = new MessageCallback() {
        @Override
        public void onMsgReply(MsgState state, BaseMessage msg) {
            DobotMessage dobotMessage = (DobotMessage) msg;
            if (dobotMessage.getCommandID() == CommandID.CMD_QUEUE_GET_LEFT_SPACE) {
                queueLeftSpace = TransformUtils.bytesToInt(dobotMessage.getParams());
            }
        }
    };

    public synchronized static DobotMessageClient Instance() {
        if (Instance == null)
            Instance = new DobotMessageClient();
        return Instance;
    }


    public DobotMessageClient() {
        addMessageCallback(mMessageCallback);
    }

    public void createBLEKits(Context context) {
        createBLEKits(context, SERVICE_UUID, READ_UUID, WRITE_UUID);
        setFilterName("USR-BLE100");
    }

    public void pause() {
        isAllowSendMsg = false;
    }

    public void start() {
        isAllowSendMsg = true;
        sendMessage(null);
    }

    private List<BaseMessage> getSendListByPriority(BaseMessage.MsgPriority msgPriority){
        List <BaseMessage>temp=null;
        switch (msgPriority)
        {
            case PRIORITY_HIGH:
                temp=addMsgLinkedListH;
                break;
            case PRIORITY_DEFAULT:
                temp=addMsgLinkedListM;
                break;
            case PRIORITY_LOW:
                temp=addMsgLinkedListL;
                break;
        }
        return temp;
    }

    private void refuseSendMessage(BaseMessage sendMessage){
        if (sendMessage.getCallback() != null)
            sendMessage.getCallback().onMsgReply(MessageCallback.MsgState.MSG_REFUSE, null);
        if (sendMessageCallbackList.size() > 0) {
            for (MessageCallback callback : sendMessageCallbackList)
                callback.onMsgReply(MessageCallback.MsgState.MSG_REFUSE, sendMessage);
        }
    }
    private void insertMsg(BaseMessage sendMessage) {
        if (sendMessage != null) {
            DobotMessage message = (DobotMessage) sendMessage;
            if (!message.isQueue())
                message.setPriority(BaseMessage.MsgPriority.PRIORITY_HIGH);

            switch (sendMessage.getPriority()) {
                case PRIORITY_HIGH:
                    addMsgLinkedListH.add(0, sendMessage);
                    break;
                case PRIORITY_DEFAULT:
                    addMsgLinkedListM.add(0, sendMessage);
                    break;
                case PRIORITY_LOW:
                    addMsgLinkedListL.add(0, sendMessage);
                    break;
            }
        }
    }

    private void appendMsg(BaseMessage sendMessage) {
        if (sendMessage != null) {
            DobotMessage message = (DobotMessage) sendMessage;
            if (!message.isQueue())
                message.setPriority(BaseMessage.MsgPriority.PRIORITY_HIGH);
            if (message.getCommandID() == CommandID.CMD_QUEUE_GET_LEFT_SPACE) {
                boolean hasCmdCurrentIndex = false;
                List <BaseMessage>temp=getSendListByPriority(message.getPriority());
                for (BaseMessage tempMsg : temp) {
                    DobotMessage tempDobotMsg = (DobotMessage) tempMsg;
                    if (tempDobotMsg.getCommandID() == message.getCommandID()) {
                        hasCmdCurrentIndex = true;
                    }
                    if (hasCmdCurrentIndex) {
                        refuseSendMessage(sendMessage);
                        return;
                    }
                }
            }
            switch (sendMessage.getPriority()) {
                case PRIORITY_HIGH:
                    addMsgLinkedListH.add(sendMessage);
                    break;
                case PRIORITY_DEFAULT:
                    addMsgLinkedListM.add(sendMessage);
                    break;
                case PRIORITY_LOW:
                    addMsgLinkedListL.add(sendMessage);
                    break;
            }
        }
    }

    @Override
    public synchronized void sendMessage(BaseMessage sendMessage) {
        if (!isConnect() && sendMessage != null) {
            refuseSendMessage(sendMessage);
            return;
        }
        appendMsg(sendMessage);
        if (!doSend)
            sendExecutor.execute(sendRunnable);
    }


    @Override
    public void clear() {
        super.clear();
        addMsgLinkedListH.clear();
        addMsgLinkedListM.clear();
        addMsgLinkedListL.clear();
        writeMsgQueue.clear();
        receiveMsgQueue.clear();

    }


    @Override
    public void handleReceiveMsg(byte[] data) {
        if (cache == null) {
            cache = data;
        } else {
            byte[] temp = new byte[data.length + cache.length];
            System.arraycopy(cache, 0, temp, 0, cache.length);
            System.arraycopy(data, 0, temp, cache.length, data.length);
            cache = temp;
        }
        {
            int i = 0;
            while (i < cache.length - DobotMessage.HEADER_LENGTH) {
                BaseMessage.CheckStatus status = DobotMessage.checkSync(cache, i);
                if (!status.isValid) {
                    i += status.skipCount;
                    continue;
                }
                int msgLength = DobotMessage.getLength(cache, i);
                //Log.i(TAG, "msglength:" + msgLength);
                if (msgLength > cache.length - i)
                    break;
                status = DobotMessage.checkIsValid(cache, i);
                if (!status.isValid) {
                    i += status.skipCount;
                    continue;
                }
                byte[] temp = new byte[msgLength];
                System.arraycopy(cache, i, temp, 0, msgLength);
                BaseMessage msg = new DobotMessage(temp);
                if (msg != null)
                    receiveMsgQueue.add(msg);
                i += msgLength;
            }
            if (cache.length - i > 0) {
                byte[] temp = new byte[cache.length - i];
                System.arraycopy(cache, i, temp, 0, temp.length);
                cache = temp;
            } else
                cache = null;

        }
        checkReceiveMsg();
    }

    private synchronized void checkReceiveMsg() {
        while (receiveMsgQueue.size() > 0) {
            DobotMessage msg = (DobotMessage) receiveMsgQueue.element();
            /*Log.i(TAG, "commandID:" + msg.getCommandID());*/
            synchronized (writeMsgQueue) {
                for (BaseMessage bm : writeMsgQueue) {
                    DobotMessage m = (DobotMessage) bm;
                    if (m.getCommandID() == msg.getCommandID()) {
                        if (m.getCallback() != null)
                            m.getCallback().onMsgReply(MessageCallback.MsgState.MSG_REPLY, msg);
                        writeMsgQueue.remove(m);
                        break;
                    }
                }
            }
            if (sendMessageCallbackList.size() > 0) {
                for (MessageCallback callback : sendMessageCallbackList)
                    callback.onMsgReply(MessageCallback.MsgState.MSG_REPLY, msg);
            }
            receiveMsgQueue.poll();
        }

        if (!doSend)
            sendExecutor.execute(sendRunnable);
    }


    private class SendRunnable implements Runnable {

        @Override
        public synchronized void run() {
            if (!doSend) {
                if (splitMessage.size() > 0) {
                    doSend = true;

                    //   Log.i(TAG,"3.send split Msg" );
                    writeExecutor.schedule(writeRunnable, delayTime, TimeUnit.MILLISECONDS);
                } else {
                    DobotMessage msg = null;
                    if (!isAllowSendMsg)
                        return;
                    if (writeMsgQueue.size() < writeQueueSizeLimit) {
                        if (addMsgLinkedListH.size() > 0) {
                            msg = (DobotMessage) addMsgLinkedListH.get(0);
                            addMsgLinkedListH.remove(0);
                        } else if (addMsgLinkedListM.size() > 0) {
                            msg = (DobotMessage) addMsgLinkedListM.get(0);
                            addMsgLinkedListM.remove(0);
                        } else if (addMsgLinkedListL.size() > 0) {
                            msg = (DobotMessage) addMsgLinkedListL.get(0);
                            addMsgLinkedListL.remove(0);
                        }
                    }

                    if (msg != null) {
                        if (msg.isQueue()) {
                            if (queueLeftSpace > 0) {
                                queueLeftSpace--;
                            } else {
                                insertMsg(msg);
                                msg = new DobotMessage();
                                msg.cmdQueueLeftSpace();
                            }
                        }
                        int index = 0;
                        byte[] msgData = msg.getData();
                        int totalSize = msgData.length;
                        while (totalSize - index > MAX_BYTES_LENGTH) {
                            byte[] temp = new byte[MAX_BYTES_LENGTH];
                            System.arraycopy(msgData, index, temp, 0, temp.length);
                            splitMessage.add(temp);
                            index += MAX_BYTES_LENGTH;
                        }
                        byte[] temp = new byte[totalSize - index];
                        System.arraycopy(msgData, index, temp, 0, temp.length);
                        splitMessage.add(temp);
                        if (msg.isNeedResponse()) {
                            AlarmRunnable runnable = new AlarmRunnable();
                            runnable.setMsg(msg);
                            timeExecutor.schedule(runnable, msg.getWaitTime(), TimeUnit.SECONDS);
                            writeMsgQueue.add(msg);
                        }
                        doSend = true;
                        writeExecutor.schedule(writeRunnable, 0, TimeUnit.MILLISECONDS);
                    }
                }
            }
        }
    }

    private class WriteRunnable implements Runnable {

        @Override
        public synchronized void run() {
            byte[] temp = splitMessage.element();
            // Log.i(TAG,"1.5.ble get temp Data" /*+ bytes2HexString(writeData)*/);
            if (temp.length > 0) {
                if (isConnect() && writeDataToBleKit(temp)) {
                    //    Log.i(TAG, "2.ble Write Data:" + bytes2HexString(temp));
                    splitMessage.poll();
                }/*else
                 Log.i(TAG,"fail"+ bytes2HexString(writeData));*/
            }
            doSend = false;
            sendExecutor.execute(sendRunnable);
        }
    }

    private class AlarmRunnable implements Runnable {
        private BaseMessage msg;

        public void setMsg(BaseMessage msg) {
            this.msg = msg;
        }

        @Override
        public synchronized void run() {
            //    Log.i(TAG,"check time out");
            if (writeMsgQueue.contains(msg)) {
                writeMsgQueue.remove(msg);
                if (isNeedResend()) {
                    DobotMessageClient.this.insertMsg(msg);
                    return;
                }
                if (msg.getCallback() != null)
                    msg.getCallback().onMsgReply(MessageCallback.MsgState.MSG_TIMEOUT, null);
                if (sendMessageCallbackList.size() > 0) {
                    for (MessageCallback callback : sendMessageCallbackList)
                        callback.onMsgReply(MessageCallback.MsgState.MSG_TIMEOUT, msg);
                }

                if (!doSend)
                    sendExecutor.execute(sendRunnable);
                //DobotMessageClient.this.sendMessage(null);
            }
        }
    }

    private boolean isNeedResend() {
        return isNeedResend;
    }

    public void setNeedResend(boolean isNeedResend) {
        this.isNeedResend = isNeedResend;
    }


    public static String bytes2HexString(byte[] bytes) {
        String ret = "";
        for (byte item : bytes) {
            String hex = Integer.toHexString(item & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase(Locale.CHINA);
        }
        return ret;
    }
}
