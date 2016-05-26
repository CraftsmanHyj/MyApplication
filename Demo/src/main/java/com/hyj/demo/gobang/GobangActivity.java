package com.hyj.demo.gobang;

import android.os.Bundle;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.R;
import com.hyj.demo.tools.DialogUtils;

/**
 * 五子棋
 */
public class GobangActivity extends BaseActivity {
    private WuziqiPanel gobang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gobang_main);

        myInit();
    }

    private void myInit() {
        initView();
        initListener();
    }

    private void initView() {
        gobang = (WuziqiPanel) findViewById(R.id.gobang);
    }

    private void initListener() {
        gobang.setOnGameOverListener(new WuziqiPanel.OnGameOverListener() {
            @Override
            public void onTheEnd(String msg) {
                DialogUtils.showConfirmDialog(GobangActivity.this, "提示", msg,
                        new DialogUtils.DialogAction() {
                            @Override
                            public void action() {
                                gobang.reStart();
                            }
                        }, new DialogUtils.DialogAction() {
                            @Override
                            public void action() {
                                GobangActivity.this.finish();
                            }
                        });
            }
        });
    }
}
