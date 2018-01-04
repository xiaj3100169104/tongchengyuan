package cn.tongchengyuan.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;


import com.baidu.mapapi.SDKInitializer;

import cn.tongchengyuan.greendao.mydao.GreenDaoManager;
import cn.tongchengyuan.manager.AccountManager;
import cn.tongchengyuan.net.request.HttpActionImpl;
import cn.tongchengyuan.chat.utils.SmileUtils;

import com.style.manager.AppManager;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import cn.smssdk.SMSSDK;

public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        //短信发送
        SMSSDK.initSDK(this, Constants.MOB_SDK_KEY, Constants.MOB_SDK_SECRET);
        // 百度MAP sdk initinitializeialize
        SDKInitializer.initialize(this);
        //Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler());
        ZXingLibrary.initDisplayOpinion(this);
        AppManager.getInstance().init(mContext);
        GreenDaoManager.getInstance().initialize(mContext);
        AccountManager.getInstance().init(mContext);
        HttpActionImpl.getInstance().init();
        SmileUtils.getInstance().init(mContext);
        /*// 设置拍摄视频缓存路径
        VCamera.setVideoCachePath(FileConfig.DIR_CACHE);
		// 开启log输出,ffmpeg输出到logcat
		VCamera.setDebugMode(true);
		// 初始化拍摄SDK，必须
		VCamera.initialize(this);*/
       /* List<UserBean> list = GreenDaoManager.getInstance().queryAllUser();
        List<FriendBean> list2 = GreenDaoManager.getInstance().queryAllFriend();
        List<DynamicBean> list3 = GreenDaoManager.getInstance().queryAll();
        //String s = JSON.toJSONString(list);
        //String s2 = JSON.toJSONString(list2);
        String s3 = JSON.toJSONString(list3);
        //logE("userList", s);
        //logE("friendList", s2);
        logE("dynamicList", s3);
        String data = AppUtil.backupDataResume(FileConfig.DIR_BACKUP + "/" + "dynamicTableBackup.txt");
        List<DynamicBean> l1 = JSON.parseObject(data, new TypeReference<List<DynamicBean>>() {
        });
        GreenDaoManager.getInstance().saveDynamic(l1);
        List<FriendBean> l12 = GreenDaoManager.getInstance().queryAllFriend();

        for (FriendBean u : l12) {
        }*/
        /*AppUtil.backupData(s2, FileConfig.DIR_BACKUP + "/" + "friendTableBackup.txt");
        AppUtil.backupData(s3, FileConfig.DIR_BACKUP + "/" + "dynamicTableBackup.txt");
*/

    }


    /**
     * 自定义异常处理类
     */
    class CustomExceptionHandler implements Thread.UncaughtExceptionHandler {
        /*
         * @param thread the thread that has an uncaught exception
         * @param ex the exception that was thrown
         */
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            ex.printStackTrace();
            String errorLogPath = Constants.ERROR_LOG_PATH;
            if (errorLogPath != null) {
                File file = new File(errorLogPath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                try {
                    FileOutputStream fos = new FileOutputStream(errorLogPath + "errorlog.txt", true);
                    PrintStream ps = new PrintStream(fos);
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formatDate = sdf.format(date);
                    ps.append("-------------------crash time ：");
                    ps.append(formatDate);
                    ps.append("-------------------\n");
                    ex.printStackTrace(ps);
                    ps.append("\n");
                    ps.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //Process.killProcess(Process.myPid());//会导致log消失
        }
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        try {
            deleteCacheDirFile(getHJYCacheDir(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.gc();
    }

    public static Context getInstance() {
        return mContext;
    }

    public static String getHJYCacheDir() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))
            return Environment.getExternalStorageDirectory().toString()
                    + "/Health/Cache";
        else
            return "/System/com.juns.Walk/Walk/Cache";
    }

    public static void deleteCacheDirFile(String filePath,
                                          boolean deleteThisPath) throws IOException {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.isDirectory()) {// 处理目录
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteCacheDirFile(files[i].getAbsolutePath(), true);
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory()) {// 如果是文件，删除
                    file.delete();
                } else {// 目录
                    if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                        file.delete();
                    }
                }
            }
        }
    }
}
