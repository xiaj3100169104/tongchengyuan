package com.juns.wechat.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.juns.wechat.R;
import com.style.album.SelectLocalPictureActivity;
import com.style.base.BaseBusFragment;
import com.juns.wechat.zxing.CaptureActivity;

public class Fragment_Dicover extends BaseBusFragment implements OnClickListener {
	// 发现
	private Activity ctx;
	private View layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (layout == null) {
			ctx = this.getActivity();
			layout = ctx.getLayoutInflater().inflate(R.layout.fragment_dicover,
					null);
			initViews();
			initData();
			setOnListener();
		} else {
			ViewGroup parent = (ViewGroup) layout.getParent();
			if (parent != null) {
				parent.removeView(layout);
			}
		}
		return layout;
	}

	private void initViews() {
		// TODO Auto-generated method stub

	}

	private void setOnListener() {
		// TODO Auto-generated method stub
		layout.findViewById(R.id.txt_pengyouquan).setOnClickListener(this);
		layout.findViewById(R.id.txt_saoyisao).setOnClickListener(this);
		layout.findViewById(R.id.txt_yaoyiyao).setOnClickListener(this);
		layout.findViewById(R.id.txt_nearby).setOnClickListener(this);
		layout.findViewById(R.id.txt_piaoliuping).setOnClickListener(this);
		layout.findViewById(R.id.txt_shop).setOnClickListener(this);
		layout.findViewById(R.id.txt_game).setOnClickListener(this);
	}

	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_pengyouquan:// 朋友圈
			startActivity(new Intent(getActivity(), SelectLocalPictureActivity.class));
			break;
		case R.id.txt_saoyisao:// 扫一扫
			startActivity(new Intent(getActivity(), CaptureActivity.class));
			break;
		/*case R.id.txt_yaoyiyao:
			CommonUtil.startActivity(getActivity(), PublicActivity.class,
					new BasicNameValuePair(Constants.NAME, getString(R.string.shake)));
			break;
		case R.id.txt_nearby:
			CommonUtil.startActivity(getActivity(), PublicActivity.class,
					new BasicNameValuePair(Constants.NAME, getString(R.string.people_nearby)));
			break;
		case R.id.txt_piaoliuping:
			CommonUtil.startActivity(getActivity(), PublicActivity.class,
					new BasicNameValuePair(Constants.NAME, getString(R.string.drift_bottle)));
			break;
		case R.id.txt_shop:
			CommonUtil.startActivity(getActivity(), PublicActivity.class,
					new BasicNameValuePair(Constants.NAME, getString(R.string.shopping)));
			break;
		case R.id.txt_game:
			CommonUtil.startActivity(getActivity(), PublicActivity.class,
					new BasicNameValuePair(Constants.NAME, getString(R.string.games)));
			break;*/
		default:
			break;
		}
	}
}
