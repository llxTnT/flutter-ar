package co.dobot.magicain.client;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import co.dobot.magicain.base.BLEKits;
import co.dobot.magicain.base.DeviceCallback;
import co.dobot.magicain.message.base.MessageCallback;
import co.dobot.magicain.message.base.BaseMessage;

/**
 * Created by x on 2018/5/14.
 */

public abstract class BaseMessageClient implements MessageClientInterface{
    private static final String TAG = "BaseMessageClient";
    private BLEKits bleKits;
    private boolean isConnect = false;
    private boolean needAutoReconnect=false;
    private String targetAddress;
    private ScanType scanType = ScanType.FIND_AND_CONNECT_FIRST;



    private List<ClientCallback.SearchDeviceCallback> searchCallbackList = new ArrayList<>();
    private List<ClientCallback.DeviceConnectCallback> connectCallbackList = new ArrayList<>();
    public List<MessageCallback> sendMessageCallbackList = new ArrayList<>();

    private enum CallbackType {
        DEVICE_FIND_TIMEOUT, DEVICE_CONNECT_TRUE, DEVICE_CONNECT_FALSE
    }
    public enum ScanType {
        FIND_ALL,FIND_AND_CONNECT_FIRST
    }

    private DeviceCallback deviceCallback = new DeviceCallback() {
        @Override
        public void onDeviceConnect(boolean isConnect) {
            BaseMessageClient.this.isConnect = isConnect;
            if (!isConnect) {
                if (needAutoReconnect) {
                    //     Log.i(TAG,"try auto connect");
                    stopFindDevice();
                    bleKits.clear();
                    startFindDevice(30);
                }
            } else {
                stopFindDevice();
            }
            if (connectCallbackList.size() > 0)
                notifyCallback(isConnect ? CallbackType.DEVICE_CONNECT_TRUE :CallbackType.DEVICE_CONNECT_FALSE);

        }

        @Override
        public void onFindDevice(BluetoothDevice device) {
            if (needAutoReconnect)
            {
                if (targetAddress!=null&&targetAddress.equals(device.getAddress())) {
                    connectDeviceWithAddress(targetAddress);
                    stopFindDevice();
                }
                return;
            }
            switch (scanType) {
                case FIND_ALL:
                    for (ClientCallback.SearchDeviceCallback callback : searchCallbackList)
                        callback.didFindAvailableDevice(device);
                    break;
                case FIND_AND_CONNECT_FIRST:
                    connectDevice(device);
                    stopFindDevice();
                    break;
            }

        }

        @Override
        public void onFindEnd() {
            if (!isConnect)
                notifyCallback(CallbackType.DEVICE_FIND_TIMEOUT);
        }

        /**
         * @param data
         */
        @Override
        public synchronized void onMessageRead(byte[] data) {
            handleReceiveMsg(data);
        }
    };

    public void setScanType(ScanType connectType) {
        this.scanType = connectType;
    }

    public void createBLEKits(Context context,String SERVICE_UUID,String READ_UUID,String WRITE_UUID) {
        if (bleKits == null)
            bleKits = new BLEKits.Builder(context)
                    .serviceID(SERVICE_UUID)
                    .readID(READ_UUID)
                    .writeID(WRITE_UUID)
                    .setCallback(deviceCallback)
                    .build();
    }

    public void startFindDevice(int timeout) throws NullPointerException {
        bleKits.startScanDevice(timeout);
    }

    public boolean connectDevice(BluetoothDevice device) {
        targetAddress = device.getAddress();
        return bleKits.connect(device);
    }

    public boolean connectDeviceWithAddress(String address) {
        targetAddress = address;
        return bleKits.connect(address);
    }

    public void disconnect() {
        bleKits.disconnect();
    }

    public boolean isConnect() {
        return isConnect;
    }

    public abstract  void sendMessage(BaseMessage sendMessage);

    public abstract void handleReceiveMsg(byte [] data);

    protected boolean writeDataToBleKit(byte[] temp)
    {
        return bleKits.write2Device(temp);
    }

    public void addConnectCallback(ClientCallback.DeviceConnectCallback callback) {
        if (callback != null)
            connectCallbackList.add(callback);
    }

    public void clearConnectCallbackList() {
        connectCallbackList.clear();
    }

    public void removeConnectCallback(ClientCallback.DeviceConnectCallback callback) {
        if (callback != null)
            connectCallbackList.remove(callback);
    }

    public void addSearchDeviceCallback(ClientCallback.SearchDeviceCallback callback) {
        if (callback != null)
            searchCallbackList.add(callback);
    }

    public void clearSearchDeviceCallbackList() {
        searchCallbackList.clear();
    }

    public void removeSearchDeviceCallback(ClientCallback.SearchDeviceCallback callback) {
        if (callback != null)
            searchCallbackList.remove(callback);
    }

    public void addMessageCallback(MessageCallback callback) {
        if (callback != null)
            sendMessageCallbackList.add(callback);
    }

    public void removeMessageCallback(MessageCallback callback) {
        if (callback != null)
            sendMessageCallbackList.remove(callback);
    }

    public void clearMessageCallbackList() {
        sendMessageCallbackList.clear();
    }


    public void clear() {
        scanType = ScanType.FIND_AND_CONNECT_FIRST;
        targetAddress = null;
    }


    public void setFilterName(String filterName) {
        bleKits.setFilterName(filterName);
    }



    private void stopFindDevice() {
        bleKits.stopFindDevice();
    }

    private void notifyCallback(CallbackType type) {
        switch (type) {
            case DEVICE_CONNECT_TRUE:
                for (ClientCallback.DeviceConnectCallback callback : connectCallbackList)
                    callback.didConnect();
                break;
            case DEVICE_CONNECT_FALSE:
                for (ClientCallback.DeviceConnectCallback callback : connectCallbackList)
                    callback.didDisConnect();
                break;
            case DEVICE_FIND_TIMEOUT:
                for (ClientCallback.SearchDeviceCallback callback : searchCallbackList)
                    callback.FindDeviceTimeout();
        }
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

