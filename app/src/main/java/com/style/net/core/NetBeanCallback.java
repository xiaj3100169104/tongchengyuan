package com.style.net.core;

import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.same.city.love.R;
import cn.tongchengyuan.util.ToastUtil;

import retrofit2.Call;
import retrofit2.Response;


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
    public void onResponse(Call<String> call, Response<String> response) {
        String body = response.body();
        Log.e(TAG, "response.body==" + body);
        T data = null;
        if (this.clazz != null)
            data = JSON.parseObject(body, this.clazz);
        if (this.type != null)
            data = JSON.parseObject(body, this.type);

        onResultSuccess(data);
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        ToastUtil.showToast(R.string.toast_network_error, Toast.LENGTH_SHORT);
        onFailure("网络错误，请检查网络设置");
    }

    protected void onResultSuccess(T data) {

    }

    protected void onFailure(String msg) {

    }
}
