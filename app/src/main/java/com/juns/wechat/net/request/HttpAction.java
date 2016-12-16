package com.juns.wechat.net.request;

import com.juns.wechat.config.ConfigUtil;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.callback.BaseCallBack;

import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vong on 2016/5/19.
 */
public class HttpAction {
    protected String TAG = "HttpAction";

    private static String URL_BASE = ConfigUtil.REAL_API_URL;
    private static String URL_ADD_DYNAMIC = URL_BASE + "/addDynamic";

    private static String userId;
    private static String token;

    private static void initCommonParams() {
        token = AccountManager.getInstance().getToken();
    }

    public static void login(String name) {

    }

    public static void addDynamic(String content, File[] fileList, BaseCallBack callBack) {
        initCommonParams();
        RequestParams params = new RequestParams(URL_ADD_DYNAMIC);
        params.addBodyParameter("content", content);
        if (fileList != null && fileList.length > 0) {
            List<KeyValue> list = new ArrayList<>();
            for (int i = 0; i < fileList.length; i++) {
                File file = fileList[i];
                list.add(new KeyValue("avatar" + i, file));
            }
            MultipartBody multipartBody = new MultipartBody(list, null);
            params.setRequestBody(multipartBody);
        }
        x.http().post(params, callBack);
    }
}

