//
//  YJDobot_B.m
//  YJDobotKit_B
//
//  Created by 刘清 on 2019/7/17.
//  Copyright © 2019 YuejianTechnology.com.www. All rights reserved.
//

#import "BabyBluetooth.h"
#import "YJDobot_B_Header.h"
#import "YJDobot_B_Log.h"
#import "YJDobot_B.h"
#import "YJDobot_B_Config.h"
#import "YJDobot_B_SRManager.h"
#import "YJDobotDev_Scan.h"
#import "YJDobotDev_Connected.h"



@interface YJDobot_B ()

@property (nonatomic, strong)YJDobot_B_Config *config;
@property (nonatomic, strong)YJDobot_B_SRManager *sRmanager;
@property (nonatomic, strong) NSMutableArray *scanDevices;//扫描到的设备;


@end

@implementation YJDobot_B
{
@protected BabyBluetooth *babyBluetooth;//第三方蓝牙实例
@private   NSTimer *scanTimeOutTimer;//扫描超时定时器
@private   BOOL  hasScanDevices;//是否扫描到设备


}

#pragma mark - init method

+ (instancetype)sharedYjDobot_B{
    static YJDobot_B *dobot = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        dobot = [[YJDobot_B alloc] init];
    });
    return dobot;
}

- (instancetype)init{
    if (self = [super init]) {
        _config = [YJDobot_B_Config sharedYjDobotBConfig];
        _sRmanager = [YJDobot_B_SRManager sharedYjDobot_B_SRManager];
        _scanDevices = [NSMutableArray array];
    }
    return self;
}

- (id)copyWithZone:(nullable NSZone *)zone {
    return [YJDobot_B sharedYjDobot_B];
}

- (id)mutableCopyWithZone:(nullable NSZone *)zone{
    return [YJDobot_B sharedYjDobot_B];
}

