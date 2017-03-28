package com.style.net.file;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiajun on 2016/12/22.
 */
public class MultiThreadDownloadManager {
    protected String TAG = getClass().getSimpleName();

    private Map<Object, MultiThreadDownloadTask> taskMap = new HashMap<>();
    private static MultiThreadDownloadManager instance;

    public synchronized static MultiThreadDownloadManager getInstance() {
        if (instance == null) {
            instance = new MultiThreadDownloadManager();
        }
        return instance;
    }

    public void down(Object tag, String url, String targetPath, FileCallback callback) {
        MultiThreadDownloadTask task = taskMap.get(tag);
        if (task == null) {
            task = new MultiThreadDownloadTask(tag, url, 5, targetPath, callback);
            task.start();
            taskMap.put(tag, task);
        }
        task.setCallback(callback);
        task.setCanCallback(true);
    }

    public void cancelCallback(Object tag) {
        MultiThreadDownloadTask task = taskMap.get(tag);
        if (task != null)
            task.setCanCallback(false);
    }
    public void removeTask(Object tag) {
        taskMap.remove(tag);
    }
}

