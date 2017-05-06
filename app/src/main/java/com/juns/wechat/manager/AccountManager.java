package com.juns.wechat.manager;

import android.content.Context;
import android.text.TextUtils;

import com.juns.wechat.App;
import com.juns.wechat.Constants;
import com.juns.wechat.activity.MainActivity;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.database.dao.UserDao;
import com.juns.wechat.util.SharedPreferencesUtil;
import com.juns.wechat.chat.xmpp.XmppManagerImpl;

import org.xutils.db.sqlite.WhereBuilder;

/**
 * Created by 王宗文 on 2016/6/8.
 */
public class AccountManager {
    private static AccountManager instance;
    private UserBean user;
    private Context context;
    private UserDao userDao;

    private static final String CURRENT_LOGIN_USER_NAME = "current_login_username";
    private static final String CURRENT_LOGIN_USER_ID = "current_login_user_id";

    public synchronized static AccountManager getInstance() {
        if (instance == null) {
            instance = new AccountManager();
        }
        return instance;
    }

    public void init(Context mContext) {
        this.context = mContext;
        initUser();
    }

    private void initUser() {
        userDao = UserDao.getInstance();
        String userName = getUserName();
        WhereBuilder whereBuilder = WhereBuilder.b(UserBean.USERNAME, "=", userName);
        user = userDao.findByParams(whereBuilder);
    }

    public void setUser(UserBean userBean) {
        if (userDao.replace(userBean)) {
            setLogin(true);
            setUserId(userBean.getUserId());
            setUserName(userBean.getUserName());
            user = userBean;
        }
    }

    public UserBean getUser() {
        if (user == null) {
            String token = getToken();
            if (!TextUtils.isEmpty(token)) {
                initUser();
                if (user != null) {
                    String userName = user.getUserName();
                    String password = user.getPassWord();
                    if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
                        setUserId(user.getUserId());
                        setUserName(userName);
                    }
                }
            }
        }
        return user;
    }

    public void logOut() {
        setLogin(false);
        setToken(null);
        setUserName(null);
        setUserPassWord(null);
        user = null;

        XmppManagerImpl.getInstance().shutDownConn();

        MainActivity.logout();
    }

    public boolean isLogin() {
        return SharedPreferencesUtil.getBooleanValue(App.getInstance(), Constants.LoginState);
    }

    public void setLogin(boolean login) {
        SharedPreferencesUtil.putBooleanValue(context, Constants.LoginState, login);
    }

    public void setUserId(int userId) {
        SharedPreferencesUtil.putIntValue(context, CURRENT_LOGIN_USER_ID, userId);
    }

    public int getUserId() {
        return SharedPreferencesUtil.getIntValue(context, CURRENT_LOGIN_USER_ID);
    }

    public void setUserName(String userName) {
        SharedPreferencesUtil.putValue(context, CURRENT_LOGIN_USER_NAME, userName);
    }

    public String getUserName() {
        return SharedPreferencesUtil.getValue(context, CURRENT_LOGIN_USER_NAME);
    }

    public void setUserPassWord(String passWord) {
        SharedPreferencesUtil.putValue(context, Constants.PWD, passWord);
    }

    public String getUserPassWord() {
        return SharedPreferencesUtil.getValue(context, Constants.PWD);
    }

    public void setToken(String token) {
        SharedPreferencesUtil.putValue(context, "token", token);
        setTokenRefreshTime(System.currentTimeMillis());
    }

    public String getToken() {
        return SharedPreferencesUtil.getValue(context, "token");
    }

    private void setTokenRefreshTime(long time) {
        SharedPreferencesUtil.putLongValue(context, "token_refresh_time", time);
    }

    public long getTokenRefreshTime() {
        return SharedPreferencesUtil.getLongValue(context, "token_refresh_time", 0);
    }

    public void setHeadUrl(String headUrl) {
        user.setHeadUrl(headUrl);
        userDao.replace(user);
    }

    public String getHeadUrl() {
        return user.getHeadUrl();
    }

}
