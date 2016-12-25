package com.juns.wechat.net.callback;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.response.BaseResponse;
import com.juns.wechat.net.response.BaseResponse2;

/**
 * Created by 夏军 on 2016/12/15.
 */
public class NetNormalCallBack<T> extends BaseCallBack2<BaseResponse2> {
    protected String TAG = "NetNormalCallBack";
    protected Class<T> clazz;
    protected TypeReference<T> type;

    public NetNormalCallBack() {
    }

    public NetNormalCallBack(Class<T> clazz) {
        this.clazz = clazz;
    }

    public NetNormalCallBack(TypeReference<T> type) {
        this.type = type;
    }

    @Override
    protected void handleResponse(BaseResponse2 result) {
        Object data = null;
        String jsonData = (String) result.data;
        Log.e(TAG, "jsonData==" + jsonData);
        if (this.clazz != null)
            data = JSON.parseObject(jsonData, this.clazz);
        if (this.type != null)
            data = JSON.parseObject(jsonData, this.type);
        onResultSuccess(data, result.msg);
    }

    protected void onResultSuccess(Object data, String msg) {

    }

    protected void onFailure(int code, String msg) {

    }

}
