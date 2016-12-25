package com.style.constant;

import android.os.Environment;

/**
 * Created by xiajun on 2016/11/25.
 */

public class FileDirectory {
    /**
     * app文件根目录
     */
    public static final String DIR_APP = Environment.getExternalStorageDirectory() + "/wechat";
    /**
     * 图片保存目录
     */
    public static final String DIR_IMAGE = DIR_APP + "/image";
    /**
     * 文件缓存目录
     */
    public static final String DIR_CACHE = DIR_APP + "/cache";
}
