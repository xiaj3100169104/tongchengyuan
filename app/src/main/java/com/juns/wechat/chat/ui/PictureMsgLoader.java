package com.juns.wechat.chat.ui;

import com.juns.wechat.chat.bean.MessageBean;
import com.juns.wechat.chat.xmpp.util.FileTransferManager;
import com.juns.wechat.util.ThreadPoolUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiajun on 2016/12/22.
 */
public class PictureMsgLoader {
    protected String TAG = getClass().getSimpleName();

    private Map<String, PictureDownloadTask> taskMap = new HashMap<>();
    private static PictureMsgLoader instance;

    public synchronized static PictureMsgLoader getInstance() {
        if (instance == null) {
            instance = new PictureMsgLoader();
        }
        return instance;
    }

    //注意tag唯一
    public void down(String url, int fileSize,  MessageBean messageBean) {
        PictureDownloadTask task = taskMap.get(url);
        if (task == null) {
            task = new PictureDownloadTask(url, fileSize, messageBean);
            ThreadPoolUtil.execute(task);
            taskMap.put(url, task);
        }
    }

    public void removeTask(String tag) {
        taskMap.remove(tag);
    }
}

