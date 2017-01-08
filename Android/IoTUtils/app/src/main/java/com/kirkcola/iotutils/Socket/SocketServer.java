package com.kirkcola.iotutils.Socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by falling on 2015/12/1.
 */
public class SocketServer extends Thread {
    public static ServerSocket sst = null;
    public static boolean isContinue = true;//表示接入的Socket是否继续执行
    private int port;

    public SocketServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        startServer();
    }

    private void startServer() {
        try {
            sst = new ServerSocket(port);
            while (true) {
                System.out.println("Server start!");
                Socket s = sst.accept();
                new SocketProcess(s).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
