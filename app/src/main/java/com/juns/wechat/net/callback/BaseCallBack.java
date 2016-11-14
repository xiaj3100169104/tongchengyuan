package com.juns.wechat.net.callback;

import android.widget.Toast;

import com.juns.wechat.R;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.response.BaseResponse;
import com.juns.wechat.util.ToastUtil;

import org.xutils.common.Callback;

/**
 * Created by 王宗文 on 2016/7/7.
 */
public abstract class BaseCallBack<T> implements Callback.CommonCallback<T>{

    @Override
    public final void onSuccess(T result) {
        BaseResponse response = (BaseResponse) result;
        if(response.code == 0){
            handleResponse(result);
        }else if(response.code == BaseResponse.SERVER_ERROR){
            ToastUtil.showToast("服务器出错了", Toast.LENGTH_SHORT);
            handleResponse(result);
        }else if(response.code == BaseResponse.TOKEN_EXPIRED || response.code == BaseResponse.TOKEN_INVALID){
            handleTokenError();
        }else {
            handleResponse(result);
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        ToastUtil.showToast(R.string.toast_network_error, Toast.LENGTH_SHORT);
    }

    protected abstract void handleResponse(T result);


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
