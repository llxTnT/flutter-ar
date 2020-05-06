//
//  YJDobot_B_Msg.m
//  YJDobotKit_B
//
//  Created by 刘清 on 2019/7/18.
//  Copyright © 2019 YuejianTechnology.com.www. All rights reserved.
//

#import "YJDobot_B_Msg.h"

@interface YJDobot_B_Msg ()



@end

@implementation YJDobot_B_Msg

- (instancetype)init{
    if (self = [super init]) {
        prefix = YJDobot_Msg_Prefix;
        contentLength = 0x00;
        content = [[YJDobot_B_MsgContent alloc] init];
    }
    return self;
}

+ (instancetype)cmdMsgWithCmdString:(NSString *)cmdString{
    YJDobot_B_Msg *msg = [[YJDobot_B_Msg alloc] init];
    return msg;
}

#pragma mark - 命令

- (void)cmdGetDeviceSn{
    [content setProtocolID:ProtocolDeviceSN];
}

- (void)cmdSetDeviceSn:(char *)sn{
    [content setProtocolID:ProtocolDeviceSN];
    [content setWriteType];
    [content paramsWithP:sn andLength:charLen(sn)];
}

- (void)cmdGetDeviceName{
     [content setProtocolID:ProtocolDeviceName];
}

- (void)cmdSetDeviceName:(char *)name{
    [content setProtocolID:ProtocolDeviceName];
    [content setWriteType];
    [content paramsWithP:name andLength:charLen(name)];
}

- (void)cmdGetPose{
    [content setProtocolID:ProtocolGetPose];
}

- (void)cmdSetPose:(Pose)pose{
    [content setProtocolID:ProtocolGetPose];
    [content setWriteType];
    [content paramsWithP:&pose andLength:sizeof(pose)];
}

-(void)cmdGetDeviceUid{
    [content setProtocolID:ProtocolDeviceUid];
}

-(void)cmdGetDeviceVersion{
    [content setProtocolID:ProtocolDeviceVersion];
}


