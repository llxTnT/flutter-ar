package co.dobot.magicain.message;

import co.dobot.magicain.message.base.BaseMessage;
import co.dobot.magicain.message.cmd.CMDParams;
import co.dobot.magicain.message.cmd.CommandID;
import co.dobot.magicain.message.cmd.ControlType;

/**
 * Created by x on 2018/5/14.
 */

public class DobotMessage extends BaseMessage implements DobotMessageInterface {
    public static final int HEADER_LENGTH = 2;
    public static final int PLENGTH_INDEX = 3;
    public static final int LENGTH_WITHOUT_PAYLOAD = 4;
    public static final byte SYNC_BYTE = (byte) 0xAA;
    private byte pLength;
    private byte sync0 = SYNC_BYTE;
    private byte sync1 = SYNC_BYTE;
    private byte checkSum;
    private Payload payload = new Payload();

    public DobotMessage() {

    }

    public static CheckStatus checkSync(byte[] cache, int i) {
        CheckStatus status = new CheckStatus();
        if (cache[i] != SYNC_BYTE) {
            status.skipCount = 1;
            return status;
        }
        if (cache[i + 1] == SYNC_BYTE)
            status.isValid = true;
        else
            status.skipCount = 2;
        return status;
    }

    public static CheckStatus checkIsValid(byte[] data, int i) {
        CheckStatus status = new CheckStatus();
        int pLength = getPLength(data, i);
        byte checkSum = calculateCheckSum(data, i + 1 + HEADER_LENGTH, pLength);
        if (checkSum == data[i + HEADER_LENGTH + pLength + 1])
            status.isValid = true;
        else
            status.skipCount = LENGTH_WITHOUT_PAYLOAD + pLength;
        return status;
    }

    public static int getPLength(byte[] data, int i) {
        return data[i + PLENGTH_INDEX - 1] & 0xff;
    }

    public static int getLength(byte[] data, int i) {
        return getPLength(data, i) + LENGTH_WITHOUT_PAYLOAD;
    }

    public static byte calculateCheckSum(byte[] data, int index, int length) {
        byte checkSum = 0;
        for (int j = 0; j < length; j++) {
            checkSum += data[index + j];
        }
        checkSum = (byte) (256 - checkSum);
        return checkSum;
    }

    public DobotMessage(byte[] data) {
        int i = 0;
        sync0 = data[i++];
        sync1 = data[i++];
        pLength = data[i++];
        byte payLoadData[] = new byte[pLength & 0xff];
        System.arraycopy(data, i, payLoadData, 0, payLoadData.length);
        payload = new Payload(payLoadData);
        i += payLoadData.length;
        checkSum = data[i];
    }

    @Override
    public byte[] getData() {
        pLength = payload.getPlength();
        checkSum = getCheckSum(payload.toBytes());
        byte[] data = new byte[0xff & pLength + LENGTH_WITHOUT_PAYLOAD];
        int i = 0;
        data[i++] = sync0;
        data[i++] = sync1;
        data[i++] = pLength;
        byte[] payloadTemp = payload.toBytes();
        System.arraycopy(payloadTemp, 0, data, i, pLength & 0xff);
        data[i + pLength & 0xff] = checkSum;
        return data;
    }

    private byte getCheckSum(byte[] payloadTemp) {
        checkSum = calculateCheckSum(payloadTemp, 0, payloadTemp.length);
        return checkSum;
    }

    public void setWriteMode(boolean isWrite) {
        if (isWrite)
            payload.setRw(ControlType.WRITE_ONLY);
        else
            payload.setRw(ControlType.READ_ONLY);
    }

    public void setQueueMode(boolean isQueue) {
        if (isQueue)
            payload.setQueue(ControlType.IS_QUEUE);
        else
            payload.setQueue(ControlType.NOT_QUEUE);
    }


    public byte getCommandID() {
        return this.payload.getCommandID();
    }

    public boolean isQueue() {
        return payload.getQueue() == ControlType.IS_QUEUE;
    }

    public byte[] getParams() {
        return this.payload.getParams();
    }

    public class Payload {
        byte commandID;
        ControlTypeField controlType = new ControlTypeField();
        // byte controlType;
        byte[] params;

