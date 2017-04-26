package com.style.constant;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import java.util.List;
/**
 *  * 多处重用的跳转传值的key和请求码，统一放在这里
 * Created by xiajun on 2016/11/25.
 */
public class Skip {
    /**
     * 界面跳转通用请求码
     */
    public static final int CODE_TAKE_CAMERA = 0x000098;// 拍照
    public static final int CODE_TAKE_ALBUM = 0x0000099;// 从相册中选择

    /**
     * 界面跳转通用传值key
     */
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_USER_NAME = "userName";

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
