package co.dobot.magicain.client;

import android.bluetooth.BluetoothDevice;

/**
 * Created by x on 2018/1/15.
 */

public interface ClientCallback {
    interface SearchDeviceCallback extends ClientCallback{
        void didFindAvailableDevice(BluetoothDevice device);
        void FindDeviceTimeout();
    }
    interface DeviceConnectCallback extends ClientCallback{
        void didConnect();
        void didDisConnect();
    }
}
