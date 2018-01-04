package cn.tongchengyuan.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * 
 * <一句话功能简述>SD卡工具类<功能详细描述>
 * 
 */
public class SDCardUtil
{
    /**
     * 判断SD卡是否可用并且剩余容量大于5M
     * 
     * @param context 上下文
     * @return
     */
	public static boolean checkSDCard(Context context)
    {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            return false;
        }
        else
        {
            File sdcard = Environment.getExternalStorageDirectory();
            /**
             * 我们可以通过StatFs访问文件系统的空间容量等信息
             */
            StatFs statFs = new StatFs(sdcard.getPath());
            
            /**
             * statFs.getBlockSize可以获取当前的文件系统中，一个block所占有的字节数
             */
            int blockSize = statFs.getBlockSize();
            /**
             * statFs.getAvaliableBlocks方法可以返回尚未使用的block的数量
             */
            int avaliableBlocks = statFs.getAvailableBlocks();
            /**
             * statFs.getBlockCount可以获取总的block数量
             */
            if ((Long.valueOf(avaliableBlocks) * Long.valueOf(blockSize)) < (5 * Long.valueOf((1024 * 1024))))
            {
                return false;
            }
            return true;
        }
    }
    
    /**
     * SD卡是否存在并且可用
     * @return true 存在， false 不存在
     */
    public static boolean exists()
    {
        boolean success = false;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            if (Environment.getExternalStorageDirectory().canWrite())
            {
                success = true;
            }
        }
        return success;
    }

    /**
     * 获取SDCard根目录
     * @return
     */
    public static String getSDCardRootPath(){
        if(exists()){    //SD卡是否存在并且可用
            return Environment.getExternalStorageDirectory().getPath();
        }
        return null;
    }
}
