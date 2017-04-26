package com.juns.wechat.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.config.ConfigUtil;
import com.style.base.BaseToolbarActivity;
import com.style.constant.FileConfig;
import com.style.constant.Skip;
import com.style.manager.ImageLoader;
import com.style.net.image.FileDownloadCallback;
import com.style.net.image.FileDownloadManager;
import com.style.utils.FileUtil;

import butterknife.Bind;

/**
 * 下载显示大图
 */
public class ShowBigImage extends BaseToolbarActivity {

    @Bind(R.id.image)
    ImageView image;
    @Bind(R.id.progressbar)
    ProgressBar progressBar;
    @Bind(R.id.tv_percent)
    TextView tvPercent;
    private String imgName;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_show_big_image;
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initData() {
        setToolbarTitle("我的头像");

        imgName = getIntent().getStringExtra("imgName");
        String path = FileConfig.DIR_CACHE + "/" + imgName;
        if (FileUtil.isExist(path)) {
            fileExist(path);
            return;
        }
        url = ConfigUtil.getDownUrl(imgName);
        FileDownloadManager.getInstance().down(url, path, new FileDownloadCallback() {
            @Override
            public void complete(String filePath) {
                super.complete(filePath);
                fileExist(filePath);
            }

            @Override
            public void start(int fileSize) {
                super.start(fileSize);
                progressBar.setVisibility(View.VISIBLE);
                tvPercent.setVisibility(View.VISIBLE);
                progressBar.setMax(100);
            }

            @Override
            public void inProgress(int fileSize, int progress, int percent) {
                super.inProgress(fileSize, progress, percent);
                progressBar.setProgress(percent);
                tvPercent.setText(percent + "%");
            }

            @Override
            public void failed(String error) {
                super.failed(error);
                progressBar.setVisibility(View.GONE);
                tvPercent.setVisibility(View.GONE);
                showToast(error);
            }
        });

    }

    private void fileExist(String path) {
        progressBar.setVisibility(View.GONE);
        tvPercent.setVisibility(View.GONE);
        logE(TAG, path);
        ImageLoader.loadBigPicture(this, image, path);
       /* PhotoViewAttacher attacher = new PhotoViewAttacher(image);
        attacher.update();*/
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        FileDownloadManager.getInstance().cancelCallback(url);
    }
}
