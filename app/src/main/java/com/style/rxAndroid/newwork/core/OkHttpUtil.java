package com.style.rxAndroid.newwork.core;


import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.juns.wechat.App;
import com.juns.wechat.R;
import com.style.utils.CommonUtil;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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

    private static boolean isNetNotWork() {
        if (!CommonUtil.isNetWorkConnected(App.getInstance())) {
            Toast.makeText(App.getInstance(), R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    //同步请求
    public static NetJsonResult postAsyn(String url, Object obj) {
        NetJsonResult result = null;
        if (isNetNotWork()) {
            result.setCode(1);
            result.setMsg("网络不可用");
            return result;
        }
        if (obj != null) {
            Params params;
            if (obj instanceof Params)
                params = (Params) obj;
            else
                params = new Params(obj);
            RequestBody requestBody;
            FormBody.Builder builder;
            if (params.getFiles() == null) {
                builder = new FormBody.Builder();
                Map<String, Object> map = params.getParams();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String key = entry.getKey();
                    Object o = entry.getValue();
                    String value;
                    if (o == null) {
                        value = "";
                    } else if (o.equals("")) {
                        value = "";
                    } else {
                        value = String.valueOf(o);
                    }
                    builder.add(key, value);
                }
                requestBody = builder.build();
            } else {
                MultipartBody.Builder multipart = new MultipartBody.Builder();
                multipart.setType(MultipartBody.FORM);
                File[] files = params.getFiles();
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        if (files[i] != null) {
                            if (files[i].exists()) {
                                RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), files[i]);
                                multipart.addFormDataPart("file", files[i].getName(), fileBody);
                            }
                        }
                    }
                }
                Map<String, Object> map = params.getParams();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String key = entry.getKey();
                    Object o = entry.getValue();
                    multipart.addFormDataPart(key, String.valueOf(o));
                }
                requestBody = multipart.build();
            }
            Request request = new Request.Builder().url(url).post(requestBody).build();
            Call call = client.newCall(request);
            try {
                Response response = call.execute();
                if (response.isSuccessful()) {
                    String body = response.body().string();//"{code:0,body: {name: \"夏军\",phone: \"18202823096\"},msg: \"请求成功\"}";
                    Log.e("body", body + "");
                    result = JSON.parseObject(body, NetJsonResult.class);
                }
                response.body().close();
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
                result = new NetJsonResult();
                result.setCode(1);
                result.setMsg("请求异常");
            }
        }
        return result;
    }
}
