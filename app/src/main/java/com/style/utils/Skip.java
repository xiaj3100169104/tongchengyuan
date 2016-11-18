package com.style.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import java.util.List;

public class Skip {

    public static final String ACCOUNT_KEY = "account";
    public static final String PASSWORD_KEY = "password";
    public static final String USER_KEY = "user";
    public static final String CURUSER_KEY = "curUser";
    public static final String OUSER_KEY = "oUser";
    public static final String CIRCLE_KEY = "circle";
    public static final String PHONE_KEY = "phone";
    public static final String EMODATA_KEY = "emoData";
    public static final String USERDYNAMIC_KEY = "UserDynamic";
    public static final String POSITION_KEY = "position";
    public static final String SQUAREINFO_KEY = "SquareInfo";
    public static final String ISDELETE_KEY = "isDelete";
    public static final String USERLIST_KEY = "userList";
    public static final String YY_KEY = "yy";

    public static void skipClearTop(Context context, Class<?> cls) {
        context.startActivity(new Intent().setClass(context, cls).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public static void skipClearTop(Context context, Class<?> cls, Intent intent) {
        intent.setClass(context, cls).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static void exitToDesk(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static void backToApp(Context context) {
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.setAction(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//        context.startActivity(intent);
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
}
