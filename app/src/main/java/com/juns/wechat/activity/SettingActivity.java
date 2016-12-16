package com.juns.wechat.activity;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.juns.wechat.R;
import com.style.base.BaseToolbarActivity;
import com.juns.wechat.manager.AccountManager;


//设置
public class SettingActivity extends BaseToolbarActivity implements OnClickListener {

	@Override
	public void initData() {
		setToolbarTitle("设置");
		setOnListener();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mLayoutResID = R.layout.activity_setting;
		super.onCreate(savedInstanceState);

	}

	private void setOnListener() {
		findViewById(R.id.btnexit).setOnClickListener(this);
		findViewById(R.id.txt_msgtip).setOnClickListener(this);
		findViewById(R.id.txt_usersafe).setOnClickListener(this);
		findViewById(R.id.txt_yinsi).setOnClickListener(this);
		findViewById(R.id.txt_tongyong).setOnClickListener(this);
		findViewById(R.id.txt_about).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
	/*	case R.id.txt_about:
			CommonUtil.startActivity(SettingActivity.this, WebViewActivity.class,
					new BasicNameValuePair(Constants.Title, "关于微信"),
					new BasicNameValuePair(Constants.URL,
							"https://github.com/motianhuo/wechat"));
			break;*/

		case R.id.btnexit:
            AccountManager.getInstance().logOut();
            finish();
			break;
		default:
			break;
		}
	}
}
