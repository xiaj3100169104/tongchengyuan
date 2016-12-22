package com.juns.wechat.net.common;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by xiajun on 2016/12/22.
 */
public class NetWorkManager {
    private static NetWorkManager instance;

    public synchronized static NetWorkManager getInstance() {
        if (instance == null) {
            instance = new NetWorkManager();
        }
        return instance;
    }

    public void post(RequestParams params, Callback.CommonCallback callback) {

        x.http().post(params, callback);
    }
}

