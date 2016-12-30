package com.juns.wechat.net.common;

import android.util.Log;
import android.widget.Toast;

import com.juns.wechat.R;
import com.juns.wechat.util.ToastUtil;

import org.xutils.common.Callback;


public class NetStringCallback implements Callback.CommonCallback<String> {
    protected String TAG = "NetStringCallback";

    @Override
    public void onSuccess(String result) {
        Log.e(TAG, "result==" + result);
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        ToastUtil.showToast(R.string.toast_network_error, Toast.LENGTH_SHORT);
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }
}
