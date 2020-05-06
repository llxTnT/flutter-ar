//
//  YJDobot_B.h
//  YJDobotKit_B
//
//  Created by 刘清 on 2019/7/17.
//  Copyright © 2019 YuejianTechnology.com.www. All rights reserved.
//

#import "YJDobot_B_Header.h"
@class YJDobot_B_Config;
@class YJDobotDev_Scan;
@class YJDobotDev_Connected;
@class YJDobotDev;
@class YJDobot_B_Msg;
@class YJDobot_B_ReMsg;

NS_ASSUME_NONNULL_BEGIN

@interface YJDobot_B : NSObject

+ (instancetype)sharedYjDobot_B;

//初始化
- (instancetype)yj_Doboy_B_InitWithBlock:(void(^)(YJDobot_B_Config *config))initBlock;


//扫描设备
- (void)yj_Dobot_B_ScanDeviceWithReslutBlock:(void(^)(YJDobotDev_Scan *dev))resultBlock;

- (void)yj_Dobot_B_ScanDeviceWithandTimeOut:(float)time andReslutBlock:(void(^)(YJDobotDev_Scan *dev))resultBlock andTimeOutBlock:(void(^)(void))timeOutBlock;

//连接设备
- (void)yj_Dobot_B_ConnectDeviceWithDev:(YJDobotDev_Scan *)dev andResultBlock:(void(^)(YJDobotDev_Connected *connectedDev,BOOL success ,NSString *errorMsg))resultBlock;

//断开连接
- (void)yj_Dobot_B_DisconnectDevice:(YJDobotDev_Connected *)dev;

//无回调发送命令
- (void)yj_Dobot_B_SendMsg:(YJDobot_B_Msg *)msg ToDevice:(YJDobotDev_Connected *)dev;
//回调发送命令
- (void)yj_Dobot_B_SendMsg:(YJDobot_B_Msg *)msg ToDevice:(YJDobotDev_Connected *)dev andResultBlock:(void(^)(YJDobotDev_Connected *dev,YJDobot_B_ReMsg *reMsg))resultBlock;



@end

NS_ASSUME_NONNULL_END
