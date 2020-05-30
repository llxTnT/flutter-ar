package co.dobot.magicain.message.cmd;

import android.os.ParcelUuid;

import co.dobot.magicain.utils.TransformUtils;

import static co.dobot.magicain.message.cmd.CMDParams.HHTTrigMode.TRIGGERED_ON_KEY_RELEASED;
import static co.dobot.magicain.message.cmd.CMDParams.HHTTrigMode.TRIGGERED_ON_PERIODIC_INTERVAL;


/**
 * Created by x on 2018/5/15.
 */

public class CMDParams {
    public interface CMDParamsTransInterFace {
        void initDataByBytes(byte[] bytes);

        byte[] transDataToBytes();
    }

    public static class DeviceSN implements CMDParamsTransInterFace {
        public String sn;

        @Override
        public void initDataByBytes(byte[] bytes) {
            sn = TransformUtils.bytesToString(bytes);
        }

        @Override
        public byte[] transDataToBytes() {
            return TransformUtils.stringToBytes(sn);
        }
    }

    public static class DeviceName implements CMDParamsTransInterFace {
        public String name;

        @Override
        public void initDataByBytes(byte[] bytes) {
            name = TransformUtils.bytesToString(bytes);
        }

        @Override
        public byte[] transDataToBytes() {
            return TransformUtils.stringToBytes(name);
        }
    }

    public static class Pose implements CMDParamsTransInterFace {
        public static final int POSE_DATA_BYTE_LENGTH = 8 * 4;
        public float x;
        public float y;
        public float z;
        public float r;
        public float[] jointAngle = new float[4];

        @Override
        public void initDataByBytes(byte[] bytes) {
            int i = 0;
            x = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            y = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            z = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            r = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            jointAngle[0] = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            jointAngle[1] = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            jointAngle[2] = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            jointAngle[3] = TransformUtils.bytesToFloat(bytes, i);

        }

        @Override
        public byte[] transDataToBytes() {
            byte[] temp = new byte[POSE_DATA_BYTE_LENGTH];
            int i = 0;
            System.arraycopy(TransformUtils.floatToBytes(x), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(y), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(z), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(r), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(jointAngle[0]), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(jointAngle[1]), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(jointAngle[2]), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(jointAngle[3]), 0, temp, i, 4);
            return temp;
        }
    }

    public static class PoseL implements CMDParamsTransInterFace {
        public float poseL;

        @Override
        public void initDataByBytes(byte[] bytes) {
            poseL = TransformUtils.bytesToFloat(bytes, 0);
        }

        @Override
        public byte[] transDataToBytes() {
            return TransformUtils.floatToBytes(poseL);
        }
    }

    public static class ResetPose implements CMDParamsTransInterFace {
        public static final int RESET_POSE_DATA_BYTE_LENGTH = 2 * 4 + 1;
        public byte manual;
        public float rearArmAngle;
        public float frontArmAngle;

        @Override
        public void initDataByBytes(byte[] bytes) {
            manual = bytes[0];
            rearArmAngle = TransformUtils.bytesToFloat(bytes, 1);
            frontArmAngle = TransformUtils.bytesToFloat(bytes, 5);
        }

        @Override
        public byte[] transDataToBytes() {
            byte[] temp = new byte[RESET_POSE_DATA_BYTE_LENGTH];
            temp[0] = manual;
            System.arraycopy(TransformUtils.floatToBytes(rearArmAngle), 0, temp, 1, 4);
            System.arraycopy(TransformUtils.floatToBytes(frontArmAngle), 0, temp, 5, 4);
            return temp;
        }
    }

    public static class AlarmsState implements CMDParamsTransInterFace {
        public byte[] alarmsStateData;

        @Override
        public void initDataByBytes(byte[] bytes) {
            alarmsStateData = bytes;
        }

        @Override
        public byte[] transDataToBytes() {
            return alarmsStateData;
        }
    }

    public static class Kinematics implements CMDParamsTransInterFace {
        public final static int KINEMATICS_DATA_BYTE_LENGTH = 4;
        public float velocity;
        public float acceleration;

        @Override
        public void initDataByBytes(byte[] bytes) {
            velocity = TransformUtils.bytesToFloat(bytes, 0);
            acceleration = TransformUtils.bytesToFloat(bytes, 4);
        }

        @Override
        public byte[] transDataToBytes() {
            byte[] temp = new byte[KINEMATICS_DATA_BYTE_LENGTH];
            System.arraycopy(TransformUtils.floatToBytes(velocity), 0, temp, 0, 4);
            System.arraycopy(TransformUtils.floatToBytes(acceleration), 0, temp, 4, 4);
            return temp;
        }
    }

    public static class HomeParams implements CMDParamsTransInterFace {
        public static final int HOME_PARAMS_DATA_BYTE_LENGTH = 4 * 4;
        public float x;
        public float y;
        public float z;
        public float r;

