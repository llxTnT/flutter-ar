//
//  YJDobot_B_MsgContent.m
//  YJDobotKit_B
//
//  Created by 刘清 on 2019/7/19.
//  Copyright © 2019 YuejianTechnology.com.www. All rights reserved.
//

#import "YJDobot_B_MsgContent.h"

@implementation YJDobot_B_MsgContent

- (instancetype)init{
    if (self = [super init]) {
        ctrTypeCmd.isWrite = controlTypeRead;
        ctrTypeCmd.isQueue = controlTypeNormal;
        ctrTypeCmd.isExc = controlTypeIsMagician;
        
        cmdId = -1;
    }
    return self;
}

- (void)setProtocolID:(ProtocolID)Id{
    cmdId = Id;
}

- (void)setWriteType{
    ctrTypeCmd.isWrite = controlTypeWrite;
}

- (void)setQueueType{
    ctrTypeCmd.isQueue = controlTypeQueue;
}

- (controlTypeIsQueue)getQueueType{
    return ctrTypeCmd.isQueue;
}

- (CmdType)cmdType{
    if (cmdId == ProtocolGetPose) {
        return CmdGetPose;
    }else if (cmdId == ProtocolQueuedCmdCurrentIndex){
        return CmdGetIndex;
    }
    return (CmdType)ctrTypeCmd.isQueue;
}

- (NSData *)contentData{
    NSMutableData *msg = [[NSMutableData alloc] initWithCapacity:512];
    
    //转换一下 NSUInteger-> uint8_t
    uint8_t cmdID = (uint8_t)cmdId;
    
    [msg appendBytes:&cmdID length:sizeof(cmdID)];
    
    uint8_t p;
    memcpy(&p, &ctrTypeCmd, sizeof(p));
    
    [msg appendBytes:&p length:sizeof(uint8_t)];
    [msg appendData:params];
    return msg;
}

- (void)paramsWithP:(void *)p andLength:(unsigned int)lenth{
    NSMutableData *data = [NSMutableData data];
    [data appendBytes:p length:lenth];
    params = data;
}

@end
