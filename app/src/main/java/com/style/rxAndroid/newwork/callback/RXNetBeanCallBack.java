package com.style.rxAndroid.newwork.callback;

import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.juns.wechat.App;
import com.juns.wechat.R;
import com.style.rxAndroid.newwork.core.NetJsonResult;
import com.style.utils.CommonUtil;

/**
 * Created by xiajun on 2016/10/8.
 */
public class RXNetBeanCallBack<T> extends BaseRXTaskCallBack {
    protected String TAG = "RXNetBeanCallBack";
    protected Class<T> clazz;

    public RXNetBeanCallBack(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Object doInBackground() {
        Log.e(TAG, "doInBackground");
        return null;
    }

    @Override
    public void onNextOnUIThread(Object data) {
        Log.e(TAG, "onNextOnUIThread");
        NetJsonResult result = (NetJsonResult) data;
        if (clazz != null) {
            if (result.getCode() == 0)
                OnSuccess(JSON.parseObject(result.getBody(), clazz));
            else
                OnFailed(result.getMsg());
        }
    }


    @Override
    public void OnSuccess(Object object) {

    }

    @Override
    public void OnFailed(String message) {
        if (!CommonUtil.isNetWorkConnected(App.getInstance())) {
            Toast.makeText(App.getInstance(), R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
        }
    }
}
