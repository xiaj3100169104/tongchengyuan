package cn.tongchengyuan.activity;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.CompoundButton;

import com.same.city.love.R;
import com.same.city.love.databinding.ActivitySettingPushNotifyBinding;
import com.style.base.BaseToolbarActivity;


public class SettingNewMessageActivity extends BaseToolbarActivity implements CompoundButton.OnCheckedChangeListener {

    ActivitySettingPushNotifyBinding bd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_setting_push_notify);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    public void initData() {
        setToolbarTitle("新消息通知");

        bd.switchShowMessage.setOnCheckedChangeListener(this);
        bd.switchRingtone.setOnCheckedChangeListener(this);
        bd.switchVibrate.setOnCheckedChangeListener(this);
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
