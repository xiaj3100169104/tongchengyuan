package com.juns.wechat.net.request;

import com.juns.wechat.config.ConfigUtil;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.callback.AddDynamicCallBack;

import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.body.MultipartBody;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/13.
 */

public class AddDynamicRequest {

    @HttpRequest(host = ConfigUtil.REAL_API_URL, path = "addDynamic")
    public static class AddDynamicParams extends RequestParams{
        private String token;
        private String content;

        public AddDynamicParams(String content, List<File> fileList){
            token = AccountManager.getInstance().getToken();
            this.content = content;

            if(fileList != null && !fileList.isEmpty()){
                List<KeyValue> params = new ArrayList<>();
                for(int i = 0 ; i < fileList.size(); i++){
                    File file = fileList.get(i);
                    params.add(new KeyValue("avatar" + i, file));
                }
                MultipartBody multipartBody = new MultipartBody(params, null);
                setRequestBody(multipartBody);
            }

        }
    }

    public static void addDynamic(String content, List<File> images, AddDynamicCallBack callBack){
        x.http().post(new AddDynamicParams(content, images), callBack);
    }
}