        @Override
        public void initDataByBytes(byte[] bytes) {
            int i = 0;
            x = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            y = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            z = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            r = TransformUtils.bytesToFloat(bytes, i);
        }

        @Override
        public byte[] transDataToBytes() {
            byte[] temp = new byte[HOME_PARAMS_DATA_BYTE_LENGTH];
            int i = 0;
            System.arraycopy(TransformUtils.floatToBytes(x), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(y), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(z), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(r), 0, temp, i, 4);
            return temp;
        }
    }

    public static class HomeCmd implements CMDParamsTransInterFace {
        public int reserved;

        @Override
        public void initDataByBytes(byte[] bytes) {
            reserved = TransformUtils.bytesToInt(bytes);
        }

        @Override
        public byte[] transDataToBytes() {
            return TransformUtils.intToBytes(reserved);
        }
    }

    public static class HHTTrigModeParam implements CMDParamsTransInterFace {
        public static final byte TRIGGERED_ON_KEY_RELEASED_VALUE = 0X00;
        public static final byte TRIGGERED_ON_PERIODIC_INTERVAL_VALUE = 0X01;
        public HHTTrigMode mode;
        public byte value = 0X00;

        @Override
        public void initDataByBytes(byte[] bytes) {

            value = bytes[0];
            switch (value) {
                case TRIGGERED_ON_KEY_RELEASED_VALUE:
                    mode = TRIGGERED_ON_KEY_RELEASED;
                    break;
                case TRIGGERED_ON_PERIODIC_INTERVAL_VALUE:
                    mode = TRIGGERED_ON_PERIODIC_INTERVAL;
                    break;
                default:
                    mode = TRIGGERED_ON_KEY_RELEASED;
                    break;
            }
        }

        @Override
        public byte[] transDataToBytes() {
            switch (mode) {
                default:
                case TRIGGERED_ON_KEY_RELEASED:
                    this.mode = TRIGGERED_ON_KEY_RELEASED;
                    this.value = TRIGGERED_ON_KEY_RELEASED_VALUE;
                    break;
                case TRIGGERED_ON_PERIODIC_INTERVAL:
                    this.mode = TRIGGERED_ON_KEY_RELEASED;
                    this.value = TRIGGERED_ON_PERIODIC_INTERVAL_VALUE;
                    break;
            }
            return new byte[]{value};
        }
    }

    public enum HHTTrigMode {
        TRIGGERED_ON_KEY_RELEASED,
        TRIGGERED_ON_PERIODIC_INTERVAL
    }

    public static class HHTTrigOutputEnabled implements CMDParamsTransInterFace {
        public boolean isEnable = false;

        @Override
        public void initDataByBytes(byte[] bytes) {
            if (bytes != null) {
                if (bytes[0] != 0x00)
                    isEnable = true;
                else
                    isEnable = false;
            }
        }

        @Override
        public byte[] transDataToBytes() {
            if (isEnable)
                return new byte[]{0x01};
            else
                return new byte[]{0x00};
        }
    }

    public static class HHTTrigOutput implements CMDParamsTransInterFace {
        public boolean isTriggered = false;

        @Override
        public void initDataByBytes(byte[] bytes) {
            if (bytes != null) {
                if (bytes[0] != 0x00)
                    isTriggered = true;
                else
                    isTriggered = false;
            }
        }

        @Override
        public byte[] transDataToBytes() {
            if (isTriggered)
                return new byte[]{0x01};
            else
                return new byte[]{0x00};
        }
    }

    public static class JOGCmd implements CMDParamsTransInterFace {

        public byte isJoint;
        public byte cmd;

        public void initDataByEnum(JOGCmdMode mode, JOGCmdCoordinate cmdCoordinate) {
            isJoint = mode.getValue();
            cmd = cmdCoordinate.getValue();
        }

        @Override
        public void initDataByBytes(byte[] bytes) {
            isJoint = bytes[0];
            cmd = bytes[1];
        }

        @Override
        public byte[] transDataToBytes() {
            return new byte[]{isJoint, cmd};
        }
    }

    public enum JOGCmdMode {
        COORDINATE(0), JOINT(1);
        public static final byte COORDINATE_VALUE = 0;
        public static final byte JOINT_VALUE = 1;
        public byte value;

        JOGCmdMode(int i) {
            value = (byte) i;
        }

        public byte getValue() {
            return value;
        }

        public JOGCmdMode getMode(byte mode_value) {
            switch (mode_value) {
                case COORDINATE_VALUE:
                    return COORDINATE;
                case JOINT_VALUE:
                    return JOINT;
                default:
                    return null;
            }
        }
    }

    public enum JOGCmdCoordinate {
        STOP(0),
        X_PLUS(1),
        X_MINUS(2),
        Y_PLUS(3),
        Y_MINUS(4),
        Z_PLUS(5),
        Z_MINUS(6),
        R_PLUS(7),
        R_MINUS(8);
        public static final byte STOP_VALUE = 0;
        public static final byte X_PLUS_VALUE = 1;
        public static final byte X_MINUS_VALUE = 2;
        public static final byte Y_PLUS_VALUE = 3;
        public static final byte Y_MINUS_VALUE = 4;
        public static final byte Z_PLUS_VALUE = 5;
        public static final byte Z_MINUS_VALUE = 6;
        public static final byte R_PLUS_VALUE = 7;
        public static final byte R_MINUS_VALUE = 8;
        private byte value;

