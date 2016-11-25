package com.style.rxAndroid.newwork.callback;

import android.util.Log;

/**
 * Created by xiajun on 2016/10/8.
 */
public  class RXOtherCallBack extends BaseRXTaskCallBack {
    protected String TAG = "RXOtherCallBack";

    public Object doInBackground(){
        Log.e(TAG, "doInBackground");
        return null;
    }

    @Override
    public void onNextOnUIThread(Object object) {
        OnSuccess(object);
    }


    @Override
    public void OnSuccess(Object object) {

    }

    @Override
    public void OnFailed(String message) {

    }
}
