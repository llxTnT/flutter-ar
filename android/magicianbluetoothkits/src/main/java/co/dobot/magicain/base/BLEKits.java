package co.dobot.magicain.base;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.ParcelUuid;
import android.text.TextUtils;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static co.dobot.magicain.client.DobotMessageClient.bytes2HexString;

/**
 * Created by x on 2018/1/8.
 */

public class BLEKits {
    private static final String TAG = "BLEKits";

    private final Context context;

    private final String serviceUUID;
    private final String readUUID;
    private final String writeUUID;
    private String filterName = "";
    private int searchTimeOut = 5;
    private boolean isConnect = false;
    private List<BluetoothGattService> services;  // 获得设备所有的服务
    private List<BluetoothGattCharacteristic> characteristics = new ArrayList<>();
    private HashSet<BluetoothDevice> deviceList = new HashSet<>();

    private final BluetoothAdapter bleAdapter;
    private final BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (TextUtils.isEmpty(device.getName())) {
                return;
            }
            Log.i(TAG,"device name:"+device.getName());
            if (!device.getName().contains(filterName)) {
                return;
            }

           /* ParsedAd ad = parseData(scanRecord);
            if (ad.uuids != null) {
                boolean isDevice = false;
                for (UUID uuid : ad.uuids) {
                    Log.i(TAG,"get uuid:"+uuid.toString());
                    if (uuid.toString().equals(serviceUUID)) {
                        Log.i(TAG,"get equal uuid:"+uuid.toString());
                        isDevice = true;
                        break;
                    }
                }
                if (!isDevice)
                    return;
            } else {
                Log.i(TAG, "can't get uuid");
            }*/
            if (!deviceList.contains(device)) {
                deviceList.add(device);
                callback.onFindDevice(device);
            }
        }
    };

    private BluetoothGatt bleGatt;
    private BluetoothGattCallback bleGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothGatt.STATE_CONNECTED) {   // 连接成功
                gatt.discoverServices();
            } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {   // 连接失败
                Log.d(TAG, "connect fail");
                isConnect = false;
                close();
                callback.onDeviceConnect(false);
            }
        }


        // 发现设备的服务(Service)回调，需要在这里处理订阅事件。
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {  // 成功订阅
                services = gatt.getServices();  // 获得设备所有的服务
                characteristics.clear();
                int checkID = 0;
                // 依次遍历每个服务获得相应的Characteristic集合
                // 之后遍历Characteristic获得相应的Descriptor集合
                for (BluetoothGattService service : services) {
                    if (service.getUuid().toString().equals(serviceUUID)) {
                        for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                            characteristics.add(characteristic);
                            // 判断当前的Characteristic是否想要订阅的，这个要依据各自蓝牙模块的协议而定
                            if (characteristic.getUuid().toString().equals(readUUID)) {
                                checkID++;
                                // 依据协议订阅相关信息,否则接收不到数据
                                gatt.setCharacteristicNotification(characteristic, true);
                                gatt.readCharacteristic(characteristic);
                                for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
                                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                    gatt.writeDescriptor(descriptor);
                                }
                            }
                            if (characteristic.getUuid().toString().equals(writeUUID)) {
                                // 依据协议订阅相关信息,否则接收不到数据
                                checkID++;
                                //characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
                            }
                        }
                    }
                }
                if (checkID >= 2)
                    callback.onDeviceConnect(true);
                else {
                    disconnect();
                }
            } else {
                disconnect();
            }
        }

        // 发送消息结果回调
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic,
                                          int status) {

            if (BluetoothGatt.GATT_SUCCESS == status) {
                // Log.i(TAG,"ble write status:"+status+" characteristic:"+bytes2HexString(characteristic.getValue()));
                // 发送成功
                // Log.d(TAG, "send success");
            } else {   // 发送失败
                //        Log.d(TAG, "send fail");
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            //super.onCharacteristicRead(gatt, characteristic, status);
            //  Log.i(TAG,"on read:"+status+"  read value:"+characteristic.getValue());
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            //Log.i(TAG,"on read:"+Thread.currentThread().getName());
            byte[] data = characteristic.getValue();
           // Log.i(TAG,"gatt callback:"+this.toString()+" Value:"+bytes2HexString(data));
            callback.onMessageRead(data);
        }

    };

    private DeviceCallback callback;

    public static class Builder {
        private final Context context;

        private String serviceUUID;
        private String readUUID;
        private String writeUUID;

        private DeviceCallback deviceCallback;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder serviceID(String val) {
            serviceUUID = val;
            return this;
        }

        public Builder readID(String val) {
            readUUID = val;
            return this;
        }

        public Builder writeID(String val) {
            writeUUID = val;
            return this;
        }

        public Builder setCallback(DeviceCallback callback) {
            deviceCallback = callback;
            return this;
        }

        public BLEKits build() {
            return new BLEKits(this);
        }
    }

    private BLEKits(Builder builder) {
        this.context = builder.context;
        this.serviceUUID = builder.serviceUUID;
        this.readUUID = builder.readUUID;
        this.writeUUID = builder.writeUUID;
        this.callback = builder.deviceCallback;
        BluetoothManager manager = (BluetoothManager) this.context.getSystemService(Context.BLUETOOTH_SERVICE);
        bleAdapter = manager.getAdapter();
    }

    /*
     public method;
     */
    public void startScanDevice(int searchTimeOut) throws NullPointerException {
        if (leScanCallback == null)
            throw new NullPointerException("please set leScanCallback");
        if (TextUtils.isEmpty(serviceUUID))
            throw new NullPointerException("please set serviceUUID");
        if (check()) {
            clearList();
            bleAdapter.startLeScan(leScanCallback);
            this.searchTimeOut = searchTimeOut;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {  // 扫描一定时间"scanDelay"就停止。
                @Override
                public void run() {
                    stopScanDevice();
                }
            }, this.searchTimeOut * 1000);
        } else
            throw new NullPointerException("BleAdapter Can't Use");
    }

    private void stopScanDevice() throws NullPointerException {
        if (leScanCallback == null)
            throw new NullPointerException("please set leScanCallback");
        bleAdapter.stopLeScan(leScanCallback);
        if (deviceList.size() == 0)
            startDiscovery();
        else {
            clearList();
            callback.onFindEnd();
        }

    }
    public void stopFindDevice(){
        if (leScanCallback == null)
            throw new NullPointerException("please set leScanCallback");
        bleAdapter.stopLeScan(leScanCallback);
        if (bleAdapter.isDiscovering())
            bleAdapter.cancelDiscovery();
        clearList();
        callback.onFindEnd();
    }

    public void startDiscovery() {
        if (bleAdapter.isDiscovering())
            bleAdapter.cancelDiscovery();
        bleAdapter.startDiscovery();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(mReceiver, filter);
        filter = new IntentFilter(BluetoothDevice.ACTION_UUID);
        context.registerReceiver(mReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.registerReceiver(mReceiver, filter);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {  // 扫描一定时间"scanDelay"就停止。
            @Override
            public void run() {
                stopDiscovery();
            }
        }, this.searchTimeOut * 1000);
    }

    public void stopDiscovery() {
        if (bleAdapter.isDiscovering())
            bleAdapter.cancelDiscovery();
        try {
            context.unregisterReceiver(mReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        callback.onFindEnd();
    }

    public boolean connect(BluetoothDevice device) {
        if (device == null)
            return false;
        if (isConnect)
            return false;
        else
            isConnect = true;
        if (bleGattCallback == null)
            throw new NullPointerException("please set GattCallback");
        if (bleGatt != null)
            disconnect();
        // 连接设备，第二个参数为是否自动连接，第三个为回调函数
        bleGatt = device.connectGatt(context, false, bleGattCallback);
        return bleGatt.connect();
    }

    public boolean connect(String address) {
        Iterator<BluetoothDevice> iterator = deviceList.iterator();
        BluetoothDevice targetDevice = null;
        while (iterator.hasNext()) {
            BluetoothDevice device = iterator.next();
            if (device.getAddress().equals(address)) {
                targetDevice = device;
                break;
            }
        }
        return connect(targetDevice);
    }

    public void disconnect() {
        if (null != bleGatt) {
            bleGatt.disconnect();
        }
    }

    private void close() {
        if (null != bleGatt) {
            bleGatt.close();
            bleGatt = null;
        }

    }

    public HashSet<BluetoothDevice> getDeviceList() {
        return deviceList;
    }

    public boolean write2Device(byte[] data) {
        return writeCharacteristic(writeUUID, data, characteristics);
    }

    public void clear() {
        disconnect();
        bleGatt = null;
    }

    private boolean check() {
        return (null != bleAdapter && bleAdapter.isEnabled());
    }

    private boolean writeCharacteristic(String uuid, byte[] bytes, List<BluetoothGattCharacteristic> characteristics) {
        if (null == characteristics) {
            return false;
        }
        if (null != bleGatt) {
            // characteristics保存了蓝牙模块所有的特征Characteristic
            for (BluetoothGattCharacteristic characteristic : characteristics) {
                // 判断是否为协议约定的特征Characteristic
                if (TextUtils.equals(uuid, characteristic.getUuid().toString())) {
                    // 找到特征，设置要写入的数据
                    characteristic.setValue(bytes);
                    // 写入数据，蓝牙模块就接收到啦
                    characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
                    //    Log.i(TAG,"write bytes:"+bytes2HexString(bytes));
                    return bleGatt.writeCharacteristic(characteristic);
                }
            }
        }
        return false;
    }

    private void clearList() {
        Iterator<BluetoothDevice> iterator = this.deviceList.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public ParsedAd parseData(byte[] adv_data) {
        ParsedAd parsedAd = new ParsedAd();
        ByteBuffer buffer = ByteBuffer.wrap(adv_data).order(ByteOrder.LITTLE_ENDIAN);
        while (buffer.remaining() > 2) {
            byte length = buffer.get();
            if (length == 0)
                break;

            byte type = buffer.get();
            length -= 1;
            switch (type) {
                case 0x01: // Flags
                    parsedAd.flags = buffer.get();
                    length--;
                    break;
                case 0x02: // Partial list of 16-bit UUIDs
                case 0x03: // Complete list of 16-bit UUIDs
                case 0x14: // List of 16-bit Service Solicitation UUIDs
                    while (length >= 2) {
                        parsedAd.uuids.add(UUID.fromString(String.format(
                                "%08x-0000-1000-8000-00805f9b34fb", buffer.getShort())));
                        length -= 2;
                    }
                    break;
                case 0x04: // Partial list of 32 bit service UUIDs
                case 0x05: // Complete list of 32 bit service UUIDs
                    while (length >= 4) {
                        parsedAd.uuids.add(UUID.fromString(String.format(
                                "%08x-0000-1000-8000-00805f9b34fb", buffer.getInt())));
                        length -= 4;
                    }
                    break;
                case 0x06: // Partial list of 128-bit UUIDs
                case 0x07: // Complete list of 128-bit UUIDs
                case 0x15: // List of 128-bit Service Solicitation UUIDs
                    while (length >= 16) {
                        long lsb = buffer.getLong();
                        long msb = buffer.getLong();
                        parsedAd.uuids.add(new UUID(msb, lsb));
                        length -= 16;
                    }
                    break;
                case 0x08: // Short local device name
                case 0x09: // Complete local device name
                    byte sb[] = new byte[length];
                    buffer.get(sb, 0, length);
                    length = 0;
                    parsedAd.localName = new String(sb).trim();
                    break;
                case (byte) 0xFF: // Manufacturer Specific Data
                    parsedAd.manufacturer = buffer.getShort();
                    length -= 2;
                    break;
                default: // skip
                    break;
            }
            if (length > 0) {
                buffer.position(buffer.position() + length);
            }
        }
        return parsedAd;
    }

    private class ParsedAd {
        public byte flags;
        public List<UUID> uuids = new ArrayList<>();
        public String localName;
        public short manufacturer;

        public List<UUID> getUuids() {
            return uuids;
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    device.fetchUuidsWithSdp();
                    if (TextUtils.isEmpty(device.getName())) {
                        return;
                    }

                    if (!device.getName().contains(filterName)) {
                        return;
                    }
                    // ParsedAd ad = parseData(scanRecord);
                    if (device.getUuids() != null) {
                        //   Log.i(TAG, "has uuid");
                        boolean isDevice = false;
                        for (ParcelUuid uuid : device.getUuids()) {
                            if (uuid.toString().equals(serviceUUID)) {
                                //       Log.i(TAG, "get uuid:" + uuid.toString());
                                isDevice = true;
                                break;
                            }
                        }
                        if (!isDevice)
                            return;
                    } else {
                        Log.i(TAG, "can't get uuid");
                    }

                    if (!deviceList.contains(device)) {
                        deviceList.add(device);
                        callback.onFindDevice(device);
                    }
                }

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

            }
        }
    };
}
