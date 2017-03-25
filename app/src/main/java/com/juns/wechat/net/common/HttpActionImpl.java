package com.juns.wechat.net.common;

import com.juns.wechat.bean.CommentBean;
import com.juns.wechat.config.ConfigUtil;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.response.LoginBean;

import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by xiajun on 2016/12/22.
 */
public class HttpActionImpl {
    protected String TAG = "HttpAction";
    private static String URL_BASE = ConfigUtil.REAL_API_URL;

    private Map<String, List<Call>> mTaskMap = new HashMap();
    private static HttpActionImpl mInstance;
    private HttpActionService service;

    public static HttpActionImpl getInstance() {
        if (mInstance == null) {
            mInstance = new HttpActionImpl();
        }
        return mInstance;
    }
    public void init(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(URL_BASE)
                .addConverterFactory(ScalarsConverterFactory.create()).build();
        service = retrofit.create(HttpActionService.class);
    }
    public void addTask(String tag, Call subscription) {
        List<Call> mSubscriptions = mTaskMap.get(tag);
        if (mSubscriptions == null) {
            mSubscriptions = new ArrayList<>();
            mTaskMap.put(tag, mSubscriptions);
        }
        mSubscriptions.add(subscription);
    }

    public void removeTask(String tag) {
        List<Call> mSubscriptions = mTaskMap.get(tag);
        if (mSubscriptions != null) {
            for (Call s : mSubscriptions) {
                if (s != null && !s.isCanceled()) {
                    s.cancel();
                }
            }
        }
    }
    public void runTask(String tag, Call call, NetDataBeanCallback callback) {
        call.enqueue(callback);
        addTask(tag, call);
    }

    public void register(String tag, String userName, String passWord, NetDataBeanCallback callback) {
        Call<String> call = service.register(userName, passWord);
        runTask(tag, call, callback);
    }

    public void login(String tag, String userName, String passWord, NetDataBeanCallback callback) {
        Call<String> call = service.login(userName, passWord);
        runTask(tag, call, callback);
    }

    public void refreshToken(String tag, NetDataBeanCallback callback) {
        Call<String> call = service.refreshToken();
        runTask(tag, call, callback);
    }

    public void updateUser(String tag, String field, Object value, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams();
        params.addParameter("field", field);
        params.addParameter("value", value);
        Call<String> call = service.updateUser(params.map);
        runTask(tag, call, callback);
    }

    public void searchUser(String tag, String search, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams();
        params.addParameter("search", search);
        Call<String> call = service.searchUser(params.map);
        runTask(tag, call, callback);
    }

    public void syncUserData(String tag, Integer[] userIds, long lastModifyDate, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams();
        params.addParameter("modifyDate", lastModifyDate);
        if (userIds != null && userIds.length != 0) {
            for (Integer userId : userIds) {
                params.addParameter("userIds[]", userId + "");
            }
        }
        Call<String> call = service.syncUserData(params.map);
        runTask(tag, call, callback);
    }

    public void queryUserData(String tag, int userId, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams();
        params.addParameter("userId", userId);
        Call<String> call = service.queryUserData(params.map);
        runTask(tag, call, callback);
    }

    public void queryPhone(String tag, String userName, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams();
        params.addParameter("userName", userName);
        Call<String> call = service.queryPhone(params.map);
        runTask(tag, call, callback);
    }

    public void getUsersByNames(String tag, String[] userNames, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams();
        for (String userName : userNames) {
            params.addParameter("userNames[]", userName);
        }
        Call<String> call = service.getUsersByNames(params.map);
        runTask(tag, call, callback);
    }

    public void syncFriendData(String tag, long lastModifyDate, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams();
        //params.addBodyParameter("token", token);
        params.addParameter("modifyDate", lastModifyDate);
        Call<String> call = service.syncFriendData(params.map);
        runTask(tag, call, callback);
    }

    public void addFriend(String tag, int contactName, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams();
        params.addParameter("contactName", contactName);
        Call<String> call = service.addFriend(params.map);
        runTask(tag, call, callback);
    }

    public void uploadAvatar(String tag, String filePath, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams();
        List<KeyValue> multipartParams = new ArrayList<>();
        multipartParams.add(new KeyValue("avatar", new File(filePath)));
        MultipartBody multipartBody = new MultipartBody(multipartParams, null);
        // FileBody fileBody = new FileBody(file, "multipart/form-data");
        //params.setRequestBody(multipartBody);
        Call<String> call = service.updateUser(params.map);
        runTask(tag, call, callback);
    }

    /**
     * @param content  动态内容
     * @param fileList 图片列表
     * @param callback
     */
    public void addDynamic(String tag, String content, File[] fileList, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams();
        params.addParameter("content", content);
        if (fileList != null && fileList.length > 0) {
            List<KeyValue> list = new ArrayList<>();
            for (int i = 0; i < fileList.length; i++) {
                File file = fileList[i];
                list.add(new KeyValue("avatar" + i, file));
            }
            MultipartBody multipartBody = new MultipartBody(list, null);
            //params.setRequestBody(multipartBody);
        }
        Call<String> call = service.addDynamic(params.map);
        runTask(tag, call, callback);
    }

    /**
     * @param dynamicId   动态id
     * @param replyUserId 被评论用户id,为-1时表示评论动态，不传该值
     * @param content     内容
     * @param callback
     */
    public void addComment2Dynamic(String tag, int dynamicId, int replyUserId, String content, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams();
        params.addParameter("dynamicId", String.valueOf(dynamicId));
        if (replyUserId != -1)
            params.addParameter("replyUserId", String.valueOf(replyUserId));
        params.addParameter("type", CommentBean.SubType.COMMENT.toString());
        params.addParameter("content", content);
        Call<String> call = service.addComment2Dynamic(params.map);
        runTask(tag, call, callback);
    }

    /**
     * @param action    0:刷新；1：加载更多
     * @param dynamicId 最新记录的id
     * @param limit     当前页条数
     * @param callback
     */
    public void getFriendCircleDynamic(String tag, int action, int dynamicId, int limit, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams();
        params.addParameter("action", action);
        if (action == 1)
            params.addParameter("dynamicId", dynamicId);
        params.addParameter("limit", limit);
        Call<String> call = service.getFriendCircleDynamic(params.map);
        runTask(tag, call, callback);
    }
}

