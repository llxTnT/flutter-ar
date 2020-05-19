//
//  YJDobotDev.m
//  YJDobotKit_B
//
//  Created by 刘清 on 2019/7/17.
//  Copyright © 2019 YuejianTechnology.com.www. All rights reserved.
//

#import "YJDobotDev.h"

@implementation YJDobotDev

+ (instancetype)yj_DobotDevWithPeri:(CBPeripheral *)peri{
    YJDobotDev *dev = [[self alloc] init];
    dev.cp = peri;
    return dev;
}

@end
