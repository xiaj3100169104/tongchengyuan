package com.style.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import cn.tongchengyuan.util.ToastUtil;
import com.style.event.EventManager;
import com.style.manager.LogManager;
import com.style.manager.ToastManager;
import com.style.dialog.LoadingDialog;
import com.style.net.core.HttpActionManager;
import com.style.rxAndroid.RXTaskManager;
import com.style.utils.CommonUtil;


public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG = getClass().getSimpleName();
    private Context mContext;
    public LayoutInflater mInflater;
    protected View mContentView;
    protected boolean isVisibleToUser = false;
    private LoadingDialog progressDialog;

    protected abstract void initData();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mContext = this;
        mInflater = LayoutInflater.from(mContext);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  //竖屏
        customWindowOptions(getWindow());
        if (registerEventBus()) {
            EventManager.getDefault().register(this);
        }
    }

    public Context getContext() {
        return mContext;
    }

    //自定义窗口属性
    protected void customWindowOptions(Window window) {
        //定义4.4以下窗口属性
        customWindowKitkat(window);
        //定义5.0以上窗口属性
        customWindowLollipop(window);
    }

    //定义5.0以上窗口属性
    protected void customWindowLollipop(Window w) {
        w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //设置状态栏颜色
            //window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            //预留状态栏空间
            //mContentView.setFitsSystemWindows(true);
        }
    }

    //定义4.4窗口属性
    protected void customWindowKitkat(Window window) {
       /* if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //去掉状态栏
            //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            mContentView.setFitsSystemWindows(false);
        }*/
    }

    @Override
    public void setContentView(View mContentView) {

    }


    protected boolean registerEventBus() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisibleToUser = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        CommonUtil.hiddenSoftInput(this);
        isVisibleToUser = false;
    }

    @Override
    public void onBackPressed() {
        onBackFinish();
    }

    protected void onBackFinish() {
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (registerEventBus()) {
            EventManager.getDefault().unRegister(this);
        }
        dismissProgressDialog();
        RXTaskManager.getInstance().removeTask(TAG);
        HttpActionManager.getInstance().removeTask(TAG);
    }

    public void skip(Class<?> cls) {
        startActivity(new Intent(mContext, cls));
    }

    public void skipForResult(Class<?> cls, int requestCode) {
        startActivityForResult(new Intent(mContext, cls), requestCode);
    }

    public void showToast(int resId) {
        showToast(getString(resId));
    }

    public void showToast(String prompt) {
        if (isVisibleToUser) {
            ToastUtil.showToast(prompt, Toast.LENGTH_SHORT);
        }
    }

    public void showToastLong(String msg) {
        ToastManager.showToastLong(mContext, msg);
    }

    public void showToastLong(int msgId) {
        ToastManager.showToastLong(mContext, msgId);
    }

    public void showProgressDialog() {
        showProgressDialog("");
    }

    public void showProgressDialog(int msgId) {
        showProgressDialog(getString(msgId));
    }

    public void showProgressDialog(String msg) {
        if (progressDialog == null)
            progressDialog = new LoadingDialog(mContext);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void logE(String tag, String msg) {
        LogManager.logE(tag, msg);
    }

    protected CharSequence getNotNullText(CharSequence str) {
        return CommonUtil.getNotNullText(str);
    }

    protected int dip2px(float dpValue) {
        return CommonUtil.dip2px(mContext, dpValue);
    }
}
