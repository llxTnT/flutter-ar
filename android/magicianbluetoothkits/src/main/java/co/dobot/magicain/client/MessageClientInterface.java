package co.dobot.magicain.client;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import co.dobot.magicain.message.base.MessageCallback;
import co.dobot.magicain.message.base.BaseMessage;


/**
 * Created by x on 2018/2/8.
 */

public interface MessageClientInterface {
    /**
     * 初始化蓝牙
     * @param context
     */
    void createBLEKits(Context context,String SERVICE_UUID,String READ_UUID,String WRITE_UUID);
    /**
     * 设置搜索方式
     * @param scanType
     */
     void setScanType(BaseMessageClient.ScanType scanType);
    /**
     * 搜索蓝牙设备
     * @param timeout
     */
    void startFindDevice(int timeout);

    /**
     * 连接设备
     * @param device
     * @return
     */
    boolean connectDevice(BluetoothDevice device);

    /**
     * 连接设备
     * @param address Mac地址
     * @return
     */
    boolean connectDeviceWithAddress(String address);

    /**
     * 断开连接
     */
    void disconnect();

    /**
     * 获取连接状态
     * @return
     */
    boolean isConnect();

    /**
     * 发送消息
     * @param sendMessage
     */
    void sendMessage(BaseMessage sendMessage);

    /**
     * 添加连接回调
     * @param callback
     */
    void addConnectCallback(ClientCallback.DeviceConnectCallback callback);

    /**
     * 清除连接回调
     */
    void clearConnectCallbackList();
    void removeConnectCallback(ClientCallback.DeviceConnectCallback callback);

    /**
     * 添加设备搜索回调
     * @param callback
     */
    void addSearchDeviceCallback(ClientCallback.SearchDeviceCallback callback);

    /**
     * 清除设备搜索回调
     */
    void clearSearchDeviceCallbackList();
    void removeSearchDeviceCallback(ClientCallback.SearchDeviceCallback callback);

    /**
     * 添加消息回调
     * @param callback
     */
    void addMessageCallback(MessageCallback callback);

    /**
     * 清除消息回调
     */
    void clearMessageCallbackList();
    void removeMessageCallback(MessageCallback callback);

    /**
     * clear缓存数据
     */
    void clear();

    /**
     * 设置蓝牙搜索过滤名称
     * @param filterName
     */
    void setFilterName(String filterName);

}
