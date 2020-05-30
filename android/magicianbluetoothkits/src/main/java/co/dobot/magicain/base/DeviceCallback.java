package co.dobot.magicain.base;

import android.bluetooth.BluetoothDevice;

/**
 * Created by x on 2018/1/15.
 */

public interface DeviceCallback {
    void onDeviceConnect(boolean isConnect);
    void onFindDevice(BluetoothDevice device);
    void onFindEnd();
    void onMessageRead(byte[] data);
}
