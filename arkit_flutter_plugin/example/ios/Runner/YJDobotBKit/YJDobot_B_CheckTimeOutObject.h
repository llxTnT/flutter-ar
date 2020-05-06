//
//  YJDobot_B_CheckTimeOutObject.h
//  YJDobotKit_B
//
//  Created by 刘清 on 2019/7/24.
//  Copyright © 2019 YuejianTechnology.com.www. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YJDobotDev_Connected.h"
#import "YJDobot_B_ReMsg.h"

NS_ASSUME_NONNULL_BEGIN

@interface YJDobot_B_CheckTimeOutObject : NSObject

@property (nonatomic, assign) NSTimeInterval time;//时间戳
@property (nonatomic, strong) YJDobotDev_Connected *dev;
@property (nonatomic, strong) YJDobot_B_ReMsg *reMsg;
@property (nonatomic, strong) id object;

+ (instancetype)yj_Dobot_BCheckTimeOutObjectWithObject:(id)object andDev:(YJDobotDev_Connected *)dev andMsg:(NSData *)msg;

@end

NS_ASSUME_NONNULL_END
