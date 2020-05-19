//
//  YJDobotDev.h
//  YJDobotKit_B
//
//  Created by 刘清 on 2019/7/17.
//  Copyright © 2019 YuejianTechnology.com.www. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreBluetooth/CoreBluetooth.h>

NS_ASSUME_NONNULL_BEGIN

@interface YJDobotDev : NSObject

@property (nonatomic, weak) CBCentralManager *cm;
@property (nonatomic, strong) CBPeripheral *cp;
@property (nonatomic, strong) NSDictionary *data;
@property (nonatomic, strong) NSNumber *rssi;
@property (nonatomic, copy) NSString *name;

+ (instancetype)yj_DobotDevWithPeri:(CBPeripheral *)peri;

@end

NS_ASSUME_NONNULL_END
