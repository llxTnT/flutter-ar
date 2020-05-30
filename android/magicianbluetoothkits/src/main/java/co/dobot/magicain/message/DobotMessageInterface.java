package co.dobot.magicain.message;

import co.dobot.magicain.message.cmd.CMDParams;

/**
 * Created by x on 2018/5/17.
 */

public interface DobotMessageInterface {
    byte getCommandID();

    boolean isQueue();

    byte[] getParams();

    void setWriteMode(boolean isWrite);

    void setQueueMode(boolean isQueue);

    void cmdSetDeviceSN(String sn);

    void cmdGetDeviceSN();

    void cmdSetDeviceName(String name);

    void cmdGetDeviceName();

    void cmdGetPose();

    void cmdSetPose(CMDParams.Pose pose);

    void cmdResetPose(CMDParams.ResetPose resetPose);

    void cmdGetAlarmsState();

    void cmdClearAllAlarmsState();

    void cmdGetKinematics();

    void cmdGetHomeParams();

    void cmdSetHomeParams(CMDParams.HomeParams homeParams);

    void cmdSetHomeCmd(CMDParams.HomeCmd homeCmd);

    void cmdSetHomeCmdQueue(CMDParams.HomeCmd homeCmd);

    void cmdHome();

    void cmdGetHHTTrigeMode();

    void cmdSetHHTTrigeMode(CMDParams.HHTTrigMode trigMode);

    void cmdSetHHTTrigOutputEnabled(boolean isEnable);

    void cmdGetHHTTrigOutputEnabled();

    void cmdGetHHTTrigOutput();

    void cmdJOG(CMDParams.JOGCmd jogCmd, boolean isQueue);

    void cmdGetJOGJointParams();

    void cmdSetJOGJointParams(CMDParams.JOGJointParams jogJointParams);

    void cmdGetJOGCoordinateParams();

    void cmdSetJOGCoordinateParams(CMDParams.JOGCoordinateParams jogCoordinateParams);

    void cmdGetJOGLParams();

    void cmdSetJOGLParams(CMDParams.JOGLParams joglParams);

    void cmdJOGCommonVelocityRatio(float velocityRatio, float accelerationRatio);

    void cmdGetJOGCommonParams();

    void cmdSetJOGCommonParams(CMDParams.JOGCommonParams params);

    void cmdPTP(CMDParams.PTPCmd ptpCmd);

    void cmdGetPTPJointParams();

    void cmdSetPTPJointParams(CMDParams.PTPJointParams ptpJointParams);

    void cmdGetPTPCoordinateParams();

    void cmdSetPTPCoordinateParams(CMDParams.PTPCoordinateParams ptpCoordinateParams);

    void cmdGetPTPCommonParams();

    void cmdSetPTPCommonParams(CMDParams.PTPCommonParams ptpCommonParams);

    void cmdGetPTPJumpParams();

    void cmdSetPTPJumpParams(CMDParams.PTPJumpParams ptpJumpParams);

    void cmdGetPTPJump2Params();

    void cmdSetPTPJump2Params(CMDParams.PTPJump2Params ptpJump2Params);

    void cmdGetPTPLParams();

    void cmdSetPTPLParams(CMDParams.PTPLParams params, boolean isQueue);

    void cmdSetPTPWithL(CMDParams.PTPLCmd ptplCmd);

    void cmdSetPTPPO(CMDParams.PTPCmd ptpCmd, CMDParams.PTPPOCmd ptpPOCmd[]);

    void cmdSetPTPPOWithL(CMDParams.PTPLCmd ptplCmd, CMDParams.PTPPOCmd ptpPOCmd[]);

    void cmdGetCPParams();

    void cmdSetCPParams(CMDParams.CPParams params);

    void cmdSetEndEffectorParams(CMDParams.EndEffectorParams params);

    void cmdGetEndEffectorParams();

    void cmdSetSuctionCupControlEnable(boolean controlEnable, boolean isSucked);

    void cmdGetSuctionCupOutput();

    void cmdSetGripperControlEnable(boolean controlEnable, boolean grip);

    void cmdGetGripper();

    void cmdSetLaser(boolean isOn, boolean isQueue);

    void cmdGetLaser();

    void cmdCP(CMDParams.CPCmd cpCmd);

    void cmdCPLE(CMDParams.CPCmd cpCmd);

    void cmdGetDeviceWithL();

    void cmdSetDeviceWithLOn(boolean isOn);

    void cmdGetPoseL();

    void cmdSetDeviceWithL(CMDParams.WithL l);

    void cmdSetWait(CMDParams.WaitCmd waitCmd, boolean isQueue);

    void cmdSetTrig(CMDParams.TrigCmd trigCmd, boolean isQueue);

    void cmdSetIOMultiplexing(CMDParams.IOMultiplexing multiplexing, boolean isQueue);

    void cmdGetIOMultiplexing(CMDParams.IOMultiplexing multiplexing);

    void cmdSetIODO(CMDParams.IODO iodo, boolean isQueue);

    void cmdGetIODO(CMDParams.IODO iodo);

    void cmdSetIOPWM(CMDParams.IOPWM iopwm, boolean isQueue);

    void cmdGetIOPWM(CMDParams.IOPWM iopwm);

    void cmdGetIODI(CMDParams.IODI iodi);

    void cmdGetIOADC(CMDParams.IOADC ioadc);

    void cmdSetEMotor(CMDParams.EMotor eMotor, boolean isQueue);

    void cmdGetColorSensor();

    void cmdSetColorSensor(CMDParams.DeviceSensor deviceSensor);

    void cmdGetInfraredSensor();

    void cmdSetInfraredSensor(CMDParams.DeviceSensor deviceSensor);

    void cmdSetLostStepParams(CMDParams.LostStepParams lostStepParams);

    void cmdSetLostStep(boolean isQueue);

    void cmdClearQueue();

    void cmdStartQueue();

    void cmdStopQueue();

    void cmdForceStopQueue();

    void cmdQueueCurrentIndex();

    void cmdQueueLeftSpace();
}
