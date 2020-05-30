package co.dobot.magicain.message.cmd;

/**
 * Created by x on 2018/1/17.
 */

public interface ControlType {
    byte READ_ONLY=0x00;
    byte WRITE_ONLY=0x01;
    byte IS_QUEUE=0x01;
    byte NOT_QUEUE=0x00;
}
