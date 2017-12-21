package com.style.net.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by xiajun on 2016/12/22.
 */
public class HttpActionManager {
    protected String TAG = "HttpManager";

    private Map<String, List<Call>> mTaskMap = new HashMap();
    private static HttpActionManager mInstance;
    public static HttpActionManager getInstance() {
        if (mInstance == null) {
            mInstance = new HttpActionManager();
        }
        return mInstance;
    }

    public void addTask(String tag, Call call) {
        List<Call> callList = mTaskMap.get(tag);
        if (callList == null) {
            callList = new ArrayList<>();
            mTaskMap.put(tag, callList);
        }
        callList.add(call);
    }

    public void removeTask(String tag) {
        List<Call> callList = mTaskMap.get(tag);
        if (callList != null) {
            for (Call s : callList) {
                if (s != null && !s.isCanceled()) {
                    s.cancel();
                }
            }
        }
    }

}

