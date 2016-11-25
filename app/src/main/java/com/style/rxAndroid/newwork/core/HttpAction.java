package com.style.rxAndroid.newwork.core;

/**
 * Created by vong on 2016/5/19.
 */
public class HttpAction {
    protected String TAG = "HttpAction";

    private static String testUrl = "https://www.hao123.com/";

    public static NetJsonResult login(String name) {
        return OkHttpUtil.postAsyn(testUrl, name);
    }
}

