//
//  YJDobot_B_CheckTimeOutObject.m
//  YJDobotKit_B
//
//  Created by 刘清 on 2019/7/24.
//  Copyright © 2019 YuejianTechnology.com.www. All rights reserved.
//

#import "YJDobot_B_CheckTimeOutObject.h"

@implementation YJDobot_B_CheckTimeOutObject

+ (instancetype)yj_Dobot_BCheckTimeOutObjectWithObject:(id)object andDev:(nonnull YJDobotDev_Connected *)dev andMsg:(NSData *)msg{
    YJDobot_B_CheckTimeOutObject *ob = [[YJDobot_B_CheckTimeOutObject alloc] init];
    ob.object = object;
    ob.dev = dev;
    NSDate *date = [NSDate date];
    ob.time = [date timeIntervalSince1970];
    
    ob.reMsg = [YJDobot_B_ReMsg yj_Dobot_B_ReMsgWithSendData:msg];
    
    return ob;
}

@end
