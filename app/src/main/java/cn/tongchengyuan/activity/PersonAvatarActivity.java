package cn.tongchengyuan.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import cn.tongchengyuan.greendao.mydao.GreenDaoManager;
import cn.tongchengyuan.manager.AccountManager;
import cn.tongchengyuan.bean.UserBean;

import com.same.city.love.R;
import com.same.city.love.databinding.ActivityPersonAvatarBinding;
import com.style.base.BaseToolbarActivity;
import com.style.constant.FileConfig;
import com.style.constant.Skip;
import com.style.dialog.SelAvatarDialog;
import com.style.event.EventCode;
import com.style.event.EventManager;
import com.style.manager.ImageLoader;
import com.style.utils.BitmapUtil;
import com.style.utils.CommonUtil;
import com.style.utils.FileUtil;
import com.style.utils.PictureUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PersonAvatarActivity extends BaseToolbarActivity {

    ActivityPersonAvatarBinding bd;
    private File photoFile;
    private boolean isFromCamera;
    private SelAvatarDialog dialog;
    private UserBean user;
    private Uri uri2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_person_avatar);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    public void initData() {
        setToolbarTitle("头像");
        user = AccountManager.getInstance().getUser();
        setAvatar(user.headUrl);
    }

    private void setAvatar(String url) {
        ImageLoader.loadBigPicture(this, bd.ivAvatar, url);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friend_circle, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.select:
                showSelPicPopupWindow();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSelPicPopupWindow() {
        if (dialog == null) {
            dialog = new SelAvatarDialog(this, R.style.Dialog_NoTitle);
            dialog.setOnItemClickListener(new SelAvatarDialog.OnItemClickListener() {
                @Override
                public void OnClickCamera() {
                    if (!CommonUtil.isSDcardAvailable()) {
                        showToast("sd卡不可用");
                        return;
                    }
                    photoFile = CommonUtil.takePhoto(PersonAvatarActivity.this, FileConfig.DIR_IMAGE + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg");

                }

                @Override
                public void OnClickPhoto() {
                    if (!CommonUtil.isSDcardAvailable()) {
                        showToast("sd卡不可用");
                        return;
                    }
                    CommonUtil.selectPhoto(PersonAvatarActivity.this);
                }

                @Override
                public void OnClickCancel() {

                }
            });
        }
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Skip.CODE_TAKE_CAMERA:// 拍照
                    isFromCamera = true;
                    if (null != photoFile && photoFile.exists()) {
                        CommonUtil.notifyUpdateGallary(this, photoFile);// 通知系统更新相册
                        dealPicture(photoFile);
                    } else {
                        showToast(R.string.File_does_not_exist);
                    }
                    break;
                case Skip.CODE_TAKE_ALBUM:// 本地
                    if (data != null) {
                        isFromCamera = false;
                        Uri uri = data.getData();
                        File fromFile = FileUtil.UriToFile(this, uri);
                        dealPicture(fromFile);
                    } else {
                        showToast("照片获取失败");
                    }
                    break;
                case Skip.CODE_PHOTO_CROP:// 裁剪头像返回
                    if (data != null) {
                        int degree = 0;
                        if (isFromCamera) {
                            if (photoFile.exists()) {
                                degree = PictureUtils.readPictureDegree(photoFile.getAbsolutePath());
                                logE("life", "拍照后的角度：" + degree);
                            } else {
                                showToast(R.string.File_does_not_exist);
                            }
                        }
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri2));
                            if (isFromCamera && degree != 0) {// 旋转图片 动作
                                bitmap = BitmapUtil.rotate(bitmap, 0);
                            }
                            String savePath = FileConfig.DIR_CACHE + "/" + System.currentTimeMillis() + ".image";
                            // 保存图片
                            BitmapUtil.saveBitmap(savePath, bitmap, 100);
                            Log.e("PersonAvatar", "图片的路径是--->" + savePath);
                            uploadAvatar(savePath);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
            }
        }

    }

    private void dealPicture(File fromFile) {
        //需要把原文件复制一份，否则会在原文件上操作
        File f = new File(FileConfig.DIR_CACHE, String.valueOf(System.currentTimeMillis()) + ".image");
        if (f.exists()) {
            f.delete();
        }
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        boolean isCopy = FileUtil.copyfile(fromFile, f, true);
        if (isCopy) {
            uri2 = Uri.fromFile(f);
            Log.e("PersonAvatar", "本地图片的路径-->" + uri2);
            Intent i = PictureUtils.getCropImageIntent(uri2, 200, 200);
            this.startActivityForResult(i, Skip.CODE_PHOTO_CROP);
        } else {
            showToast("复制图片出错");
        }
    }

    private void uploadAvatar(final String path) {
        File f = new File(path);
        user.setHeadUrl(f.getName());
        GreenDaoManager.getInstance().save(user);
        EventManager.getDefault().post(EventCode.UPDATE_USER_HEAD, user);
    }
}
