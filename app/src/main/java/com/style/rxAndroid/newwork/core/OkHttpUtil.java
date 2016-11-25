package com.style.rxAndroid.newwork.core;



import com.alibaba.fastjson.JSON;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xiajun on 2016/1/15.
 */
public class OkHttpUtil {
    protected static String TAG = "OkHttpUtil";

    //全局
    public static OkHttpClient client = new OkHttpClient();

    static {
    }

    //同步请求
    public static NetJsonResult postAsyn(String url, String params) {
        NetJsonResult result = null;
        Request request = new Request.Builder().url(url).get().build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                String body = "{code:0,body: {name: \"夏军\",phone: \"18202823096\"},msg: \"请求成功\"}";
                result = JSON.parseObject(body, NetJsonResult.class);
            }
            response.body().close();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
            result = new NetJsonResult();
            result.setCode(1);
        }
        return result;
    }
}
