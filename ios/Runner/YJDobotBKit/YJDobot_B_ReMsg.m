//
//  YJDobot_B_ReMsg.m
//  YJDobotKit_B
//
//  Created by 刘清 on 2019/7/18.
//  Copyright © 2019 YuejianTechnology.com.www. All rights reserved.
//

#import "YJDobot_B_ReMsg.h"
#import "YJDobot_B_Struct.h"

@implementation YJDobot_B_ReMsg

+ (instancetype)yj_Dobot_B_ReMsgWithData:(NSData *)data{
    YJDobot_B_ReMsg *msg = [[YJDobot_B_ReMsg alloc] init];
   
    msg.status = YJDobot_B_ReMsgStatusOK;
    NSInteger cmdId = 0;
    [data getBytes:&cmdId range:NSMakeRange(3, 1)];
    
    controlTypeCmd typecmd;
    [data getBytes:&typecmd range:NSMakeRange(4, 1)];
    
    if (cmdId == ProtocolGetPose) {
        msg.type = CmdGetPose;
    }else if(cmdId == ProtocolQueuedCmdCurrentIndex){
        msg.type = CmdGetIndex;
    }else{
        if (typecmd.isQueue) {
            msg.type = CmdPTP;
        }else{
            msg.type = CmdNormal;
        }
    }
    
    if (data.length <= 6) {
        msg.paramter = nil;
    }else{
        NSMutableData *paramters = [NSMutableData data];
        [paramters appendBytes:(data.bytes + 5) length:data.length - 6];
        msg.paramter = paramters;
    }
    msg.response = data;
    msg.cmdId = cmdId;
    return msg;
}

+ (instancetype)yj_Dobot_B_ReMsgWithSendData:(NSData *)data{
    YJDobot_B_ReMsg *msg = [[YJDobot_B_ReMsg alloc] init];
    
    msg.status = YJDobot_B_ReMsgStatusTimeOut;
    ProtocolID cmdId;
    [data getBytes:&cmdId range:NSMakeRange(3, 1)];
    
    controlTypeCmd typecmd;
    [data getBytes:&typecmd range:NSMakeRange(4, 1)];
    
    if (cmdId == ProtocolGetPose) {
        msg.type = CmdGetPose;
    }else if(cmdId == ProtocolQueuedCmdCurrentIndex){
        msg.type = CmdGetIndex;
    }else{
        if (typecmd.isQueue) {
            msg.type = CmdPTP;
        }else{
            msg.type = CmdNormal;
        }
    }
    
    return msg;
}

@end
