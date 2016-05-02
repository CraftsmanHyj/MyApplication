package com.hyj.lib.lock;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hyj.lib.BaseActivity;
import com.hyj.lib.Constants;
import com.hyj.lib.R;
import com.hyj.lib.tools.DialogUtils;
import com.hyj.lib.tools.MD5Utils;
import com.hyj.lib.tools.SPUtils;

import java.util.HashMap;
import java.util.Map;

public class LockQuestionActivity extends BaseActivity {
    /**
     * 设置
     */
    private final int STATE_SET = 0X001;
    /**
     * 修改
     */
    private final int STATE_UPDATE = 0X002;
    /**
     * 验密
     */
    private final int STATE_UNLOCKING = 0X003;

    private TextView tvTitle;
    private EditText etQuestion, etAnswer;
    private TextView tvUpdate, tvSave;

    private int state;
    private boolean isInit = false;//是否是第一次进入APP设置密保
    private String question, answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_question);

        myInit();
    }

    private void myInit() {
        initView();
        initData();
        initListener();
        updateView();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.lockQuTvTitle);
        etQuestion = (EditText) findViewById(R.id.lockQuEtQuestion);
        etAnswer = (EditText) findViewById(R.id.lockQuEtAnswer);
        tvUpdate = (TextView) findViewById(R.id.lockQuTvUpdate);
        tvSave = (TextView) findViewById(R.id.lockQuTvSave);
    }

    private void initData() {
        question = (String) SPUtils.getParam(this, Constants.FILE_NAME_SHARED, Constants.FIELD_QUESTION, "");
        answer = (String) SPUtils.getParam(this, Constants.FILE_NAME_SHARED, Constants.FIELD_ANSWER, "");

        if (TextUtils.isEmpty(question)) {
            isInit = true;
            state = STATE_SET;
        } else {
            state = STATE_UNLOCKING;
        }
    }

    private void initListener() {
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInit = true;
                state = STATE_UPDATE;
                updateView();
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCharge();
            }
        });
    }

    private void updateView() {
        switch (state) {
            case STATE_SET:
                etQuestion.setEnabled(true);
                etQuestion.setText("");
                etQuestion.requestFocus();
                etAnswer.setText("");

                tvTitle.setText("设置密保问题");
                tvUpdate.setVisibility(View.INVISIBLE);
                break;

            case STATE_UPDATE:
                etQuestion.setText(question);
                etQuestion.setEnabled(false);
                etAnswer.requestFocus();

                tvTitle.setText("验证密保问题");
                tvUpdate.setVisibility(View.INVISIBLE);
                break;

            case STATE_UNLOCKING:
                etQuestion.setText(question);
                etQuestion.setEnabled(false);
                etAnswer.requestFocus();
                etAnswer.setText("");

                tvTitle.setText("输入密保");
                tvUpdate.setVisibility(View.VISIBLE);
                break;
        }
    }


    private void myCharge() {
        String strQuestion = etQuestion.getText().toString().trim();
        String strAnswer = etAnswer.getText().toString().trim();

        if (TextUtils.isEmpty(strAnswer)) {
            DialogUtils.showToast(this, etAnswer, "请输入答案");
            return;
        }
        strAnswer = MD5Utils.toMD5(strAnswer);

        switch (state) {
            case STATE_SET:
                if (TextUtils.isEmpty(strQuestion)) {
                    DialogUtils.showToast(this, etQuestion, "请填写问题");
                    return;
                }

                question = strQuestion;
                answer = strAnswer;

                Map<String, Object> mapValue = new HashMap<String, Object>();
                mapValue.put(Constants.FIELD_QUESTION, question);
                mapValue.put(Constants.FIELD_ANSWER, answer);
                SPUtils.putParam(this, Constants.FILE_NAME_SHARED, mapValue);

                state = STATE_UNLOCKING;
                updateView();
                break;

            case STATE_UPDATE:
                if (!answer.equals(strAnswer)) {
                    etAnswer.setText("");
                    DialogUtils.showToast(this, etAnswer, "答案错误，请重新输入");
                    return;
                }

                state = STATE_SET;
                updateView();
                break;

            case STATE_UNLOCKING:
                if (!answer.equals(strAnswer)) {
                    etAnswer.setText("");
                    DialogUtils.showToast(this, etAnswer, "答案错误，请重新输入");
                    return;
                }

                if (!isInit) {
                    //重置九宫格密码
                    SPUtils.putParam(this, Constants.FILE_NAME_SHARED, Constants.FIELD_PWD, "");
                    Intent intent = new Intent(this, LockActivity.class);
                    startActivity(intent);
                }

                finish();
                break;
        }
    }
}
