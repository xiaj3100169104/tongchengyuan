package com.juns.wechat.net.request;

import com.juns.wechat.config.ConfigUtil;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.callback.UpdateUserCallBack;

import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.body.MultipartBody;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 王宗文 on 2016/7/12.
 */
public class UploadFileRequest {

    @HttpRequest(host = ConfigUtil.REAL_API_URL, path = "uploadAvatar")
    public static class AvatarParams extends RequestParams{
        private String token;

        public AvatarParams(File file){
                token = AccountManager.getInstance().getToken();
                List<KeyValue> params = new ArrayList<>();
                params.add(new KeyValue("avatar", file));
                MultipartBody multipartBody = new MultipartBody(params, null);
               // FileBody fileBody = new FileBody(file, "multipart/form-data");
                setRequestBody(multipartBody);
               // LogUtil.i("conetentType:" + fileBody.getContentType());
        }
    }

    public static void uploadAvatar(String filePath, UpdateUserCallBack callBack){
        x.http().post(new AvatarParams(new File(filePath)), callBack);
    }




}
