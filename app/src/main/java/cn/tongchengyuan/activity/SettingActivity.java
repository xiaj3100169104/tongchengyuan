package cn.tongchengyuan.activity;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.same.city.love.R;
import com.same.city.love.databinding.ActivitySettingBinding;
import com.style.base.BaseToolbarActivity;

import cn.tongchengyuan.manager.AccountManager;


public class SettingActivity extends BaseToolbarActivity {

    ActivitySettingBinding bd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    public void initData() {
        setToolbarTitle("设置");
        bd.setOnItemClickListener(new OnItemClickListener());

    }

    public class OnItemClickListener {

        public void onClickEvent1(View v) {
            skip(SettingNewMessageActivity.class);
        }

        public void onClickEvent2(View v) {
            skip(AboutProductActivity.class);
        }

        public void onClickEvent3(View v) {
            AccountManager.getInstance().logOut();
            finish();
        }
    }
}
