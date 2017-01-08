package com.kirkcola.iotutils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.kirkcola.iotutils.Socket.SocketServer;

import java.io.IOException;

//import com.kirkcola.iotutils.data.receive.ReceiveIOTData;
//import com.kirkcola.iotutils.data.send.SendIOTData;


public class MainActivity extends AppCompatActivity {
    private static boolean LED_Flag = false;
    private short heartbeat = 0;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("IoT Remote Control Utility");
        toolbar.setSubtitle("31401419 徐可");
        setSupportActionBar(toolbar);
        Snackbar.make(findViewById(android.R.id.content),"Welcome!", Snackbar.LENGTH_SHORT)
                .show();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int i, float v, int i2) {}
            @Override
            public void onPageSelected(int i) {
            switch(i){
                case 0: toolbar.setTitle("Iot Kit Configuration");
                    break;
                case 1: toolbar.setTitle("IoT Kit Info");
                    break;
                case 2: toolbar.setTitle("IoT Kit Control Panel");
                    break;
                default:
                    break;
            }
        }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return Fragment_Settings.Instance();
                case 1:
                    return Fragment_Detail.Instance();
                case 2:
                    return Fragment_Control.Instance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        //socket 在程序处于后台或者屏幕关闭的时候关闭socket
        try {
            if (!SocketServer.sst.isClosed()) {
                SocketServer.sst.close(); //关闭Server socket
                SocketServer.isContinue = false;//停止 socket继续执行
                System.out.println("Server close");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
