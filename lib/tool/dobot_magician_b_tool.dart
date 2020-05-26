//声明MethodChannel
import 'package:flutter/services.dart';

class MagicianConnectTool {
  MagicianConnectTool() {
    channel = MethodChannel('magician/bkit');
    doInitMagicianClient();
  }

  MethodChannel channel;

//初始化蓝牙方法
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

//方法：搜索设备
//返回：String类型；返回搜索到的第一个device的Name
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


//方法：连接设备
//返回：String类型；返回"true"或者"false"
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

//方法：断开连接
//返回：String类型；返回"true"或者"false"
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
  
  //方法：获取设备位置
  //返回：String类型；返回json数据：{"x":120,"y":120,"z":120,"r":120}
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

  //方法：执行PTP指令 默认使用第一种模式进行PTP
  //返回：String类型；返回"true"或者"false"
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
