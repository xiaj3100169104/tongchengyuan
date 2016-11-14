package com.juns.wechat.view.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.juns.wechat.Constants;
import com.juns.wechat.R;
import com.juns.wechat.common.CommonUtil;
import com.juns.wechat.view.BaseActivity;

//从通讯录添加好友
public class AddFromContactActivity extends BaseActivity implements
		OnClickListener {
	private TextView txt_title;
	private ImageView img_back;
	private ListView mlistview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_listview);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initControl() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setText("通讯录朋友");
		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setVisibility(View.VISIBLE);
		mlistview = (ListView) findViewById(R.id.lvNewFriends);
	}

	@Override
	protected void initView() {
		getLoadingDialog("正在获取联系人").show();
	}

	@Override
	protected void initData() {
	}

	@Override
	protected void setListener() {
		img_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			CommonUtil.finish(AddFromContactActivity.this);
			break;
		default:
			break;
		}
	}

}
