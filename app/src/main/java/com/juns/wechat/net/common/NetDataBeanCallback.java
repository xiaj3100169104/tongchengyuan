package com.juns.wechat.net.common;

import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.juns.wechat.R;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.util.ToastUtil;

import org.xutils.common.Callback;


public class NetDataBeanCallback<T> implements Callback.CommonCallback<String> {
    protected String TAG = "NetDataBeanCallback";
    protected Class<T> clazz;
    protected TypeReference<T> type;

    public NetDataBeanCallback() {

    }

    public NetDataBeanCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    public NetDataBeanCallback(TypeReference<T> type) {
        this.type = type;
    }

    @Override
    public void onSuccess(String result) {
        Log.e(TAG, "result==" + result);
        NetDataBean response = JSON.parseObject(result, NetDataBean.class);
        int code = response.code;
        String jsonData = response.data;
        String msg = response.msg;

        T data = null;
        if (this.clazz != null)
            data = JSON.parseObject(jsonData, this.clazz);
        if (this.type != null)
            data = JSON.parseObject(jsonData, this.type);

        if (code == NetDataBean.SUCCESS) {
            onCodeSuccess();
            onCodeSuccess(data);
            onCodeSuccess(data, msg);
        } else {
            onCodeFailure(msg);
            onCodeFailure(code, msg);
            onCodeFailure(code, data);
            if (code == NetDataBean.SERVER_ERROR) {
                ToastUtil.showToast("服务器出错了", Toast.LENGTH_SHORT);
            } else if (code == NetDataBean.TOKEN_EXPIRED || code == NetDataBean.TOKEN_INVALID) {
                handleTokenError();
            }
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        ToastUtil.showToast(R.string.toast_network_error, Toast.LENGTH_SHORT);
        onCodeFailure("网络错误，请检查网络设置");
    }
    protected void onCodeSuccess() {

    }
    protected void onCodeSuccess(T data) {

    }

    protected void onCodeSuccess(T data, String msg) {

    }

    protected void onCodeFailure(String msg) {

    }

    protected void onCodeFailure(int code, String msg) {

    }
    protected void onCodeFailure(int code, T data) {

    }
    private void handleTokenError() {
        AccountManager.getInstance().logOut();
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }
}
