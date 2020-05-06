//
//  YJDobot_B_Config.m
//  YJDobotKit_B
//
//  Created by 刘清 on 2019/7/17.
//  Copyright © 2019 YuejianTechnology.com.www. All rights reserved.
//

#import "YJDobot_B_Config.h"

@implementation YJDobot_B_Config

+ (instancetype)sharedYjDobotBConfig{
    static YJDobot_B_Config *config = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        config = [[YJDobot_B_Config alloc] init];
    });
    return config;
}

- (instancetype)init{
    if (self = [super init]) {
        scan_TimeOut = kDefault_ScanTimeOut;
        service_uuid = kDefault_Severice_Uuid;
        charaWrite_uuid = kDefault_CharaWrite_Uuid;
        charaRead_uuid = kDefault_CharaRead_uuid;
    }
    return self;
}

@end
