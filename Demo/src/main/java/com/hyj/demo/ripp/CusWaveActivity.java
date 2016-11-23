package com.hyj.demo.ripp;

import android.graphics.Color;
import android.os.Bundle;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.R;
import com.hyj.lib.view.WaveView;

public class CusWaveActivity extends BaseActivity {

    private WaveView wvCus;//自定义波形图

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ripple_cus_main);

        myInit();
    }

    private void myInit() {
        wvCus = (WaveView) findViewById(R.id.rippWvCus);

        wvCus.setDuration(5000);
        wvCus.setSpeed(400);
        wvCus.setColor(Color.RED);
        wvCus.setFill(false);//设置是否实心圆
        wvCus.setEqual(true);//设置圆是否等间距扩散
        wvCus.start();

//        wvCus.setDuration(5000);
//        wvCus.setColor(Color.RED);
//        wvCus.setFill(true);
//        wvCus.setEqual(false);
//        wvCus.start();
////        wvCus.postDelayed(new Runnable() {
////            @Override
////            public void run() {
////                wvCus.stop();
////            }
////        }, 10000);
    }
}
