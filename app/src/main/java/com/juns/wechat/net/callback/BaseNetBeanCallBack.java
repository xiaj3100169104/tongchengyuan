package com.juns.wechat.net.callback;

import android.widget.Toast;

import com.juns.wechat.R;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.response.BaseResponse;
import com.juns.wechat.util.ToastUtil;
import com.style.rxAndroid.newwork.core.NetJsonResult;

import org.xutils.common.Callback;


public abstract class BaseNetBeanCallBack<T> implements Callback.CommonCallback<T>{

    @Override
    public void onSuccess(Object result) {
        BaseResponse response = (BaseResponse) result;
        if(response.code == BaseResponse.SUCCESS){
            handleResponse( response.data);
        }else if(response.code == BaseResponse.SERVER_ERROR){
            ToastUtil.showToast("服务器出错了", Toast.LENGTH_SHORT);
            onFailure(response.code, response.msg);
        }else if(response.code == BaseResponse.TOKEN_EXPIRED || response.code == BaseResponse.TOKEN_INVALID){
            handleTokenError();
        }else {
            onFailure(response.code, response.msg);
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        ToastUtil.showToast(R.string.toast_network_error, Toast.LENGTH_SHORT);

    }

    protected void handleResponse(Object result){

    }

    protected void onFailure(int code, String msg) {

    }

    private void handleTokenError(){
        AccountManager.getInstance().logOut();
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }
}
