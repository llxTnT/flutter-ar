//
//  YJDobot_B_SRManager.h
//  YJDobotKit_B
//
//  Created by 刘清 on 2019/7/19.
//  Copyright © 2019 YuejianTechnology.com.www. All rights reserved.
//  蓝牙收发器

#import <Foundation/Foundation.h>
#import "YJDobot_B_Struct.h"
#import "YJDobotDev_Connected.h"
#import "YJDobot_B_ReMsg.h"
#import "YJDobot_B_Header.h"


NS_ASSUME_NONNULL_BEGIN

@interface YJDobot_B_SRManager : NSObject


+ (instancetype)sharedYjDobot_B_SRManager;

- (void)deviceDidConnect:(CBPeripheral *)cp;
- (void)deviceDidDisconnect:(CBPeripheral *)cp;

- (void)sendMsg:(NSArray *)msgs ToDevice:(YJDobotDev_Connected *)dev andType:(CmdType)cmdType andReslutBlock:(void(^)(YJDobotDev_Connected *dev,YJDobot_B_ReMsg *reMsg))resultBlock;

- (void)updateMsg:(NSData *)msg fromPeri:(CBPeripheral *)cp;

@end

NS_ASSUME_NONNULL_END
