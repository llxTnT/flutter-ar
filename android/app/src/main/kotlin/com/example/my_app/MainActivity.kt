package com.example.my_app

import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.NonNull
import co.dobot.magicain.client.ClientCallback
import co.dobot.magicain.client.DobotMessageClient
import co.dobot.magicain.message.DobotMessage
import co.dobot.magicain.message.base.BaseMessage
import co.dobot.magicain.message.base.MessageCallback
import co.dobot.magicain.message.cmd.CMDParams
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import org.json.JSONObject


class MainActivity : FlutterActivity() {

    private val CHANNEL = "magician/bkit"
    private var connectResult: MethodChannel.Result? = null
    private var searchResult: MethodChannel.Result? = null

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
//        initClient()
        MethodChannel(flutterEngine?.dartExecutor?.binaryMessenger, CHANNEL).setMethodCallHandler { methodCall, result ->
            handMethodCall(methodCall, result)
        }
    }

    private fun initClient() {
        DobotMessageClient.Instance().createBLEKits(this)
        DobotMessageClient.Instance().addConnectCallback(object : ClientCallback.DeviceConnectCallback {
            override fun didConnect() {
                connectResult?.success("true")
            }

            override fun didDisConnect() {
                connectResult?.success("false")
            }
        })
        DobotMessageClient.Instance().addSearchDeviceCallback(object : ClientCallback.SearchDeviceCallback {
            override fun didFindAvailableDevice(device: BluetoothDevice?) {
                searchResult?.success(device?.address)
            }

            override fun FindDeviceTimeout() {

            }
        })
    }

    private fun handMethodCall(methodCall: MethodCall, result: MethodChannel.Result) {
        if (methodCall.method == "initDevice") {
            initClient()
        } else if (methodCall.method == "searchDevice") {
            searchDevice(result)
        } else if (methodCall.method == "connectDevice") {
            val connectDevice = methodCall.arguments.toString()
            connectDevice(result, connectDevice)
        } else if (methodCall.method == "disConnectDevice") {
            disConnectDevice(result)
        } else if (methodCall.method == "getPose") {
            getPose(result)
        } else if (methodCall.method == "ptpCmd") {
            val map = methodCall.arguments as HashMap<String, Float>
            ptpCmd(map, result)
        }
    }

    private fun searchDevice(result: MethodChannel.Result) {
        searchResult = result
        DobotMessageClient.Instance().startFindDevice(10 * 1000)
    }

    private fun connectDevice(result: MethodChannel.Result, connectDevice: String) {
        connectResult = result
        DobotMessageClient.Instance().connectDeviceWithAddress(connectDevice)
    }

    private fun disConnectDevice(result: MethodChannel.Result) {
        DobotMessageClient.Instance().disconnect()
        result.success("true")
    }

    private fun getPose(result: MethodChannel.Result) {
        val poseMessage = DobotMessage()
        poseMessage.cmdGetPose()
        poseMessage.setCallback(object : MessageCallback {
            override fun onMsgReply(state: MessageCallback.MsgState, baseMessage: BaseMessage) {
                if (state === MessageCallback.MsgState.MSG_REPLY) {
                    val message = baseMessage as DobotMessage
                    val pose = CMDParams.Pose()
                    pose.initDataByBytes(message.getParams())
                    val jsonObject = JSONObject()
                    jsonObject.put("x", pose.jointAngle[0])
                    jsonObject.put("y", pose.jointAngle[1])
                    jsonObject.put("z", pose.jointAngle[2])
                    jsonObject.put("r", pose.jointAngle[3])
                    result.success(jsonObject.toString())
                }
            }
        })
        DobotMessageClient.Instance().sendMessage(poseMessage)
    }

    private fun ptpCmd(params: HashMap<String, Float>, result: MethodChannel.Result) {
        val dobotMessage = DobotMessage()
        val ptpCmd = CMDParams.PTPCmd()
        ptpCmd.ptpMode = CMDParams.PTPMode.JUMP
        ptpCmd.x = params["x"] as Float
        ptpCmd.y = params["y"] as Float
        ptpCmd.z = params["z"] as Float
        ptpCmd.r = params["r"] as Float
        dobotMessage.cmdPTP(ptpCmd)
        dobotMessage.callback = object : MessageCallback{
            override fun onMsgReply(msgState: MessageCallback.MsgState, baseMessage: BaseMessage) {
                if (msgState === MessageCallback.MsgState.MSG_REPLY) {
                    result.success("true")
                }else{
                    result.success("false")
                }
            }
        }
        DobotMessageClient.Instance().sendMessage(dobotMessage)
    }
}
