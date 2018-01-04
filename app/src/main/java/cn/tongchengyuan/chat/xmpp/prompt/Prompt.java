package cn.tongchengyuan.chat.xmpp.prompt;

import android.app.ActivityManager;
import android.content.Context;

import cn.tongchengyuan.app.App;

import java.util.List;

/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * <p/>
 * This file is part of YY Cube project.
 * <p/>
 * It can not be copied and/or distributed without the express
 * permission of Yunyun Network
 * Created by wangshuai on 2015/8/31
 *******************************************************/
public abstract class Prompt {

    public Context mContext;

    public Prompt(Context context){
        mContext = context;
    }

    /**
     * 查看当前是否正在运行
     * @return
     */
    public final boolean isOnForeGround(){
        ActivityManager activityManager =(ActivityManager) App.getInstance().getSystemService(
                Context.ACTIVITY_SERVICE);
        String packageName = App.getInstance().getPackageName();
        List<ActivityManager.RunningAppProcessInfo>appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    public final boolean isOnChatActivity(){
        ActivityManager activityManager = ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> taskInfos = activityManager.getRunningTasks(1);
        if (taskInfos.size() ==0){
            return false;
        }
        String className = taskInfos.get(0).topActivity.getClassName();
        if (className.contains("ChatActivity")){
            return true;
        }
        return false;
    }
}