- (void)setupBlockCallBack{
    __weak typeof(self) weakself = self;
    //蓝牙状态回调
    [babyBluetooth setBlockOnCentralManagerDidUpdateState:^(CBCentralManager *central) {
        if (weakself.config.PowerState_Block) {
            if (@available(iOS 10.0, *)) {
                if (central.state == CBManagerStatePoweredOn) {
                    weakself.config.PowerState_Block(YJDobot_BLEStateOn);
                }else{
                    weakself.config.PowerState_Block(YJDobot_BLEStateOff);
                }
            } else {
                if (central.state == CBCentralManagerStatePoweredOn) {
                    weakself.config.PowerState_Block(YJDobot_BLEStateOn);
                }else{
                    weakself.config.PowerState_Block(YJDobot_BLEStateOff);
                }
            }
        }
    }];
    
    //设置扫描条件
    [babyBluetooth setFilterOnDiscoverPeripherals:^BOOL(NSString *peripheralName, NSDictionary *advertisementData, NSNumber *RSSI) {
        if (weakself.config.FltterDev_Block) {
            return weakself.config.FltterDev_Block(peripheralName,advertisementData,RSSI);
        }else{
            for (NSString *name in kFltterDeviceNames) {
                if ([name isEqualToString:peripheralName]) return YES;
            }
            return NO;
        }
    }];
  
    //发现设备
    [babyBluetooth setBlockOnDiscoverToPeripherals:^(CBCentralManager *central, CBPeripheral *peripheral, NSDictionary *advertisementData, NSNumber *RSSI) {
        if (self->hasScanDevices == NO) self->hasScanDevices = YES;
        BOOL isCommonDevice = NO;
        for (CBPeripheral *peri in weakself.scanDevices) {
            if (peri == peripheral) isCommonDevice = YES;
        }
        if (isCommonDevice) return;
        [weakself.scanDevices addObject:peripheral];
        if (weakself.config.ScanDev_Block) {
            YJDobotDev_Scan *dev = [YJDobotDev_Scan yj_DobotDev_ScanWithCm:central andCp:peripheral andData:advertisementData andRssi:RSSI];
            weakself.config.ScanDev_Block(dev);
        }
    }];
    //连接成功
    [babyBluetooth setBlockOnConnected:^(CBCentralManager *central, CBPeripheral *peripheral) {
        [weakself.sRmanager deviceDidConnect:peripheral];
    }];
    //连接失败
    [babyBluetooth setBlockOnFailToConnect:^(CBCentralManager *central, CBPeripheral *peripheral, NSError *error) {
        
    }];
    //发现服务
    [babyBluetooth setBlockOnDiscoverServices:^(CBPeripheral *peripheral, NSError *error) {
       
        if(![weakself checkServiceUUid])return;//如果没指定sevice不做处理
        
        YJDobotDev_Connected *dev = [YJDobotDev_Connected yj_DobotDevWithPeri:peripheral];
        if (error) {
            if (weakself.config.connectedBlock) {
                weakself.config.connectedBlock(dev, NO, error.localizedDescription);
                return;
            }
        }
        for (CBService *service in peripheral.services) {
            if ([service.UUID.UUIDString isEqualToString:weakself.config->service_uuid]) {
                return;
            }
        }
        
        if (weakself.config.connectedBlock) {
            weakself.config.connectedBlock(dev, NO, @"没有找到指定服务");
            return;
        }
    }];
    //发现特征
    [babyBluetooth setBlockOnDiscoverCharacteristics:^(CBPeripheral *peripheral, CBService *service, NSError *error) {
        if (![weakself checkCharacteristicUUid]) {
            if (weakself.config.connectedBlock) {
                weakself.config.connectedBlock( [YJDobotDev_Connected yj_DobotDevWithPeri:peripheral], NO, @"没有指定读写特征uuid");
            }
            return;
        }
       
        YJDobotDev_Connected *dev = [weakself findReadCharacteristic:weakself.config->charaRead_uuid andWriteCharacteristic:weakself.config->charaWrite_uuid InService:service];
        
        if (dev && weakself.config.connectedBlock) {
            dev.cp = peripheral;
            dev.name = peripheral.name;
            [dev.cp setNotifyValue:YES forCharacteristic:dev.readChara];
            weakself.config.connectedBlock(dev, YES, @"");
            return;
        }
    }];
    
    [babyBluetooth setBlockOnDisconnect:^(CBCentralManager *central, CBPeripheral *peripheral, NSError *error) {
        [weakself.sRmanager deviceDidDisconnect:peripheral];
        if (weakself.config.disconnectedBlock) {
            weakself.config.disconnectedBlock([YJDobotDev yj_DobotDevWithPeri:peripheral]);
        }
    }];
    
    [babyBluetooth setBlockOnDidWriteValueForCharacteristic:^(CBCharacteristic *characteristic, NSError *error) {
       // YJLog(@"写入data 1111 %@",characteristic.value);
    }];
    
    [babyBluetooth setBlockOnReadValueForCharacteristic:^(CBPeripheral *peripheral, CBCharacteristic *characteristic, NSError *error) {
        YJLog(@"%@ 回调data %@",peripheral.name,characteristic.value);
        [weakself.sRmanager updateMsg:characteristic.value fromPeri:peripheral];
    }];
}

#pragma mark - API

- (instancetype)yj_Doboy_B_InitWithBlock:(void(^)(YJDobot_B_Config *config))initBlock{
    
    NSAssert(initBlock != nil, @"初始化block不能为空");
    
    [YJDobot_B_Log yj_Dobot_B_LogWithFileConfig];//配置沙盒输出文件
    
    initBlock(_config);//初始化配置
    babyBluetooth = [BabyBluetooth shareBabyBluetooth];//初始化第三方实例
    [self setupBlockCallBack];//设置回调
    
    return self;
}

- (void)yj_Dobot_B_ScanDeviceWithReslutBlock:(void(^)(YJDobotDev_Scan *dev))resultBlock{
    [self yj_Dobot_B_ScanDeviceWithandTimeOut:kDefault_ScanTimeOut andReslutBlock:resultBlock andTimeOutBlock:^{
        
    }];
}

