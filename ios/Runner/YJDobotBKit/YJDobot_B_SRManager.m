//
//  YJDobot_B_SRManager.m
//  YJDobotKit_B
//
//  Created by 刘清 on 2019/7/19.
//  Copyright © 2019 YuejianTechnology.com.www. All rights reserved.
//

#import "YJDobot_B_SRManager.h"
#import "YJDobot_B_CheckTimeOutObject.h"

#define kQueue  @"kQueue"

#define kQueueNomal    @"kQueueNomal"
#define kQueuePTP      @"kQueuePTP"
#define kQueueGetPose  @"kQueueGetPose"
#define kQueueGetIndex @"kQueueGetIndex"

#define kResponseCache @"kResponseCache"

#define kReponseCacheNoaml     @"kReponseCacheNoaml"
#define kReponseCachePTP       @"kReponseCacheNoaml"
#define kReponseCacheGetPose   @"kReponseCacheGetPose"
#define kReponseCacheGetIndex  @"kReponseCacheGetIndex"

#define kReponseKey   @"kReponseKey"

typedef void(^reBlock)(YJDobotDev_Connected *dev,YJDobot_B_ReMsg *reMsg);


@implementation YJDobot_B_SRManager
{
    NSMutableDictionary *devicesCache;//多机支持
    NSMutableArray *timeOutQueue;//检测超时队列
    NSTimer *timeOutCheckTimer;//超时检测器
}

+ (instancetype)sharedYjDobot_B_SRManager{
    static YJDobot_B_SRManager *manager = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        manager = [[YJDobot_B_SRManager alloc] init];
    });
    return manager;
}

- (instancetype)init{
    if (self = [super init]) {
        devicesCache = [NSMutableDictionary dictionary];
        timeOutQueue = [NSMutableArray array];
    }
    return self;
}

- (void)deviceDidConnect:(CBPeripheral *)cp{
    
    NSMutableDictionary *queueCache = [NSMutableDictionary dictionary];
    [queueCache setObject:dispatch_queue_create("sendPTPQueue", DISPATCH_QUEUE_SERIAL) forKey:kQueuePTP];
    [queueCache setObject:dispatch_queue_create("sendNomalQueue", DISPATCH_QUEUE_SERIAL) forKey:kQueueNomal];
    [queueCache setObject:dispatch_queue_create("sendGetPoseQueue", DISPATCH_QUEUE_SERIAL) forKey:kQueueGetPose];
    [queueCache setObject:dispatch_queue_create("sendGetIndexQueue", DISPATCH_QUEUE_SERIAL) forKey:kQueueGetIndex];
    
    [devicesCache setObject:queueCache forKey:[NSString stringWithFormat:@"%@_%@",kQueue,cp.identifier.UUIDString]];
    
    NSMutableDictionary *responsCache = [NSMutableDictionary dictionary];
    [responsCache setObject:[NSMutableArray array] forKey:kReponseCacheNoaml];
    [responsCache setObject:[NSMutableArray array] forKey:kReponseCachePTP];
    [responsCache setObject:[NSMutableArray array] forKey:kReponseCacheGetPose];
    [responsCache setObject:[NSMutableArray array] forKey:kReponseCacheGetIndex];
    
    [devicesCache setObject:responsCache forKey:[NSString stringWithFormat:@"%@_%@",kResponseCache,cp.identifier.UUIDString]];
}

- (void)deviceDidDisconnect:(CBPeripheral *)cp{
    [self clearStuffWith:cp];
}

