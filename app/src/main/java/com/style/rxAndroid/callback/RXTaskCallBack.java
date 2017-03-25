package com.style.rxAndroid.callback;

import android.util.Log;

/**
 * Created by xiajun on 2016/10/8.
 */
public  class RXTaskCallBack<T> extends BaseRXTaskCallBack {
    protected String TAG = "RXTaskCallBack";

    public T doInBackground(){
        Log.e(TAG, "doInBackground");
        return null;
    }

    @Override
    public void onNextOnUIThread(Object o) {
        Log.e(TAG, "onNextOnUIThread");
        T data = (T) o;
        onSuccess(data);
    }

    public void onSuccess(T object) {

    }

    public void onFailed(String message) {

    }
}
