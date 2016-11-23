package com.juns.wechat.common;

import android.app.NotificationManager;
import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.juns.wechat.annotation.AnnotationUtil;
import com.juns.wechat.dialog.FlippingLoadingDialog;
import com.juns.wechat.util.ToastUtil;

import org.xutils.x;


public abstract class BaseActivity extends AppCompatActivity {
    private FlippingLoadingDialog mLoadingDialog;
    protected boolean isVisibleToUser = false;

    private Context mContext;

    private static final int notifiId = 11;
    protected NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        AnnotationUtil.initAnnotation(this);
        mContext = this;
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

    protected void skip(Class<?> cls) {
        startActivity(new Intent(mContext, cls));
    }

    protected void showToast(int resId) {
        showToast(getString(resId));
    }

    protected void showToast(String prompt) {
        if (isVisibleToUser) {
            ToastUtil.showToast(prompt, Toast.LENGTH_LONG);
        }
    }

    public FlippingLoadingDialog getLoadingDialog(String msg) {
        if (mLoadingDialog == null)
            mLoadingDialog = new FlippingLoadingDialog(this, msg);
        else {
            mLoadingDialog.setText(msg);
        }
        return mLoadingDialog;
    }

}
