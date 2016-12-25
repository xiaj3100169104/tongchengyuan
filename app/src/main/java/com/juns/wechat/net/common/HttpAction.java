package com.juns.wechat.net.common;

import com.juns.wechat.config.ConfigUtil;
import com.juns.wechat.manager.AccountManager;

import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiajun on 2016/12/22.
 */
public class HttpAction {
    protected String TAG = "HttpAction";
    private static String URL_BASE = ConfigUtil.REAL_API_URL;
    private static String URL_ADD_DYNAMIC = URL_BASE + "/addDynamic";
    private static String URL_GET_DYNAMICS_FRIEND_CIRCLE = URL_BASE + "/getDynamicsByPage";

    private static String userId;
    private static String token;

    private static void initCommonParams() {
        token = AccountManager.getInstance().getToken();
    }

    public static void login(String name) {

    }

    /**
     * @param content  动态内容
     * @param fileList 图片列表
     * @param callback
     */
    public static void addDynamic(String content, File[] fileList, NetBeanCallback callback) {
        initCommonParams();
        RequestParams params = new RequestParams(URL_ADD_DYNAMIC);
        params.addBodyParameter("token", token);
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
        NetWorkManager.getInstance().post(params, callback);
    }

    /**
     * @param action    0:刷新；1：加载更多
     * @param dynamicId 最新记录的id
     * @param limit     当前页条数
     * @param callback
     */
    public static void getFriendCircleDynamic(int action, int dynamicId, int limit, NetBeanCallback callback) {
        //initCommonParams();
        TokenRequestParams params = new TokenRequestParams(URL_GET_DYNAMICS_FRIEND_CIRCLE);
        //params.addBodyParameter("token", token);
        params.addParameter("action", action);
        if (action == 1)
            params.addParameter("dynamicId", dynamicId);
        params.addParameter("limit", limit);
        NetWorkManager.getInstance().post(params, callback);
    }
}

