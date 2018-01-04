package cn.tongchengyuan.manager;

import android.content.Context;
import android.text.TextUtils;

import cn.tongchengyuan.app.App;
import cn.tongchengyuan.bean.UserBean;
import cn.tongchengyuan.chat.xmpp.XmppManagerImpl;
import cn.tongchengyuan.chat.xmpp.event.XmppEvent;
import cn.tongchengyuan.greendao.mydao.GreenDaoManager;
import cn.tongchengyuan.util.SharedPreferencesUtil;

import org.simple.eventbus.EventBus;

/**
 * Created by 王宗文 on 2016/6/8.
 */
public class AccountManager {
    private UserBean user;
    private Context context;

    private static final String CURRENT_LOGIN_USER_NAME = "current_login_username";
    private static final String CURRENT_LOGIN_USER_ID = "current_login_user_id";
    private static final String CURRENT_LOGIN_PWD = "PWD";
    private static final String LOGIN_STATE = "LoginState";

    private static AccountManager instance;

    public synchronized static AccountManager getInstance() {
        if (instance == null) {
            instance = new AccountManager();
        }
        return instance;
    }

    public void init(Context mContext) {
        this.context = mContext;
    }

    public void setUser(UserBean userBean) {
        setLogin(true);
        setUserId(userBean.getUserId());
        setUserName(userBean.getUserName());
    }

    public UserBean getUser() {
        if (user == null) {
            String token = getToken();
            if (!TextUtils.isEmpty(token)) {
                UserBean u = GreenDaoManager.getInstance().findByUserId(getUserId());
                if (u != null) {
                    setUser(u);
                    user = u;
                }
            }
        }
        return user;
    }

    public void logOut() {
        setLogin(false);
        setToken(null);
        /*setUserName(null);
        setUserPassWord(null);*/
        user = null;

        XmppManagerImpl.getInstance().shutDownConn();

        EventBus.getDefault().post(new XmppEvent(XmppEvent.LOGOUT), "tagLogout");

    }

    public boolean isLogin() {
        return SharedPreferencesUtil.getBooleanValue(App.getInstance(), LOGIN_STATE);
    }

    public void setLogin(boolean login) {
        SharedPreferencesUtil.putBooleanValue(context, LOGIN_STATE, login);
    }

    public void setUserId(String userId) {
        SharedPreferencesUtil.putValue(context, CURRENT_LOGIN_USER_ID, userId);
    }

    public String getUserId() {
        return SharedPreferencesUtil.getValue(context, CURRENT_LOGIN_USER_ID);
    }

    public void setUserName(String userName) {
        SharedPreferencesUtil.putValue(context, CURRENT_LOGIN_USER_NAME, userName);
    }

    public String getUserName() {
        return SharedPreferencesUtil.getValue(context, CURRENT_LOGIN_USER_NAME);
    }

    public void setUserPassWord(String passWord) {
        SharedPreferencesUtil.putValue(context, CURRENT_LOGIN_PWD, passWord);
    }

    public String getUserPassWord() {
        return SharedPreferencesUtil.getValue(context, CURRENT_LOGIN_PWD);
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
}
