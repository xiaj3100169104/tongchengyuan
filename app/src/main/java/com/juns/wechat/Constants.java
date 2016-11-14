package com.juns.wechat;

import com.juns.wechat.util.SDCardUtil;

public interface Constants {

	// 聊天
	public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
	String isFriend = "isFriend";
	String LoginState = "LoginState";
	String UserInfo = "UserInfo";
	String URL = "URL";
	String Title = "Title";
	String ID = "id";
	String NAME = "NAME";
	String PWD = "PWD";
	String User_ID = "User_ID";
	String GROUP_ID = "GROUP_ID";
	String TYPE = "COLUMN_TYPE";
	// JSON status

    String MOB_SDK_KEY = "1607163f39976";
    String MOB_SDK_SECRET = "23886b7a0a42425a2f89d8b73dbf3d17";
    /**
     * SD卡根路径,如果SD卡不存在，则为NULL
     * @see SDCardUtil#getSDCardRootPath()
     */
    String SD_ROOT_PATH = SDCardUtil.getSDCardRootPath();
    /**
     * 错误日志记录目录
     */
   String ERROR_LOG_PATH = SD_ROOT_PATH == null ? null : SD_ROOT_PATH  + "/wechat/log/";

}
