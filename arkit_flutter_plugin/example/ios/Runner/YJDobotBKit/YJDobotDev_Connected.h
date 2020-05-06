//
//  YJDobotDev_Connected.h
//  YJDobotKit_B
//
//  Created by 刘清 on 2019/7/18.
//  Copyright © 2019 YuejianTechnology.com.www. All rights reserved.
//

#import "YJDobotDev.h"

NS_ASSUME_NONNULL_BEGIN

@interface YJDobotDev_Connected : YJDobotDev

@property (nonatomic, strong) CBCharacteristic *readChara;
@property (nonatomic, strong) CBCharacteristic *writeChara;

@end

NS_ASSUME_NONNULL_END
