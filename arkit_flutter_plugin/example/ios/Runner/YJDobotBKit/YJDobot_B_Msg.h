//
//  YJDobot_B_Msg.h
//  YJDobotKit_B
//
//  Created by 刘清 on 2019/7/18.
//  Copyright © 2019 YuejianTechnology.com.www. All rights reserved.
//

#import "YJDobot_B_BaseMsg.h"
#import "YJDobot_B_Header.h"


NS_ASSUME_NONNULL_BEGIN

@interface YJDobot_B_Msg : YJDobot_B_BaseMsg

@property (nonatomic, strong) NSArray *contentDatas;
@property (nonatomic, assign) CmdType type;


+ (instancetype)cmdMsgWithCmdString:(NSString *)cmdString;//传命令对应的string 获取命令

#pragma mark - 设备信息相关

-(void)cmdGetDeviceSn;//获取设备序列号
-(void)cmdSetDeviceSn:(char *)sn;//设置设备序列号

-(void)cmdGetDeviceName;//获取设备名字
-(void)cmdSetDeviceName:(char *)name;//设置设备名字

-(void)cmdGetDeviceUid;//读取设备uuid

-(void)cmdGetDeviceVersion;//读取固件信息

#pragma mark - 获取参数

-(void)cmdGetPose;//获取实时位置
-(void)cmdSetPose:(Pose)pose;//设置实时位置

-(void)cmdResetPose:(ResetPose)p;//重设实时位置

-(void)cmdGetKinematices;

//设置／读取回零参数
-(void)cmdGetHOMEParams;
-(void)cmdSetHOMEParams:(HomeParams)homeParams;
-(void)cmdSetHOMECmd:(HOMECmd)p;
-(void)cmdSetHOMECmdQueue:(HOMECmd)p;

//执行回零操作
-(void)cmdHome;

-(void)cmdGetHHTTrigeMode;
-(void)cmdSetHHTTrigeMode:(HHTTrigMode)mode;

//获取警报状态
-(void)cmdGetAlarmState;
-(void)cmdClearAllAlarms;

//设置丢步阈值
-(void)cmdSetLostStep:(float)value;
-(void)cmdSetDoLostStep;

//执行点动
-(void)cmdJog:(JOGCmd)jogCmd;

//执行坐标轴点动
-(void)cmdJOGcoordinate:(JOGCmdCoordinate)cmdCoordinate;

//点动公共参数 速度比例 加速度比例
-(void)cmdJOGCommonVelocityRatio:(float)velocityRatio accelerationRatio:(float)accelerationRatio;

//点动公共参数
-(void)cmdGetJOGCommonParams;
-(void)cmdSetJOGCommonParams:(JOGCommonParams)p;

//获取坐标轴点动参数
-(void)cmdGetJOGCoordinateParams;
-(void)cmdSetJOGCoordinateParams:(JOGCoordinateParams)params;

//PTP关节点动参数
-(void)cmdGetJOGPTPJointParams;
-(void)cmdSetJOGPTPJointParams:(JOGJointParams)params;


//PTP坐标轴点位参数
-(void)cmdGetPTPCoordinateParams;
-(void)cmdSetPTPCoordinateParams:( PTPCoordinateParams)params;

//设置和获取点位公共参数

-(void)cmdGetPTPCommonParams;
-(void)cmdSetPTPCommonParams:(PTPCommonParams)params;

//PTP门型点动参数
-(void)cmdGetPTPJumpParams;
-(void)cmdSetPTPJumpParams:(PTPJumpParams)params;

//执行PTP
-(void)cmdPTP:(PTPCmd)p;

//获取cp参数
-(void)cmdGetCPParams;
-(void)cmdSetCPParams:(CPParams)p;

//设置末端参数
-(void)cmdSetEndEffectorParams:(EndEffectorParams)p;
-(void)cmdGetEndEffectorParams;

//设置/获取吸盘输出
-(void)cmdSetSuctionCupControlEnable:(BOOL)controlEnable sucked:(BOOL)isSucked;
-(void)cmdGetSuctionCupOutput;

//设置/获取爪子输出
-(void)cmdSetGripperControlEnable:(BOOL)controlEnable grip:(BOOL)grip;
-(void)cmdGetGripper;

//设置电机
-(void)cmdSetEMotorParams:(EMotor)params;
-(void)cmdSetEMotorSParams:(EMotorS)params;


//激光
-(void)cmdSetLaser:(BOOL)isOn isQueue:(BOOL)isQueue;
-(void)cmdGetLaser;

//执行CP
-(void)cmdCP:(CPCmd)p;
-(void)cmdCPLE:(CPCmd)p;

//导轨相关
-(void)cmdGetDeviceWithL;
-(void)cmdSetDeviceWithLOn:(BOOL)isOn;
-(void)cmdGetPoseL;
-(void)cmdGetPTPLParams;
-(void)cmdSetPTPLParams:(PTPLParams)p isQueue:(BOOL)isQueue;
-(void)cmdPTPL:(PTPLCmd)p;

//设置EIO复用
-(void)cmdSetIOMultiplexing:(IOMultiplexing)ioMultiplexing isQueue:(BOOL)isQueue;
-(void)cmdGetIOMultiplexing:(IOMultiplexing)ioMultiplexing;

//设置/读取IO输出电平
-(void)cmdSetIODO:(IODO)ioDO isQueue:(BOOL)isQueue;
-(void)cmdGetIODO:(IODO)ioDO;

//设置/读取IO PWM
-(void)cmdSetIOPWM:(IOPWM)ioPWM isQueue:(BOOL)isQueue;
-(void)cmdGetIOPWM:(IOPWM)ioPWM;

//读取IO 输入电平
-(void)cmdGetIODI:(IODI)ioDI;

//读取IO 模数转换值
-(void)cmdGetIOADC:(IOADC)ioADC;

-(void)cmdSetWait:(uint32_t)timeOut isQueue:(BOOL)isQueue;


//队列命令
-(void)cmdClearQueue;//清除队列
-(void)cmdStartQueue;//开启队列
-(void)cmdStopQueue;//暂停队列
-(void)cmdForceStopQueue;//强制暂停队列

-(void)cmdQueueCurrentIndex;//获取执行索引
-(void)cmdQueueLeftSpace;//获取队列剩余空间


@end

NS_ASSUME_NONNULL_END
