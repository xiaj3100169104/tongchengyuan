package com.style.net.image;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiajun on 2016/12/22.
 */
public class ImageManager {
    protected String TAG = getClass().getSimpleName();

    private Map<String, ImageDownloadTask> taskMap = new HashMap<>();
    private static ImageManager instance;

    public synchronized static ImageManager getInstance() {
        if (instance == null) {
            instance = new ImageManager();
        }
        return instance;
    }

    //注意tag唯一
    public void down(String url, String dir, String fileName, ImageCallback callback) {
        ImageDownloadTask task = taskMap.get(url);
        if (task == null) {
            task = new ImageDownloadTask(url, dir, fileName, callback);
            task.start();
            taskMap.put(url, task);
        }
        task.setCallback(callback);
        task.setCanCallback(true);
    }

    public void cancelCallback(String tag) {
        ImageDownloadTask task = taskMap.get(tag);
        if (task != null)
            task.setCanCallback(false);
    }
    public void removeTask(String tag) {
        taskMap.remove(tag);
    }
}

