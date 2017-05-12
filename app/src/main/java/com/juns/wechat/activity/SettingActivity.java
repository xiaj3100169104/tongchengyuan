package com.juns.wechat.activity;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.juns.wechat.dynamic.FriendCircleActivity;
import com.same.city.love.R;
import com.style.base.BaseToolbarActivity;
import com.juns.wechat.manager.AccountManager;

import butterknife.OnClick;


//设置
public class SettingActivity extends BaseToolbarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_setting;
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initData() {
        setToolbarTitle("设置");
    }

    @OnClick(R.id.layout_message_notify)
    public void onClickEvent1() {
        skip(SettingNewMessageActivity.class);
    }

    @OnClick(R.id.layout_about)
    public void onClickEvent2() {
        skip(AboutProductActivity.class);
    }

    @OnClick(R.id.btn_exit)
    public void onClickEvent3() {
        AccountManager.getInstance().logOut();
        finish();
    }

}
