package com.juns.wechat.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.juns.wechat.R;
import com.juns.wechat.common.ToolbarActivity;

public class NewFriendsActivity extends ToolbarActivity {

    @Override
    public void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_new_friends;

        super.onCreate(savedInstanceState);
    }
}
