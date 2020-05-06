//
//  YJDobot_B_Log.m
//  YJDobotKit_B
//
//  Created by 刘清 on 2019/7/22.
//  Copyright © 2019 YuejianTechnology.com.www. All rights reserved.
//

#import "YJDobot_B_Log.h"
#import "YJDobot_B_Header.h"

#define kLogFileNameFormatter @"yyyy-MM-dd HH:mm:ss"

@implementation YJDobot_B_Log

+ (instancetype)sharedYjDobotBLog{
    static YJDobot_B_Log *log = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        log = [[YJDobot_B_Log alloc] init];
    });
    return log;
}

+ (void)yj_Dobot_B_LogWithFileConfig{
    if (kLogLevel == YJLogLevelWrite) {
        //创建文件和文件夹
        if (![[NSFileManager defaultManager] fileExistsAtPath:kLogWriteDocumentPath]) {
            [[NSFileManager defaultManager] createDirectoryAtPath:kLogWriteDocumentPath withIntermediateDirectories:YES attributes:nil error:nil];
        }
        
        NSDate *date = [NSDate date];
        NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
        [formatter setDateFormat:kLogFileNameFormatter];
        NSString *dateName = [formatter stringFromDate:date];
        
        NSString *dateLogPath = [NSString stringWithFormat:@"%@/%@.text",kLogWriteDocumentPath,dateName];
        
        [[NSFileManager defaultManager] createFileAtPath:dateLogPath contents:nil attributes:nil];
        
        [YJDobot_B_Log sharedYjDobotBLog].dateLogFilePath = dateLogPath;
    }
    [self checkLogFileCount];
}

+ (void)checkLogFileCount{//检查log文件能否删除
    if (![[NSFileManager defaultManager] fileExistsAtPath:kLogWriteDocumentPath]) {
        NSArray *files = [[NSFileManager defaultManager] contentsOfDirectoryAtPath:kLogWriteDocumentPath error:nil];
        NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
        [formatter setDateFormat:kLogFileNameFormatter];
        
        NSDate *cDate = [NSDate date];
        NSTimeInterval cTimeInterval = [cDate timeIntervalSince1970];
        
        for (NSString *fileName in files) {
            NSDate *date = [formatter dateFromString:fileName];
            NSTimeInterval timeInterval = [date timeIntervalSince1970];
            NSTimeInterval cT = timeInterval - cTimeInterval;
            if (cT > (60 * 60 * 24) * kLogFileDeleteTimeDay) {
                [[NSFileManager defaultManager] removeItemAtPath:[NSString stringWithFormat:@"%@/%@",kLogWriteDocumentPath,fileName] error:nil];
            }
        }
    }
}

+ (void)yj_Dobot_Get_LogMeassage:(NSString *)msg{
    
    NSDate *date = [NSDate date];
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"HH:mm:ss.SSS"];
    
    NSString *str = [NSMutableString stringWithFormat:@"%@-%@",[formatter stringFromDate:date],msg];
    [[YJDobot_B_Log sharedYjDobotBLog].logMeassages addObject:str];
    
    [self yj_Dobot_NotifiDelegateLogMeassage];
}

+ (void)yj_Dobot_ClearLogMeassages{
    [[YJDobot_B_Log sharedYjDobotBLog].logMeassages removeAllObjects];
    [self yj_Dobot_NotifiDelegateLogMeassage];
}

+ (void)yj_Dobot_NotifiDelegateLogMeassage{
    if ([YJDobot_B_Log sharedYjDobotBLog].delegate && [[YJDobot_B_Log sharedYjDobotBLog].delegate respondsToSelector:@selector(yj_Dobot_B_LogDidChangeLogMeassages:)]) {
        
        [[YJDobot_B_Log sharedYjDobotBLog].delegate yj_Dobot_B_LogDidChangeLogMeassages:[YJDobot_B_Log sharedYjDobotBLog].logMeassages.mutableCopy];
    }
}

#pragma mark - getter && setter

- (NSMutableArray *)logMeassages{
    if (_logMeassages == nil) {
        _logMeassages = [NSMutableArray array];
    }
    return _logMeassages;
}

@end
