package com.kirkcola.iotutils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kirkcola.iotutils.DataIO.IoTData_Receive;
import com.kirkcola.iotutils.Socket.SocketProcess;
import com.kirkcola.iotutils.Socket.SocketServer;

import java.io.IOException;


public class Fragment_Settings extends Fragment {
    private static Fragment_Settings settings;
    private static SocketServer server;
    private static boolean isPortOn = false;
    public static String iot_ip;
    public static String iot_port;
    private View rootView;

    private TextView valueIoTIP;
    private EditText edtIoTPort;
    private TextView valueIoTNum;
    private Button btnConnect;
    private TextView valueHeartbeat;

    public void setIPandPort(String ip, String port) {
        this.iot_ip = ip;
        this.iot_port = port;
    }

    public static Fragment_Settings Instance() {
        if (settings == null)
            settings = new Fragment_Settings();
        return settings;
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO 接收消息并且去更新UI线程上的控件内容
            if (msg.what == 0) {
                Bundle b = msg.getData();
                byte[] content = (byte[]) b.get("content");
                Fragment_Settings.Instance().display(content);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        valueIoTIP = (TextView) rootView.findViewById(R.id.txtIoTIPValue);
        edtIoTPort = (EditText) rootView.findViewById(R.id.edtIoTPort);
        valueIoTNum = (TextView) rootView.findViewById(R.id.valueIoTNum);
        btnConnect = (Button) rootView.findViewById(R.id.btnConnect);
        valueHeartbeat = (TextView) rootView.findViewById(R.id.valueHeartbeat);
        if (isPortOn) {
            btnConnect.setText("关闭端口");
        }else{
            btnConnect.setText("开启端口");
        }

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPortOn) {
                    try {
                        setIPandPort(valueIoTIP.getText().toString(), edtIoTPort.getText().toString());
                        int port = Integer.parseInt(iot_port);
                        //新建一个Socket并开启
                        server = new SocketServer(port);
                        server.start();
                        SocketServer.isContinue = true; //端口继续执行
                        isPortOn = true; //将端口状态置为开启
                        System.out.println("Server open on port " + port);
                        Snackbar.make(rootView, "已开启服务器端口" + iot_port, Snackbar.LENGTH_SHORT).show();
                        btnConnect.setText("关闭端口");
                    } catch (NumberFormatException e) {
                        Snackbar.make(rootView, "请输入有效的端口号！", Snackbar.LENGTH_SHORT).show();
                    }
                    //若
                } else {
                    try {
                        //关闭socket服务
                        if (!SocketServer.sst.isClosed()) {
                            SocketServer.sst.close();//关闭Server socket
                            SocketServer.isContinue = false;//接入的socket不再继续执行
                            isPortOn = false;
                            btnConnect.setText("开启端口");
                            System.out.println("Server close");
                            Snackbar.make(rootView, "已关闭端口", Snackbar.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return rootView;
    }

    public void display(byte[] bytes) {
        try {
            IoTData_Receive ir;
            ir = new IoTData_Receive(bytes);
            valueIoTNum.setText(ir.getID() + "");
            valueHeartbeat.setText(ir.getHeartbeat() + "");
            valueIoTIP.setText(SocketProcess.clientIP + "");
        } catch (Exception e) {
            System.out.println("数据错误!");
            e.printStackTrace();
        }
    }
}