        public Payload(byte[] data) {
            if (data.length > 0) {
                int i = 0;
                commandID = data[i++];
                controlType.setValue(data[i++]);
                if (i < data.length) {
                    params = new byte[data.length - i];
                    System.arraycopy(data, i, params, 0, params.length);
                }
            }
        }

        public Payload() {

        }

        public byte getCommandID() {
            return commandID;
        }

        public void setCommandID(byte commandID) {
            this.commandID = commandID;
        }

        public ControlTypeField getControlType() {
            return controlType;
        }

        public byte getRw() {
            return controlType.getRw();
        }

        public void setRw(byte rw) {
            controlType.setRw(rw);
        }

        public byte getQueue() {
            return controlType.getQueue();
        }

        public void setQueue(byte queue) {
            controlType.setQueue(queue);
        }

        public byte[] getParams() {
            return params;
        }

        public void setParams(byte[] params) {
            this.params = params;
        }

        public byte[] toBytes() {
            byte[] temp;
            if (params != null)
                temp = new byte[2 + params.length];
            else
                temp = new byte[2];
            int i = 0;
            temp[i++] = commandID;
            temp[i++] = controlType.getValue();
            if (params != null)
                System.arraycopy(params, 0, temp, i, params.length);
            return temp;
        }

        public byte getPlength() {
            if (params != null)
                return (byte) (2 + params.length);
            else return 2;
        }
    }

    @Override
    public void cmdSetDeviceSN(String name) {
        payload.setCommandID(CommandID.CMD_DEVICE_SN);
        payload.setRw(ControlType.WRITE_ONLY);
        CMDParams.DeviceName deviceName = new CMDParams.DeviceName();
        deviceName.name = name;
        payload.setParams(deviceName.transDataToBytes());
        //  payload.setParams();
    }

    @Override
    public void cmdGetDeviceSN() {
        payload.setCommandID(CommandID.CMD_DEVICE_SN);
    }

    @Override
    public void cmdSetDeviceName(String name) {
        payload.setCommandID(CommandID.CMD_DEVICE_NAME);
        payload.setRw(ControlType.WRITE_ONLY);
        CMDParams.DeviceName deviceName = new CMDParams.DeviceName();
        deviceName.name = name;
        payload.setParams(deviceName.transDataToBytes());
        //  payload.setParams();
    }

    @Override
    public void cmdGetDeviceName() {
        payload.setCommandID(CommandID.CMD_DEVICE_NAME);
    }

    public void cmdGetPose() {
        payload.setCommandID(CommandID.CMD_POSE);
    }

    public void cmdSetPose(CMDParams.Pose pose) {
        payload.setCommandID(CommandID.CMD_POSE);
        payload.setRw(ControlType.WRITE_ONLY);
        payload.setParams(pose.transDataToBytes());
    }

    public void cmdResetPose(CMDParams.ResetPose resetPose) {
        payload.setCommandID(CommandID.CMD_RESET_POSE);
        payload.setRw(ControlType.WRITE_ONLY);
        payload.setParams(resetPose.transDataToBytes());
    }

    @Override
    public void cmdGetAlarmsState() {
        payload.setCommandID(CommandID.CMD_ALARMS_STATE);
    }

    @Override
    public void cmdClearAllAlarmsState() {
        payload.setCommandID(CommandID.CMD_ALARMS_STATE);
        payload.setRw(ControlType.WRITE_ONLY);
    }

    public void cmdGetKinematics() {
        payload.setCommandID(CommandID.CMD_GET_KINEMATICS);
    }

    public void cmdGetHomeParams() {
        payload.setCommandID(CommandID.CMD_HOME_PARAMS);
    }

    public void cmdSetHomeParams(CMDParams.HomeParams homeParams) {
        payload.setCommandID(CommandID.CMD_HOME_PARAMS);
        payload.setRw(ControlType.WRITE_ONLY);
        payload.setParams(homeParams.transDataToBytes());
    }

    public void cmdSetHomeCmd(CMDParams.HomeCmd homeCmd) {
        payload.setCommandID(CommandID.CMD_HOME_CMD);
        payload.setRw(ControlType.WRITE_ONLY);
        payload.setParams(homeCmd.transDataToBytes());
    }

