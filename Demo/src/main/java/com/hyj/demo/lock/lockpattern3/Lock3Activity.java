package com.hyj.demo.lock.lockpattern3;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.R;

public class Lock3Activity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private LockPointView lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock3_main);

        myInit();
    }

    private void myInit() {
        lock = (LockPointView) findViewById(R.id.lock3Pattern);
        lock.setPointCount(5);

        ((CheckBox) findViewById(R.id.lock3Chkline)).setOnCheckedChangeListener(this);
        ((CheckBox) findViewById(R.id.lock3ChkSound)).setOnCheckedChangeListener(this);
        ((CheckBox) findViewById(R.id.lock3ChkShake)).setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.lock3Chkline:
                lock.setHasLine(isChecked);
                break;

            case R.id.lock3ChkSound:
                lock.setHasSound(isChecked);
                break;

            case R.id.lock3ChkShake:
                lock.setHasShake(isChecked);
                break;
        }
    }
}
