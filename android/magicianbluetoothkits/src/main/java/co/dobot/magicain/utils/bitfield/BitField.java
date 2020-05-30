package co.dobot.magicain.utils.bitfield;

/**
 * Created by x on 2018/4/16.
 */

public interface BitField<V> {
    void setValueArray(V value[]/*, int rule[]*/);
    void setValue(V value/*, int rule[]*/);
    V getValue();
    V[] getValueArray();
    int [] getRule();
}
