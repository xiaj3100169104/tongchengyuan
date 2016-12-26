package com.juns.wechat.net.common;

import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.juns.wechat.R;
import com.juns.wechat.util.ToastUtil;


public class NetBeanCallback<T> extends NetStringCallback {
    protected String TAG = "NetBeanCallback";
    protected Class<T> clazz;
    protected TypeReference<T> type;

    public NetBeanCallback() {
    }

    public NetBeanCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    public NetBeanCallback(TypeReference<T> type) {
        this.type = type;
    }

    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);
        T data = null;
        if (this.clazz != null)
            data = JSON.parseObject(result, this.clazz);
        if (this.type != null)
            data = JSON.parseObject(result, this.type);

        onResultSuccess(data);
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        ToastUtil.showToast(R.string.toast_network_error, Toast.LENGTH_SHORT);
        onFailure("网络错误，请检查网络设置");
    }

    protected void onResultSuccess(T data) {

    }

    protected void onFailure(String msg) {

    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }
}
