#include "AppDelegate.h"
#include "GeneratedPluginRegistrant.h"

#import "YJDobotBKit/YJDobot_B.h"
@implementation AppDelegate
{
    @private YJDobot_B *dobot;
    @private NSMutableArray *discoverDevices;
    @private NSArray *logMeassages;
    @private YJDobotDev_Connected *connectedDevice;
    @private BOOL isInit;
}

- (BOOL)application:(UIApplication *)application
    didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
  [GeneratedPluginRegistrant registerWithRegistry:self];
  // Override point for customization after application launch.
    FlutterMethodChannel* channel = [FlutterMethodChannel methodChannelWithName:@"magician/bkit" binaryMessenger:(FlutterViewController *)self.window.rootViewController];
    //往方法通道注册方法调用处理回调
    [channel setMethodCallHandler:^(FlutterMethodCall* call, FlutterResult result) {
      //方法名称一致
      if ([@"initDevice" isEqualToString:call.method]) {
        //初始化控件
          if (!isInit) {
              if (discoverDevices == nil) {
                     discoverDevices = [NSMutableArray array];
                 }
              dobot = [[YJDobot_B sharedYjDobot_B] yj_Doboy_B_InitWithBlock:^(YJDobot_B_Config * _Nonnull config) {
              //在此处可以设置config的参数
                  isInit=true;
                  result(@"init success");
              }];
          }else
              result(@"init success");
        //返回方法处理结果
      }else if ([@"searchDevice" isEqualToString:call.method]) {
                 //初始化控件
                   if (isInit) {
                       [dobot yj_Dobot_B_ScanDeviceWithandTimeOut:10.0 andReslutBlock:^(YJDobotDev_Scan * _Nonnull dev) {
                           [discoverDevices addObject:dev];
                           result(dev.name);
                        } andTimeOutBlock:^{
                            NSString * resStr=@"time out:";
                            for (int i=0;i<discoverDevices.count;i++ ) {
                                [resStr stringByAppendingString:@" "];
                                [resStr stringByAppendingString:discoverDevices[i]];
                                [resStr stringByAppendingString:@" "];
                            }
                            result(resStr);
                        }];
                   }
                 //返回方法处理结果
               }
        
        else if ([@"connectDevice" isEqualToString:call.method]) {
          //初始化控件
            if (isInit) {
                id para = call.arguments;
                NSString *deviceName=(NSString *)para;
                NSLog(@"flutter传给原生的参数：%@", para);
                NSLog(@"flutter connect dev name：%@", deviceName);
                for (int i=0;i<discoverDevices.count;i++ ) {
                    YJDobotDev_Scan * dev=discoverDevices[i];
                    if([dev.name isEqualToString:deviceName]){
                        NSLog(@"connect dev name：%@", deviceName);
                        [dobot yj_Dobot_B_ConnectDeviceWithDev:dev andResultBlock:^(YJDobotDev_Connected * _Nonnull connectedDev, BOOL success, NSString * _Nonnull errorMsg) {
                            
                            connectedDevice=connectedDev;
                            NSLog(@"finish connect:%@",success?@"YES":@"NO");
                            result(success?@"true":@"false");
                        }];
                        break;
                    }
                }
            }else{
                result(@false);
            }
          //返回方法处理结果
        }else if([@"disConnectDevice" isEqualToString:call.method]){
                if (isInit) {
                    [dobot yj_Dobot_B_DisconnectDevice:connectedDevice];
                    isInit=false;
                    connectedDevice=NULL;
                    result(@"true");
                }else{
                    result(@"false");
                }
        }else if ([@"getPose" isEqualToString:call.method]) {
                 //初始化控件
                   if (isInit) {
 
                       YJDobot_B_Msg *msg = [[YJDobot_B_Msg alloc] init];
                       [msg cmdGetPose];
                       [dobot yj_Dobot_B_SendMsg:msg ToDevice:connectedDevice andResultBlock:^(YJDobotDev_Connected * _Nonnull dev, YJDobot_B_ReMsg * _Nonnull reMsg) {
                           if (reMsg.status == YJDobot_B_ReMsgStatusOK) {
                               //NSLog(@"repsonseData = %@",reMsg.response);
                               Pose pose;
                               [reMsg.paramter getBytes:&pose length:sizeof(pose)];
                               
                              // result(@"get pose success");
                             // NSLog(@"getPose return str:%@",resStr);
                              //result(resStr);
                               result([NSString stringWithFormat:@"{\"x\":%f,\"y\":%f,\"z\":%f,\"r\":%f}",pose.x,pose.y,pose.z,pose.r]);
                           }else
                           {
                               result(@"get pose error");
                           }
                       }];

                 //返回方法处理结果
                   }else{
                        result(@"get pose error");
                   }
        }
        else if([@"ptpCmd" isEqualToString:call.method]){
            if (isInit) {
                NSDictionary *args = call.arguments;
                float x=[(NSString *)args[@"x"] floatValue];
                float y=[(NSString *)args[@"y"] floatValue];
                float z=[(NSString *)args[@"z"] floatValue];
                float r=[(NSString *)args[@"r"] floatValue];
                
               YJDobot_B_Msg *msg = [[YJDobot_B_Msg alloc] init];
                PTPCmd p = {PTPMode_MoveL,x,y,z,r};
                [msg cmdPTP:p];
            //    [msg cmdClearAllAlarms];
                [dobot yj_Dobot_B_SendMsg:msg ToDevice:connectedDevice andResultBlock:^(YJDobotDev_Connected * _Nonnull dev, YJDobot_B_ReMsg * _Nonnull reMsg) {

                    if (reMsg.status == YJDobot_B_ReMsgStatusOK) {
                        result(@"ptp success");
                        if (reMsg.type == CmdPTP) {
                            uint64_t index;
                            [reMsg.paramter getBytes:&index length:sizeof(index)];
                        }
                    }else
                    {
                        result(@"ptp error");
                    }
                }];
            }
            else{
                 result(@"ptp error");
            }
        }
      else {
        //找不到被调用的方法
        result(FlutterMethodNotImplemented);
      }
    }];
  return [super application:application didFinishLaunchingWithOptions:launchOptions];
}
@end
