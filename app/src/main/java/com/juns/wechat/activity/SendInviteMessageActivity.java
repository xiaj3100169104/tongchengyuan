package com.juns.wechat.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.annotation.Id;
import com.juns.wechat.common.ToolbarActivity;

public class SendInviteMessageActivity extends ToolbarActivity {
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