        JOGCmdCoordinate(int i) {
            value = (byte) i;
        }

        public byte getValue() {
            return value;
        }

        public JOGCmdCoordinate getJOGCmdCoordinate(byte value) {
            switch (value) {
                case STOP_VALUE:
                    return STOP;
                case X_PLUS_VALUE:
                    return X_PLUS;
                case X_MINUS_VALUE:
                    return X_MINUS;
                case Y_PLUS_VALUE:
                    return Y_PLUS;
                case Y_MINUS_VALUE:
                    return Y_MINUS;
                case Z_PLUS_VALUE:
                    return Z_PLUS;
                case Z_MINUS_VALUE:
                    return Z_MINUS;
                case R_PLUS_VALUE:
                    return R_PLUS;
                case R_MINUS_VALUE:
                    return R_MINUS;
                default:
                    return null;
            }
        }

    }

    public static class JOGJointParams implements CMDParamsTransInterFace {
        public static final int JOG_CMD_COORDINATE_DATA_BYTE_LENGTH = 8 * 4;

        public float velocity[] = new float[4];
        public float acceleration[] = new float[4];

        @Override
        public void initDataByBytes(byte[] bytes) {
            int i = 0;
            velocity[0] = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            velocity[1] = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            velocity[2] = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            velocity[3] = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            acceleration[0] = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            acceleration[1] = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            acceleration[2] = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            acceleration[3] = TransformUtils.bytesToFloat(bytes, i);
        }

        @Override
        public byte[] transDataToBytes() {
            byte[] temp = new byte[JOG_CMD_COORDINATE_DATA_BYTE_LENGTH];
            int i = 0;
            System.arraycopy(TransformUtils.floatToBytes(velocity[0]), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(velocity[1]), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(velocity[2]), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(velocity[3]), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(acceleration[0]), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(acceleration[1]), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(acceleration[2]), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(acceleration[3]), 0, temp, i, 4);
            return temp;
        }
    }

    public static class JOGCoordinateParams implements CMDParamsTransInterFace {
        public static final int JOG_CMD_COORDINATE_DATA_BYTE_LENGTH = 8 * 4;

        public float velocity[] = new float[4];
        public float acceleration[] = new float[4];

        @Override
        public void initDataByBytes(byte[] bytes) {
            int i = 0;
            velocity[0] = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            velocity[1] = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            velocity[2] = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            velocity[3] = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            acceleration[0] = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            acceleration[1] = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            acceleration[2] = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            acceleration[3] = TransformUtils.bytesToFloat(bytes, i);
        }

        @Override
        public byte[] transDataToBytes() {
            byte[] temp = new byte[JOG_CMD_COORDINATE_DATA_BYTE_LENGTH];
            int i = 0;
            System.arraycopy(TransformUtils.floatToBytes(velocity[0]), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(velocity[1]), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(velocity[2]), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(velocity[3]), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(acceleration[0]), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(acceleration[1]), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(acceleration[2]), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(acceleration[3]), 0, temp, i, 4);
            return temp;
        }
    }


    public static class JOGLParams implements CMDParamsTransInterFace {
        float velocity;
        float acceleration;
        public static final int JOG_L_PARAMS_DATA_BYTE_LENGTH = 2 * 4;

        @Override
        public void initDataByBytes(byte[] bytes) {
            velocity = TransformUtils.bytesToFloat(bytes, 0);
            acceleration = TransformUtils.bytesToFloat(bytes, 4);
        }

        @Override
        public byte[] transDataToBytes() {
            byte[] temp = new byte[JOG_L_PARAMS_DATA_BYTE_LENGTH];
            System.arraycopy(TransformUtils.floatToBytes(velocity), 0, temp, 0, 4);
            System.arraycopy(TransformUtils.floatToBytes(acceleration), 0, temp, 4, 4);
            return temp;
        }
    }

    public static class JOGCommonParams implements CMDParamsTransInterFace {
        public static final int JOG_COMMON_PARAMS_DATA_BYTE_LENGTH = 2 * 4;
        public float velocityRatio;
        public float accelerationRatio;

        @Override
        public void initDataByBytes(byte[] bytes) {
            velocityRatio = TransformUtils.bytesToFloat(bytes, 0);
            accelerationRatio = TransformUtils.bytesToFloat(bytes, 4);
        }

        @Override
        public byte[] transDataToBytes() {
            byte[] temp = new byte[JOG_COMMON_PARAMS_DATA_BYTE_LENGTH];
            System.arraycopy(TransformUtils.floatToBytes(velocityRatio), 0, temp, 0, 4);
            System.arraycopy(TransformUtils.floatToBytes(accelerationRatio), 0, temp, 4, 4);
            return temp;
        }
    }


