package cn.tongchengyuan.net.request;

import cn.tongchengyuan.bean.CommentBean;
import cn.tongchengyuan.chat.config.ConfigUtil;
import cn.tongchengyuan.manager.AccountManager;
import com.style.net.core.HttpActionManager;
import com.style.net.core.NetDataBeanCallback;

import java.io.File;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by xiajun on 2016/12/22.
 */
public class HttpActionImpl {
    protected String TAG = "HttpActionImpl";
    private static String URL_BASE_REMOTE = ConfigUtil.REMOTE_SERVER;
    private static String URL_BASE_LOCAL = ConfigUtil.LOCAL_SERVER;

    private static HttpActionImpl mInstance;
    private HttpActionService service;
    private HttpActionService serviceLocal;

    private HttpActionManager httpActionManager;

    public static HttpActionImpl getInstance() {
        if (mInstance == null) {
            mInstance = new HttpActionImpl();
        }
        return mInstance;
    }

    public HttpActionImpl() {

    }

    public void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(getClient())
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(URL_BASE_REMOTE)
                .build();
        service = retrofit.create(HttpActionService.class);
        //initLocalService();
        httpActionManager = HttpActionManager.getInstance();
    }

    private void initLocalService() {
        Retrofit retrofit = new Retrofit.Builder()
                //.client(getClient())
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(URL_BASE_LOCAL)
                .build();
        serviceLocal = retrofit.create(HttpActionService.class);

    }

    private OkHttpClient getClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                //添加请求拦截
                //.connectTimeout(5, TimeUnit.SECONDS)//设置超时时间
                .retryOnConnectionFailure(true)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request original = chain.request();

                        // Request customization: add request headers
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("token", AccountManager.getInstance().getToken());
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                });

        return client.build();
    }

    private void runTask(String tag, Call call, NetDataBeanCallback callback) {
        httpActionManager.addTask(tag, call);
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
        Call<String> call = service.refreshToken(AccountManager.getInstance().getToken());
        runTask(tag, call, callback);
    }

    public void updateUser(String tag, String field, String value, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams();
        params.addParameter("field", field);
        params.addParameter("value", value);
        Call<String> call = service.updateUser(params.map);
        runTask(tag, call, callback);
    }

    public void updateUserProperty(String tag, String value, NetDataBeanCallback callback) {
        /*RequestBody requestBody = new MultipartBody.Builder()
                //.setType(MultipartBody.FORM)
                .addFormDataPart("userProperties", userProperties)
                .build();*/
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), value);
        Call<String> call = service.updateUserProperty(requestBody);
        runTask(tag, call, callback);
    }

    public void uploadAvatar(String tag, String filePath, NetDataBeanCallback callback) {
         File file = new File(filePath);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("avatar", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                .build();

        Call<String> call = service.uploadAvatar(requestBody);
        runTask(tag, call, callback);
    }

    public void searchUser(String tag, String search, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams();
        params.addParameter("search", search);
        Call<String> call = service.searchUser(params.map);
        runTask(tag, call, callback);
    }

    public void syncFriendData(String tag, long lastModifyDate, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams();
        params.addParameter("modifyDate", lastModifyDate);
        Call<String> call = service.syncFriendData(params.map);
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

    public void queryUserData(String tag, String userId, NetDataBeanCallback callback) {
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

    public void addFriend(String tag, String contactName, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams();
        params.addParameter("contactName", contactName);
        Call<String> call = service.addFriend(params.map);
        runTask(tag, call, callback);
    }

    /**
     * @param content  动态内容
     * @param fileList 图片列表
     * @param callback
     */
    public void addDynamic(String tag, String content, File[] fileList, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams();
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("content", content);
        if (fileList != null && fileList.length > 0) {
            for (int i = 0; i < fileList.length; i++) {
                File file = fileList[i];
                requestBody.addFormDataPart("avatar" + i, file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
            }
        }
        Call<String> call = service.addDynamic(requestBody.build());
        runTask(tag, call, callback);
    }

    /**
     * @param dynamicId   动态id
     * @param replyUserId 被评论用户id,为-1时表示评论动态，不传该值
     * @param content     内容
     * @param callback
     */
    public void addComment2Dynamic(String tag, String dynamicId, String replyUserId, String content, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams();
        params.addParameter("dynamicId", String.valueOf(dynamicId));
        if (replyUserId != null)
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
    public void getFriendCircleDynamic(String tag, int action, String dynamicId, int limit, NetDataBeanCallback callback) {
        TokenRequestParams params = new TokenRequestParams();
        params.addParameter("action", action);
        if (action == 1)
            params.addParameter("dynamicId", dynamicId);
        params.addParameter("limit", limit);
        Call<String> call = service.getFriendCircleDynamic(params.map);
        runTask(tag, call, callback);
    }
}

