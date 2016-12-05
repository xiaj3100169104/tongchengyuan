package com.style.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.juns.wechat.R;
import com.juns.wechat.dialog.FlippingLoadingDialog;
import com.juns.wechat.util.ToastUtil;
import com.style.manager.LogManager;
import com.style.manager.ToastManager;
import com.style.dialog.MaterialProgressDialog;
import com.style.rxAndroid.BaseRxActivity;
import com.style.utils.CommonUtil;

import butterknife.ButterKnife;


public abstract class BaseActivity extends BaseRxActivity {
    private Context mContext;
    public LayoutInflater mInflater;
    protected Integer mLayoutResID;
    protected View mContentView;
    private FlippingLoadingDialog mLoadingDialog;
    protected boolean isVisibleToUser = false;
    private MaterialProgressDialog progressDialog;
    private AlertDialog dlgPrompt;

    protected abstract void initData();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mContext = this;
        mInflater = LayoutInflater.from(mContext);
        if (mLayoutResID != null)
            mContentView = mInflater.inflate(mLayoutResID, null);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  //竖屏
        customWindowOptions(getWindow());

        setContentView(mContentView);
        //ButterKnife must set after setContentView
        ButterKnife.bind(this);
        initData();
    }

    //自定义窗口属性
    protected void customWindowOptions(Window window) {
        //定义4.4以下窗口属性
        customWindowKitkat(window);
        //定义5.0以上窗口属性
        customWindowLollipop(window);
    }

    //定义5.0以上窗口属性
    protected void customWindowLollipop(Window window) {
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //设置状态栏颜色
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            //预留状态栏空间
            mContentView.setFitsSystemWindows(true);
        }
    }

    //定义4.4窗口属性
    protected void customWindowKitkat(Window window) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //去掉状态栏
            //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            mContentView.setFitsSystemWindows(false);
        }
    }

    @Override
    public void setContentView(View mContentView) {
        customTitleOptions(mContentView);
        super.setContentView(mContentView);
    }

    protected void customTitleOptions(View mContentView) {
    }

    @Override
    public void onBackPressed() {
        onBackFinish();
    }

    protected void onBackFinish() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisibleToUser = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isVisibleToUser = false;
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        dismissProgressDialog();
    }

    protected void skip(Class<?> cls) {
        startActivity(new Intent(mContext, cls));
    }

    protected void showToast(int resId) {
        showToast(getString(resId));
    }

    protected void showToast(String prompt) {
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

    public FlippingLoadingDialog getLoadingDialog(String msg) {
        if (mLoadingDialog == null)
            mLoadingDialog = new FlippingLoadingDialog(this, msg);
        else {
            mLoadingDialog.setText(msg);
        }
        return mLoadingDialog;
    }

    public void showProgressDialog() {
        showProgressDialog("");
    }

    public void showProgressDialog(int msgId) {
        showProgressDialog(getString(msgId));
    }

    public void showProgressDialog(String msg) {
        if (progressDialog == null)
            progressDialog = new MaterialProgressDialog(mContext, R.style.Dialog_NoTitle);
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

    protected void showGiveUpEditDialog(final com.style.base.BaseActivity.OnGiveUpEditDialogListener listener) {
        if (dlgPrompt == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("放弃编辑");
            builder.setMessage("确定要放弃此次编辑吗？");
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onPositiveButton();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onNegativeButton();
                }
            });
            dlgPrompt = builder.create();
        }
        dlgPrompt.show();
    }

    public interface OnGiveUpEditDialogListener {
        void onPositiveButton();

        void onNegativeButton();
    }

    protected void setText(TextView textView, int strId) {
        setText(textView, mContext.getString(strId));
    }

    protected void setText(TextView textView, String str) {
        CommonUtil.setText(textView, getNotNullText(str));
    }

    protected static String getNotNullText(String str) {
        return CommonUtil.getNotNullText(str);
    }

    protected int dip2px(float dpValue) {
        return CommonUtil.dip2px(mContext, dpValue);
    }

    protected int px2dip(float pxValue) {
        return CommonUtil.px2dip(mContext, pxValue);
    }
}
