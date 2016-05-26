package com.hyj.demo.annotaionsframe;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.R;
import com.hyj.demo.tools.DialogUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * annotation框架使用，最多只能有6543个actiivty
 * 
 * @Author hyj
 * @Date 2016-1-26 上午9:15:19
 */
@EActivity(R.layout.annotations_main)
public class AnnotationsActivity extends BaseActivity {

	@ViewById(R.id.annoBtTest)
	protected Button btnTest;
	@ViewById(R.id.annoTv1)
	public TextView tv1;
	@ViewById(R.id.annoTv2)
	public TextView tv2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Click(R.id.annoBtTest)
	public void annoBtnClick() {
		DialogUtils.showToastShort(this, "AnnoFrame绑定clickListener事件");
	}

	/**
	 * 在view都初始化之后 注解会自动调用此方法来给界面赋值
	 */
	@AfterViews
	public void setTextView() {
		tv1.setText("@AfterViews 测试1");
		tv2.setText("@AfterViews 测试2");
	}
}
