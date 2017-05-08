package com.juns.wechat.chat.voice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.same.city.love.R;
import com.juns.wechat.util.SipClient;
import com.style.base.BaseActivity;
import com.style.constant.MyAction;
import com.style.constant.Skip;

import butterknife.Bind;

/**
 * Created by xiajun on 2016/6/6.
 */
public class CallVoiceBaseActivity extends BaseActivity {
    protected CallReceiver broadCast;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.bt_Hang_up)
    Button btHangUp;
    @Bind(R.id.bt_answer)
    Button btAnswer;
    @Bind(R.id.layout_before_connected)
    LinearLayout layoutBeforeConnected;
    @Bind(R.id.iv_mute)
    ImageView ivMute;
    @Bind(R.id.layout_mute)
    LinearLayout layoutMute;
    @Bind(R.id.iv_speaker)
    ImageView ivSpeaker;
    @Bind(R.id.layout_speaker)
    LinearLayout layoutSpeaker;
    @Bind(R.id.layout_shrink)
    LinearLayout layoutShrink;
    @Bind(R.id.layout_option)
    LinearLayout layoutOption;

    private String toUserName;

    @Override
    protected void onCreate(Bundle arg0) {
        mLayoutResID = R.layout.activity_call_voice;
        super.onCreate(arg0);
    }

    @Override
    public void initData() {
        toUserName = getIntent().getStringExtra(Skip.KEY_USER_NAME);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyAction.ACTION_CALL_CONNECTING);
        intentFilter.addAction(MyAction.ACTION_CALL_CONNECT_FAILED);
        intentFilter.addAction(MyAction.ACTION_CALL_CONNECTED);
        intentFilter.addAction(MyAction.ACTION_CALL_HANGUP);
        broadCast = new CallReceiver();
        registerReceiver(broadCast, intentFilter);

        //SipClient.getInstance().makeAudioCall(toUserName);
    }

    @Override
    public void onBackPressed() {
        backAfterConnected();
    }

    protected void backAfterConnected() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //SipClient.getInstance().endCall();
        unregisterReceiver(broadCast);
    }

    public class CallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case MyAction.ACTION_CALL_CONNECTED:

                    break;
            }
        }
    }
}
