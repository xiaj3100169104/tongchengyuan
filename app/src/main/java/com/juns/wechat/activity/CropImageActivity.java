package com.juns.wechat.activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;

import com.juns.wechat.R;
import com.juns.wechat.annotation.Click;
import com.juns.wechat.annotation.Content;
import com.juns.wechat.annotation.Extra;
import com.juns.wechat.annotation.Id;
import com.juns.wechat.common.CommonUtil;
import com.juns.wechat.common.ToolbarActivity;
import com.juns.wechat.net.callback.UpdateUserCallBack;
import com.juns.wechat.net.request.UploadFileRequest;
import com.juns.wechat.net.response.UpdateUserResponse;
import com.juns.wechat.util.PhotoUtil;
import com.juns.wechat.view.ClipImageLayout;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Content(R.layout.activity_crop_image)
public class CropImageActivity extends ToolbarActivity {

    @Id
    private ClipImageLayout clipImageLayout;
    @Extra(name = "uri")
    private Uri uri;

    private String imageName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Bitmap  bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            clipImageLayout.setCropImage(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Click(viewId = R.id.tvRightText)
    private void saveInfo(View v){
        Bitmap croppedBitmap = clipImageLayout.clip();
        imageName = getNowTime() + ".image";
        PhotoUtil.saveBitmap(croppedBitmap, PhotoUtil.PHOTO_PATH + "/" + imageName);

        updateAvatarInServer(imageName);
    }

    private String getNowTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }

    private void  updateAvatarInServer(String imageName){
        String filePath = PhotoUtil.PHOTO_PATH + "/" + imageName;
        UploadFileRequest.uploadAvatar(filePath, callBack);
    }

    private UpdateUserCallBack callBack = new UpdateUserCallBack() {
        @Override
        protected void handleResponse(UpdateUserResponse result) {
            super.handleResponse(result);
            CommonUtil.finish(CropImageActivity.this);
        }

    };
}
