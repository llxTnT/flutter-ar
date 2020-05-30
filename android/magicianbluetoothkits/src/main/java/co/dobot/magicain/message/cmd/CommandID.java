package co.dobot.magicain.message.cmd;

/**
 * Created by x on 2018/1/17.
 */

public interface CommandID {
    byte CMD_DEVICE_SN = 0;
    byte CMD_DEVICE_NAME = 1;
    byte CMD_DEVICE_VERSION = 2;
    byte CMD_DEVICE_WITH_L = 3;

    byte CMD_POSE = 10;
    byte CMD_RESET_POSE = 11;
    byte CMD_GET_KINEMATICS = 12;
    byte CMD_GET_POSE_L = 13;

    byte CMD_ALARMS_STATE = 20;

    byte CMD_HOME_PARAMS = 30;
    byte CMD_HOME_CMD = 31;

    byte CMD_HHT_TRIG_MODE = 40;
    byte CMD_HHT_TRIG_OUTPUT_ENABLED = 41;
    byte CMD_HHT_TRIG_OUTPUT = 42;

    byte CMD_ARM_ORIENTATION = 50;

    byte CMD_END_EFFECT_PARAMS = 60;
    byte CMD_END_EFFECT_LASER = 61;
    byte CMD_END_EFFECT_SUCTIONCUP = 62;
    byte CMD_END_EFFECT_GRIPPER = 63;

    byte CMD_JOG_JOINT_PARAMS = 70;
    byte CMD_JOG_COORDINATE_PARAMS = 71;
    byte CMD_JOG_COMMON_PARAMS = 72;
    byte CMD_JOG_CMD = 73;
    byte CMD_JOG_L_CMD = 74;

    byte CMD_PTP_JOINT_PARAMS = 80;
    byte CMD_PTP_COORDINATE_PARAMS = 81;
    byte CMD_PTP_JUMP_PARAMS = 82;
    byte CMD_PTP_COMMON_PARAMS = 83;
    byte CMD_PTP_CMD = 84;
    byte CMD_PTP_L_PARAMS = 85;
    byte CMD_PTP_WITH_L = 86;
    byte CMD_PTP_JUMP_2_PARAMS = 87;
    byte CMD_PTP_PO = 88;
    byte CMD_PTP_PO_WITH_L = 89;

    byte CMD_CP_PARAMS = 90;
    byte CMD_CP = 91;
    byte CMD_CP_LE = 92;

    byte CMD_ARC_PARAMS = 100;
    byte CMD_ARC = 101;

    byte CMD_WAIT = 110;

    byte CMD_TRIG = 120;

    byte CMD_IO_MULTIPLEXING = (byte) 130;
    byte CMD_IO_DO = (byte) 131;
    byte CMD_IO_PWM = (byte) 132;
    byte CMD_IO_DI = (byte) 133;
    byte CMD_IO_ADC = (byte) 134;
    byte CMD_EMOTOR= (byte) 135;
    byte CMD_COLOR_SENSOR= (byte) 137;
    byte CMD_IR_SWITCH= (byte) 138;

    byte CMD_ANGLE_SENSOR_STATIC_ERROR = (byte) 140;

    byte CMD_LOST_STEP_PARAMS = (byte) 170;
    byte CMD_LOST_STEP = (byte) 171;

    byte CMD_USER_PARAMS = (byte) 220;

    byte CMD_ZDF_CALIB_REQUEST = (byte) 230;
    byte CMD_ZDF_CALIB_STATUS = (byte) 231;

    byte CMD_QUEUE_START_EXEC= (byte) 240;
    byte CMD_QUEUE_STOP_EXEC= (byte) 241;
    byte CMD_QUEUE_FORCE_STOP_EXEC= (byte) 242;
    byte CMD_QUEUE_START_DOWNLOAD= (byte) 243;
    byte CMD_QUEUE_STOP_DOWNLOAD= (byte) 244;
    byte CMD_QUEUE_CLEAR= (byte) 245;
    byte CMD_QUEUE_CURRENT_INDEX= (byte) 246;
    byte CMD_QUEUE_GET_LEFT_SPACE = (byte) 247;
}
