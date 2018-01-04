package cn.tongchengyuan.chat.voice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.same.city.love.R;
import com.same.city.love.databinding.ActivityCallVoiceBinding;
import com.style.base.BaseActivity;
import com.style.constant.MyAction;
import com.style.constant.Skip;

/**
 * Created by xiajun on 2016/6/6.
 */
public class CallVoiceBaseActivity extends BaseActivity {
    protected CallReceiver broadCast;
    ActivityCallVoiceBinding bd;

    private String toUserName;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_call_voice);
        super.setContentView(bd.getRoot());
        initData();
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
