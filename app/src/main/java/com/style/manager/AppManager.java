package com.style.manager;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.List;


public class AppManager {
    protected String TAG = getClass().getSimpleName();

    private static final String APP_INFO = "appInfo";
    private static final String IS_FIRST_LOGIN = "isFirstLogin";

    protected static Context context;
    public static Typeface TEXT_TYPE;
    private static AppManager mInstance;

    public static AppManager getInstance() {
        if (mInstance == null) {
            mInstance = new AppManager();
        }
        return mInstance;
    }

    public void init(Context context) {
        this.context = context;
    }

    public static Context getContext() {
        return context;
    }

    public SharedPreferences getAppSharedPreferences() {
        SharedPreferences sp = context.getSharedPreferences(APP_INFO, Context.MODE_PRIVATE);
        return sp;
    }

    public void putFirstOpen(boolean isFirstLogin) {
        SharedPreferences.Editor editor = getAppSharedPreferences().edit();
        editor.putBoolean(IS_FIRST_LOGIN, isFirstLogin).apply();
    }

    public boolean isFirstOpen() {
        boolean value = getAppSharedPreferences().getBoolean(IS_FIRST_LOGIN, true);
        return value;
    }

    /**
     * 退出到登录界面
     */
    public void exit2loginInterface() {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.channelsoft.cdesk", "com.channelsoft.cdesk.activity.LoginActivity");
        intent.setComponent(componentName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    /**
     * 退出app
     */
    public void exitApp() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("后台", appProcess.processName);
                    return true;
                } else {
                    Log.i("前台", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    //在application中使用//不让其他应用接收到广播
    public static void sendLocalBroadcast(Intent intent) {
        LocalBroadcastManager.getInstance(getContext()).sendBroadcastSync(intent);
    }

    public static void registerLocalReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, filter);
    }

    public static void unregisterLocalReceiver(BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    private void loadCustomFront() {
        // 加载自定义字体
        try {
            TEXT_TYPE = Typeface.createFromAsset(getContext().getAssets(), "fronts/black_simplified.TTF");
        } catch (Exception e) {
            Log.i("MyApp", "加载第三方字体失败。");
            TEXT_TYPE = null;
        }
    }
}
