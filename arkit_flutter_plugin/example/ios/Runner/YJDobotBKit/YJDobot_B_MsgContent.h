//
//  YJDobot_B_MsgContent.h
//  YJDobotKit_B
//
//  Created by 刘清 on 2019/7/19.
//  Copyright © 2019 YuejianTechnology.com.www. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YJDobot_B_Struct.h"

@interface YJDobot_B_MsgContent : NSObject
{
    @protected  ProtocolID  cmdId;//功能id
    @private    controlTypeCmd ctrTypeCmd;//控制类型
    @private    NSData *params;//功能参数
    @public     CmdType cmdType;//命令类型
}

- (void)setProtocolID:(ProtocolID)Id;

- (void)setWriteType;

- (void)setQueueType;
- (controlTypeIsQueue)getQueueType;

- (NSData *)contentData;
- (CmdType)cmdType;

- (void)paramsWithP:(void *)p andLength:(unsigned int)lenth;

@end


