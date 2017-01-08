package com.kirkcola.iotutils.DataIO;
import com.kirkcola.iotutils.DataIO.DataTypeConvert;

import static com.kirkcola.iotutils.DataIO.DataTypeConvert.*;

public class SensorData_Receive {

    private short ID;
    private short v1;
    private short v2;
    private short v3;

    public SensorData_Receive(byte[] bytes){
        ID = bytesToShort(bytes,0);
        v1 = bytesToShort(bytes,2);
        v2 = bytesToShort(bytes,4);
        v3 = bytesToShort(bytes,6);
    }

    public short getID() {
        return ID;
    }

    public void setID(short ID) {
        this.ID = ID;
    }

    public short getV1() {
        return v1;
    }

    public void setV1(short v1) {
        this.v1 = v1;
    }

    public short getV2() {
        return v2;
    }

    public void setV2(short v2) {
        this.v2 = v2;
    }

    public short getV3() {
        return v3;
    }

    public void setV3(short v3) {
        this.v3 = v3;
    }
}
