package com.juns.wechat.net.callback;

import com.juns.wechat.bean.UserBean;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.response.BaseResponse;
import com.juns.wechat.net.response.UpdateUserResponse;

/**
 * Created by 王宗文 on 2016/7/11.
 */
public abstract class UpdateUserCallBack extends BaseCallBack<UpdateUserResponse>{

    @Override
    protected void handleResponse(UpdateUserResponse result) {
        if(result.code == BaseResponse.SUCCESS){
            saveUserInfo(result.userBean);
        }
    }

    private void saveUserInfo(UserBean userBean){
        AccountManager.getInstance().setUser(userBean);
    }


}
