//
//  YJDobot_B_Struct.h
//  YJDobotKit_B
//
//  Created by 刘清 on 2019/7/18.
//  Copyright © 2019 YuejianTechnology.com.www. All rights reserved.
//

#ifndef YJDobot_B_Struct_h
#define YJDobot_B_Struct_h

#pragma pack(1)

typedef NS_ENUM(NSUInteger, ProtocolID) {
    // Device information
    //    ProtocolFunctionDeviceInfoBase = 0,
    ProtocolDeviceSN = 0,
    ProtocolDeviceName,
    ProtocolDeviceVersion,
    ProtocolDeviceWithL,
    
    ProtocolDeviceUid = 5,
    
    // Pose
    //    ProtocolFunctionPoseBase = 10,
    ProtocolGetPose = 10,
    ProtocolResetPose,
    ProtocolGetKinematics,
    ProtocolGetPoseL = 13,
    
    // Alarm
    //    ProtocolFunctionALARMBase = 20,
    ProtocolAlarmsState = 20,
    
    // HOME
    //    ProtocolFunctionHOMEBase = 30,
    ProtocolHOMEParams = 30,
    ProtocolHOMECmd,
    
    // HHT
    //    ProtocolFunctionHHTBase = 40,
    
    ProtocolHHTTrigMode = 40,
    ProtocolHHTTrigOutputEnabled,
    ProtocolHHTTrigOutput,
    
    // Function-Arm Orientation
    //    ProtocolFunctionArmOrientationBase = 50,
    ProtocolArmOrientation = 50,
    
    // End effector
    //    ProtocolFunctionEndEffectorBase = 60,
    ProtocolEndEffectorParams = 60,
    ProtocolEndEffectorLaser,
    ProtocolEndEffectorSuctionCup,
    ProtocolEndEffectorGripper,
    
    // Function-JOG
    //    ProtocolFunctionJOGBase = 70,
    ProtocolJOGJointParams = 70,
    ProtocolJOGCoordinateParams,
    ProtocolJOGCommonParams,
    ProtocolJOGCmd,
    
    // Function-PTP
    //    ProtocolFunctionPTPBase = 80,
    
    ProtocolPTPJointParams = 80,
    ProtocolPTPCoordinateParams,
    ProtocolPTPJumpParams,
    ProtocolPTPCommonParams,
    ProtocolPTPCmd,
    ProtocolPTPLParams,
    ProtocolPTPWithLCmd,
    ProtocolPTPJump2Params,
    ProtocolPTPPOCmd,
    ProtocolPTPPOWithLCmd,
    
    // Function-CP
    //    ProtocolFunctionCPBase = 90,
    
    ProtocolCPParams = 90,
    ProtocolCPCmd,
    ProtocolCPLECmd,//激光灰度雕刻
    
    // Function-ARC
    //    ProtocolFunctionARCBase = 100,
    
    ProtocolARCParams = 100,
    ProtocolARCCmd,
    
    // Function-WAIT
    //    ProtocolFunctionWAITBase = 110,
    ProtocolWAITCmd = 110,
    
    // Function-TRIG
    //    ProtocolFunctionTRIGBase = 120,
    ProtocolTRIGCmd = 120,
    
    // Function-EIO
    //    ProtocolFunctionEIOBase = 130,
    
    ProtocolIOMultiplexing = 130,
    ProtocolIODO,
    ProtocolIOPWM,
    ProtocolIODI,
    ProtocolIOADC,
    ProtocolSetMotor,
    ProtocolSetMotorS,
    
    // Function-CAL
    //    ProtocolFunctionCALBase = 140,
    ProtocolAngleSensorStaticError = 140,
    
    // Function-TEST
    //    ProtocolTESTBase = 220,
    ProtocolSetLostStepValue = 170,
    ProtocolSetDoLostStep,
    
    ProtocolUserParams = 220,
    
    // Function-ZDF
    //    ProtocolZDFBase = 230,
    ProtocolZDFCalibRequest = 230,
    ProtocolZDFCalibStatus,
    
    // Function-QueuedCmd
    //    ProtocolFunctionQueuedCmdBase = 240,
    ProtocolQueuedCmdStartExec = 240,
    ProtocolQueuedCmdStopExec,
    ProtocolQueuedCmdForceStopExec,
    ProtocolQueuedCmdStartDownload,
    ProtocolQueuedCmdStopDownload,
    ProtocolQueuedCmdClear,
    ProtocolQueuedCmdCurrentIndex,
    ProtocolQueuedCmdLeftSpace,
    
    ProtocolMax = 256
} ;

typedef NS_ENUM(NSUInteger, CmdType) {
    CmdNormal = 0,
    CmdPTP,
    CmdGetPose,
    CmdGetIndex,
};


