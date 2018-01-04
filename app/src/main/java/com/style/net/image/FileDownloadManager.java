package com.style.net.image;

import cn.tongchengyuan.util.ThreadPoolUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiajun on 2016/12/22.
 */
public class FileDownloadManager {
    protected String TAG = getClass().getSimpleName();

    private Map<String, FileDownloader> taskMap = new HashMap<>();
    private static FileDownloadManager instance;

    public synchronized static FileDownloadManager getInstance() {
        if (instance == null) {
            instance = new FileDownloadManager();
        }
        return instance;
    }

    //注意tag唯一
    public void down(String url, String targetPath, FileDownloadCallback callback) {
        FileDownloader task = taskMap.get(url);
        if (task == null) {
            task = new FileDownloader(url, targetPath, callback);
            ThreadPoolUtil.execute(task);
            taskMap.put(url, task);
        }
        task.setCallback(callback);
        task.setCanCallback(true);
    }

    public void cancelCallback(String tag) {
        FileDownloader task = taskMap.get(tag);
        if (task != null)
            task.setCanCallback(false);
    }
    public void removeTask(String tag) {
        taskMap.remove(tag);
    }
}

