package com.juns.wechat.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.juns.wechat.R;
import com.juns.wechat.annotation.Content;
import com.juns.wechat.common.ToolbarActivity;

/**
 * Created by 王宗文 on 2016/6/20.
 */
public class AddFriendActivity extends ToolbarActivity {

    @Override
    public void initData() {
        setListener();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_add_friend;
        super.onCreate(savedInstanceState);

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