    public enum PTPMode {
        JUMP(0),
        MOVE_J(1),
        MOVE_L(2),
        JUMP_ANGLE(3),
        MOVE_J_ANGLE(4),
        MOVE_L_ANGLE(5),
        MOVE_J_INCREMENT(6),
        MOVE_L_INCREMENT(7);
        private byte values = 0x00;
        public final static byte JUMP_VALUE = 0x00;
        public final static byte MOVE_J_VALUE = 0X01;
        public final static byte MOVE_L_VALUE = 0X02;
        public final static byte JUMP_ANGLE_VALUE = 0X03;
        public final static byte MOVE_J_ANGLE_VALUE = 0X04;
        public final static byte MOVE_L_ANGLE_VALUE = 0X05;
        public final static byte MOVE_J_INCREMENT_VALUE = 0X06;
        public final static byte MOVE_L_INCREMENT_VALUE = 0X07;

        PTPMode(int i) {
            this.values = (byte) i;
        }

        public byte getValues() {
            return values;
        }

        public PTPMode getMode(byte mode_value) {
            switch (mode_value) {
                case CMDParams.PTPMode.JUMP_VALUE:
                    return CMDParams.PTPMode.JUMP;
                case CMDParams.PTPMode.MOVE_J_VALUE:
                    return CMDParams.PTPMode.MOVE_J;
                case CMDParams.PTPMode.MOVE_L_VALUE:
                    return CMDParams.PTPMode.MOVE_L;
                case CMDParams.PTPMode.JUMP_ANGLE_VALUE:
                    return CMDParams.PTPMode.JUMP_ANGLE;
                case CMDParams.PTPMode.MOVE_J_ANGLE_VALUE:
                    return CMDParams.PTPMode.MOVE_J_ANGLE;
                case CMDParams.PTPMode.MOVE_L_ANGLE_VALUE:
                    return CMDParams.PTPMode.MOVE_L_ANGLE;
                case CMDParams.PTPMode.MOVE_J_INCREMENT_VALUE:
                    return CMDParams.PTPMode.MOVE_J_INCREMENT;
                case CMDParams.PTPMode.MOVE_L_INCREMENT_VALUE:
                    return CMDParams.PTPMode.MOVE_L_INCREMENT;
                default:
                    return null;
            }
        }
    }

    public static class PTPCmd implements CMDParamsTransInterFace {
        public static final int PTP_CMD_DATA_BYTE_LENGTH = 4 * 4 + 1;
        public byte mode_value;
        public PTPMode ptpMode;
        public float x;
        public float y;
        public float z;
        public float r;

        public PTPCmd() {
        }

        public PTPCmd(PTPMode ptpMode, float x, float y, float z, float r) {
            this.ptpMode = ptpMode;
            this.x = x;
            this.y = y;
            this.z = z;
            this.r = r;
        }

        @Override
        public void initDataByBytes(byte[] bytes) {
            mode_value = bytes[0];
            ptpMode = PTPMode.JUMP;
            ptpMode = ptpMode.getMode(mode_value);
            x = TransformUtils.bytesToFloat(bytes, 1);
            y = TransformUtils.bytesToFloat(bytes, 5);
            z = TransformUtils.bytesToFloat(bytes, 9);
            r = TransformUtils.bytesToFloat(bytes, 13);
        }

        @Override
        public byte[] transDataToBytes() {
            byte[] temp = new byte[PTP_CMD_DATA_BYTE_LENGTH];
            if (ptpMode != null) {
                temp[0] = ptpMode.getValues();
            } else
                temp[0] = mode_value;
            System.arraycopy(TransformUtils.floatToBytes(x), 0, temp, 1, 4);
            System.arraycopy(TransformUtils.floatToBytes(y), 0, temp, 5, 4);
            System.arraycopy(TransformUtils.floatToBytes(z), 0, temp, 9, 4);
            System.arraycopy(TransformUtils.floatToBytes(r), 0, temp, 13, 4);
            return temp;
        }
    }

    public static class PTPJointParams implements CMDParamsTransInterFace {

        public static final int PTP_CMD_JOINT_PARAMS_DATA_BYTE_LENGTH = 8 * 4;

        public float velocity[] = new float[4];
        public float acceleration[] = new float[4];

        @Override
        public void initDataByBytes(byte[] bytes) {
            int i = 0;
            velocity[0] = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            velocity[1] = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            velocity[2] = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            velocity[3] = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            acceleration[0] = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            acceleration[1] = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            acceleration[2] = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            acceleration[3] = TransformUtils.bytesToFloat(bytes, i);
        }

        @Override
        public byte[] transDataToBytes() {
            byte[] temp = new byte[PTP_CMD_JOINT_PARAMS_DATA_BYTE_LENGTH];
            int i = 0;
            System.arraycopy(TransformUtils.floatToBytes(velocity[0]), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(velocity[1]), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(velocity[2]), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(velocity[3]), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(acceleration[0]), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(acceleration[1]), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(acceleration[2]), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(acceleration[3]), 0, temp, i, 4);
            return temp;
        }
    }

