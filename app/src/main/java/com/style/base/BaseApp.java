package com.style.base;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

public class BaseApp extends Application {
    protected String TAG = getClass().getSimpleName();

    private static final String LOGIN_INFO = "loginInfo";
    private static final String IS_AUTO_LOGIN = "is_auto_login";
    private static final String CURRENT_ACCOUNT = "currentAccount";
    private static final String ACCOUNT_All = "accountAll";
    private static final String PASSWORD = "password";
    private static final String IS_FIRST_LOGIN = "isFirstLogin";
    private static final String SIGN_KEY = "signKey";
    private static final String DOWNLOAD_APP_KEY = "downloadAppKey";
    private static final String APP_INFO = "appInfo";

    protected static Context context;
    public static Typeface TEXT_TYPE;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getInstance() {
        return context;
    }

    protected static SharedPreferences getLoginInfo() {
        SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
        return sp;
    }

    public void putIsAutoLogin(boolean value) {
        SharedPreferences.Editor editor = getLoginInfo().edit();
        editor.putBoolean(IS_AUTO_LOGIN, value).apply();
    }

    public boolean getIsAutoLogin() {
        boolean value = getLoginInfo().getBoolean(IS_AUTO_LOGIN, true);
        return value;
    }

    protected static SharedPreferences getUserInfo(String account) {
        SharedPreferences sp = context.getSharedPreferences(account, Context.MODE_PRIVATE);
        return sp;
    }

    protected static SharedPreferences getApp(Context context) {
        SharedPreferences sp = context.getSharedPreferences(APP_INFO, Context.MODE_PRIVATE);
        return sp;
    }

    public void putSignKey(String account, String signKey) {
        SharedPreferences.Editor editor = getUserInfo(account).edit();
        editor.putString(SIGN_KEY, signKey).apply();
    }

    public String getSignKey(String account) {
        String value = getUserInfo(account).getString(SIGN_KEY, null);
        return value;

    }

    public void putCurrentAccount(String account) {
        SharedPreferences.Editor editor = getLoginInfo().edit();
        editor.putString(CURRENT_ACCOUNT, account).apply();// 异步真正提交到硬件磁盘,
        // 而commit是同步的提交到硬件磁盘
        putAccount(account);
    }

    public String getCurrentAccount() {
        String value = getLoginInfo().getString(CURRENT_ACCOUNT, null);
        return value;
    }

    public void putAccount(String account) {
        SharedPreferences.Editor editor = getLoginInfo().edit();
        Set<String> set = getAllAccounts();
        if (set == null)
            set = new HashSet<>();
        set.add(account);
        editor.putStringSet(ACCOUNT_All, set).apply();
    }

    public Set<String> getAllAccounts() {
        return getLoginInfo().getStringSet(ACCOUNT_All, null);
    }

    public void putPassword(String account, String password) {
        if (!TextUtils.isEmpty(account)) {
            SharedPreferences.Editor editor = getUserInfo(account).edit();
            editor.putString(PASSWORD, password).apply();
        }
    }

    public String getPassword(String account) {
        String value = getUserInfo(account).getString(PASSWORD, null);
        return value;
    }

    public static void putFirstLogin(boolean isFirstLogin) {
        SharedPreferences.Editor editor = getLoginInfo().edit();
        editor.putBoolean(IS_FIRST_LOGIN, isFirstLogin).apply();
    }

    public static boolean isFirstLogin() {
        boolean value = getLoginInfo().getBoolean(IS_FIRST_LOGIN, true);
        return value;
    }


    //在application中使用//不让其他应用接收到广播
    public static void sendLocalBroadcast(Intent intent) {
        LocalBroadcastManager.getInstance(getInstance()).sendBroadcastSync(intent);
    }

    public static void registerLocalReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        LocalBroadcastManager.getInstance(getInstance()).registerReceiver(receiver, filter);
    }

    public static void unregisterLocalReceiver(BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(getInstance()).unregisterReceiver(receiver);
    }

    private void loadCustomFront() {
        // 加载自定义字体
        try {
            TEXT_TYPE = Typeface.createFromAsset(getAssets(), "fronts/black_simplified.TTF");
        } catch (Exception e) {
            Log.i("MyApp", "加载第三方字体失败。");
            TEXT_TYPE = null;
        }
    }

    public static long getAppInfo() {
        long downId = getApp(context).getLong(DOWNLOAD_APP_KEY, -1);
        return downId;
    }

    public static void putAppInfo(long id) {
        SharedPreferences.Editor editor = getApp(context).edit();
        editor.putLong(DOWNLOAD_APP_KEY, id).commit();
    }
}
