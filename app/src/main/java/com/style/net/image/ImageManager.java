package com.style.net.image;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiajun on 2016/12/22.
 */
public class ImageManager {
    protected String TAG = getClass().getSimpleName();

    private Map<Object, ImageDownloadTask> taskMap = new HashMap<>();
    private static ImageManager instance;

    public synchronized static ImageManager getInstance() {
        if (instance == null) {
            instance = new ImageManager();
        }
        return instance;
    }

    public void down(Object tag, String url, String dir, String fileName, ImageCallback callback) {
        ImageDownloadTask task = taskMap.get(tag);
        if (task == null) {
            task = new ImageDownloadTask(tag, url, dir, fileName, callback);
            task.start();
            taskMap.put(tag, task);
        }
        task.setCallback(callback);
        task.setCanCallback(true);
    }

    public void cancelCallback(Object tag) {
        ImageDownloadTask task = taskMap.get(tag);
        if (task != null)
            task.setCanCallback(false);
    }
    public void removeTask(Object tag) {
        taskMap.remove(tag);
    }
}

