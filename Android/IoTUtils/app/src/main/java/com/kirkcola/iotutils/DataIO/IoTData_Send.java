package com.kirkcola.iotutils.DataIO;

import android.provider.ContactsContract;

/**
 * Created by Kirk on 2016/12/28.
 */

public class IoTData_Send {
    private byte START_F0;
    private byte START_F1;
    private byte START_F2;
    private byte START_F3; //4

    private SensorData_Send sensor_dev1; //LED 13
    private SensorData_Send sensor_dev2; //RGB 19
    private SensorData_Send sensor_dev3; //Motor 25

    byte[] bytes = new byte[25];

    public IoTData_Send(){
        START_F0= (byte)0xAA;
        START_F1= (byte)0xAA;
        START_F2= (byte)0xAA;
        START_F3= (byte)0xAB;

        sensor_dev1 = new SensorData_Send();
        sensor_dev2 = new SensorData_Send();
        sensor_dev3 = new SensorData_Send();
    }

    public byte getSTART_F0() {
        return START_F0;
    }

    public void setSTART_F0(byte START_F0) {
        this.START_F0 = START_F0;
    }

    public byte getSTART_F1() {
        return START_F1;
    }

    public void setSTART_F1(byte START_F1) {
        this.START_F1 = START_F1;
    }

    public byte getSTART_F2() {
        return START_F2;
    }

    public void setSTART_F2(byte START_F2) {
        this.START_F2 = START_F2;
    }

    public byte getSTART_F3() {
        return START_F3;
    }

    public void setSTART_F3(byte START_F3) {
        this.START_F3 = START_F3;
    }

    public SensorData_Send getSensor_dev1() {
        return sensor_dev1;
    }

    public void setSensor_dev1(SensorData_Send sensor_dev1) {
        this.sensor_dev1 = sensor_dev1;
    }

    public SensorData_Send getSensor_dev2() {
        return sensor_dev2;
    }

    public void setSensor_dev2(SensorData_Send sensor_dev2) {
        this.sensor_dev2 = sensor_dev2;
    }

    public SensorData_Send getSensor_dev3() {
        return sensor_dev3;
    }

    public void setSensor_dev3(SensorData_Send sensor_dev3) {
        this.sensor_dev3 = sensor_dev3;
    }

    public byte[] getBytes() {
        bytes[0] = this.getSTART_F0();
        bytes[1] = this.getSTART_F1();
        bytes[2] = this.getSTART_F2();
        bytes[3] = this.getSTART_F3();

        byte[] d1=this.getSensor_dev1().getBytes();
        byte[] d2=this.getSensor_dev2().getBytes();
        byte[] d3=this.getSensor_dev3().getBytes();

        for(int i=0; i<6;i++){
            bytes[4+i]=d1[i];
            bytes[10+i]=d2[i];
            bytes[16+i]=d3[i];
        }

        DataTypeConvert.setCheckCode(bytes);
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
