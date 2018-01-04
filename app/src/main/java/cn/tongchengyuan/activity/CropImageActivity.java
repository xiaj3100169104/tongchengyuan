package cn.tongchengyuan.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.same.city.love.R;

import cn.tongchengyuan.net.request.HttpActionImpl;
import cn.tongchengyuan.bean.UserBean;
import cn.tongchengyuan.manager.AccountManager;

import com.same.city.love.databinding.ActivityCropImageBinding;
import com.style.base.BaseToolbarBtnActivity;
import com.style.net.core.NetDataBeanCallback;
import com.style.constant.FileConfig;
import com.style.utils.BitmapUtil;

import org.jivesoftware.smack.packet.id.StanzaIdUtil;

import java.io.File;
import java.io.IOException;


public class CropImageActivity extends BaseToolbarBtnActivity {

    ActivityCropImageBinding bd;
    private Uri uri;

    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_crop_image);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    public void initData() {
        uri = getIntent().getExtras().getParcelable("uri");
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            bd.clipImageLayout.setCropImage(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onClickTitleRightView() {
        Bitmap croppedBitmap = bd.clipImageLayout.clip();
        fileName = StanzaIdUtil.newStanzaId() + ".image";
        String filePath = FileConfig.DIR_CACHE + File.separator + fileName;
        try {
            BitmapUtil.saveBitmap(filePath, croppedBitmap, 100);
            updateAvatarInServer(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateAvatarInServer(String filePath) {
        HttpActionImpl.getInstance().uploadAvatar(TAG, filePath, new NetDataBeanCallback<UserBean>(UserBean.class) {
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
