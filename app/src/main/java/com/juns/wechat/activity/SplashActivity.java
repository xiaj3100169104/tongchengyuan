package com.juns.wechat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.juns.wechat.MainActivity;
import com.juns.wechat.R;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.manager.AccountManager;

public class SplashActivity extends Activity {
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        UserBean userBean = AccountManager.getInstance().getUser();
        if (userBean != null) {
            pageToHome();
        } else {
            pageToLogin();
        }
    }

    private void pageToHome() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 600);
    }

    private void pageToLogin() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 600);
    }

}
