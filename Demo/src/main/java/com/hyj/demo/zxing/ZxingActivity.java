package com.hyj.demo.zxing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.R;
import com.hyj.lib.tools.ToastUtils;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

/**
 * <pre>
 *     Zxing第三方扫描库：https://github.com/zxing/zxing
 *     由开源者抽取出来的：
 *     这里的jar包是从慕课网中下载，基于zxing 3.1抽取的一个android端扫描、解码、识别的库
 * </pre>
 *
 * @Author hyj
 * @Date 2016/5/8 22:11
 */
public class ZxingActivity extends BaseActivity {
    private final int REQCODE_ZXING = 0X00000001;//扫描二维码请求码

    private Button btScan;
    private Button btGenerate;
    private TextView tvResult;
    private EditText etEwm;
    private ImageView imgEwm;
    private CheckBox cbLoglo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zxing_main);

        myInit();
    }

    private void myInit() {
        initView();
        initData();
        initListener();
        GenerateEwm();
    }

    private void initView() {
        btScan = (Button) findViewById(R.id.zxingBtScan);
        btGenerate = (Button) findViewById(R.id.zxingBtGenerate);
        tvResult = (TextView) findViewById(R.id.zxingTvResult);
        etEwm = (EditText) findViewById(R.id.zxingEtEwmInfo);
        imgEwm = (ImageView) findViewById(R.id.zxingImgEwm);
        cbLoglo = (CheckBox) findViewById(R.id.zxingCbLogo);
    }

    private void initData() {
        etEwm.setText("https://github.com/asyncHyj/MyApplication");
    }

    private void initListener() {
        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ZxingActivity.this, CaptureActivity.class);//CaptureActivity
                startActivityForResult(intent, REQCODE_ZXING);
            }
        });

        btGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenerateEwm();
            }
        });

        etEwm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEwm.setText(etEwm.getText().toString());
                etEwm.selectAll();
            }
        });

        cbLoglo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenerateEwm();
            }
        });
    }

    /**
     * 生成二维码
     */
    private void GenerateEwm() {
        String str = etEwm.getText().toString().trim();
        if (TextUtils.isEmpty(str)) {
            ToastUtils.showToast(this, etEwm, "请输入要生成二维码的内容");
            return;
        }

        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        logo = cbLoglo.isChecked() ? logo : null;
        Bitmap bitmap = EncodingUtils.createQRCode(str, 500, 500, logo);
        imgEwm.setImageBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == data) {
            return;
        }

        switch (requestCode) {
            case REQCODE_ZXING:
                Bundle bundle = data.getExtras();
                String result = bundle.getString("result");
                tvResult.setText(result);
                break;
        }
    }
}
