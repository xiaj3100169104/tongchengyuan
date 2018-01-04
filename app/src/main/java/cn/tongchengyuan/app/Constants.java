package cn.tongchengyuan.app;

import cn.tongchengyuan.util.SDCardUtil;

public interface Constants {

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
