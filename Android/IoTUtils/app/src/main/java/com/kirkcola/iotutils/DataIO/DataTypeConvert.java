package com.kirkcola.iotutils.DataIO;

/**
 * Created by Kirk on 2016/12/29.
 */

public class DataTypeConvert {
    //byte 转 Int
    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF)
                | ((src[offset + 1] & 0xFF) << 8)
                | ((src[offset + 2] & 0xFF) << 16)
                | ((src[offset + 3] & 0xFF) << 24));
        return value;
    }
    //int 转 byte
    public static byte[] intTobyte(int value) {
        byte[] src = new byte[4];
        src[3] =  (byte) ((value>>24) & 0xFF);
        src[2] =  (byte) ((value>>16) & 0xFF);
        src[1] =  (byte) ((value>>8) & 0xFF);
        src[0] =  (byte) (value & 0xFF);
        return src;
    }


    public static short bytesToShort(byte[] src, int offset) {
        short value;
        value = (short) ((src[offset] & 0xFF)
                | ((src[offset + 1] & 0xFF) << 8));
        return value;
    }

    public static byte[] shortToBytes(short value) {
        byte[] bytes = new byte[2];
        bytes[1] = (byte) ((value>>8)& 0xFF);
        bytes[0] = (byte) ((value)& 0xFF);

        return bytes;
    }

    //获取RGB的信息 RGB[2] = R RGB[1]=G RGB[0]=B
    public static short[] getRGBvalue(int value) {
        short RGB[] = new short[3];
        RGB[2] = (short)((value) &0xFF);
        RGB[1] = (short)((value>>>8) &0xFF);
        RGB[0] = (short)((value>>>16) &0xFF);

        return RGB;
    }
    //把RGB三个数值封装成一个int
    public static int FormateRGB(int R,int G,int B) {
        //RGB范围控制0~255
        if(R<0)
            R=-R;
        if(G<0)
            G=-G;
        if(B<0)
            B=-B;
        R%=256;
        G%=256;
        B%=256;

        int RGB=R << 16
                |(G << 8)
                |(B);

        return RGB;
    }


    //判断校验码是否正确
    public static boolean check(byte[] bytes){
        byte re=0x00;
        int i;
        for(i =0;i<bytes.length-4;i++){
            re^=bytes[i];
        }
        return re==bytes[i];

    }

    //加校验码
    public static void setCheckCode(byte[] data) {
        byte CheckCode = 0x00;
        int i;
        for(i =0;i<data.length-1;i++){
            CheckCode ^= data[i];
        }
        data[i]=CheckCode;
    }

    public static String bytes2HexString(byte[] b) {
        String ret = "";
        for (byte aB : b) {
            String hex = Integer.toHexString(aB & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase()+"-";
        }
        return ret;
    }
}
