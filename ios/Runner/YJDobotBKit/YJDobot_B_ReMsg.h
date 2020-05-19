//
//  YJDobot_B_ReMsg.h
//  YJDobotKit_B
//
//  Created by 刘清 on 2019/7/18.
//  Copyright © 2019 YuejianTechnology.com.www. All rights reserved.
//

#import "YJDobot_B_BaseMsg.h"

typedef NS_ENUM(NSUInteger,YJDobot_B_ReMsgStatus) {
    YJDobot_B_ReMsgStatusOK,
    YJDobot_B_ReMsgStatusTimeOut,
};


NS_ASSUME_NONNULL_BEGIN

@interface YJDobot_B_ReMsg : YJDobot_B_BaseMsg

@property (nonatomic, assign) YJDobot_B_ReMsgStatus status;
@property (nonatomic, strong) NSData *response;
@property (nonatomic, assign) ProtocolID cmdId;
@property (nonatomic, assign) CmdType type;
@property (nonatomic, strong, nullable) NSData *paramter;

+ (instancetype)yj_Dobot_B_ReMsgWithData:(NSData *)data;

+ (instancetype)yj_Dobot_B_ReMsgWithSendData:(NSData *)data;

@end

NS_ASSUME_NONNULL_END