    public static class PTPCoordinateParams implements CMDParamsTransInterFace {
        public static final int PTP_COORDINATE_PARAMS_DATA_BYTE_LENGTH = 4 * 4;
        public float xyzVelocity;
        public float rVelocity;
        public float xyzAcceleration;
        public float rAcceleration;

        @Override
        public void initDataByBytes(byte[] bytes) {
            int i = 0;
            xyzVelocity = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            rVelocity = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            xyzAcceleration = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            rAcceleration = TransformUtils.bytesToFloat(bytes, i);
        }

        @Override
        public byte[] transDataToBytes() {
            byte[] temp = new byte[PTP_COORDINATE_PARAMS_DATA_BYTE_LENGTH];
            int i = 0;
            System.arraycopy(TransformUtils.floatToBytes(xyzVelocity), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(rVelocity), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(xyzAcceleration), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(rAcceleration), 0, temp, i, 4);
            return temp;
        }
    }

    public static class PTPJumpParams implements CMDParamsTransInterFace {
        public static final int PTP_JUMP_PARAMS_DATA_BYTE_LENGTH = 2 * 4;
        public float jumpHeight;
        public float zLimit;

        @Override
        public void initDataByBytes(byte[] bytes) {
            int i = 0;
            jumpHeight = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            zLimit = TransformUtils.bytesToFloat(bytes, i);
        }

        @Override
        public byte[] transDataToBytes() {
            byte[] temp = new byte[PTP_JUMP_PARAMS_DATA_BYTE_LENGTH];
            int i = 0;
            System.arraycopy(TransformUtils.floatToBytes(jumpHeight), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(zLimit), 0, temp, i, 4);
            return temp;
        }
    }

    public static class PTPJump2Params implements CMDParamsTransInterFace {
        public static final int PTP_JUMP_2_PARAMS_DATA_BYTE_LENGTH = 3 * 4;
        public float startJumpHeight;
        public float endJumpHeight;
        public float zLimit;

        @Override
        public void initDataByBytes(byte[] bytes) {
            int i = 0;
            startJumpHeight = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            endJumpHeight = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            zLimit = TransformUtils.bytesToFloat(bytes, i);
        }

        @Override
        public byte[] transDataToBytes() {
            byte[] temp = new byte[PTP_JUMP_2_PARAMS_DATA_BYTE_LENGTH];
            int i = 0;
            System.arraycopy(TransformUtils.floatToBytes(startJumpHeight), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(endJumpHeight), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(zLimit), 0, temp, i, 4);
            return temp;
        }
    }

    public static class PTPCommonParams implements CMDParamsTransInterFace {
        public static final int PTP_COMMON_PARAMS_DATA_BYTE_LENGTH = 2 * 4;
        public float velocityRatio;
        public float accelerationRatio;

        @Override
        public void initDataByBytes(byte[] bytes) {
            int i = 0;
            velocityRatio = TransformUtils.bytesToFloat(bytes, i);
            i += 4;
            accelerationRatio = TransformUtils.bytesToFloat(bytes, i);
        }

        @Override
        public byte[] transDataToBytes() {
            byte[] temp = new byte[PTP_COMMON_PARAMS_DATA_BYTE_LENGTH];
            int i = 0;
            System.arraycopy(TransformUtils.floatToBytes(velocityRatio), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(accelerationRatio), 0, temp, i, 4);
            return temp;
        }
    }

    public static class PTPLParams implements CMDParamsTransInterFace {
        public static final int PTPL_PARAMS_DATA_BYTE_LENGTH = 2 * 4;
        public float velocity;
        public float acceleration;


        @Override
        public void initDataByBytes(byte[] bytes) {
            velocity = TransformUtils.bytesToFloat(bytes, 0);
            acceleration = TransformUtils.bytesToFloat(bytes, 4);
        }

        @Override
        public byte[] transDataToBytes() {
            byte[] temp = new byte[PTPL_PARAMS_DATA_BYTE_LENGTH];
            System.arraycopy(TransformUtils.floatToBytes(velocity), 0, temp, 0, 4);
            System.arraycopy(TransformUtils.floatToBytes(acceleration), 0, temp, 4, 4);
            return temp;
        }
    }

    public static class PTPLCmd implements CMDParamsTransInterFace {
        public static final int PTPL_CMD_DATA_BYTE_LENGTH = 5 * 4 + 1;
        public byte mode_value;
        public PTPMode ptpMode;
        public float x;
        public float y;
        public float z;
        public float r;
        public float l;

