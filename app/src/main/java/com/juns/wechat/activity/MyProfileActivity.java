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
import com.juns.wechat.dao.DbDataEvent;
import com.juns.wechat.database.UserTable;
import com.juns.wechat.dialog.SelectPhotoDialog;
import com.juns.wechat.dialog.SelectSexDialog;
import com.juns.wechat.helper.CommonViewHelper;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.common.HttpActionImpl;
import com.juns.wechat.net.common.NetDataBeanCallback;
import com.juns.wechat.util.PhotoUtil;
import com.style.base.BaseToolbarActivity;
import com.style.constant.Skip;

import org.simple.eventbus.Subscriber;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * create by 王者 on 2016/7/14
 */
public class MyProfileActivity extends BaseToolbarActivity {

    @Bind(R.id.ivAvatar)
    ImageView ivAvatar;
    @Bind(R.id.tvNickName)
    TextView tvNickName;
    @Bind(R.id.tvUserName)
    TextView tvUserName;
    @Bind(R.id.tvSex)
    TextView tvSex;

    private UserBean curUser;

    private SelectPhotoDialog selectPhotoDialog;
    private SelectSexDialog selectSexDialog;
    private String imageName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_my_profile;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
        setToolbarTitle(R.string.my_profile);
        setData();
    }

    private void setData() {
        curUser = AccountManager.getInstance().getUser();
        CommonViewHelper.setUserViewInfo(curUser, ivAvatar, tvNickName, null, tvUserName, false);

        String sex = curUser.getSex();
        tvSex.setText(UserBean.Sex.isMan(sex) ? "男" : "女");
    }

    @OnClick(R.id.rlAvatar)
    public void modifyAvatar() {
        if (selectPhotoDialog == null) {
            selectPhotoDialog = new SelectPhotoDialog(this);
            selectPhotoDialog.setOnItemClickListener(new SelectPhotoDialog.OnItemClickListener() {
                @Override
                public void takePhoto(View v) {
                    imageName = getNowTime() + ".png";
                    PhotoUtil.takePhoto(MyProfileActivity.this, Skip.CODE_TAKE_CAMERA, imageName);
                    selectPhotoDialog.dismiss();
                }

                @Override
                public void openAlbum(View v) {
                    imageName = getNowTime() + ".png";
                    PhotoUtil.openAlbum(MyProfileActivity.this, Skip.CODE_TAKE_ALBUM);
                    selectPhotoDialog.dismiss();
                }
            });
        }
        selectPhotoDialog.show();
    }

    @OnClick(R.id.ivAvatar)
    public void showBigAvatar() {
        Intent intent = new Intent(this, ShowBigImage.class);
        intent.putExtra(Skip.KEY_IMG_NAME, curUser.getHeadUrl());
        startActivity(intent);
    }

    @OnClick(R.id.rlNickName)
    public void modifyNickName() {
        skip(ModifyNameActivity.class);
    }

    @OnClick(R.id.rlQrCode)
    public void myQRCodeCard() {
        skip(MyQRCodeActivity.class);
    }

    @OnClick(R.id.rlSex)
    public void modifySex() {
        final String sex = curUser.getSex();
        final int position = UserBean.Sex.isMan(sex) ? 0 : 1;
        if (selectSexDialog == null)
            selectSexDialog = new SelectSexDialog(this);
        selectSexDialog.show();
        selectSexDialog.setOnItemClickListener(new SelectSexDialog.OnItemClickListener() {
            @Override
            public void onClickMan(View v) {
                if (position == 1)
                    modifySexToServer(UserBean.Sex.MAN.value);
                else
                    selectSexDialog.dismiss();
            }

            @Override
            public void onClickWoman(View v) {
                if (position == 0)
                    modifySexToServer(UserBean.Sex.WOMAN.value);
                else
                    selectSexDialog.dismiss();
            }
        });
    }

    @Subscriber(tag = UserTable.TABLE_NAME)
    private void onDbDataChanged(DbDataEvent<UserBean> event) {
        if (event.action == DbDataEvent.REPLACE || event.action == DbDataEvent.UPDATE) {
            List<UserBean> updateData = event.data;
            if (updateData != null && !updateData.isEmpty()) {
                for (UserBean userBean : updateData) {
                    if (userBean.getUserName().equals(curUser.getUserName())) {
                        setData();
                    }
                }
            }
        }
    }

    private void modifySexToServer(String sex) {
        HttpActionImpl.getInstance().updateUser(TAG, UserBean.SEX, sex, new NetDataBeanCallback<UserBean>(UserBean.class) {
            @Override
            protected void onCodeSuccess(UserBean data) {
                dismissProgressDialog();
                selectSexDialog.dismiss();
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

    private String getNowTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }
}
