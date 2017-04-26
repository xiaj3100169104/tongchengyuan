package com.juns.wechat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.juns.wechat.R;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.homePage.MainActivity;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.util.SyncDataUtil;
import com.style.base.BaseActivity;

public class SplashActivity extends BaseActivity {
    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_start;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        UserBean userBean = AccountManager.getInstance().getUser();
        if (userBean != null) {
            SyncDataUtil.getInstance().syncData(TAG, new SyncDataUtil.Callback() {
                @Override
                public void onSuccess() {
                    pageToHome();
                }

                @Override
                public void onFailure() {
                    pageToHome();
                }
            });
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