        @Override
        public void initDataByBytes(byte[] bytes) {
            mode_value = bytes[0];
            ptpMode = ptpMode.getMode(mode_value);
            x = TransformUtils.bytesToFloat(bytes, 1);
            y = TransformUtils.bytesToFloat(bytes, 5);
            z = TransformUtils.bytesToFloat(bytes, 9);
            r = TransformUtils.bytesToFloat(bytes, 13);
            l = TransformUtils.bytesToFloat(bytes, 17);
        }

        @Override
        public byte[] transDataToBytes() {
            byte[] temp = new byte[PTPL_CMD_DATA_BYTE_LENGTH];
            if (ptpMode != null) {
                temp[0] = ptpMode.getValues();
            } else
                temp[0] = mode_value;
            System.arraycopy(TransformUtils.floatToBytes(x), 0, temp, 1, 4);
            System.arraycopy(TransformUtils.floatToBytes(y), 0, temp, 5, 4);
            System.arraycopy(TransformUtils.floatToBytes(z), 0, temp, 9, 4);
            System.arraycopy(TransformUtils.floatToBytes(r), 0, temp, 13, 4);
            System.arraycopy(TransformUtils.floatToBytes(l), 0, temp, 17, 4);
            return temp;
        }
    }

    public static class PTPPOCmd implements CMDParamsTransInterFace {
        public int ratio = 0;
        public short address = 0;
        public int level = 0;

        @Override
        public void initDataByBytes(byte[] bytes) {
            ratio = bytes[0];
            address = (short) TransformUtils.bytesToInt(new byte[]{bytes[1], bytes[2], 0x00, 0x00});
            level = bytes[3];
        }

        @Override
        public byte[] transDataToBytes() {
            byte[] addressBytes = TransformUtils.intToBytes(address);
            return new byte[]{(byte) ratio, addressBytes[0], addressBytes[1], (byte) level};
        }
    }

    public enum CPMode {
        RELATIVE, ABSOLUTE;
        public static final byte RELATIVE_VALUE = 0;
        public static final byte ABSOLUTE_VALUE = 1;
    }

    public static class CPCmd implements CMDParamsTransInterFace {
        public static final int CP_CMD_DATA_BYTE_LENGTH = 4 * 4 + 1;
        public CPMode CPMode;
        public byte cpMode;
        public float x;
        public float y;
        public float z;
        public float velocity;
        public float power;


        @Override
        public void initDataByBytes(byte[] bytes) {
            if (bytes[0] == CPMode.RELATIVE_VALUE)
                CPMode = CMDParams.CPMode.RELATIVE;
            else
                CPMode = CMDParams.CPMode.ABSOLUTE;
            bytes[0] = cpMode;
            x = TransformUtils.bytesToFloat(bytes, 1);
            y = TransformUtils.bytesToFloat(bytes, 5);
            z = TransformUtils.bytesToFloat(bytes, 9);
            power = TransformUtils.bytesToFloat(bytes, 13);
        }

        @Override
        public byte[] transDataToBytes() {
            byte[] temp = new byte[CP_CMD_DATA_BYTE_LENGTH];
            if (CPMode != null) {
                if (CPMode == CPMode.RELATIVE)
                    temp[0] = CMDParams.CPMode.RELATIVE_VALUE;
                else
                    temp[0] = CMDParams.CPMode.ABSOLUTE_VALUE;
            } else
                temp[0] = cpMode;
            System.arraycopy(TransformUtils.floatToBytes(x), 0, temp, 1, 4);
            System.arraycopy(TransformUtils.floatToBytes(y), 0, temp, 5, 4);
            System.arraycopy(TransformUtils.floatToBytes(z), 0, temp, 9, 4);
            System.arraycopy(TransformUtils.floatToBytes(power), 0, temp, 13, 4);
            return temp;
        }
    }


    public enum CPParamsEnums {
        PEN, LASER, LE, LC
    }

    public static class CPParams implements CMDParamsTransInterFace {
        public static final int CP_PARAMS_DATA_BYTE_LENGTH = 3 * 4 + 1;
        public float planAcc;
        public float junctionVel;
        public float acc;
        public float peroid;
        public byte realTimeTrack;

        public void initByEnum(CPParamsEnums mode) {
            switch (mode) {
                case PEN:
                    planAcc = 100.0f;
                    junctionVel = 100.0f;
                    acc = 100.0f;
                    realTimeTrack = 0;
                    break;
                case LASER:
                    planAcc = 10.0f;
                    junctionVel = 10.0f;
                    acc = 10.0f;
                    realTimeTrack = 0;
                    break;
                case LE:
                    planAcc = 5.0f;
                    junctionVel = 5.0f;
                    acc = 5.0f;
                    realTimeTrack = 0;
                    break;
                case LC:
                    planAcc = 0.1f;
                    junctionVel = 0.1f;
                    acc = 0.1f;
                    realTimeTrack = 0;
                    break;
            }
        }

        @Override
        public void initDataByBytes(byte[] bytes) {

            planAcc = TransformUtils.bytesToFloat(bytes, 0);
            junctionVel = TransformUtils.bytesToFloat(bytes, 4);
            realTimeTrack = bytes[12];
            if (realTimeTrack == 0) {
                acc = TransformUtils.bytesToFloat(bytes, 8);
            } else
                peroid = TransformUtils.bytesToFloat(bytes, 8);
        }

