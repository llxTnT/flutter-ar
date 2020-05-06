//
//  YJDobot_B_Config.h
//  YJDobotKit_B
//
//  Created by 刘清 on 2019/7/17.
//  Copyright © 2019 YuejianTechnology.com.www. All rights reserved.
//

#import "YJDobot_B_Header.h"
@class YJDobotDev;
@class YJDobotDev_Scan;
@class YJDobotDev_Connected;


NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSUInteger, YJDobot_BLEState) {
    YJDobot_BLEStateOn = 0,
    YJDobot_BLEStateOff
};


typedef void (^YJDobot_B_StateBlock)(YJDobot_BLEState state);

typedef void (^YJDobot_B_ReBlock)(void);

typedef void (^YJDobot_B_ScanReBlock)(YJDobotDev_Scan *);

typedef BOOL (^YJDobot_B_FltterBlock)(NSString *name, NSDictionary *advData, NSNumber *rsii)  ;

typedef void (^YJDobot_B_ConnectedBlock)(YJDobotDev_Connected *,BOOL,NSString *);

typedef void (^YJDobot_B_DisconnectedBlock)(YJDobotDev *);



@interface YJDobot_B_Config : NSObject
{
@public NSTimeInterval scan_TimeOut;//扫描超时时间
@public NSTimeInterval connect_TimeOut;//连接超时时间
@public NSString *service_uuid;//服务uuid
@public NSString *charaWrite_uuid;//特征写uuuuidid
@public NSString *charaRead_uuid;//特征读uuid
}

//蓝牙未打开回调;
@property (nonatomic, copy) YJDobot_B_StateBlock PowerState_Block;
//扫描蓝牙设备回调;
@property (nonatomic, copy) YJDobot_B_ScanReBlock ScanDev_Block;

@property (nonatomic, copy) YJDobot_B_ReBlock scanTimeOutBlock;
//过滤方式回调
@property (nonatomic, copy) YJDobot_B_FltterBlock FltterDev_Block;
//连接成功回调
@property (nonatomic, copy) YJDobot_B_ConnectedBlock connectedBlock;
//断开连接回调
@property (nonatomic, copy) YJDobot_B_DisconnectedBlock disconnectedBlock;


+ (instancetype)sharedYjDobotBConfig;


@end

NS_ASSUME_NONNULL_END
