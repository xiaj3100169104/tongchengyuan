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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_invite_message);
        init();
    }

    private void init(){
        tvRightText.setText("发送");
    }
}
