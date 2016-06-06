package com.hyj.demo.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.R;
import com.hyj.lib.tools.DialogUtils;

public class DialogActivity extends BaseActivity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_main);

		myInit();
	}

	private void myInit() {
		findViewById(R.id.dialogRb1).setOnClickListener(this);
		findViewById(R.id.dialogRb2).setOnClickListener(this);
		findViewById(R.id.dialogRb3).setOnClickListener(this);
		findViewById(R.id.dialogRb4).setOnClickListener(this);
		findViewById(R.id.dialogRb5).setOnClickListener(this);
		findViewById(R.id.dialogRb6).setOnClickListener(this);
		findViewById(R.id.dialogRb7).setOnClickListener(this);
		findViewById(R.id.dialogRb8).setOnClickListener(this);
		findViewById(R.id.dialogRb9).setOnClickListener(this);
		findViewById(R.id.dialogRb10).setOnClickListener(this);
		findViewById(R.id.dialogRb11).setOnClickListener(this);
		findViewById(R.id.dialogRb12).setOnClickListener(this);
	}

	@SuppressLint("InflateParams")
	@Override
	public void onClick(View v) {
		DialogUtils.DialogAction okAction = new DialogUtils.DialogAction() {

			@Override
			public void action() {
				DialogUtils.showToastShort(DialogActivity.this, "点击确认操作按钮");
			}
		};

		DialogUtils.DialogAction cancelAction = new DialogUtils.DialogAction() {

			@Override
			public void action() {
				DialogUtils.showToastShort(DialogActivity.this, "点击取消操作按钮");
			}
		};

		View view = LayoutInflater.from(this).inflate(
				R.layout.listviewindex_main, null, false);

		Throwable e = new Throwable("抛出异常测试");

		String title = "对话框测试案例";

		switch (v.getId()) {
		case R.id.dialogRb1:
			DialogUtils.showMessageDialog(this, title, "含有确认操作", okAction);
			break;

		case R.id.dialogRb2:
			DialogUtils.showMessageDialog(this, title, "不含确认操作");
			break;

		case R.id.dialogRb3:
			DialogUtils.showMessageDialog(this, "没有标题");
			break;

		case R.id.dialogRb4:
			DialogUtils.showExceptionDialog(this, title, e, okAction);
			break;

		case R.id.dialogRb5:
			DialogUtils.showExceptionDialog(this, title, e);
			break;

		case R.id.dialogRb6:
			DialogUtils.showExceptionDialog(this, e, okAction);
			break;

		case R.id.dialogRb7:
			DialogUtils.showExceptionDialog(this, e);
			break;

		case R.id.dialogRb8:
			DialogUtils.showConfirmDialog(this, title, "确认对话框", okAction,
					cancelAction);
			break;

		case R.id.dialogRb9:
			DialogUtils.showConfirmDialog(this, title, "确认对话框", okAction);
			break;

		case R.id.dialogRb10:
			DialogUtils.showDialog(this, title, view, okAction, cancelAction);
			break;

		case R.id.dialogRb11:
			DialogUtils.showDialog(this, title, view, okAction);
			break;

		case R.id.dialogRb12:
			DialogUtils.showDialog(this, title, view);
			break;
		}
	}
}
