package cn.tongchengyuan.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.same.city.love.R;

import cn.tongchengyuan.manager.AccountManager;
import cn.tongchengyuan.bean.UserBean;

import com.same.city.love.databinding.ActivityStartBinding;
import com.style.base.BaseActivity;

public class SplashActivity extends BaseActivity {
    ActivityStartBinding bd;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_start);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    protected void customWindowOptions(Window window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void initData() {
        UserBean userBean = AccountManager.getInstance().getUser();
        if (userBean != null) {
            //GreenDaoManager.getInstance().markAsSendSucceed(userBean.userId);
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
