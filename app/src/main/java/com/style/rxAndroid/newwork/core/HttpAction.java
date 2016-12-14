package com.style.rxAndroid.newwork.core;

import com.juns.wechat.bean.DynamicBean;
import com.juns.wechat.config.ConfigUtil;

/**
 * Created by vong on 2016/5/19.
 */
public class HttpAction {
    protected String TAG = "HttpAction";

    private static String testUrl = "https://www.hao123.com/";
    private static String addDynamicUrl = ConfigUtil.REAL_API_URL + "/addDynamic";

    public static NetJsonResult login(String name) {
        return OkHttpUtil.postAsyn(testUrl, name);
    }
    public static NetJsonResult addDynamic(DynamicBean bean) {
        return OkHttpUtil.postAsyn(addDynamicUrl, bean);
    }
}

