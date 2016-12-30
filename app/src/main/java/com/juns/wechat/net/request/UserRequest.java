package com.juns.wechat.net.request;

import com.juns.wechat.config.ConfigUtil;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.callback.BaseCallBack;
import com.juns.wechat.net.callback.LoginCallBack;
import com.juns.wechat.net.callback.QueryUserCallBack;
import com.juns.wechat.net.callback.UpdateUserCallBack;
import com.juns.wechat.net.response.BaseResponse;
import com.juns.wechat.net.response.SearchUserResponse;
import com.juns.wechat.net.response.UserListResponse;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.x;

/**
 * Created by 王宗文 on 2016/7/7.
 */

public class UserRequest extends RequestParams {
    private static final String URL = ConfigUtil.REAL_API_URL;

    public static void register(String userName, String passWord, BaseCallBack<BaseResponse.RegisterResponse> callBack){
        x.http().post(new RegisterParams(userName, passWord), callBack);
    }


    @HttpRequest(host = URL, path = "addUser")
    public static class RegisterParams extends RequestParams{
        private String userName;
        private String passWord;


        public RegisterParams(String userName, String passWord){
            this.userName = userName;
            this.passWord = passWord;
        }
    }

    @HttpRequest(host = URL, path = "login")
    public static class LoginParams extends RequestParams{
        private String userName;
        private String passWord;

        public LoginParams(String userName, String passWord){
            this.userName = userName;
            this.passWord = passWord;
        }
    }

    public static void login(String userName, String passWord, LoginCallBack callBack){
        x.http().post(new LoginParams(userName, passWord), callBack);
    }

    @HttpRequest(host = ConfigUtil.REAL_API_URL, path = "updateUser")
    public static class UpdateUserParams extends RequestParams{
        private String field;
        private Object value;
        private String token;

        public UpdateUserParams(String field, Object value){
            this.field = field;
            this.value = value;
            token = AccountManager.getInstance().getToken();
        }
    }

    public static void updateUser(String userName, String field, Object value, UpdateUserCallBack callBack){
        x.http().post(new UpdateUserParams(field, value), callBack);
    }

    @HttpRequest(host = ConfigUtil.REAL_API_URL, path = "searchUser")
    public static class SearchUserParams extends RequestParams{
        private String search;
        private String token;

        public SearchUserParams(String search){
            this.search = search;
            token = AccountManager.getInstance().getToken();
        }
    }

    public static void searchUser(String search, BaseCallBack<SearchUserResponse> callBack){
        x.http().post(new SearchUserParams(search), callBack);
    }

    @HttpRequest(host = ConfigUtil.REAL_API_URL, path = "syncUserData")
    public static class SyncUserParams extends RequestParams{
        private long modifyDate;
        private String token;

        public SyncUserParams(Integer[] userNames, long modifyDate){
            this.modifyDate = modifyDate;
            if(userNames != null && userNames.length != 0){
                for(Integer userName : userNames){
                    addBodyParameter("userNames[]", String.valueOf(userName));
                }
            }
            token = AccountManager.getInstance().getToken();
        }
    }



    @HttpRequest(host = ConfigUtil.REAL_API_URL, path = "getUsersByNames")
    public static class GetUsersParams extends RequestParams{
        private String token;

        public GetUsersParams(String[] userNames){
            for(String userName : userNames){
                addBodyParameter("userNames[]", userName);
            }
            token = AccountManager.getInstance().getToken();
        }
    }

    public static void getUsersByNames(String[] userNames, BaseCallBack<UserListResponse> callBack){
        x.http().post(new GetUsersParams(userNames), callBack);
    }



}
