package com.style.rxAndroid;


import com.style.rxAndroid.callback.BaseRXTaskCallBack;

import java.util.HashMap;
import java.util.Map;

import rx.Subscription;

/**
 * Created by xiajun on 2016/10/9.
 */

public class RXAsynTaskManager {
    private Map<String, Subscription> mTaskMap = new HashMap();
    private static RXAsynTaskManager mInstance;

    public static RXAsynTaskManager getInstance() {
        if (mInstance == null) {
            mInstance = new RXAsynTaskManager();
        }
        return mInstance;
    }

    public Subscription runTask(BaseRXTaskCallBack callBack) {
        return callBack.run();
    }

    public void addTask(String tag, Subscription subscription) {
        mTaskMap.put(tag, subscription);
    }

    public void removeTask(String tag) {

    }
}