    public void cmdSetHomeCmdQueue(CMDParams.HomeCmd homeCmd) {
        payload.setCommandID(CommandID.CMD_HOME_CMD);
        payload.setRw(ControlType.WRITE_ONLY);
        payload.setQueue(ControlType.IS_QUEUE);
        payload.setParams(homeCmd.transDataToBytes());
    }

    public void cmdHome() {
        CMDParams.HomeCmd homeCmd = new CMDParams.HomeCmd();
        homeCmd.reserved = 0;
        cmdSetHomeCmd(homeCmd);
    }

    public void cmdGetHHTTrigeMode() {
        payload.setCommandID(CommandID.CMD_HHT_TRIG_MODE);
    }

    public void cmdSetHHTTrigeMode(CMDParams.HHTTrigMode trigMode) {
        payload.setCommandID(CommandID.CMD_HHT_TRIG_MODE);
        payload.setRw(ControlType.WRITE_ONLY);
        switch (trigMode) {
            case TRIGGERED_ON_KEY_RELEASED:
                payload.setParams(new byte[]{0x00});
                break;
            case TRIGGERED_ON_PERIODIC_INTERVAL:
                payload.setParams(new byte[]{0x01});
                break;
        }
    }

    @Override
    public void cmdSetHHTTrigOutputEnabled(boolean isEnable) {
        payload.setCommandID(CommandID.CMD_HHT_TRIG_OUTPUT_ENABLED);
        payload.setRw(ControlType.WRITE_ONLY);
        CMDParams.HHTTrigOutputEnabled hhtTrigOutputEnabled = new CMDParams.HHTTrigOutputEnabled();
        hhtTrigOutputEnabled.isEnable = isEnable;
        payload.setParams(hhtTrigOutputEnabled.transDataToBytes());
    }

    @Override
    public void cmdGetHHTTrigOutputEnabled() {
        payload.setCommandID(CommandID.CMD_HHT_TRIG_OUTPUT_ENABLED);
    }

    @Override
    public void cmdGetHHTTrigOutput() {
        payload.setCommandID(CommandID.CMD_HHT_TRIG_OUTPUT);
    }

    public void cmdJOG(CMDParams.JOGCmd jogCmd, boolean isQueue) {
        payload.setCommandID(CommandID.CMD_JOG_CMD);
        payload.setRw(ControlType.WRITE_ONLY);
        if (isQueue)
            payload.setQueue(ControlType.IS_QUEUE);
        payload.setParams(jogCmd.transDataToBytes());
    }

    @Override
    public void cmdGetJOGJointParams() {
        payload.setCommandID(CommandID.CMD_JOG_JOINT_PARAMS);
    }


    @Override
    public void cmdSetJOGJointParams(CMDParams.JOGJointParams jogJointParams) {
        payload.setCommandID(CommandID.CMD_JOG_JOINT_PARAMS);
        payload.setRw(ControlType.WRITE_ONLY);
        payload.setParams(jogJointParams.transDataToBytes());
    }

    @Override
    public void cmdGetJOGCoordinateParams() {
        payload.setCommandID(CommandID.CMD_JOG_COORDINATE_PARAMS);
    }

    public void cmdSetJOGCoordinateParams(CMDParams.JOGCoordinateParams jogCoordinateParams) {
        payload.setCommandID(CommandID.CMD_JOG_COORDINATE_PARAMS);
        payload.setRw(ControlType.WRITE_ONLY);
        payload.setParams(jogCoordinateParams.transDataToBytes());

    }

    @Override
    public void cmdGetJOGLParams() {
        payload.setCommandID(CommandID.CMD_JOG_L_CMD);
    }

    @Override
    public void cmdSetJOGLParams(CMDParams.JOGLParams joglParams) {
        payload.setCommandID(CommandID.CMD_JOG_L_CMD);
        payload.setRw(ControlType.WRITE_ONLY);
        payload.setParams(joglParams.transDataToBytes());
    }

    public void cmdJOGCommonVelocityRatio(float velocityRatio, float accelerationRatio) {
        payload.setCommandID(CommandID.CMD_JOG_COMMON_PARAMS);
        payload.setRw(ControlType.WRITE_ONLY);
        CMDParams.JOGCommonParams params = new CMDParams.JOGCommonParams();
        params.velocityRatio = velocityRatio;
        params.accelerationRatio = accelerationRatio;
        payload.setParams(params.transDataToBytes());
    }

