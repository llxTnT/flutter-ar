//
//  YJDobot_B_Header.h
//  YJDobotKit_B
//
//  Created by 刘清 on 2019/7/17.
//  Copyright © 2019 YuejianTechnology.com.www. All rights reserved.
//

#ifndef YJDobot_B_Header_h
#define YJDobot_B_Header_h

#import <Foundation/Foundation.h>
#import "YJDobot_BKit.h"

#pragma mark - 初始化默认配置

#define kDefault_ScanTimeOut      10.0f
#define kDefault_Severice_Uuid    @"0003CDD0-0000-1000-8000-00805F9B0131"
#define kDefault_CharaWrite_Uuid  @"0003CDD2-0000-1000-8000-00805F9B0131"
#define kDefault_CharaRead_uuid   @"0003CDD1-0000-1000-8000-00805F9B0131"

#define kFltterDeviceNames        @[@"USR-BLE100",@"WH-BLE 102"]

#pragma mark - Log 配置

#define kLogWriteDocumentName   @"YJ_Dobot_B_Log"
#define kSandBoxDocumentPath    NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask,YES).firstObject

#define kLogWriteDocumentPath   [NSString stringWithFormat:@"%@/%@",kSandBoxDocumentPath,kLogWriteDocumentName]

#define kLogFileDeleteTimeDay 7 // log文件最多存在的时间天为单位

/*  YJLogLevelNone = 0,//不打印
    YJLogLevelLog = 1,//打印
    YJLogLevelHighLog = 2,//打印详细的内容（函数名,文件名,行号）
    YJLogLevelShowLog = 3,//保存打印日志在内存中 用kvo监听YJDobot_B_Log 的logChange属性 在YJDobot_B_Log 的logMeassages 属性中获取log日志 用UI展示出来
    YJLogLevelWrite = 4,//生成日志文件 */
#define kLogLevel  3

#if kLogLevel == 0
#define YJLog(fmt,...)
#elif kLogLevel == 1
#define YJLog(fmt,...)  NSLog((fmt), ## __VA_ARGS__);
#elif kLogLevel == 2
#define YJLog(fmt,...)  NSLog((@"[文件名:%s]\n" "[函数名:%s]\n" "[行号:%d] \n" fmt), __FILE__, __FUNCTION__, __LINE__, ##__VA_ARGS__);
#elif kLogLevel == 3
#define YJLog(fmt,...)   [YJDobot_B_Log yj_Dobot_Get_LogMeassage:[NSString stringWithFormat:fmt,## __VA_ARGS__]];
#elif kLogLevel == 4
#define YJLog(fmt,...)   [[NSString stringWithFormat:fmt,## __VA_ARGS__] writeToFile:[YJDobot_B_Log sharedYjDobotBLog].dateLogFilePath atomically:YES encoding:NSUTF8StringEncoding error:nil];
#else
#define YJLog(fmt,...)
#endif


#pragma mark - 蓝牙相关

#define YJDobot_Msg_Prefix   0xAAAA

#define YJDobot_Msg_TimeOut  2.0 // 超时时间 单位秒


#endif