        @Override
        public byte[] transDataToBytes() {
            byte[] temp = new byte[CP_PARAMS_DATA_BYTE_LENGTH];
            temp[CP_PARAMS_DATA_BYTE_LENGTH - 1] = realTimeTrack;
            System.arraycopy(TransformUtils.floatToBytes(planAcc), 0, temp, 0, 4);
            System.arraycopy(TransformUtils.floatToBytes(junctionVel), 0, temp, 4, 4);
            if (realTimeTrack == 0)
                System.arraycopy(TransformUtils.floatToBytes(acc), 0, temp, 8, 4);
            else
                System.arraycopy(TransformUtils.floatToBytes(peroid), 0, temp, 8, 4);
            return temp;
        }
    }

    public enum ArmOrientation {
        Left(0), Right(1);
        byte value;

        ArmOrientation(int i) {
            value = (byte) i;
        }

        public byte getValue() {
            return value;
        }

        public ArmOrientation getOrientation(byte value) {
            switch (value) {
                case 0:
                    return Left;
                case 1:
                    return Right;
                default:
                    return null;
            }
        }
    }

    public enum EndEffectorParamsEnums {
        PEN, LASER
    }

    public static class EndEffectorParams implements CMDParamsTransInterFace {
        public static final int END_EFFECT_PARAMS_DATA_BYTE_LENGTH = 3 * 4;
        public float xBias;
        public float yBias;
        public float zBias;

        public void initByEnum(EndEffectorParamsEnums enums) {
            switch (enums) {
                case PEN:
                    xBias = 61.0f;
                    yBias = 0.0f;
                    zBias = 0.0f;
                    break;
                case LASER:
                    xBias = 70.0f;
                    yBias = 0.0f;
                    zBias = 0.0f;
                    break;
            }
        }

        @Override
        public void initDataByBytes(byte[] bytes) {
            xBias = TransformUtils.bytesToFloat(bytes, 0);
            yBias = TransformUtils.bytesToFloat(bytes, 4);
            zBias = TransformUtils.bytesToFloat(bytes, 8);
        }

        @Override
        public byte[] transDataToBytes() {
            byte[] temp = new byte[END_EFFECT_PARAMS_DATA_BYTE_LENGTH];
            int i = 0;
            System.arraycopy(TransformUtils.floatToBytes(xBias), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(yBias), 0, temp, i, 4);
            i += 4;
            System.arraycopy(TransformUtils.floatToBytes(zBias), 0, temp, i, 4);
            return temp;
        }
    }

    public static class EndEffectorOutput implements CMDParamsTransInterFace {
        public byte isCtrlEnabled;
        public byte isOn;

        @Override
        public void initDataByBytes(byte[] bytes) {
            isCtrlEnabled = bytes[0];
            isOn = bytes[1];
        }

        @Override
        public byte[] transDataToBytes() {
            return new byte[]{isCtrlEnabled, isOn};
        }
    }

    public static class WithL implements CMDParamsTransInterFace {
        public boolean on;

        @Override
        public void initDataByBytes(byte[] bytes) {
            if (bytes[0] == 0)
                on = false;
            else
                on = true;
        }

        @Override
        public byte[] transDataToBytes() {
            if (on)
                return new byte[]{1};
            else
                return new byte[]{0};
        }
    }

    public static class WaitCmd implements CMDParamsTransInterFace {
        public int timeout;

        @Override
        public void initDataByBytes(byte[] bytes) {
            timeout = TransformUtils.bytesToInt(bytes);
        }

        @Override
        public byte[] transDataToBytes() {
            return TransformUtils.intToBytes(timeout);
        }
    }

    public static class TrigCmd implements CMDParamsTransInterFace {
        public static final byte TRIG_MODE_LEVEL = 0X00;
        public static final byte TRIG_MODE_A_D = 0X01;

        public static final byte TRIG_CONDITION_LEVEL_EQUAL = 0X00;
        public static final byte TRIG_CONDITION_LEVEL_UNEQUAL = 0X01;

        public static final byte TRIG_CONDITION_A_D_LESS_THAN = 0X00;
        public static final byte TRIG_CONDITION_A_D_LESS_THAN_OR_EQUAL = 0X01;
        public static final byte TRIG_CONDITION_A_D_GREATER_THAN_OR_EQUAL = 0X02;
        public static final byte TRIG_CONDITION_A_D_GREATER_TAN = 0X03;

        public byte address = 0x00;
        public byte mode = 0x00;
        public byte condition = 0x00;
        public short threshold = 0;

        @Override
        public void initDataByBytes(byte[] bytes) {
            address = bytes[0];
            mode = bytes[1];
            condition = bytes[2];
            threshold = (short) TransformUtils.bytesToInt(new byte[]{bytes[3], bytes[4], 0x00, 0x00});
        }