typedef NS_ENUM(NSUInteger, controlTypeIsWrite) {
    controlTypeRead = 0,
    controlTypeWrite,
};

typedef NS_ENUM(NSUInteger, controlTypeIsQueue) {
    controlTypeNormal = 0,
    controlTypeQueue,
};

typedef NS_ENUM(NSInteger ,controlTypeIsExc){
    controlTypeIsMagician = 0,
    controlTypeIsExController
};

typedef struct  {
    
    unsigned int isWrite:1;//1 写   0读
    unsigned int isQueue:1;//1 队列  0非队列
    unsigned int :1;//1 异常 0正常
    unsigned int :1;//预留
    unsigned int isExc:2;//0 Magician/MagicianLite  1 ExController 2,3预留
    unsigned int :2;//0 设备0 1 设备1 2设备2 3设备3 默认0 不需要更改
    
    
}controlTypeCmd;

#pragma mark - 命令参数结构体

typedef struct {
    uint8_t typeIndex;
    uint8_t majorVersion;
    uint8_t minorVersion;
    uint8_t revision;
    
}FirmwareParam;

enum FirmwareType{
    NO_SWITCH, DOBOT_SWITCH, PRINTING_SWITCH, DRIVER1_SWITCH, DRIVER2_SWITCH, DRIVER3_SWITCH, DRIVER4_SWITCH, DRIVER5_SWITCH, FPGA_SWITCH, SWITCH_FM_MAX
};

//位姿
typedef struct {
    
    float x;
    float y;
    float z;
    float r;
    float jointAngle[4];
}Pose;

//导轨位姿
typedef struct {
    
    float l;
}PoseL;


//重设位姿
typedef struct {
    
    uint8_t manual;
    float rearArmAngle;
    float frontArmAngle;
}ResetPose;

//运动学参数

typedef struct {
    
    float velocity;
    float acceleration;
}Kinematics;

//回零参数
typedef struct  {
    
    float velocity[4];
    float acceleration[4];
}HomeParams;

//触发模式
typedef NS_ENUM(NSUInteger,HHTTrigMode){
    
    TriggeredOnKeyReleased,//按键释放时更新
    TriggeredOnPeriodicInterval,//定时触发
};

//末端参数
typedef struct {
    
    float xBias;
    float yBias;
    float zBias;
}EndEffectorParams;

//末端输出
typedef struct {
    uint8_t isCtrlEnabled;
    uint8_t isOn;
    
}EndEffectorOutput;

typedef struct {
    uint8_t status[16];
}AlarmStatus;

typedef NS_ENUM(NSUInteger,EndEffectorType){
    
    EndEffectorType_Pen,
    EndEffectorType_Laser,
    EndEffectorType_SuctionCup,
    EndEffectorType_Gripper,
    
};


//笔
static EndEffectorParams EndEffector_Pen = {
    61.0f,
    0.0f,
    0.0f,
};

//激光
static EndEffectorParams EndEffector_Laser = {
    70.0f,
    0.0f,
    0.0f,
};


//手臂方向
typedef NS_ENUM(NSUInteger,ArmOrientation){
    
    ArmOrientationLeft,
    ArmOrientationRight,
};


//点动公共参数
typedef struct  {
    
    float velocityRatio; //1-100 速度比例
    float accelerationRatio;//1-100 加速度比例
}JOGCommonParams;


//关节点动参数
typedef struct  {
    float velocity[4];     //每个关节的最大速度
    float acceleration[4]; //每个关节的最大加速度
}JOGJointParams;

//坐标轴点动参数
typedef struct  {
    
    float velocity[4];      //每个坐标轴方向的最大速度
    float acceleration[4];  //每个坐标轴方向的最大加速度
}JOGCoordinateParams;

//执行点动命令
typedef struct  {
    
    uint8_t isJoint;//1 关节点动  0 坐标轴点动
    uint8_t cmd;//命令 0 停止（按键抬起） 坐标轴点动时：1:X+ 2:X- 3:Y+ 4:Y- 5:Z+ 6:Z- 7:R+ 8:R-
}JOGCmd;

//点动命令 mode（坐标轴／关节）
typedef NS_ENUM(NSUInteger,JOGCmdMode){
    
    JOGCmdMode_Coordinate = 0,//坐标轴
    JOGCmdMode_Joint,         //关节
};

