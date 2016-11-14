package com.juns.wechat.net.request;

import android.text.TextUtils;

import com.juns.wechat.config.ConfigUtil;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.callback.RefreshTokenCallBack;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.x;

/**
 * Created by 王宗文 on 2016/7/12.
 */
public class TokenRequest {
    private static final long REFRESH_TIME = 6 * 60 * 60 * 1000;

    @HttpRequest(host = ConfigUtil.REAL_API_URL, path = "newToken")
    public static class TokenParams extends RequestParams{
        private String token;

        public TokenParams(String token){
            this.token = token;
        }
    }

    public static void refreshToken(RefreshTokenCallBack callBack){
        AccountManager userManager = AccountManager.getInstance();

        if(!TextUtils.isEmpty(userManager.getToken())){
            if(userManager.getTokenRefreshTime() + REFRESH_TIME < System.currentTimeMillis()){
                return;
            }else {
                x.http().post(new TokenParams(userManager.getToken()), callBack);
            }
        }

    }
}