- (void)sendMsg:(NSArray *)msgs ToDevice:(YJDobotDev_Connected *)dev  andType:(CmdType)cmdType andReslutBlock:(void(^)(YJDobotDev_Connected *dev,YJDobot_B_ReMsg *reMsg))resultBlock{
    
    if (resultBlock == nil) resultBlock = ^(YJDobotDev_Connected *dev,YJDobot_B_ReMsg *reMsg){};
    
    //加入超时检测队列
    if (timeOutQueue.count == 0) {//开启检测
        [self openTimeOutCheckQueue];
    }
    
    YJDobot_B_CheckTimeOutObject *object = [YJDobot_B_CheckTimeOutObject yj_Dobot_BCheckTimeOutObjectWithObject:resultBlock andDev:dev andMsg:msgs.firstObject];
    [timeOutQueue addObject:object];
    
    NSString *responseKey = [NSString stringWithFormat:@"%@_%@",kResponseCache,dev.cp .identifier.UUIDString];
    NSString *queuesKey = [NSString stringWithFormat:@"%@_%@",kQueue,dev.cp.identifier.UUIDString];
    
    NSMutableDictionary *reponseCache = devicesCache[responseKey];
    NSMutableDictionary *queuesCache = devicesCache[queuesKey];
    
    NSAssert(reponseCache != nil, @"回调池不存在");
    NSAssert(queuesCache != nil, @"发送队列池不存在");
    
    dispatch_queue_t queue;
    NSMutableArray *reponses;
    
    switch (cmdType) {
        case CmdGetPose:
            reponses = reponseCache[kReponseCacheGetPose];
            queue = queuesCache[kQueueGetPose];
            break;
        case CmdPTP:
            reponses = reponseCache[kReponseCachePTP];
            queue = queuesCache[kQueuePTP];
            break;
        case CmdGetIndex:
            reponses = reponseCache[kReponseCacheGetIndex];
            queue = queuesCache[kQueueGetIndex];
            break;
        default:
            reponses = reponseCache[kReponseCacheNoaml];
            queue = queuesCache[kQueueNomal];
            break;
    }
    
    NSAssert(reponses != nil, @"回调队列不存在");
    NSAssert(queue != nil, @"发送队列不存在");
    
    [reponses addObject:resultBlock];
    [self sendDatas:msgs ToDevice:dev.cp WithSendQueue:queue InChara:dev.writeChara];
}

- (void)sendDatas:(NSArray *)datas ToDevice:(CBPeripheral *)cp WithSendQueue:(dispatch_queue_t)queue InChara:(CBCharacteristic *)chara{
    dispatch_async(queue, ^{
        if (datas.count == 2) {
            [self sendData:datas.firstObject ToPeri:cp InChara:chara];//发送第一段命令
            [NSThread sleepForTimeInterval:0.02];//0.02秒过后再发第二段命令
            [self sendData:datas.lastObject ToPeri:cp InChara:chara];
        }else{
           [self sendData:datas.lastObject ToPeri:cp InChara:chara];//发送第二段命令
        }
    });
}

- (void)sendData:(NSData *)data ToPeri:(CBPeripheral *)cp InChara:(CBCharacteristic *)chara{
    NSAssert(data != nil, @"写入数据不能为空");
    NSAssert(cp != nil, @"写入设备不能为空");
    NSAssert(chara != nil, @"写入特征值不能为空");
    NSLog(@"写入data = %@",data);
    [cp writeValue:data forCharacteristic:chara type:CBCharacteristicWriteWithoutResponse];
}

- (void)updateMsg:(NSData *)msg fromPeri:(CBPeripheral *)cp{
    YJDobot_B_ReMsg *reMsg;
    uint16_t prefix;
    [msg getBytes:&prefix length:2];
    if (prefix == YJDobot_Msg_Prefix) {//AAAA开头
        //获取长度
        uint8_t length;
        [msg getBytes:&length range:NSMakeRange(2, 1)];
        
        if (msg.length - 4 == length) {//内容已经全部接受完成不用拼接
            reMsg = [YJDobot_B_ReMsg yj_Dobot_B_ReMsgWithData:msg];
        }else{//内容没有接收完成
            //先放入缓存中
            NSString *reCacheKey = [NSString stringWithFormat:@"%@_%@",kReponseKey,cp.identifier.UUIDString];
            NSMutableArray *reCache = devicesCache[reCacheKey];
            if (reCache == nil) {
                reCache = [NSMutableArray array];
                [devicesCache setObject:reCache forKey:reCacheKey];
            }
            [reCache addObject:msg];
        }
        
    }else{//不是AAAA开头 ,需要拼接
        
        NSString *reCacheKey = [NSString stringWithFormat:@"%@_%@",kReponseKey,cp.identifier.UUIDString];
        NSMutableArray *reCache = devicesCache[reCacheKey];
        if (!reCache) return;
        //只考虑最多两次接受完全部返回数据的情况
        for (NSData *preData in reCache) {
            NSMutableData *mData = [NSMutableData dataWithData:preData];
            [mData appendData:msg];
            
            uint8_t length;
            [mData getBytes:&length range:NSMakeRange(2, 1)];
            if (mData.length - 4 == length) {//长度匹配
                //check sum
                if ([self checkSumData:mData]) {//找到前缀
                    reMsg = [YJDobot_B_ReMsg yj_Dobot_B_ReMsgWithData:mData];
                    [reCache removeObject:preData];
                    break;
                }
            }
        }
    }
    
    if (reMsg) {//如果有值
        //找出回调block
        reBlock block = [self getReBlockWithPeri:cp andCmdType:reMsg.type];
     
        if (block) {
            YJDobotDev_Connected *dev = [YJDobotDev_Connected yj_DobotDevWithPeri:cp];
            block(dev,reMsg);
        }
    }
}


