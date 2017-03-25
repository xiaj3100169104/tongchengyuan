package com.style.rxAndroid;


import com.style.rxAndroid.callback.BaseRXTaskCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;


/**
 * Created by xiajun on 2016/10/9.
 */

public class RXTaskManager {

    private Map<String, List<Disposable>> mTaskMap = new HashMap();
    private static RXTaskManager mInstance;

    public static RXTaskManager getInstance() {
        if (mInstance == null) {
            mInstance = new RXTaskManager();
        }
        return mInstance;
    }

    public Disposable runTask(String tag, BaseRXTaskCallBack callBack) {
        Disposable subscription = callBack.run();
        addTask(tag, subscription);
        return subscription;
    }

    public void addTask(String tag, Disposable subscription) {
        List<Disposable> mSubscriptions = mTaskMap.get(tag);
        if (mSubscriptions == null) {
            mSubscriptions = new ArrayList<>();
            mTaskMap.put(tag, mSubscriptions);
        }
        mSubscriptions.add(subscription);
    }

    public void removeTask(String tag) {
        List<Disposable> mSubscriptions = mTaskMap.get(tag);
        if (mSubscriptions != null) {
            for (Disposable s : mSubscriptions) {
                if (s != null && !s.isDisposed()) {
                    s.dispose();
                }
            }
        }
    }
}
