package com.style.base;


import android.os.Environment;

public class Constant {
    /**
     * app文件根目录
     */
    public static final String DIR_APP = Environment.getExternalStorageDirectory() + "/style";
    /**
     * 图片保存目录
     */
    public static final String DIR_APP_IMAGE_CAMERA = DIR_APP + "/image";
    /**
     * 文件缓存目录
     */
    public static final String DIR_APP_IMAGE_CACHE = DIR_APP + "/cache";
    /**
     * 拍照回调
     */
    public static final int REQUESTCODE_TAKE_CAMERA = 0x000001;// 拍照
    public static final int REQUESTCODE_TAKE_LOCAL = 0x000002;// 本地图片
    public static final int REQUESTCODE_PHOTO_CROP = 0x000003;// 系统裁剪头像
    public static final int REQUESTCODE_EDIT_OSTN_NUMBER = 0x000004;//编辑pstn号码
    public static final int REQUESTCODE_SELECT_DISPLAY = 0x000005;

    /**
     * 广播action
     */
    public static final String ACTION_REFRESH_CONVERSATION = "action.refresh.conversation";

    public static final String ACTION_FILE_PREPARE_DOWNLOAD = "action.file.prepare.download";
    public static final String ACTION_FILE_GET_FAIL = "action.file.get.fail";
    public static final String ACTION_FILE_CREATE_FAIL = "action.file.create.fail";
    public static final String ACTION_FILE_DOWNING = "action.file.downing";
    public static final String ACTION_FILE_CANCEL_DOWNLOAD = "action.file.cancel.download";
    public static final String ACTION_FILE_DOWN_COMPLETE = "action.file.down.complete";
    public static final String ACTION_FILE_DOWN_FAIL = "action.file.down.fail";

}
