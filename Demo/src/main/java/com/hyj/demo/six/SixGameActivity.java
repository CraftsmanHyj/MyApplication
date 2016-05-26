package com.hyj.demo.six;

import android.os.Bundle;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.R;
import com.hyj.demo.tools.DialogUtils;

public class SixGameActivity extends BaseActivity {

    private SixChessPanel chessboard;//棋盘

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.six_main);

        myInit();
    }

    private void myInit() {
        initView();
        initData();
        initListener();
    }

    private void initView() {
        chessboard = (SixChessPanel) findViewById(R.id.sixChessboard);
    }

    private void initData() {
    }

    private void initListener() {
        chessboard.setOnGameOverListener(new SixChessPanel.OnGameOverListener() {
            @Override
            public void onGameOver(int chessType) {
                String msg = "系统错误";
                if (Chess.WHITE == chessType) {
                    msg = "白方获胜";
                } else if (Chess.BLACK == chessType) {
                    msg = "黑方获胜";
                }

                DialogUtils.showConfirmDialog(SixGameActivity.this, "提示", msg,
                        new DialogUtils.DialogAction() {
                            @Override
                            public void action() {
                                chessboard.reStart();
                            }
                        }, new DialogUtils.DialogAction() {
                            @Override
                            public void action() {
                                SixGameActivity.this.finish();
                            }
                        });
            }
        });
    }
}
