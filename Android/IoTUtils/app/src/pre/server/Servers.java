package com.kirkcola.iotutils.server;

import com.kirkcola.iotutils.data.receive.ReceiveIOTData;
import ui.FrmMain;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by falling on 2015/10/16.
 */
public class Servers extends Thread {
        ServerSocket sst = null;
        @Override
        public void run() {
            startServer();
        }

        private void startServer() {
            try {
                System.out.println("Server start!");
                sst = new ServerSocket(8000);
                ExecutorService pool = Executors.newCachedThreadPool();

                while (true) {
                    Socket s = sst.accept();
                    FrmMain f = new FrmMain();
                    dealSocket t =new dealSocket(s,f);
                    pool.execute(t);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

class dealSocket extends Thread {
    ReceiveIOTData d ;
    byte[] bytes =new byte[56];
    Socket s = null;
    FrmMain  frame ;
    InputStream is;
    DataInputStream in;
    DataOutputStream out;
    OutputStream os;
    public dealSocket(Socket s,FrmMain f) {
        this.s = s;
        this.frame=f;
        frame.setVisible(true);
        frame.setTitle(s.getInetAddress().toString());
    }

    @Override
    public void run() {
       deal();
    }

    private void deal() {

        try {
            System.out.println("socket deal start");
            while(true) {
                is = s.getInputStream();
                os = s.getOutputStream();
                out = new DataOutputStream(os);
                in = new DataInputStream(is);

                in.read(bytes);



                d = new ReceiveIOTData(bytes);

                // System.out.println(d.toString());
                frame.DisDate(d);
                //发送控制信息
                out.write(frame.LoadDate());//
                out.flush();
            }




        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            System.out.println("数据错误!");
            e.printStackTrace();

        }
        finally {
            try {
                is.close();
                in.close();
                os.close();
                out.close();
                s.close();
                frame.setVisible(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


