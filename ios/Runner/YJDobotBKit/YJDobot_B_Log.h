//
//  YJDobot_B_Log.h
//  YJDobotKit_B
//
//  Created by 刘清 on 2019/7/22.
//  Copyright © 2019 YuejianTechnology.com.www. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSUInteger, YJLogLevel) {
    YJLogLevelNone = 0,//不打印
    YJLogLevelLog,//打印
    YJLogLevelHighLog,//打印详细的内容（函数名,文件名,行号）
    YJLogLevelShowLog,//保存打印日志在内存中 实现代理方法 logMeassages中获取log日志 用UI展示出来
    YJLogLevelWrite,//生成日志文件
};

@protocol YJDobotLogDelegate <NSObject>

- (void)yj_Dobot_B_LogDidChangeLogMeassages:(NSArray *)logMeassages;

@end

@interface YJDobot_B_Log : NSObject

@property (nonatomic, copy) NSString *dateLogFilePath;

@property (nonatomic, strong) NSMutableArray *logMeassages;
@property (nonatomic, weak) id<YJDobotLogDelegate>delegate;

+ (instancetype)sharedYjDobotBLog;

+ (void)yj_Dobot_B_LogWithFileConfig;

+ (void)yj_Dobot_Get_LogMeassage:(NSString *)msg;

+ (void)yj_Dobot_ClearLogMeassages;

@end

NS_ASSUME_NONNULL_END