        @Override
        public byte[] transDataToBytes() {
            byte[] thresholdBytes = TransformUtils.intToBytes(threshold);
            return new byte[]{address, mode, condition, thresholdBytes[0], thresholdBytes[1]};
        }
    }

    public enum IOFunction {
        DUMMY(0), PWM(1), DO(2), DI(3), ADC(4);
        byte value;

        IOFunction(int i) {
            value = (byte) i;
        }

        public byte getValue() {
            return value;
        }
    }

    public static class IOMultiplexing implements CMDParamsTransInterFace {
        public byte address;
        public byte multiplex;

        @Override
        public void initDataByBytes(byte[] bytes) {
            address = bytes[0];
            multiplex = bytes[1];
        }

        @Override
        public byte[] transDataToBytes() {
            return new byte[]{address, multiplex};
        }
    }

    public static class IODO implements CMDParamsTransInterFace {
        public byte address;
        public byte level;

        @Override
        public void initDataByBytes(byte[] bytes) {
            address = bytes[0];
            level = bytes[1];
        }

        @Override
        public byte[] transDataToBytes() {
            return new byte[]{address, level};
        }
    }

    public static class IOPWM implements CMDParamsTransInterFace {
        public byte address;
        public byte frequency;
        public byte dutyCycle;

        @Override
        public void initDataByBytes(byte[] bytes) {
            address = bytes[0];
            frequency = bytes[1];
            dutyCycle = bytes[2];
        }

        @Override
        public byte[] transDataToBytes() {
            return new byte[]{address, frequency, dutyCycle};
        }
    }

    public static class IODI implements CMDParamsTransInterFace {
        public byte address;
        public byte level;

        @Override
        public void initDataByBytes(byte[] bytes) {
            address = bytes[0];
            level = bytes[1];
        }

        @Override
        public byte[] transDataToBytes() {
            return new byte[]{address, level};
        }
    }

    public static class IOADC implements CMDParamsTransInterFace {
        public byte address;
        public short value = 0;

        @Override
        public void initDataByBytes(byte[] bytes) {
            address = bytes[0];
            value = (short) TransformUtils.bytesToInt(new byte[]{bytes[1], bytes[2], 0x00, 0x00});
        }

        @Override
        public byte[] transDataToBytes() {
            byte[] thresholdBytes = TransformUtils.intToBytes(value);
            return new byte[]{address, thresholdBytes[0], thresholdBytes[1]};
        }
    }

    public static class EMotor implements CMDParamsTransInterFace {
        public final static byte EMOTOR_DATA_BYTE_LENGTH = 6;
        public byte index;
        public boolean isEnable;
        public float speed;

        @Override
        public void initDataByBytes(byte[] bytes) {
            index = bytes[0];
            if (bytes[1] == 0x00)
                isEnable = false;
            else
                isEnable = true;
            speed = TransformUtils.bytesToFloat(bytes, 2);
        }

        @Override
        public byte[] transDataToBytes() {
            byte[] temp = new byte[EMOTOR_DATA_BYTE_LENGTH];
            temp[0] = index;
            if (isEnable)
                temp[1] = 0x01;
            else
                temp[1] = 0x00;
            System.arraycopy(TransformUtils.floatToBytes(speed), 0, temp, 2, 4);
            return temp;
        }
    }

    public static class DeviceSensor implements CMDParamsTransInterFace {
        public boolean isEnable;
        public byte port;
        public DeviceVersion version;

        @Override
        public void initDataByBytes(byte[] bytes) {
            if (bytes[0] == 0x00)
                isEnable = false;
            else
                isEnable = true;
            port = bytes[1];
            switch (bytes[2]) {
                case 0x01:
                    version = DeviceVersion.VER_V2;
                    break;
                case 0x00:
                default:
                    version = DeviceVersion.VER_V1;
                    break;
            }
        }

        @Override
        public byte[] transDataToBytes() {
            return new byte[]{(byte) (isEnable ? 0x01 : 0x00), port, version.getValue()};
        }
    }

    public enum DeviceVersion {
        VER_V1((byte) 0x00),
        VER_V2((byte) 0x01);
        byte value;

        DeviceVersion(byte v) {
            value = v;
        }

        public byte getValue() {
            return value;
        }
    }

    public static class Color implements CMDParamsTransInterFace {
        public byte r;
        public byte g;
        public byte b;

        @Override
        public void initDataByBytes(byte[] bytes) {
            r = bytes[0];
            g = bytes[1];
            b = bytes[2];
        }

        @Override
        public byte[] transDataToBytes() {
            return new byte[]{r, g, b};
        }
    }

    public static class LostStepParams implements CMDParamsTransInterFace {
        public float value = 0;

        @Override
        public void initDataByBytes(byte[] bytes) {
            value = TransformUtils.bytesToFloat(bytes, 0);
        }

        @Override
        public byte[] transDataToBytes() {
            return TransformUtils.floatToBytes(value);
        }
    }

}