    public void cmdGetJOGCommonParams() {
        payload.setCommandID(CommandID.CMD_JOG_COMMON_PARAMS);
    }

    public void cmdSetJOGCommonParams(CMDParams.JOGCommonParams params) {
        payload.setCommandID(CommandID.CMD_JOG_COMMON_PARAMS);
        payload.setRw(ControlType.WRITE_ONLY);
        payload.setParams(params.transDataToBytes());
    }

    public void cmdPTP(CMDParams.PTPCmd ptpCmd) {
        payload.setCommandID(CommandID.CMD_PTP_CMD);
        payload.setRw(ControlType.WRITE_ONLY);
        payload.setQueue(ControlType.IS_QUEUE);
        payload.setParams(ptpCmd.transDataToBytes());
    }

    @Override
    public void cmdGetPTPJointParams() {
        payload.setCommandID(CommandID.CMD_PTP_JOINT_PARAMS);
    }

    @Override
    public void cmdSetPTPJointParams(CMDParams.PTPJointParams ptpJointParams) {
        payload.setCommandID(CommandID.CMD_PTP_JOINT_PARAMS);
        payload.setRw(ControlType.WRITE_ONLY);
        payload.setParams(ptpJointParams.transDataToBytes());
    }

    public void cmdGetPTPCoordinateParams() {
        payload.setCommandID(CommandID.CMD_PTP_COORDINATE_PARAMS);
    }

    public void cmdSetPTPCoordinateParams(CMDParams.PTPCoordinateParams ptpCoordinateParams) {
        payload.setCommandID(CommandID.CMD_PTP_COORDINATE_PARAMS);
        payload.setRw(ControlType.WRITE_ONLY);
        payload.setParams(ptpCoordinateParams.transDataToBytes());
    }

    public void cmdGetPTPCommonParams() {
        payload.setCommandID(CommandID.CMD_PTP_COMMON_PARAMS);
    }

    public void cmdSetPTPCommonParams(CMDParams.PTPCommonParams ptpCommonParams) {
        payload.setCommandID(CommandID.CMD_PTP_COMMON_PARAMS);
        payload.setRw(ControlType.WRITE_ONLY);
        payload.setParams(ptpCommonParams.transDataToBytes());
    }

    @Override
    public void cmdGetPTPJumpParams() {
        payload.setCommandID(CommandID.CMD_PTP_JUMP_PARAMS);
    }


    @Override
    public void cmdSetPTPJumpParams(CMDParams.PTPJumpParams ptpJumpParams) {
        payload.setCommandID(CommandID.CMD_PTP_JUMP_PARAMS);
        payload.setRw(ControlType.WRITE_ONLY);
        payload.setParams(ptpJumpParams.transDataToBytes());
    }

    @Override
    public void cmdGetPTPJump2Params() {
        payload.setCommandID(CommandID.CMD_PTP_JUMP_2_PARAMS);
    }

    @Override
    public void cmdSetPTPJump2Params(CMDParams.PTPJump2Params ptpJump2Params) {
        payload.setCommandID(CommandID.CMD_PTP_JUMP_2_PARAMS);
        payload.setRw(ControlType.WRITE_ONLY);
        payload.setParams(ptpJump2Params.transDataToBytes());
    }

    public void cmdGetPTPLParams() {
        payload.setCommandID(CommandID.CMD_PTP_L_PARAMS);
    }

    public void cmdSetPTPLParams(CMDParams.PTPLParams params, boolean isQueue) {
        payload.setCommandID(CommandID.CMD_PTP_L_PARAMS);
        payload.setRw(ControlType.WRITE_ONLY);
        if (isQueue) {
            payload.setQueue(ControlType.IS_QUEUE);
        }
        payload.setParams(params.transDataToBytes());
    }

    public void cmdSetPTPWithL(CMDParams.PTPLCmd ptplCmd) {
        payload.setCommandID(CommandID.CMD_PTP_WITH_L);
        payload.setRw(ControlType.WRITE_ONLY);
        payload.setQueue(ControlType.IS_QUEUE);
        payload.setParams(ptplCmd.transDataToBytes());
    }

