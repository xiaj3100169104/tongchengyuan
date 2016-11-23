package com.juns.wechat.manager;

import android.content.Context;

import com.juns.wechat.App;
import com.juns.wechat.Constants;
import com.juns.wechat.MainActivity;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.dao.UserDao;
import com.juns.wechat.util.SharedPreferencesUtil;
import com.juns.wechat.xmpp.XmppManagerImpl;

import org.xutils.db.sqlite.WhereBuilder;

/**
 * Created by 王宗文 on 2016/6/8.
 */
public class AccountManager {
    private static AccountManager instance;
    private UserBean user;
    private Context context = App.getInstance();
    private UserDao userDao = UserDao.getInstance();

    private static final String CURRENT_LOGIN_USER = "current_login_user";

    public synchronized static AccountManager getInstance(){
        if(instance == null){
            instance = new AccountManager();
        }
        return instance;
    }

    private AccountManager(){
        initUser();
    }

    private void initUser(){
        String userName = getUserName();
        WhereBuilder whereBuilder = WhereBuilder.b(UserBean.USERNAME, "=", userName);
        user = userDao.findByParams(whereBuilder);
    }

    public void setUser(UserBean userBean){
        if(userDao.replace(userBean)){
            setLogin(true);
            setUserName(userBean.getUserName());
            user = userBean;
        }
    }

    public UserBean getUser(){
        return user;
    }

    public void logOut(){
        setLogin(false);
        setToken(null);
        setUserName(null);
        setUserPassWord(null);
        user = null;

        XmppManagerImpl.getInstance().shutDownConn();

        MainActivity.logout();
    }

    public boolean isLogin(){
        return SharedPreferencesUtil.getBooleanValue(App.getInstance(), Constants.LoginState);
    }

    public void setLogin(boolean login){
        SharedPreferencesUtil.putBooleanValue(context, Constants.LoginState, login);
    }

    public void setUserName(String userName){
        SharedPreferencesUtil.putValue(context, CURRENT_LOGIN_USER, userName);
    }

    public String getUserName(){
        return SharedPreferencesUtil.getValue(context, CURRENT_LOGIN_USER);
    }

    public void setUserPassWord(String passWord){
        SharedPreferencesUtil.putValue(context, Constants.PWD, passWord);
    }

    public String getUserPassWord(){
        return SharedPreferencesUtil.getValue(context, Constants.PWD);
    }

    public void setToken(String token){
        SharedPreferencesUtil.putValue(context, "token", token);
        setTokenRefreshTime(System.currentTimeMillis());
    }

    public String getToken(){
        return SharedPreferencesUtil.getValue(context, "token");
    }

    private void setTokenRefreshTime(long time){
        SharedPreferencesUtil.putLongValue(context, "token_refresh_time", time);
    }

    public long getTokenRefreshTime(){
        return SharedPreferencesUtil.getLongValue(context, "token_refresh_time", 0);
    }

    public void setHeadUrl(String headUrl){
        user.setHeadUrl(headUrl);
        userDao.replace(user);
    }

    public String getHeadUrl(){
        return user.getHeadUrl();
    }

}
