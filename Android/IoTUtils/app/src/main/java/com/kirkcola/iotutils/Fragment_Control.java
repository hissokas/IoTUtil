package com.kirkcola.iotutils;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.kirkcola.iotutils.DataIO.IoTData_Send;

import org.w3c.dom.Text;

public class Fragment_Control extends Fragment {
    private static Fragment_Control control;
    IoTData_Send ids;
    public static int rgb_red;
    public static int rgb_green;
    public static int rgb_blue;
    public static short led_stat=0;
    public static short motor_speed;

    View rootView;
    Switch SwitchLED;

    SeekBar seekBarRed;
    SeekBar seekBarGreen;
    SeekBar seekBarBlue;
    SeekBar seekBarMotor;

    TextView txtColor;
    TextView valueMotor;

    public static Fragment_Control Instance() {
        if (control == null)
            control = new Fragment_Control();
        return control;
    }


    public Fragment_Control() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_control,container,false);
        SwitchLED = (Switch) rootView.findViewById(R.id.switchLED);

        seekBarRed=(SeekBar) rootView.findViewById(R.id.seekBarRed);
        seekBarGreen=(SeekBar) rootView.findViewById(R.id.seekBarGreen);
        seekBarBlue=(SeekBar) rootView.findViewById(R.id.seekBarBlue);
        seekBarMotor=(SeekBar) rootView.findViewById(R.id.seekBarMotorSpeed);

        txtColor=(TextView) rootView.findViewById(R.id.colorDisplay);
        valueMotor=(TextView) rootView.findViewById(R.id.valueMotorSpeed);
        SwitchLED.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
            boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    led_stat=1;
                } else {
                    led_stat=0;
                }
            }
        });

        seekBarRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rgb_red=progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                txtColor.setBackgroundColor(Color.rgb(rgb_red,rgb_green,rgb_blue));
            }
        });

        seekBarGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rgb_green=progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                txtColor.setBackgroundColor(Color.rgb(rgb_red,rgb_green,rgb_blue));
            }
        });

        seekBarBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rgb_blue=progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                txtColor.setBackgroundColor(Color.rgb(rgb_red,rgb_green,rgb_blue));
            }
        });

        seekBarMotor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                motor_speed=(short)progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                valueMotor.setText(motor_speed+"");
            }
        });

        return rootView;
    }

    public byte[] Send(){
        ids=new IoTData_Send();
        ids.getSensor_dev1().setV1(led_stat);
        ids.getSensor_dev2().setV1((short)rgb_red);
        ids.getSensor_dev2().setV2((short)rgb_green);
        ids.getSensor_dev2().setV3((short)rgb_blue);
        ids.getSensor_dev3().setV1(motor_speed);
        return ids.getBytes();
    }
}
