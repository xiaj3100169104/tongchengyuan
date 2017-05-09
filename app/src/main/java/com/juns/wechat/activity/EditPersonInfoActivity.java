package com.juns.wechat.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.same.city.love.R;
import com.style.base.BaseToolbarActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by xiajun on 2017/5/9.
 */

public class EditPersonInfoActivity extends BaseToolbarActivity {


    @Bind(R.id.layout_base_info)
    LinearLayout layoutBaseInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_edit_userinfo;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        setToolbarTitle("编辑个人资料");
    }

    @OnClick(R.id.layout_base_info)
    public void modifyBaseInfo() {
        skip(EditBaseInfoActivity.class);
    }


}
