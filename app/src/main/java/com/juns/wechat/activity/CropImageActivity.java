package com.juns.wechat.activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.juns.wechat.R;
import com.juns.wechat.annotation.Extra;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.common.HttpAction;
import com.juns.wechat.net.common.NetDataBeanCallback;
import com.juns.wechat.util.PhotoUtil;
import com.juns.wechat.view.ClipImageLayout;
import com.style.base.BaseToolbarActivity;
import com.style.constant.FileConfig;

import org.jivesoftware.smack.packet.id.StanzaIdUtil;

import java.io.IOException;

import butterknife.Bind;
import butterknife.OnClick;

public class CropImageActivity extends BaseToolbarActivity {

    @Bind(R.id.clipImageLayout)
    ClipImageLayout clipImageLayout;
    @Extra(name = "uri")
    private Uri uri;

    private String imageName;

    @Override
    public void initData() {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            clipImageLayout.setCropImage(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_crop_image;
        super.onCreate(savedInstanceState);

    }

    @OnClick(R.id.view_toolbar_right)
    public void saveInfo(View v) {
        Bitmap croppedBitmap = clipImageLayout.clip();
        imageName = StanzaIdUtil.newStanzaId() + ".image";
        PhotoUtil.saveBitmap(croppedBitmap, FileConfig.DIR_IMAGE + "/" + imageName);

        updateAvatarInServer(imageName);
    }

    private void updateAvatarInServer(String imageName) {
        String filePath = FileConfig.DIR_IMAGE + "/" + imageName;
        //UploadFileRequest.uploadAvatar(filePath, callBack);
        HttpAction.uploadAvatar(filePath, new NetDataBeanCallback<UserBean>(UserBean.class) {
            @Override
            protected void onCodeSuccess(UserBean data) {
                dismissProgressDialog();
                AccountManager.getInstance().setUser(data);
                finish();
            }

            @Override
            protected void onCodeFailure(String msg) {
                dismissProgressDialog();
                showToast(msg);
            }
        });
    }
}
