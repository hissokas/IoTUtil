package com.kirkcola.iotutils;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kirkcola.iotutils.DataIO.IoTData_Receive;
import com.kirkcola.iotutils.Socket.SocketProcess;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Detail extends Fragment {

    private static Fragment_Detail detail;
    View rootView;
    private TextView valueIP;
    private TextView valueID;
    private TextView heartbeat;
    private TextView ir_stat;
    private TextView temp_hum;
    private TextView led_stat;
    private TextView rgb;
    private TextView motor_speed;

    public static Fragment_Detail Instance() {
        if (detail == null)
            detail = new Fragment_Detail();
        return detail;
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO 接收消息并且去更新UI线程上的控件内容
            if (msg.what == 0) {
                Bundle b = msg.getData();
                byte[] content = (byte[]) b.get("content");
                Fragment_Detail.Instance().display(content);
            }
            super.handleMessage(msg);
        }
    };

    public Fragment_Detail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_detail,container,false);
        valueIP = (TextView) rootView.findViewById(R.id.valueIoTIPandPort);
        valueID = (TextView) rootView.findViewById(R.id.valueIoTNum);
        heartbeat = (TextView) rootView.findViewById(R.id.valueHeartbeat);
        ir_stat = (TextView) rootView.findViewById(R.id.valueIRStatus);
        temp_hum = (TextView) rootView.findViewById(R.id.valueHumTemp);
        led_stat=(TextView) rootView.findViewById(R.id.statLED);
        rgb = (TextView) rootView.findViewById(R.id.colorDisplay);
        motor_speed = (TextView) rootView.findViewById(R.id.valueMotorSpeed);

        return rootView;
    }

    public void display(byte[] bytes) {
        try {
            IoTData_Receive ir;
            ir = new IoTData_Receive(bytes);
            valueID.setText(ir.getID()+"");
            valueIP.setText(SocketProcess.clientIP+":"+Fragment_Settings.iot_port+"");
            heartbeat.setText(ir.getHeartbeat()+"");
            if(ir.getSensor_dev3().getV1()!=0){
                ir_stat.setText("触发");
            }else{
                ir_stat.setText("就绪");
            }
            temp_hum.setText("温度:"+ir.getSensor_dev1().getV1()+"℃, "+"湿度:"+ir.getSensor_dev1().getV2()+"%");
            rgb.setBackgroundColor(Color.rgb(Fragment_Control.rgb_red,Fragment_Control.rgb_green,Fragment_Control.rgb_blue));
            if(Fragment_Control.led_stat==0){
                led_stat.setText("关");
            }else{
                led_stat.setText("开");
            }
            //SocketProcess.clientIP;

        } catch (Exception e) {
            System.out.println("数据错误!");
            e.printStackTrace();
        }
    }
}
