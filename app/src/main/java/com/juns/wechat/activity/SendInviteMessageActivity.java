package com.juns.wechat.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.annotation.Id;
import com.style.base.BaseToolbarActivity;

public class SendInviteMessageActivity extends BaseToolbarActivity {
    @Id
    private TextView tvRightText;

    @Override
    public void initData() {
        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_send_invite_message);
        super.onCreate(savedInstanceState);

    }

    private void init(){
        tvRightText.setText("发送");
    }
}
