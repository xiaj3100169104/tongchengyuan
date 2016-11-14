package com.juns.wechat.net.callback;

import com.juns.wechat.net.response.BaseResponse;
import com.juns.wechat.util.SyncDataUtil;

/**
 * Created by 王者 on 2016/8/6.
 */
public abstract class AddFriendCallBack extends BaseCallBack<BaseResponse> {
    @Override
    protected void handleResponse(BaseResponse result) {
        if(result.code == BaseResponse.SUCCESS){
            SyncDataUtil.syncData();
        }
    }

}
