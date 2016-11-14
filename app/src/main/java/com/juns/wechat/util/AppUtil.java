package com.juns.wechat.util;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;

/*******************************************************
 *
 * @since 1.5
 * Created by 王宗文 on 2015/8/21
 *******************************************************/
public class AppUtil {
    /**
     * 获取activity的lable标签
     * @return
     */
    public static String getActivityLabel(Activity activity){
        PackageManager pm = activity.getPackageManager();
        try {
            ActivityInfo activityInfo = pm.getActivityInfo(activity.getComponentName(), PackageManager.PERMISSION_GRANTED);
            return activity.getResources().getString(activityInfo.labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }catch (Resources.NotFoundException e){
            e.printStackTrace();
        }
        return "";
    }

}