-(void)cmdResetPose:(ResetPose)p
{
    [content setProtocolID:ProtocolResetPose];
    [content setWriteType];
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdGetKinematices
{
    [content setProtocolID:ProtocolGetKinematics];
}

-(void)cmdGetHOMEParams
{
    [content setProtocolID:ProtocolHOMEParams];
}

-(void)cmdSetHOMEParams:(HomeParams)p
{
    [content setProtocolID:ProtocolHOMEParams];
    [content setWriteType];
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdSetHOMECmd:(HOMECmd)p
{
    [content setProtocolID:ProtocolHOMECmd];
    [content setWriteType];
    
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdSetHOMECmdQueue:(HOMECmd)p
{
    [content setProtocolID:ProtocolHOMECmd];
    [content setWriteType];
    [content setQueueType];
    
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdHome
{
    //    [content setProtocolID:ProtocolHOMECmd];
    //    [content setWriteType];
    
    HOMECmd p;
    p.reserved = 0;
    
    [self cmdSetHOMECmd:p];
    
}

-(void)cmdGetHHTTrigeMode
{
    [content setProtocolID:ProtocolHHTTrigMode];
}

-(void)cmdSetHHTTrigeMode:(HHTTrigMode)mode
{
    [content setProtocolID:ProtocolHHTTrigMode];
    [content setWriteType];
    
    uint8_t p = (uint8_t)mode;
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdGetAlarmState{
    [content setProtocolID:ProtocolAlarmsState];
}

- (void)cmdClearAllAlarms{
    [content setProtocolID:ProtocolAlarmsState];
    [content setWriteType];
}

-(void)cmdSetLostStep:(float)p{
    [content setProtocolID:ProtocolSetLostStepValue];
    [content setWriteType];
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdSetDoLostStep{
    [content setProtocolID:ProtocolSetDoLostStep];
    [content setWriteType];
    [content setQueueType];
}

-(void)cmdJog:(JOGCmd)p
{
    [content setProtocolID:ProtocolJOGCmd];
    [content setWriteType];
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdJOGcoordinate:(JOGCmdCoordinate)cmdCoordinate
{
    [content setProtocolID:ProtocolJOGCmd];
    [content setWriteType];
    
    JOGCmd p = {JOGCmdMode_Coordinate,cmdCoordinate};
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdJOGCommonVelocityRatio:(float)velocityRatio accelerationRatio:(float)accelerationRatio
{
    [content setProtocolID:ProtocolJOGCommonParams];
    [content setWriteType];
    
    JOGCommonParams p = {velocityRatio,accelerationRatio};
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdGetJOGCommonParams
{
    [content setProtocolID:ProtocolJOGCommonParams];
}

-(void)cmdSetJOGCommonParams:(JOGCommonParams)p
{
    [content setProtocolID:ProtocolJOGCommonParams];
    [content setWriteType];
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdGetJOGPTPJointParams{
    [content setProtocolID:ProtocolPTPJointParams];
}

-(void)cmdSetJOGPTPJointParams:(JOGJointParams)p{
    [content setProtocolID:ProtocolPTPJointParams];
    [content setWriteType];
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdGetJODPTPJointParams{
    [content setProtocolID:ProtocolPTPJointParams];
}

-(void)cmdGetJOGCoordinateParams
{
    [content setProtocolID:ProtocolJOGCoordinateParams];
}

-(void)cmdSetJOGCoordinateParams:(JOGCoordinateParams)p
{
    [content setProtocolID:ProtocolJOGCoordinateParams];
    [content setWriteType];
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}



-(void)cmdGetPTPCoordinateParams
{
    [content setProtocolID:ProtocolPTPCoordinateParams];
}

-(void)cmdSetPTPCoordinateParams:(PTPCoordinateParams)p
{
    [content setProtocolID:ProtocolPTPCoordinateParams];
    [content setWriteType];
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdGetPTPCommonParams{
    [content setProtocolID:ProtocolPTPCommonParams];
}

-(void)cmdSetPTPCommonParams:(PTPCommonParams)p{
    [content setProtocolID:ProtocolPTPCommonParams];
    [content setWriteType];
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdGetPTPJumpParams{
    [content setProtocolID:ProtocolPTPJumpParams];
}

-(void)cmdSetPTPJumpParams:(PTPJumpParams)p{
    [content setProtocolID:ProtocolPTPJumpParams];
    [content setWriteType];
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdGetCPParams
{
    [content setProtocolID:ProtocolCPParams];
}

-(void)cmdSetCPParams:(CPParams)p
{
    [content setProtocolID:ProtocolCPParams];
    [content setWriteType];
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdSetEndEffectorParams:(EndEffectorParams)p;
{
    [content setProtocolID:ProtocolEndEffectorParams];
    [content setWriteType];
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdGetEndEffectorParams
{
    [content setProtocolID:ProtocolEndEffectorParams];
}


//设置/获取吸盘输出
-(void)cmdSetSuctionCupControlEnable:(BOOL)controlEnable sucked:(BOOL)isSucked
{
    [content setWriteType];
    
    [content setProtocolID:ProtocolEndEffectorSuctionCup];
    
    
    EndEffectorOutput p;
    
    if (controlEnable) {
        
        p.isCtrlEnabled = 0x01;
    }
    else
    {
        p.isCtrlEnabled = 0x00;
    }
    
    if (isSucked) {
        
        p.isOn = 0x01;
    }
    else
    {
        p.isOn = 0x00;
    }
    
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdGetSuctionCupOutput
{
    [content setProtocolID:ProtocolEndEffectorSuctionCup];
}

//设置/获取爪子输出
-(void)cmdSetGripperControlEnable:(BOOL)controlEnable grip:(BOOL)grip
{
    [content setWriteType];
    [content setProtocolID:ProtocolEndEffectorGripper];
    
    EndEffectorOutput p;
    
    if (controlEnable) {
        
        p.isCtrlEnabled = 0x01;
    }
    else
    {
        p.isCtrlEnabled = 0x00;
    }
    
    if (grip) {
        
        p.isOn = 0x01;
    }
    else
    {
        p.isOn = 0x00;
    }
    
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdGetGripper
{
    [content setProtocolID:ProtocolEndEffectorGripper];
}

-(void)cmdSetEMotorParams:(EMotor)p{
    [content setProtocolID:ProtocolSetMotor];
    [content setWriteType];
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}
-(void)cmdSetEMotorSParams:(EMotorS)p{
    [content setProtocolID:ProtocolSetMotorS];
    [content setWriteType];
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}


-(void)cmdSetLaser:(BOOL)isOn isQueue:(BOOL)isQueue
{
    [content setWriteType];
    
    [content setProtocolID:ProtocolEndEffectorLaser];
    
    
    EndEffectorOutput p;
    
    p.isCtrlEnabled = 0x01;
    
    if (isOn) {
        
        p.isOn = 0x01;
    }
    else
    {
        p.isOn = 0x00;
    }
    
    if (isQueue) {
        
        [content setQueueType];
    }
    
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdGetLaser
{
    [content setProtocolID:ProtocolEndEffectorLaser];
}

-(void)cmdPTP:(PTPCmd)p
{
    [content setProtocolID:ProtocolPTPCmd];
    [content setWriteType];
    [content setQueueType];
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdCP:(CPCmd)p
{
    [content setProtocolID:ProtocolCPCmd];
    [content setWriteType];
    [content setQueueType];
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdCPLE:(CPCmd)p
{
    [content setProtocolID:ProtocolCPLECmd];
    [content setWriteType];
    [content setQueueType];
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdGetDeviceWithL
{
    [content setProtocolID:ProtocolDeviceWithL];
}

-(void)cmdSetDeviceWithLOn:(BOOL)isOn
{
    WithL p;
    
    p.on = isOn;
    
    [self cmdSetDeviceWithL:p];
}

-(void)cmdGetPoseL
{
    [content setProtocolID:ProtocolGetPoseL];
}

-(void)cmdSetDeviceWithL:(WithL)p
{
    [content setProtocolID:ProtocolDeviceWithL];
    [content setWriteType];
    
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdGetPTPLParams
{
    [content setProtocolID:ProtocolPTPLParams];
}
-(void)cmdSetPTPLParams:(PTPLParams)p isQueue:(BOOL)isQueue
{
    [content setProtocolID:ProtocolPTPLParams];
    [content setWriteType];
    
    if (isQueue) {
        
        [content setQueueType];
    }
    
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdPTPL:(PTPLCmd)p
{
    [content setProtocolID:ProtocolPTPWithLCmd];
    [content setWriteType];
    [content setQueueType];
    
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdSetWait:(uint32_t)timeOut isQueue:(BOOL)isQueue
{
    [content setProtocolID:ProtocolWAITCmd];
    [content setWriteType];
    
    if (isQueue) {
        [content setQueueType];
    }
    
    [content paramsWithP:&timeOut andLength:sizeof(typeof(timeOut))];
}


-(void)cmdSetIOMultiplexing:(IOMultiplexing)p isQueue:(BOOL)isQueue
{
    [content setProtocolID:ProtocolIOMultiplexing];
    [content setWriteType];
    
    if (isQueue) {
        
        [content setQueueType];
    }
    
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
    
}
-(void)cmdGetIOMultiplexing:(IOMultiplexing)p
{
    [content setProtocolID:ProtocolIOMultiplexing];
    
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}


//设置/读取IO输出电平
-(void)cmdSetIODO:(IODO)p isQueue:(BOOL)isQueue
{
    
    [content setProtocolID:ProtocolIODO];
    [content setWriteType];
    
    if (isQueue) {
        
        [content setQueueType];
    }
    
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdGetIODO:(IODO)p
{
    [content setProtocolID:ProtocolIODO];
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

//设置/读取IO PWM
-(void)cmdSetIOPWM:(IOPWM)p isQueue:(BOOL)isQueue
{
    [content setProtocolID:ProtocolIOPWM];
    [content setWriteType];
    
    if (isQueue) {
        
        [content setQueueType];
    }
    
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}
-(void)cmdGetIOPWM:(IOPWM)p
{
    [content setProtocolID:ProtocolIOPWM];
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

//读取IO 输入电平
-(void)cmdGetIODI:(IODI)p
{
    [content setProtocolID:ProtocolIODI];
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

//读取IO 模数转换值
-(void)cmdGetIOADC:(IOADC)p
{
    [content setProtocolID:ProtocolIOADC];
    [content paramsWithP:&p andLength:sizeof(typeof(p))];
}

-(void)cmdClearQueue
{
    [content setProtocolID:ProtocolQueuedCmdClear];
    [content setWriteType];
}

-(void)cmdStartQueue
{
    [content setProtocolID:ProtocolQueuedCmdStartExec];
    [content setWriteType];
}

-(void)cmdStopQueue
{
    [content setProtocolID:ProtocolQueuedCmdStopExec];
    [content setWriteType];
}

-(void)cmdForceStopQueue
{
    [content setProtocolID:ProtocolQueuedCmdForceStopExec];
    [content setWriteType];
}

-(void)cmdQueueCurrentIndex
{
    [content setProtocolID:ProtocolQueuedCmdCurrentIndex];
}

-(void)cmdQueueLeftSpace
{
    [content setProtocolID:ProtocolQueuedCmdLeftSpace];
}


#pragma mark - setter && getter

- (NSArray *)contentDatas{
    
    NSMutableArray *contents = [NSMutableArray array];
    
    NSMutableData *msg = [[NSMutableData alloc] initWithCapacity:512];
    
    NSData *payloadContent = [content contentData];
    
    NSInteger payloadLength = payloadContent.length;
    
    contentLength = (uint8_t)payloadLength;
    
    [msg appendBytes:&prefix length:sizeof(prefix)];
    [msg appendBytes:&contentLength length:sizeof(contentLength)];
    [msg appendData:payloadContent];
    
    uint8_t body[payloadLength];
    
    [payloadContent getBytes:&body length:payloadLength];
    
    checksum = calculateChecksum(body, payloadLength);
    
    [msg appendBytes:&checksum length:sizeof(checksum)];
    
    NSInteger length = msg.length;
    
    NSInteger loc = 0;
    
    while (length > 0) {
        NSInteger avilableLength = length > 20?20:length;
        NSMutableData *data = [NSMutableData data];
        [data appendBytes:(msg.bytes + loc) length:avilableLength];
        [contents addObject:data];
        length = length - avilableLength;
        loc = loc + avilableLength;
    }
    
    return contents.mutableCopy;
}

- (CmdType)type{
    return self->content.cmdType;
}


uint8_t calculateChecksum(uint8_t *p, uint8_t len)
{          //校验函数
    uint8_t sum = 0;
    for (int i = 0; i < len; i++) {

        sum += *(p+i);
    }
    sum = 0 - sum;
    
    sum &= 0xFF;
    return sum;
}

unsigned int charLen(char *c){
    unsigned int i = 0;
    while (*c++) {
        i ++;
    }
    return i;
}

@end
