package cn.tongchengyuan.util;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import com.style.constant.FileConfig;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/*******************************************************
 *
 * @since 1.5
 * Created by 王宗文 on 2015/8/21
 *******************************************************/
public class AppUtil {
    /**
     * 获取activity的lable标签
     *
     * @return
     */
    public static String getActivityLabel(Activity activity) {
        PackageManager pm = activity.getPackageManager();
        try {
            ActivityInfo activityInfo = pm.getActivityInfo(activity.getComponentName(), PackageManager.PERMISSION_GRANTED);
            return activity.getResources().getString(activityInfo.labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void backupData(String data, String filePath) {
        File userBack = new File(filePath);
        FileOutputStream fos = null;
        if (userBack.exists())
            userBack.delete();
        if (!userBack.getParentFile().exists())
            userBack.mkdirs();
        try {
            fos = new FileOutputStream(userBack);
            fos.write(data.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String backupDataResume(String filePath) {
        StringBuilder result = new StringBuilder();
        File userBack = new File(filePath);
        BufferedReader br = null;
        if (userBack.exists()) {
            try {
                br = new BufferedReader(new FileReader(userBack));//构造一个BufferedReader类来读取文件
                String s = null;
                int line = 1;
                while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                    if (line != 1)//第一行不加换行符
                        result.append(System.lineSeparator());
                    result.append(s);
                    line++;
                }
                br.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return result.toString();
    }
}
