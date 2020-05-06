//
//  YJDobotDev_Scan.m
//  YJDobotKit_B
//
//  Created by 刘清 on 2019/7/17.
//  Copyright © 2019 YuejianTechnology.com.www. All rights reserved.
//

#import "YJDobotDev_Scan.h"

@implementation YJDobotDev_Scan

+ (instancetype)yj_DobotDev_ScanWithCm:(CBCentralManager *)cm andCp:(CBPeripheral *)cp andData:(NSDictionary *)data andRssi:(NSNumber *)rssi{
    YJDobotDev_Scan *dev = [[YJDobotDev_Scan alloc] init];
    dev.cm = cm;
    dev.cp = cp;
    dev.data = data;
    dev.rssi = rssi;
    dev.name = cp.name;
    return dev;
}

@end
