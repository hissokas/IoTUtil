package com.kirkcola.iotutils.DataIO;

/**
 * Created by Kirk on 2016/12/28.
 */

public class IoTData_Receive {
    private short ID;
    private short sensor_num;
    private short heartbeat;

    private SensorData_Receive sensor_dev1; //HT
    private SensorData_Receive sensor_dev2; //RGB
    private SensorData_Receive sensor_dev3; //IR

    private byte[] bytes;

    public IoTData_Receive(byte[] b) throws Exception{
        this.bytes=b;
        System.out.print(DataTypeConvert.bytes2HexString(bytes));
        if(bytes[0]==(byte)0xAA&&
                bytes[1]==(byte)0xAA&&
                    bytes[2]==(byte)0xAA&&
                        bytes[3]==(byte)0xAB){
            /**
             * 校验码核对
             */
        }else{
            throw new Exception("起始标志错误!");
        }
        /*AA AA AA AB ID SN (HT BT) (ID ID) (V1 V1) (V2 V2) (V3 V3)*/
        ID=(short)bytes[4];
        sensor_num=(short)bytes[5];
        heartbeat=DataTypeConvert.bytesToShort(bytes,6);

        byte[] dev_data1 = new byte[8];
        byte[] dev_data2 = new byte[8];
        byte[] dev_data3 = new byte[8];

        for(int i=0;i<8;i++){
            dev_data1[i]=bytes[i+8];
            dev_data2[i]=bytes[i+16];
            dev_data3[i]=bytes[i+24];
        }

        sensor_dev1 = new SensorData_Receive(dev_data1);
        sensor_dev2 = new SensorData_Receive(dev_data2);
        sensor_dev3 = new SensorData_Receive(dev_data3);

    }

    public short getID() {
        return ID;
    }

    public void setID(short ID) {
        this.ID = ID;
    }

    public short getSensor_num() {
        return sensor_num;
    }

    public void setSensor_num(short sensor_num) {
        this.sensor_num = sensor_num;
    }

    public short getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(short heartbeat) {
        this.heartbeat = heartbeat;
    }

    public SensorData_Receive getSensor_dev1() {
        return sensor_dev1;
    }

    public void setSensor_dev1(SensorData_Receive sensor_dev1) {
        this.sensor_dev1 = sensor_dev1;
    }

    public SensorData_Receive getSensor_dev2() {
        return sensor_dev2;
    }

    public void setSensor_dev2(SensorData_Receive sensor_dev2) {
        this.sensor_dev2 = sensor_dev2;
    }

    public SensorData_Receive getSensor_dev3() {
        return sensor_dev3;
    }

    public void setSensor_dev3(SensorData_Receive sensor_dev3) {
        this.sensor_dev3 = sensor_dev3;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
