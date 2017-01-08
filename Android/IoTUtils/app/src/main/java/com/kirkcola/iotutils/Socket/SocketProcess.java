package com.kirkcola.iotutils.Socket;

import android.os.Bundle;
import android.os.Message;

import com.kirkcola.iotutils.DataIO.IoTData_Receive;
import com.kirkcola.iotutils.DataIO.IoTData_Send;
import com.kirkcola.iotutils.Fragment_Control;
import com.kirkcola.iotutils.Fragment_Detail;
import com.kirkcola.iotutils.Fragment_Settings;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by falling on 2015/12/19.
 */
public class SocketProcess extends Thread {
    private static final int UPDATE = 0;
    public static String clientIP;

    Fragment_Settings fs;

    IoTData_Receive dr;
    IoTData_Send ds;

    byte[] bytes = new byte[33];
    Socket s = null;
    InputStream is;
    DataInputStream in;
    DataOutputStream out;
    OutputStream os;

    public SocketProcess(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        deal();
    }

    private void deal() {

        try {
            while (SocketServer.isContinue) {
                is = s.getInputStream();
                os = s.getOutputStream();
                out = new DataOutputStream(os);
                in = new DataInputStream(is);
                in.read(bytes);
                clientIP=s.getInetAddress().toString().substring(1,s.getInetAddress().toString().length());
                sendDataToSettings(bytes);
                sendDataToDetail(bytes);
                out.write(Fragment_Control.Instance().Send());
                out.flush();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                is.close();
                in.close();
                s.close();
                os.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    private void sendDataToSettings(byte[] content) {
        Message msg = new Message();
        msg.what = UPDATE;
        Bundle b = new Bundle();
        b.putByteArray("content", content);
        msg.setData(b);
        Fragment_Settings.Instance().handler.sendMessage(msg);
    }
    private void sendDataToDetail(byte[] content) {
        Message msg = new Message();
        msg.what = UPDATE;
        Bundle b = new Bundle();
        b.putByteArray("content", content);
        msg.setData(b);
        Fragment_Detail.Instance().handler.sendMessage(msg);
    }

}