    @Override
    public void cmdSetPTPPO(CMDParams.PTPCmd ptpCmd, CMDParams.PTPPOCmd ptppoCmd[]) {
        payload.setCommandID(CommandID.CMD_PTP_PO);
        payload.setRw(ControlType.WRITE_ONLY);
        payload.setQueue(ControlType.IS_QUEUE);
        byte[] paramsData;
        byte[] ptpCmdBytes = ptpCmd.transDataToBytes();
        if (ptppoCmd != null) {
            paramsData = new byte[ptpCmdBytes.length + ptppoCmd.length * 4];
            int index = 0;
            System.arraycopy(ptpCmdBytes, 0, paramsData, 0, ptpCmdBytes.length);
            index += ptpCmdBytes.length;
            for (CMDParams.PTPPOCmd tempPTPPOCmd : ptppoCmd) {
                byte[] tempPTPPOCmdBytes = tempPTPPOCmd.transDataToBytes();
                System.arraycopy(tempPTPPOCmdBytes, 0, paramsData, index, tempPTPPOCmdBytes.length);
                index += tempPTPPOCmdBytes.length;
            }
        } else {
            paramsData = ptpCmdBytes;
        }
        payload.setParams(paramsData);
    }

    @Override
    public void cmdSetPTPPOWithL(CMDParams.PTPLCmd ptplCmd, CMDParams.PTPPOCmd ptppoCmd[]) {
        payload.setCommandID(CommandID.CMD_PTP_PO_WITH_L);
        payload.setRw(ControlType.WRITE_ONLY);
        payload.setQueue(ControlType.IS_QUEUE);
        byte[] paramsData;
        byte[] ptpCmdBytes = ptplCmd.transDataToBytes();
        if (ptppoCmd != null) {
            paramsData = new byte[ptpCmdBytes.length + ptppoCmd.length * 4];
            int index = 0;
            System.arraycopy(ptpCmdBytes, 0, paramsData, 0, ptpCmdBytes.length);
            index += ptpCmdBytes.length;
            for (CMDParams.PTPPOCmd tempPTPPOCmd : ptppoCmd) {
                byte[] tempPTPPOCmdBytes = tempPTPPOCmd.transDataToBytes();
                System.arraycopy(tempPTPPOCmdBytes, 0, paramsData, index, tempPTPPOCmdBytes.length);
                index += tempPTPPOCmdBytes.length;
            }
        } else {
            paramsData = ptpCmdBytes;
        }
        payload.setParams(paramsData);
    }

    public void cmdGetCPParams() {
        payload.setCommandID(CommandID.CMD_CP_PARAMS);
    }

    public void cmdSetCPParams(CMDParams.CPParams params) {
        payload.setCommandID(CommandID.CMD_CP_PARAMS);
        payload.setRw(ControlType.WRITE_ONLY);
        payload.setParams(params.transDataToBytes());
    }

    public void cmdSetEndEffectorParams(CMDParams.EndEffectorParams params) {
        payload.setCommandID(CommandID.CMD_END_EFFECT_PARAMS);
        payload.setRw(ControlType.WRITE_ONLY);
        payload.setParams(params.transDataToBytes());
    }

    public void cmdGetEndEffectorParams() {
        payload.setCommandID(CommandID.CMD_END_EFFECT_PARAMS);
    }

    public void cmdSetSuctionCupControlEnable(boolean controlEnable, boolean isSucked) {
        payload.setCommandID(CommandID.CMD_END_EFFECT_SUCTIONCUP);
        payload.setRw(ControlType.WRITE_ONLY);
        CMDParams.EndEffectorOutput output = new CMDParams.EndEffectorOutput();
        if (controlEnable)
            output.isCtrlEnabled = 0x01;
        else
            output.isCtrlEnabled = 0x00;
        if (isSucked)
            output.isOn = 0x01;
        else
            output.isOn = 0x00;
        payload.setParams(output.transDataToBytes());
    }

    public void cmdGetSuctionCupOutput() {
        payload.setCommandID(CommandID.CMD_END_EFFECT_SUCTIONCUP);
    }

    public void cmdSetGripperControlEnable(boolean controlEnable, boolean grip) {
        payload.setCommandID(CommandID.CMD_END_EFFECT_GRIPPER);
        payload.setRw(ControlType.WRITE_ONLY);
        CMDParams.EndEffectorOutput output = new CMDParams.EndEffectorOutput();
        if (controlEnable)
            output.isCtrlEnabled = 0x01;
        else
            output.isCtrlEnabled = 0x00;
        if (grip)
            output.isOn = 0x01;
        else
            output.isOn = 0x00;
        payload.setParams(output.transDataToBytes());
    }

