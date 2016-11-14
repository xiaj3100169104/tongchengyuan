package com.juns.wechat.net.callback;

import com.juns.wechat.bean.UserBean;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.response.BaseResponse;

/**
 * Created by 王宗文 on 2016/7/11.
 */
public abstract class LoginCallBack extends BaseCallBack<BaseResponse.LoginResponse>{
    @Override
    protected void handleResponse(BaseResponse.LoginResponse result) {
        if(result.code == BaseResponse.SUCCESS){
            saveUserInfo(result.userBean, result.token);
        }
    }

    private void saveUserInfo(UserBean userBean, String token){
        AccountManager.getInstance().setUser(userBean);
        AccountManager.getInstance().setToken(token);
    }


}
