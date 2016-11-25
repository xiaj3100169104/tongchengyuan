package com.style.rxAndroid.newwork.callback;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.style.rxAndroid.newwork.core.NetJsonResult;

/**
 * Created by xiajun on 2016/10/8.
 */
public  class RXNetBeanCallBack<T> extends BaseRXTaskCallBack {
    protected String TAG = "RXNetBeanCallBack";
    protected Class<T> clazz;

    public RXNetBeanCallBack(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Object doInBackground(){
        Log.e(TAG, "doInBackground");
        return null;
    }

    @Override
    public void onNextOnUIThread(Object data) {
        Log.e(TAG, "onNextOnUIThread");
        NetJsonResult result = (NetJsonResult) data;
        if (clazz != null) {
            OnSuccess(JSON.parseObject(result.getBody(), clazz));
        }
    }


    @Override
    public void OnSuccess(Object object) {

    }

    @Override
    public void OnFailed(String message) {

    }
}
