package co.dobot.magicain.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by x on 2018/5/15.
 */

public class TransformUtils {
    public static byte[] intToBytes(int src) {
        return new byte[]{(byte) (src & 0xFF),
                (byte) ((src >> 8) & 0xFF),
                (byte) ((src >> 16) & 0xFF),
                (byte) ((src >> 24) & 0xFF)

        };
    }

    public static int bytesToInt(byte[] b) {
        return b[0] & 0xFF |
                (b[1] & 0xFF) << 8 |
                (b[2] & 0xFF) << 16 |
                (b[3] & 0xFF) << 24;
    }

    /**
     * 浮点转换为字节
     *
     * @param f
     * @return
     */
    public static byte[] floatToBytes(float f) {

        // 把float转换为byte[]
        int fbit = Float.floatToIntBits(f);

        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (fbit >> (24 - i * 8));
        }

        // 翻转数组
        int len = b.length;
        // 建立一个与源数组元素类型相同的数组
        byte[] dest = new byte[len];
        // 为了防止修改源数组，将源数组拷贝一份副本
        System.arraycopy(b, 0, dest, 0, len);
        byte temp;
        // 将顺位第i个与倒数第i个交换
        for (int i = 0; i < len / 2; ++i) {
            temp = dest[i];
            dest[i] = dest[len - i - 1];
            dest[len - i - 1] = temp;
        }

        return dest;

    }

    /**
     * 字节转换为浮点
     *
     * @param b     字节（至少4个字节）
     * @param index 开始位置
     * @return
     */
    public static float bytesToFloat(byte[] b, int index) {
        int l;
        l = b[index + 0];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }

    /**
     *  bytes数组转字符串
     * @param bytes
     * @return
     */

    public static String bytesToString(byte[] bytes) {
        String strValue = new String(bytes);
        return strValue;
    }

    /**
     * 字符串转bytes数组
     *
     * @param strValue
     * @return
     */
    public static byte[] stringToBytes(String strValue) {
        if (strValue == null || strValue.length() == 0)
            return new byte[0];
        else {
            byte[] bytes=strValue.getBytes();
            return bytes;
        }
    }

    public static byte[] invertBytes(byte[] bytes){
        if (bytes==null)
            return bytes;
        byte[] tempBytes=new byte[bytes.length];
        for (int i=0;i<tempBytes.length;i++){
            tempBytes[i]=bytes[tempBytes.length-i-1];
        }
        return tempBytes;
    }
}
