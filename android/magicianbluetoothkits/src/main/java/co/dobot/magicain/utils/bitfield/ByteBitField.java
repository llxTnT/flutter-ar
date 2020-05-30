package co.dobot.magicain.utils.bitfield;

/**
 * Created by x on 2018/5/15.
 */

public class ByteBitField implements BitField<Byte> {
    private byte[] valueArray;

    private int[] rule;

    public ByteBitField(int[] rule) {
        this.rule = rule;
    }

    @Override
    public void setValueArray(Byte[] value) {
        valueArray = new byte[value.length];
        for (int i = 0; i < value.length; i++) {
            valueArray[i] = value[i];
        }
    }

    @Override
    public void setValue(Byte value) {
        this.valueArray=new byte[rule.length];
        for (int i=0;i<rule.length;i++){
            if (i+1<rule.length)
                valueArray[i]= (byte) (value>>rule[i]<<rule[i]<<(Byte.SIZE-rule[i+1])>>(Byte.SIZE-rule[i+1]));
            else
                valueArray[i]=(byte) (value>>rule[i]<<rule[i]);
        }
    }

    @Override
    public Byte getValue() {
        byte value=0;
        for (int i = 0; i < valueArray.length; i++) {
            value += (valueArray[i] << rule[i]);
        }
        return value;
    }

    @Override
    public Byte[] getValueArray() {
        Byte[] byteArray = null;
        if (valueArray != null) {
            byteArray = new Byte[valueArray.length];
            int i = 0;
            for (byte temp : valueArray) {
                byteArray[i] = new Byte(temp);
                i++;
            }
        }
        return byteArray;
    }

    @Override
    public int[] getRule() {
        return rule;
    }
}
