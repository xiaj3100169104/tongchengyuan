package com.style.net.core;

import android.util.Log;
import android.widget.Toast;


import cn.tongchengyuan.util.ToastUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NetStringCallback implements Callback<String> {
    protected String TAG = "NetStringCallback";

    public void onResponse(Call<String> call, Response<String> response) {
        Log.e(TAG, "response.body==" + response.body());
    }

        @Override
        public void onFailure(Call<String> call, Throwable t) {
        ToastUtil.showToast("请求 错误", Toast.LENGTH_SHORT);
    }


}
