//
//  YJDobot_B_BaseMsg.h
//  YJDobotKit_B
//
//  Created by 刘清 on 2019/7/18.
//  Copyright © 2019 YuejianTechnology.com.www. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YJDobot_B_MsgContent.h"


NS_ASSUME_NONNULL_BEGIN

@interface YJDobot_B_BaseMsg : NSObject
{
    uint16_t  prefix;//前缀
    uint8_t contentLength;//内容长度
    YJDobot_B_MsgContent *content;//命令内容
    uint8_t checksum;//效验值
}







@end

NS_ASSUME_NONNULL_END
