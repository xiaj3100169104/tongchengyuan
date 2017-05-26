package com.juns.wechat.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juns.wechat.bean.UserBean;
import com.juns.wechat.bean.UserPropertyBean;
import com.juns.wechat.dialog.SelectPhotoDialog;
import com.juns.wechat.net.request.HttpActionImpl;
import com.same.city.love.R;
import com.style.base.BaseToolbarActivity;
import com.style.constant.FileConfig;
import com.style.constant.Skip;
import com.style.net.core.NetDataBeanCallback;
import com.style.utils.CommonUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import me.kaede.tagview.Tag;
import me.kaede.tagview.TagView;

/**
 * Created by xiajun on 2017/5/9.
 */

public class PersonInfoEditPhotoWallActivity extends BaseToolbarActivity {

    private SelectPhotoDialog selectPhotoDialog;
    private String avatarFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_person_info_photo_wall;
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_with_text, menu);
        menu.getItem(0).setTitle(R.string.save);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_text_only:
                //saveInfo();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void initData() {
        setToolbarTitle(R.string.edit_photo_wall);

    }

    @OnClick(R.id.gl_photo_wall)
    public void modifyAvatar() {
        if (selectPhotoDialog == null) {
            selectPhotoDialog = new SelectPhotoDialog(this);
            selectPhotoDialog.setOnItemClickListener(new SelectPhotoDialog.OnItemClickListener() {
                @Override
                public void takePhoto(View v) {
                    avatarFile = FileConfig.DIR_IMAGE + File.separator + getNowTime() + ".png";
                    CommonUtil.takePhoto(PersonInfoEditPhotoWallActivity.this, avatarFile);
                    selectPhotoDialog.dismiss();
                }

                @Override
                public void openAlbum(View v) {
                    CommonUtil.selectPhoto(PersonInfoEditPhotoWallActivity.this);
                    selectPhotoDialog.dismiss();
                }
            });
        }
        selectPhotoDialog.show();
    }

    private String getNowTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Skip.CODE_TAKE_CAMERA:
                    Uri uri = Uri.fromFile(new File(avatarFile));
                    Intent intent = new Intent(this, CropImageActivity.class);
                    intent.putExtra("uri", uri);
                    startActivity(intent);
                    break;
                case Skip.CODE_TAKE_ALBUM:
                    if (data != null) {
                        Intent intent2 = new Intent(this, CropImageActivity.class);
                        intent2.putExtra("uri", data.getData());
                        startActivity(intent2);
                    }
                    break;
                case Skip.CODE_PHOTO_CROP:

                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
