package co.dobot.magicain.message.base;


/**
 * Created by x on 2018/4/16.
 */

public class BaseMessage {
    private int waitTime = 3;
    private boolean needResponse = true;
    private MessageCallback callback;
    private MsgPriority priority = MsgPriority.PRIORITY_DEFAULT;
    public  byte[] getData(){
      return null;
    }
    public BaseMessage () {

    }
    public BaseMessage (byte[] data) {

    }
    public MessageCallback getCallback() {
        return callback;
    }

    public boolean isNeedResponse() {
        return needResponse;
    }

    public void setNeedResponse(boolean needResponse) {
        this.needResponse = needResponse;
    }


    public void setPriority(MsgPriority priority) {
        this.priority = priority;
    }

    public MsgPriority getPriority() {
        return priority;
    }

    public void setCallback(MessageCallback callback) {
        this.callback = callback;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public enum MsgPriority {
        PRIORITY_HIGH, PRIORITY_DEFAULT, PRIORITY_LOW;
    }

    public static class CheckStatus {
        public boolean isValid = false;
        public int skipCount = 0;
    }

}
