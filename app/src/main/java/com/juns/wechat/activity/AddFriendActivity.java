package com.juns.wechat.activity;


import android.os.Bundle;
import android.view.View;

import com.same.city.love.R;
import com.style.base.BaseToolbarActivity;

/**
 * Created by 王宗文 on 2016/6/20.
 */
public class AddFriendActivity extends BaseToolbarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_add_friend;
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initData() {
        setToolbarTitle("添加朋友");
        setListener();
    }

    private void setListener(){
        findViewById(R.id.rl_search_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip(SearchActivity.class);
            }
        });
    }
}
