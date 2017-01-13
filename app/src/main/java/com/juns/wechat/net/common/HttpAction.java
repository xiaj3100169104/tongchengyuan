package com.juns.wechat.net.common;

import com.juns.wechat.bean.CommentBean;
import com.juns.wechat.config.ConfigUtil;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.util.LogUtil;

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
    private static String URL_REGISTER_USER = URL_BASE + "/addUser";
    private static String URL_LOGIN_USER = URL_BASE + "/login";
    private static String URL_UPDATE_USER = URL_BASE + "/updateUser";
    private static String URL_SEARCH_USER = URL_BASE + "/searchUser";
    private static String URL_SYNC_USER_DATA = URL_BASE + "/syncUserData";
    private static String URL_QUERY_USER = URL_BASE + "/queryUser";
    private static String URL_GET_USERS_BY_NAMES = URL_BASE + "/getUsersByNames";
    private static String URL_NEW_TOKEN = URL_BASE + "/newToken";
    private static String URL_SYNC_FRIEND_DATA = URL_BASE + "/syncFriendData";
    private static String URL_ADD_FRIEND = URL_BASE + "/addFriend";
    private static String URL_UPLOAD_AVATAR = URL_BASE + "/uploadAvatar";

    private static String URL_ADD_DYNAMIC = URL_BASE + "/addDynamic";
    private static String URL_ADD_COMMENT_2_DYNAMIC = URL_BASE + "/addComment";
    private static String URL_GET_DYNAMICS_FRIEND_CIRCLE = URL_BASE + "/getDynamicsByPage";

    private static String userId;
    private static String token;

    private static void initCommonParams() {
        token = AccountManager.getInstance().getToken();
    }

    public static void register(String userName, String passWord, NetDataBeanCallback callback) {
        RequestParams params = new RequestParams(URL_REGISTER_USER);
        params.addBodyParameter("userName", userName);
        params.addBodyParameter("passWord", passWord);
        NetWorkManager.getInstance().post(params, callback);
    }

    public static void login(String userName, String passWord, NetDataBeanCallback callback) {
        RequestParams params = new RequestParams(URL_LOGIN_USER);
        params.addBodyParameter("userName", userName);
        params.addBodyParameter("passWord", passWord);
        NetWorkManager.getInstance().post(params, callback);
    }

    public static void refreshToken(NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams(URL_NEW_TOKEN);

        NetWorkManager.getInstance().post(params, callback);
    }

    public static void updateUser(String field, Object value, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams(URL_UPDATE_USER);
        //params.addBodyParameter("token", token);
        params.addParameter("field", field);
        params.addParameter("value", value);
        NetWorkManager.getInstance().post(params, callback);
    }

    public static void searchUser(String search, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams(URL_SEARCH_USER);
        //params.addBodyParameter("token", token);
        params.addParameter("search", search);
        NetWorkManager.getInstance().post(params, callback);
    }

    public static void syncUserData(Integer[] userIds, long lastModifyDate, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams(URL_SYNC_USER_DATA);
        //params.addBodyParameter("token", token);
        params.addParameter("modifyDate", lastModifyDate);
        if (userIds != null && userIds.length != 0) {
            for (Integer userId : userIds) {
                params.addBodyParameter("userIds[]", userId + "");
            }
        }
        NetWorkManager.getInstance().post(params, callback);
    }

    public static void queryUserData(int userId, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams(URL_QUERY_USER);
        //params.addBodyParameter("token", token);
        params.addParameter("userId", userId);
        NetWorkManager.getInstance().post(params, callback);
    }

    public static void queryUserData(String userName, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams(URL_QUERY_USER);
        //params.addBodyParameter("token", token);
        params.addParameter("userName", userName);
        NetWorkManager.getInstance().post(params, callback);
    }

    public static void getUsersByNames(String[] userNames, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams(URL_GET_USERS_BY_NAMES);
        for (String userName : userNames) {
            params.addBodyParameter("userNames[]", userName);
        }
        NetWorkManager.getInstance().post(params, callback);
    }

    public static void syncFriendData(long lastModifyDate, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams(URL_SYNC_FRIEND_DATA);
        //params.addBodyParameter("token", token);
        params.addParameter("modifyDate", lastModifyDate);
        NetWorkManager.getInstance().post(params, callback);
    }

    public static void addFriend(String contactName, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams(URL_ADD_FRIEND);
        //params.addBodyParameter("token", token);
        params.addParameter("contactName", contactName);
        NetWorkManager.getInstance().post(params, callback);
    }

    public static void uploadAvatar(String filePath, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams(URL_UPLOAD_AVATAR);
        //params.addBodyParameter("token", token);
        List<KeyValue> multipartParams = new ArrayList<>();
        multipartParams.add(new KeyValue("avatar", new File(filePath)));
        MultipartBody multipartBody = new MultipartBody(multipartParams, null);
        // FileBody fileBody = new FileBody(file, "multipart/form-data");
        params.setRequestBody(multipartBody);
        NetWorkManager.getInstance().post(params, callback);
    }

    /**
     * @param content  动态内容
     * @param fileList 图片列表
     * @param callback
     */
    public static void addDynamic(String content, File[] fileList, NetDataBeanCallback callback) {
        //initCommonParams();
        TokenRequestParams params = new TokenRequestParams(URL_ADD_DYNAMIC);
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
     * @param dynamicId  动态id
     * @param content 内容
     * @param callback
     */
    public static void addComment2Dynamic(int dynamicId, String content, NetDataBeanCallback callback) {
        //initCommonParams();
        TokenRequestParams params = new TokenRequestParams(URL_ADD_COMMENT_2_DYNAMIC);
        params.addBodyParameter("dynamicId", String.valueOf(dynamicId));
        params.addBodyParameter("type", CommentBean.SubType.COMMENT.toString());
        params.addBodyParameter("content", content);
        NetWorkManager.getInstance().post(params, callback);
    }
    /**
     * @param dynamicId  动态id
     * @replyCommentId  被回复的评论id
     * @param content 内容
     * @param callback
     */
    public static void addReply2Comment(int dynamicId, int replyCommentId, String content, NetDataBeanCallback callback) {
        //initCommonParams();
        TokenRequestParams params = new TokenRequestParams(URL_ADD_COMMENT_2_DYNAMIC);
        params.addBodyParameter("dynamicId", String.valueOf(dynamicId));
        params.addBodyParameter("replyCommentId", String.valueOf(replyCommentId));
        params.addBodyParameter("type", "0");
        params.addBodyParameter("content", content);
        NetWorkManager.getInstance().post(params, callback);
    }
    /**
     * @param action    0:刷新；1：加载更多
     * @param dynamicId 最新记录的id
     * @param limit     当前页条数
     * @param callback
     */
    public static void getFriendCircleDynamic(int action, int dynamicId, int limit, NetDataBeanCallback callback) {
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

