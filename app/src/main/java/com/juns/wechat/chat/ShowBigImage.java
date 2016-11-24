package com.juns.wechat.chat;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.annotation.Content;
import com.juns.wechat.annotation.Extra;
import com.juns.wechat.common.ToolbarActivity;
import com.juns.wechat.util.DisplayUtil;
import com.juns.wechat.util.ImageLoader;
import com.juns.wechat.util.LogUtil;
import com.juns.wechat.util.PhotoUtil;
import com.juns.wechat.widget.scalemageview.PhotoView;
import android.app.AlertDialog;

import org.xutils.common.Callback;

/**
 * 下载显示大图
 * 
 */
public class ShowBigImage extends ToolbarActivity {
    public static final String ARG_IMG_NAME = "img_name";
	private PhotoView scaleImageView;
	private ProgressBar loadLocalPb;
    @Extra(name = ARG_IMG_NAME)
    private String imgName;

    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    private String tempImageName; //拍照临时保存的图片名字


    @Override
    public void initData() {
        setToolbarTitle("我的头像");
        scaleImageView = (PhotoView) findViewById(R.id.image);
        Point point = DisplayUtil.getScreenMetrics(this);
        ViewGroup.LayoutParams lp = scaleImageView.getLayoutParams();
        lp.width = point.x;
        lp.height = point.x;
        scaleImageView.setLayoutParams(lp);
        loadLocalPb = (ProgressBar) findViewById(R.id.pb_load_local);

        ImageLoader.loadAvatar(scaleImageView, imgName, new Callback.CommonCallback<Drawable>() {
            @Override
            public void onSuccess(Drawable result) {
                LogUtil.i("success!");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

        scaleImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_show_big_image;
		super.onCreate(savedInstanceState);

	}

    private void showPhotoDialog() {
         final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.alertdialog);

        // 为确认按钮添加事件,执行退出应用操作
        TextView tv_paizhao = (TextView) window.findViewById(R.id.tv_content1);
        tv_paizhao.setText("拍照");
        tv_paizhao.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SdCardPath")
            public void onClick(View v) {

                tempImageName = getNowTime() + ".png";
                PhotoUtil.takePhoto(ShowBigImage.this, PHOTO_REQUEST_TAKEPHOTO, tempImageName);
                dlg.cancel();
            }
        });
        TextView tv_xiangce = (TextView) window.findViewById(R.id.tv_content2);
        tv_xiangce.setText("相册");
        tv_xiangce.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tempImageName = getNowTime() + ".png";
                PhotoUtil.openAlbum(ShowBigImage.this, PHOTO_REQUEST_GALLERY);
                dlg.cancel();
            }
        });

    }

    @SuppressLint("SdCardPath")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_REQUEST_TAKEPHOTO:

                    Uri uri = Uri.fromFile(PhotoUtil.getFile(tempImageName));
                    PhotoUtil.cropView(uri, 480, ShowBigImage.this, PHOTO_REQUEST_CUT, tempImageName);
                    break;

                case PHOTO_REQUEST_GALLERY:
                    if (data != null)
                        PhotoUtil.cropView(data.getData(), 480, ShowBigImage.this, PHOTO_REQUEST_CUT, tempImageName);
                    break;

                case PHOTO_REQUEST_CUT:
                    // BitmapFactory.Options options = new BitmapFactory.Options();
                    //
                    // /**
                    // * 最关键在此，把options.inJustDecodeBounds = true;
                    // * 这里再decodeFile()，返回的bitmap为空
                    // * ，但此时调用options.outHeight时，已经包含了图片的高了
                    // */
                    // options.inJustDecodeBounds = true;
                    Bitmap bitmap = BitmapFactory.decodeFile(PhotoUtil.PHOTO_PATH + "/"
                            + tempImageName);
                    scaleImageView.setImageBitmap(bitmap);
                    break;

            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }
}