    public void cmdGetGripper() {
        payload.setCommandID(CommandID.CMD_END_EFFECT_GRIPPER);
    }

    public void cmdSetLaser(boolean isOn, boolean isQueue) {
        payload.setCommandID(CommandID.CMD_END_EFFECT_LASER);
        payload.setRw(ControlType.WRITE_ONLY);
        if (isQueue)
            payload.setQueue(ControlType.IS_QUEUE);
        CMDParams.EndEffectorOutput output = new CMDParams.EndEffectorOutput();
        output.isCtrlEnabled = 0x01;
        if (isOn)
            output.isOn = 0x01;
        else
            output.isOn = 0x00;
        payload.setParams(output.transDataToBytes());
    }

    public void cmdGetLaser() {
        payload.setCommandID(CommandID.CMD_END_EFFECT_LASER);
    }


    public void cmdCP(CMDParams.CPCmd cpCmd) {
        payload.setCommandID(CommandID.CMD_CP);
        payload.setRw(ControlType.WRITE_ONLY);
        payload.setQueue(ControlType.IS_QUEUE);
        payload.setParams(cpCmd.transDataToBytes());
    }

    public void cmdCPLE(CMDParams.CPCmd cpCmd) {
        payload.setCommandID(CommandID.CMD_CP_LE);
        payload.setRw(ControlType.WRITE_ONLY);
        payload.setQueue(ControlType.IS_QUEUE);
        payload.setParams(cpCmd.transDataToBytes());
    }

    public void cmdGetDeviceWithL() {
        payload.setCommandID(CommandID.CMD_DEVICE_WITH_L);
    }

    public void cmdSetDeviceWithLOn(boolean isOn) {
        payload.setCommandID(CommandID.CMD_DEVICE_WITH_L);
        payload.setRw(ControlType.WRITE_ONLY);
        CMDParams.WithL l = new CMDParams.WithL();
        l.on = isOn;
        payload.setParams(l.transDataToBytes());
    }

    public void cmdGetPoseL() {
        payload.setCommandID(CommandID.CMD_GET_POSE_L);
    }

    public void cmdSetDeviceWithL(CMDParams.WithL l) {
        payload.setCommandID(CommandID.CMD_DEVICE_WITH_L);
        payload.setRw(ControlType.WRITE_ONLY);
        payload.setParams(l.transDataToBytes());
    }

    @Override
    public void cmdSetWait(CMDParams.WaitCmd waitCmd, boolean isQueue) {
        payload.setCommandID(CommandID.CMD_WAIT);
        payload.setRw(ControlType.WRITE_ONLY);
        if (isQueue) {
            payload.setQueue(ControlType.IS_QUEUE);
        }
        payload.setParams(waitCmd.transDataToBytes());
    }

    @Override
    public void cmdSetTrig(CMDParams.TrigCmd trigCmd, boolean isQueue) {
        payload.setCommandID(CommandID.CMD_TRIG);
        payload.setRw(ControlType.WRITE_ONLY);
        if (isQueue) {
            payload.setQueue(ControlType.IS_QUEUE);
        }
        payload.setParams(trigCmd.transDataToBytes());
    }

    public void cmdSetIOMultiplexing(CMDParams.IOMultiplexing multiplexing, boolean isQueue) {
        payload.setCommandID(CommandID.CMD_IO_MULTIPLEXING);
        payload.setRw(ControlType.WRITE_ONLY);
        if (isQueue) {
            payload.setQueue(ControlType.IS_QUEUE);
        }
        payload.setParams(multiplexing.transDataToBytes());

    }

    public void cmdGetIOMultiplexing(CMDParams.IOMultiplexing multiplexing) {
        payload.setCommandID(CommandID.CMD_IO_MULTIPLEXING);
        payload.setParams(multiplexing.transDataToBytes());
    }

    public void cmdSetIODO(CMDParams.IODO iodo, boolean isQueue) {
        payload.setCommandID(CommandID.CMD_IO_DO);
        payload.setRw(ControlType.WRITE_ONLY);
        if (isQueue) {
            payload.setQueue(ControlType.IS_QUEUE);
        }
        payload.setParams(iodo.transDataToBytes());
    }

