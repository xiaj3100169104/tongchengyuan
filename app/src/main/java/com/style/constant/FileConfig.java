package com.style.constant;

import android.os.Environment;

import org.jivesoftware.smack.packet.id.StanzaIdUtil;

/**
 * Created by xiajun on 2016/11/25.
 */

public class FileConfig {

    /**
     * 生成唯一文件名且防止被其他软件扫描到该文件
     * @return
     */
    public static String getUniqueFileName(){
        return StanzaIdUtil.newStanzaId() + ".image";
    }
    /**
     * app文件存储根目录
     */
    public static final String DIR_APP = Environment.getExternalStorageDirectory() + "/aaaaWechat";
    /**
     * 图片存储目录
     */
    public static final String DIR_IMAGE = DIR_APP + "/image";
    /**
     * 视频存储目录
     */
    public static final String DIR_VIDEO = DIR_APP + "/video";
    /**
     * 文件缓存目录
     */
    public static final String DIR_CACHE = DIR_APP + "/cache";
    /**
     * 数据备份目录
     */
    public static final String DIR_BACKUP = DIR_APP + "/backup";
}