#pragma mark - private Method

- (reBlock)getReBlockWithPeri:(CBPeripheral *)cp andCmdType:(CmdType)type{//拿到对应的回调
    NSString *responseKey = [NSString stringWithFormat:@"%@_%@",kResponseCache,cp .identifier.UUIDString];
    NSMutableDictionary *reponseCache = devicesCache[responseKey];
    NSMutableArray *reponses;
    switch (type) {
        case CmdPTP:
            reponses = reponseCache[kReponseCachePTP];
            break;
        case CmdGetPose:
            reponses = reponseCache[kReponseCacheGetPose];
            break;
        case CmdGetIndex:
            reponses = reponseCache[kReponseCacheGetIndex];
            break;
        default:
            reponses = reponseCache[kReponseCacheNoaml];
            break;
    }
    reBlock block = reponses.firstObject;
    [reponses removeObject:block];//拿到回调完成删除回调
    for (YJDobot_B_CheckTimeOutObject *obj in timeOutQueue) {//移除超时检测队列
        if (block == obj.object) {
            [timeOutQueue removeObject:obj];
        }
    }
    return block;
}

- (BOOL)checkSumData:(NSMutableData *)data{
    
    NSMutableData *contentData = [NSMutableData data];
    [contentData appendBytes:(data.bytes + 3) length:data.length - 4];
    
    NSInteger contentLength = contentData.length;
    uint8_t len = (uint8_t)contentLength;
    uint8_t body[contentLength];
    [contentData getBytes:body length:len];
    
    uint8_t checksum = checkCalculateSum(body, len);//计算内容的sum值
    uint8_t sum;
    [data getBytes:&sum range:NSMakeRange(data.length - 1, 1)];
    if (checksum == sum) return YES;//比较sum是否匹配
    return NO;
}

uint8_t checkCalculateSum(uint8_t *p, uint8_t len)//校验函数
{
    uint8_t sum = 0;
    for (int i = 0; i < len; i++) {
        sum += *(p+i);
    }
    sum = 0 - sum;
    sum &= 0xFF;
    return sum;
}

- (void)openTimeOutCheckQueue{//开启超时检测
    dispatch_queue_t checkQueue = dispatch_queue_create("checkTimeOut", DISPATCH_QUEUE_SERIAL);
    dispatch_async(checkQueue, ^{
        self->timeOutCheckTimer = [NSTimer scheduledTimerWithTimeInterval:0.8 * YJDobot_Msg_TimeOut target:self selector:@selector(checkTimeOutObject) userInfo:nil repeats:YES];
        [[NSRunLoop currentRunLoop] addTimer:self->timeOutCheckTimer forMode:NSRunLoopCommonModes];
        [[NSRunLoop currentRunLoop] run];//常驻线程
    });
}

- (void)closeTimeOutCheckQueue{//关闭超时检测
    [self->timeOutCheckTimer invalidate];
    self->timeOutCheckTimer = nil;
}

- (void)checkTimeOutObject{
    if (timeOutQueue.count == 0) {
        [self closeTimeOutCheckQueue];
        return;
    }
    //比较时间戳
    NSTimeInterval currentTime = [[NSDate date] timeIntervalSince1970];
    
    for (YJDobot_B_CheckTimeOutObject *obj in timeOutQueue) {
        if (obj.time - currentTime >= YJDobot_Msg_TimeOut) {//超时
            reBlock block = (reBlock)obj.object;
            if (block) {
                block(obj.dev,obj.reMsg);//超时回调
            }
            [timeOutQueue removeObject:obj];
        }
    }
    if (timeOutQueue.count == 0) {//队列没有回调 关闭超时
        [self closeTimeOutCheckQueue];
    }
}

- (void)clearStuffWith:(CBPeripheral *)cp{
    for (NSString *key in devicesCache.allKeys) {
        if ([key containsString:cp.identifier.UUIDString]) {
            [devicesCache removeObjectForKey:key];
        }
    }
    
    for (YJDobot_B_CheckTimeOutObject *obj in timeOutQueue) {
        if (obj.dev.cp == cp) {
            [timeOutQueue removeObject:obj];
        }
    }
}

- (void)clearAllStuff{
    [devicesCache removeAllObjects];
    [timeOutQueue removeAllObjects];
    [timeOutCheckTimer invalidate];
    timeOutCheckTimer = nil;
}



@end
