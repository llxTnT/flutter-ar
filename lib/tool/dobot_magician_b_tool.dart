//声明MethodChannel
import 'package:flutter/services.dart';

class MagicianConnectTool {
  MagicianConnectTool() {
    channel = MethodChannel('magician/bkit');
    doInitMagicianClient();
  }

  MethodChannel channel;

//处理按钮点击
  void doInitMagicianClient() async {
    String result;
    //异常捕获
    try {
      //异步等待方法通道的调用结果
      result = await channel.invokeMethod('initDevice');
    } catch (e) {
      result = "error";
    }
  }

//处理按钮点击
  Future<String> doSearchDevice() async {
    String result;
    //异常捕获
    try {
      //异步等待方法通道的调用结果
      result = await channel.invokeMethod('searchDevice');
    } catch (e) {
      result = "error";
    }
    return result;
  }

  Future<bool> doConnectDevice(String connectDevice) async {
    String result;
    //异常捕获
    try {
      //异步等待方法通道的调用结果
      result = await channel.invokeMethod('connectDevice', connectDevice);
    } catch (e) {
      result = "error";
    }
    return result == "true";
  }

  Future<bool> disConnectDevice() async {
    String result;
    //异常捕获
    try {
      //异步等待方法通道的调用结果
      result = await channel.invokeMethod('disConnectDevice');
    } catch (e) {
      result = "error";
    }
    return result == "true";
  }

  Future<String> getPose() async {
    String result;
    //异常捕获
    try {
      //异步等待方法通道的调用结果
      result = await channel.invokeMethod('getPose');
    } catch (e) {
      result = "error";
    }
    return result;
  }

  Future<String> ptpCmd(double x, double y, double z, double r) async {
    String result;

    //异常捕获
    try {
      //异步等待方法通道的调用结果
      result = await channel.invokeMethod('ptpCmd', {
        "x": x,
        "y": y,
        "z": z,
        "r": r,
      });
    } catch (e) {
      result = "error";
    }
    return result;
  }
}
