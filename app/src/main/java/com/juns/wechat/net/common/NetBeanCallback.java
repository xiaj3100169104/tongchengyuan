package com.juns.wechat.net.common;

import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.juns.wechat.R;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.response.BaseResponse;
import com.juns.wechat.util.ToastUtil;

import org.xutils.common.Callback;


public class NetBeanCallback<T> implements Callback.CommonCallback<String> {
    protected String TAG = "NetNormalCallBack";
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
        Log.e(TAG, "result==" + result);
        NormaNetBean response = JSON.parseObject(result, NormaNetBean.class);
        int code = response.code;
        String jsonData = response.data;
        String msg = response.msg;

        if (code == BaseResponse.SUCCESS) {
            T data = null;
            if (this.clazz != null)
                data = JSON.parseObject(jsonData, this.clazz);
            if (this.type != null)
                data = JSON.parseObject(jsonData, this.type);

            onResultSuccess(data);
            onResultSuccess(data, msg);
        } else {
            onFailure(msg);
            onFailure(code, msg);
            if (code == BaseResponse.SERVER_ERROR) {
                ToastUtil.showToast("服务器出错了", Toast.LENGTH_SHORT);
            } else if (code == BaseResponse.TOKEN_EXPIRED || code == BaseResponse.TOKEN_INVALID) {
                handleTokenError();
            }
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        ToastUtil.showToast(R.string.toast_network_error, Toast.LENGTH_SHORT);
        onFailure("网络错误，请检查网络设置");
    }

    protected void onResultSuccess(T data) {

    }

    protected void onResultSuccess(T data, String msg) {

    }

    protected void onFailure(String msg) {

    }

    protected void onFailure(int code, String msg) {

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
