//
//  YJDobotDev_Scan.h
//  YJDobotKit_B
//
//  Created by 刘清 on 2019/7/17.
//  Copyright © 2019 YuejianTechnology.com.www. All rights reserved.
//

#import "YJDobotDev.h"


NS_ASSUME_NONNULL_BEGIN

@interface YJDobotDev_Scan : YJDobotDev

+ (instancetype)yj_DobotDev_ScanWithCm:(CBCentralManager *)cm andCp:(CBPeripheral *)cp andData:(NSDictionary *)data andRssi:(NSNumber *)rssi;

@end

NS_ASSUME_NONNULL_END