- (void)yj_Dobot_B_ScanDeviceWithandTimeOut:(float)time andReslutBlock:(void(^)(YJDobotDev_Scan *dev))resultBlock andTimeOutBlock:(void(^)(void))timeOutBlock{
    [self.scanDevices removeAllObjects];
    babyBluetooth.scanForPeripherals().begin();
    if (resultBlock) {
        self.config.ScanDev_Block = resultBlock;
    }
    
    if (timeOutBlock) {
        scanTimeOutTimer = [NSTimer scheduledTimerWithTimeInterval:time target:self selector:@selector(scanTimeOut) userInfo:nil repeats:NO];
        self.config.scanTimeOutBlock = timeOutBlock;
    }
}

- (void)yj_Dobot_B_ConnectDeviceWithDev:(YJDobotDev_Scan *)dev andResultBlock:(void(^)(YJDobotDev_Connected *connectedDev,BOOL success ,NSString *errorMsg))resultBlock{
    [babyBluetooth cancelScan];
    babyBluetooth.having(dev.cp).connectToPeripherals() .discoverServices().discoverCharacteristics().begin();
    if (resultBlock) {
        self.config.connectedBlock = resultBlock;
    }
}

- (void)yj_Dobot_B_DisconnectDevice:(YJDobotDev_Connected *)dev{
    [babyBluetooth cancelPeripheralConnection:dev.cp];
}

- (void)yj_Dobot_B_SendMsg:(YJDobot_B_Msg *)msg ToDevice:(YJDobotDev_Connected *)dev{
    [self yj_Dobot_B_SendMsg:msg ToDevice:dev andResultBlock:^(YJDobotDev_Connected * _Nonnull dev, YJDobot_B_ReMsg * _Nonnull reMsg) {
        
    }];
}

- (void)yj_Dobot_B_SendMsg:(YJDobot_B_Msg *)msg ToDevice:(YJDobotDev_Connected *)dev andResultBlock:(void(^)(YJDobotDev_Connected *dev,YJDobot_B_ReMsg *reMsg))resultBlock{
    [self.sRmanager sendMsg:msg.contentDatas ToDevice:dev andType:msg.type andReslutBlock:^(YJDobotDev_Connected * _Nonnull dev, YJDobot_B_ReMsg * _Nonnull reMsg) {
        if (resultBlock) {
            resultBlock(dev,reMsg);
        }
    }];
}

#pragma mark - parivte method

- (void)scanTimeOut{
    if (hasScanDevices) return;
    [babyBluetooth cancelScan];
    if (self.config.scanTimeOutBlock) {
        self.config.scanTimeOutBlock();
    }
}

- (BOOL)checkServiceUUid{
    if (self.config->service_uuid == nil || [self.config->service_uuid isEqualToString:@""])return NO;
    return YES;
}

- (BOOL)checkCharacteristicUUid{
    if ((self.config->charaRead_uuid == nil || [self.config->charaRead_uuid isEqualToString:@""]) || (self.config->charaWrite_uuid == nil || [self.config->charaWrite_uuid isEqualToString:@""])) {
        return NO;
    }
    return YES;
}

- (YJDobotDev_Connected *)findReadCharacteristic:(NSString *)readUuid andWriteCharacteristic:(NSString *)writeUuid InService:(CBService *)ser{
    
    if (readUuid == nil || writeUuid == nil) return nil;
    
    YJDobotDev_Connected *dev = [[YJDobotDev_Connected alloc] init];
    for (CBCharacteristic *chara in ser.characteristics) {
        if ([chara.UUID.UUIDString isEqualToString:readUuid]) {
            dev.readChara = chara;
        }else if ([chara.UUID.UUIDString isEqualToString:writeUuid]){
            dev.writeChara = chara;
        }
    }
    
    if (dev.readChara && dev.writeChara) return dev;
    
    return nil;
}



@end
