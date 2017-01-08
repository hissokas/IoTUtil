package com.kirkcola.iotutils.DataIO;

import android.provider.ContactsContract;

/**
 * Created by Kirk on 2016/12/28.
 */

public class SensorData_Send {
    private short v1;
    private short v2;
    private short v3;

    private byte[] bytes = new byte[6];

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

    public byte[] getBytes() {
        byte[] b;
        int i;

        b = DataTypeConvert.shortToBytes(this.getV1());
        for(i=0;i<2;i++){
            bytes[i] = b[i];
        }

        b = DataTypeConvert.shortToBytes(this.getV2());
        for(i=2;i<4;i++){
            bytes[i] = b[i-2];
        }

        b = DataTypeConvert.shortToBytes(this.getV3());
        for(i=4;i<6;i++){
            bytes[i] = b[i-4];
        }

        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
