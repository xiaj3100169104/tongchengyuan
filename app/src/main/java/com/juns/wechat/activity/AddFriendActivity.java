package com.juns.wechat.activity;


import android.os.Bundle;
import android.view.View;

import com.juns.wechat.R;
import com.juns.wechat.annotation.Content;
import com.juns.wechat.common.BaseActivity;
import com.juns.wechat.common.CommonUtil;
import com.juns.wechat.common.ToolbarActivity;

/**
 * Created by 王宗文 on 2016/6/20.
 */
@Content(R.layout.activity_add_friend)
public class AddFriendActivity extends ToolbarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListener();
    }

    private void setListener(){
        findViewById(R.id.rl_search_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.startActivity(AddFriendActivity.this, SearchActivity.class);
            }
        });
    }
}
