package com.juns.wechat.activity;


import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.juns.wechat.manager.AccountManager;
import com.same.city.love.R;
import com.style.base.BaseToolbarActivity;

import butterknife.Bind;


public class SettingNewMessageActivity extends BaseToolbarActivity implements CompoundButton.OnCheckedChangeListener {

    @Bind(R.id.switch_ringtone)
    Switch switchRingtone;
    @Bind(R.id.switch_vibrate)
    Switch switchVibrate;
    @Bind(R.id.switch_show_message)
    Switch switchShowMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_setting_push_notify;
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initData() {
        setToolbarTitle("新消息通知");

        switchShowMessage.setOnCheckedChangeListener(this);
        switchRingtone.setOnCheckedChangeListener(this);
        switchVibrate.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.switch_show_message:

                break;
            case R.id.switch_ringtone:

                break;
            case R.id.switch_vibrate:

                break;

        }
    }
}
