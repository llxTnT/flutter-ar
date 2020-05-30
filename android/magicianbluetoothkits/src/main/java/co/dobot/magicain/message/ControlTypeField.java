package co.dobot.magicain.message;

import co.dobot.magicain.message.cmd.ControlType;
import co.dobot.magicain.utils.bitfield.ByteBitField;

/**
 * Created by x on 2018/5/17.
 */

public class ControlTypeField extends ByteBitField {
    byte rw = ControlType.READ_ONLY;
    byte queue = ControlType.NOT_QUEUE;
    public final static int[] CONTROL_TYPE_RULE = new int[]{0, 1, 2};

    public byte getRw() {
        return rw;
    }

    public void setRw(byte rw) {
        this.rw = rw;
        setValueArray(new Byte[]{rw, queue});
    }

    public byte getQueue() {
        return queue;
    }

    public void setQueue(byte queue) {
        this.queue = queue;
        setValueArray(new Byte[]{rw, queue});
    }

    public ControlTypeField() {
        super(CONTROL_TYPE_RULE);
        setValueArray(new Byte[]{rw, queue});
    }

    @Override
    public void setValue(Byte value) {
        super.setValue(value);
        Byte[] temp = getValueArray();
        rw = temp[0];
        queue = temp[1];
    }
}
