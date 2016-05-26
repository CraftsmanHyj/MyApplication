package com.hyj.lib.gobang;

import android.os.Bundle;

import com.hyj.lib.BaseActivity;
import com.hyj.lib.R;
import com.hyj.lib.gobang.WuziqiPanel.OnGameOverListener;
import com.hyj.lib.tools.DialogUtils;
import com.hyj.lib.tools.DialogUtils.DialogAction;

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
        gobang.setOnGameOverListener(new OnGameOverListener() {
            @Override
            public void onTheEnd(String msg) {
                DialogUtils.showConfirmDialog(GobangActivity.this, "提示", msg,
                        new DialogAction() {
                            @Override
                            public void action() {
                                gobang.reStart();
                            }
                        }, new DialogAction() {
                            @Override
                            public void action() {
                                GobangActivity.this.finish();
                            }
                        });
            }
        });
    }
}
