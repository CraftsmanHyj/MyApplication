package com.hyj.demo.lock.number;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.hyj.demo.R;
import com.hyj.lib.lock.LockNumberView;

public class LockNumberActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

    private LockNumberView lockNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_number_main);

        myInit();
    }

    private void myInit() {
        lockNumber = (LockNumberView) findViewById(R.id.lockNumber);
        ((CheckBox) findViewById(R.id.lnChkTrack)).setOnCheckedChangeListener(this);
        ((CheckBox) findViewById(R.id.lnChkSound)).setOnCheckedChangeListener(this);
        ((CheckBox) findViewById(R.id.lnChkShake)).setOnCheckedChangeListener(this);

        lockNumber.setOnCompleteListener(new LockNumberView.OnCompleteListener() {
            @Override
            public void onComplete(String password) {
                lockNumber.resert();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.lnChkTrack:
                lockNumber.setHasTrack(isChecked);
                break;

            case R.id.lnChkSound:
                lockNumber.setHasVoice(isChecked);
                break;

            case R.id.lnChkShake:
                lockNumber.setHasShake(isChecked);
                break;
        }
    }
}
