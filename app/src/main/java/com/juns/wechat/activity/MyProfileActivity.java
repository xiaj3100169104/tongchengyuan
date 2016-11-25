package com.juns.wechat.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.chat.ShowBigImage;
import com.style.base.BaseToolbarActivity;
import com.juns.wechat.dao.DbDataEvent;
import com.juns.wechat.database.UserTable;
import com.juns.wechat.dialog.SelectPhotoDialog;
import com.juns.wechat.dialog.SelectSexDialog;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.callback.UpdateUserCallBack;
import com.juns.wechat.net.request.UserRequest;
import com.juns.wechat.net.response.UpdateUserResponse;
import com.juns.wechat.util.ImageLoader;
import com.juns.wechat.util.PhotoUtil;
import com.style.constant.Skip;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * create by 王者 on 2016/7/14
 */
public class MyProfileActivity extends BaseToolbarActivity implements SelectPhotoDialog.OnClickListener {

    @Bind(R.id.ivAvatar)
    ImageView ivAvatar;
    @Bind(R.id.tvNickName)
    TextView tvNickName;
    @Bind(R.id.tvUserName)
    TextView tvUserName;
    @Bind(R.id.tvSex)
    TextView tvSex;

    private UserBean account;

    private SelectPhotoDialog selectPhotoDialog;
    private SelectSexDialog selectSexDialog;
    private String imageName;

    @Override
    public void initData() {
        setData();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_my_profile;
        super.onCreate(savedInstanceState);
    }

    private void setData() {
        account = AccountManager.getInstance().getUser();
        tvNickName.setText(account.getNickName() == null ? "" : account.getNickName());
        tvUserName.setText(account.getUserName());
        String sex = account.getSex();
        tvSex.setText(UserBean.Sex.isMan(sex) ? "男" : "女");
        ImageLoader.loadAvatar(ivAvatar, account.getHeadUrl());
    }

    @OnClick(R.id.rlAvatar)
    public void modifyAvatar() {
        if (selectPhotoDialog == null) {
            selectPhotoDialog = SelectPhotoDialog.createDialog(this, this);
        }
        selectPhotoDialog.show();
    }

    @OnClick(R.id.ivAvatar)
    public void showBigAvatar() {
        Intent intent = new Intent(this, ShowBigImage.class);
        intent.putExtra(Skip.KEY_IMG_NAME, account.getHeadUrl());
        startActivity(intent);
    }


    @OnClick(R.id.rlNickName)
    public void modifyNickName() {
        startActivity(new Intent(this, ModifyNameActivity.class));
    }

    @OnClick(R.id.rlSex)
    public void modifySex() {
        final String sex = account.getSex();
        int position = UserBean.Sex.isMan(sex) ? 0 : 1;
        selectSexDialog = SelectSexDialog.createDialog(this, position);
        selectSexDialog.setOnItemClickListener(new SelectSexDialog.OnItemClickListener() {
            @Override
            public void onItemClicked(int oldPosition, int newPosition) {
                if (oldPosition == newPosition) {
                    selectSexDialog.dismiss();
                } else {
                    String sex = newPosition == 0 ? UserBean.Sex.MAN.value : UserBean.Sex.WOMAN.value;
                    modifySexToServer(sex);
                }
            }
        });

        selectSexDialog.show();
    }

    @Subscriber(tag = UserTable.TABLE_NAME)
    private void onDbDataChanged(DbDataEvent<UserBean> event) {
        if (event.action == DbDataEvent.REPLACE || event.action == DbDataEvent.UPDATE) {
            List<UserBean> updateData = event.data;
            if (updateData != null && !updateData.isEmpty()) {
                for (UserBean userBean : updateData) {
                    if (userBean.getUserName().equals(account.getUserName())) {
                        setData();
                    }
                }
            }
        }
    }

    private void modifySexToServer(String sex) {
        UserRequest.updateUser(account.getUserName(), UserBean.SEX, sex, new UpdateUserCallBack() {
            @Override
            protected void handleResponse(UpdateUserResponse result) {
                super.handleResponse(result);
                selectSexDialog.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                selectSexDialog.dismiss();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Skip.CODE_TAKE_CAMERA:
                 /*   startPhotoZoom(
                            Uri.fromFile(PhotoUtil.getFile(imageName)),
                            480);*/
                    Uri uri = Uri.fromFile(PhotoUtil.getFile(imageName));
                    PhotoUtil.cropView(uri, 480, MyProfileActivity.this, Skip.CODE_PHOTO_CROP, imageName);
                    break;

                case Skip.CODE_TAKE_ALBUM:
                    if (data != null)
                        PhotoUtil.cropView(data.getData(), 480, MyProfileActivity.this, Skip.CODE_PHOTO_CROP, imageName);
                    break;

                case Skip.CODE_PHOTO_CROP:

                    break;

            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void takePhoto(View v) {
        imageName = getNowTime() + ".png";
        PhotoUtil.takePhoto(this, Skip.CODE_TAKE_CAMERA, imageName);
        selectPhotoDialog.dismiss();
    }

    @Override
    public void openAlbum(View v) {
        imageName = getNowTime() + ".png";
        PhotoUtil.openAlbum(this, Skip.CODE_TAKE_ALBUM);
        selectPhotoDialog.dismiss();
    }

    private String getNowTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }
}
