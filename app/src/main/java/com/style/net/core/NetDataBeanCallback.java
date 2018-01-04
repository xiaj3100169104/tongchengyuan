package com.style.net.core;

import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import cn.tongchengyuan.manager.AccountManager;
import cn.tongchengyuan.util.ToastUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetDataBeanCallback<T> implements Callback<String> {
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
    public void onResponse(Call<String> call, Response<String> response) {
        Log.e(TAG, "response-->code: " + response.code() + "  body: " + response.body());
        if (response.code() == 200) {
            NetDataBean netDataBean = JSON.parseObject(response.body(), NetDataBean.class);
            if (netDataBean != null) {
                int code = netDataBean.code;
                String jsonData = netDataBean.data;
                String msg = netDataBean.msg;

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
            } else {
                onCodeFailure("数据解析失败");
            }
        } else {
            onCodeFailure("请求失败");
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        Log.e(TAG, t.toString());
        ToastUtil.showToast("请求错误", Toast.LENGTH_SHORT);
        onCodeFailure("请求失败");
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

}