//坐标轴点动命令
typedef NS_ENUM(NSUInteger,JOGCmdCoordinate){
    
    JOGCmdCoordinate_Stop = 0,  //停止
    JOGCmdCoordinate_Xplus,     //x+/J1+
    JOGCmdCoordinate_Xminus,    //x-/J1-
    JOGCmdCoordinate_Yplus,     //y+/J2+
    JOGCmdCoordinate_Yminus,    //y-/J2-
    JOGCmdCoordinate_Zplus,     //z+/J3+
    JOGCmdCoordinate_Zminus,    //z-/J3-
    JOGCmdCoordinate_Rplus,     //r+/J4+
    JOGCmdCoordinate_Rminus,    //r-/J4-
};

//PTP mode
typedef NS_ENUM(uint8_t,PTPMode){
    
    PTPMode_Jump = 0x00,   //门型模式
    PTPMode_MoveJ,      //关节模式
    PTPMode_MoveL,      //直线模式
    
    PTPMode_Jump_Angle,
    PTPMode_MoveJ_Angle,
    PTPMode_MoveL_Angle,
    
    PTPMode_MoveJ_increment,
    PTPMode_MoveL_increment,
    
};

typedef  struct {
    float vaule;
}LostStepValue;

//PTP命令
typedef  struct   {
    
    PTPMode ptpMode;
    float x;
    float y;
    float z;
    float r;
}PTPCmd;

//PTP坐标轴点位参数
typedef struct  {
    
    float xyzVelocity;
    float rVelocity;
    float xyzAcceleration;
    float rAcceleration;
}PTPCoordinateParams;

typedef struct  {
    float velocityRatio; //1-100 速度比例
    float accelerationRatio;//1-100 加速度比例
}PTPCommonParams;

typedef struct  {
    float jumpHeight;
    float zLimit;
}PTPJumpParams;


//CP参数
typedef struct{
    
    float planAcc;
    float junctionVel;
    union{
        float acc; //realTimeTrack = false
        float period; //realTimeTrack = true
    };
    uint8_t realTimeTrack;
    
}CPParams;

static CPParams CPParams_Pen = {
    100.0f,
    100.0f,
    100.0f,
    0x00,
};

static CPParams CPParams_Laser = {
    10.0f,
    10.0f,
    10.0f,
    0x00,
};

//激光灰度雕刻
static CPParams CPParams_LE = {
    5.0f,
    5.0f,
    5.0f,
    0x00,
};


//激光切割
static CPParams CPParams_LC = {
    0.1f,
    0.1f,
    0.1f,
    0x00,
};



typedef NS_ENUM(uint8_t,CPMode){
    
    CPMode_relative = 0x00,
    CPMode_absolute,
};

//CP命令
typedef struct{
    
    CPMode cpMode;
    float x;
    float y;
    float z;
    union{
        
        float velocity;
        float power;//0-100
    };
}CPCmd;



//QueueLeftSpace
typedef struct{
    
    uint32_t count;
}QueueLeftSpace;


//IO复用
typedef struct{
    uint8_t address;//地址 1- 20
    uint8_t multiplex;// IOFunctiion
}IOMultiplexing;


typedef NS_ENUM(NSUInteger,IOFunction){
    
    IOFunctionDummy,//不配置功能
    IOFunctionPWM,
    IOFunctionDO,
    IOFunctionDI,
    IOFunctionADC,
};

//I/O 输出电平
typedef struct{
    uint8_t address;
    uint8_t level;//0:低电平，1:高电平
}IODO;

//I/O PWM输出
typedef struct{
    uint8_t address;
    uint8_t frequency;//频率，10HZ-1MHZ
    uint8_t dutyCycle;//占空比 0-100
}IOPWM;

//I/O 输入电平
typedef struct{
    uint8_t address;
    uint8_t level;
}IODI;

//I/O 模数转换值
typedef struct{
    uint8_t address;
    uint16_t value;
}IOADC;

//扩展电机接口
typedef struct{
    uint8_t index;
    uint8_t isEnable;
    float speed;
}EMotor;

typedef struct{
    uint8_t index;
    uint8_t isEnable;
    float speed;
    float distance;
}EMotorS;

//导轨相关
typedef struct{
    
    uint8_t on;//0:关闭 1:开启
}WithL;

typedef struct{
    float velocity;        //PTP模式下4轴关节速度
    float acceleration;   //PTP模式下4轴关节加速度
}PTPLParams;

typedef struct{
    
    PTPMode ptpMode;  //PTP模式 (取值范围0~8)
    float x;           //x,y,z,r为ptpMode运动方式的参数，可为坐标、
    //关节角度、或者坐标/角度增量
    float y;
    float z;
    float r;
    float l;
}PTPLCmd;

typedef struct{
    uint32_t reserved;  // 预留未来使用
} HOMECmd;

typedef struct {
    float x;
    float y;
    float z;
    float r;
}HOMEParams;

#pragma options align=reset

#endif /* YJDobot_B_Struct_h */
