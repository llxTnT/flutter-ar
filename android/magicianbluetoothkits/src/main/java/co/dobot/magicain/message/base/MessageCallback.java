package co.dobot.magicain.message.base;


import co.dobot.magicain.message.base.BaseMessage;

/**
 * Created by x on 2018/1/22.
 */

public interface MessageCallback {
    enum MsgState{
        MSG_REPLY,MSG_TIMEOUT,MSG_REFUSE;
    }
    void onMsgReply(MsgState state, BaseMessage msg);
}
