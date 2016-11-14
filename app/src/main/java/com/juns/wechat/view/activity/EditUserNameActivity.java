package com.juns.wechat.view.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.common.CommonUtil;
import com.juns.wechat.view.BaseActivity;

//注册后填写用户信息
public class EditUserNameActivity extends BaseActivity implements
		OnClickListener {
	private TextView txt_title;
	private ImageView img_back;
	private EditText edit_name;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_edit_userinfo);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initControl() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setText("欢迎");
		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setVisibility(View.GONE);
		edit_name = (EditText) findViewById(R.id.edit_name);
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initData() {
	}

	@Override
	protected void setListener() {
		img_back.setOnClickListener(this);
		findViewById(R.id.btn_start).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			CommonUtil.finish(EditUserNameActivity.this);
			break;
		case R.id.btn_start:

			break;
		default:
			break;
		}
	}


}