    public void cmdGetIODO(CMDParams.IODO iodo) {
        payload.setCommandID(CommandID.CMD_IO_DO);
        payload.setParams(iodo.transDataToBytes());
    }

    public void cmdSetIOPWM(CMDParams.IOPWM iopwm, boolean isQueue) {
        payload.setCommandID(CommandID.CMD_IO_PWM);
        payload.setRw(ControlType.WRITE_ONLY);
        if (isQueue) {
            payload.setQueue(ControlType.IS_QUEUE);
        }
        payload.setParams(iopwm.transDataToBytes());
    }

    public void cmdGetIOPWM(CMDParams.IOPWM iopwm) {
        payload.setCommandID(CommandID.CMD_IO_PWM);
        payload.setParams(iopwm.transDataToBytes());
    }

    public void cmdGetIODI(CMDParams.IODI iodi) {
        payload.setCommandID(CommandID.CMD_IO_DI);
        payload.setParams(iodi.transDataToBytes());
    }

    public void cmdGetIOADC(CMDParams.IOADC ioadc) {
        payload.setCommandID(CommandID.CMD_IO_ADC);
        payload.setParams(ioadc.transDataToBytes());
    }

    @Override
    public void cmdSetEMotor(CMDParams.EMotor eMotor, boolean isQueue) {
        payload.setCommandID(CommandID.CMD_EMOTOR);
        payload.setRw(ControlType.WRITE_ONLY);
        if (isQueue) {
            payload.setQueue(ControlType.IS_QUEUE);
        }
        payload.setParams(eMotor.transDataToBytes());
    }

    @Override
    public void cmdGetColorSensor() {
        payload.setCommandID(CommandID.CMD_COLOR_SENSOR);
    }

    @Override
    public void cmdSetColorSensor(CMDParams.DeviceSensor deviceSensor) {
        payload.setCommandID(CommandID.CMD_COLOR_SENSOR);
        payload.setRw(ControlType.WRITE_ONLY);
        payload.setParams(deviceSensor.transDataToBytes());
    }

    @Override
    public void cmdGetInfraredSensor() {
        payload.setCommandID(CommandID.CMD_IR_SWITCH);
    }

    @Override
    public void cmdSetInfraredSensor(CMDParams.DeviceSensor deviceSensor) {
        payload.setCommandID(CommandID.CMD_IR_SWITCH);
        payload.setRw(ControlType.WRITE_ONLY);
        payload.setParams(deviceSensor.transDataToBytes());
    }

    @Override
    public void cmdSetLostStepParams(CMDParams.LostStepParams lostStepParams) {
        payload.setCommandID(CommandID.CMD_LOST_STEP_PARAMS);
        payload.setRw(ControlType.WRITE_ONLY);
        payload.setParams(lostStepParams.transDataToBytes());
    }

    @Override
    public void cmdSetLostStep(boolean isQueue) {
        payload.setCommandID(CommandID.CMD_LOST_STEP);
        payload.setRw(ControlType.WRITE_ONLY);
        if (isQueue) {
            payload.setQueue(ControlType.IS_QUEUE);
        }
    }

    public void cmdClearQueue() {
        payload.setCommandID(CommandID.CMD_QUEUE_CLEAR);
        payload.setRw(ControlType.WRITE_ONLY);
    }

    public void cmdStartQueue() {
        payload.setCommandID(CommandID.CMD_QUEUE_START_EXEC);
        payload.setRw(ControlType.WRITE_ONLY);
    }

    public void cmdStopQueue() {
        payload.setCommandID(CommandID.CMD_QUEUE_STOP_EXEC);
        payload.setRw(ControlType.WRITE_ONLY);
    }

    public void cmdForceStopQueue() {
        payload.setCommandID(CommandID.CMD_QUEUE_FORCE_STOP_EXEC);
        payload.setRw(ControlType.WRITE_ONLY);
    }

    public void cmdQueueCurrentIndex() {
        payload.setCommandID(CommandID.CMD_QUEUE_CURRENT_INDEX);
        payload.setRw(ControlType.WRITE_ONLY);
    }

    public void cmdQueueLeftSpace() {
        payload.setCommandID(CommandID.CMD_QUEUE_GET_LEFT_SPACE);
    }

}
