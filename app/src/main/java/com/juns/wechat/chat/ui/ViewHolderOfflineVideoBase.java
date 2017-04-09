package com.juns.wechat.chat.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.juns.wechat.R;
import com.juns.wechat.chat.bean.MessageBean;
import com.juns.wechat.chat.bean.OfflineVideoMsg;
import com.juns.wechat.chat.xmpp.util.FileTransferManager;
import com.juns.wechat.database.dao.MessageDao;
import com.style.constant.FileConfig;
import com.style.lib.media.video.PlayVideoActivity;
import com.style.manager.ImageLoader;
import com.style.utils.BitmapUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by xiajun on 2017/1/20.
 */

public class ViewHolderOfflineVideoBase extends BaseMsgViewHolder {
    protected ImageView ivPicture;
    protected OfflineVideoMsg videoMsg;
    private String path;

    ViewHolderOfflineVideoBase(View view) {
        super(view);
        ivPicture = (ImageView) view.findViewById(R.id.ivPicture);
    }

    @Override
    protected void updateView() {
        //先得到bean，在进行其他操作
        videoMsg = (OfflineVideoMsg) messageBean.getMsgObj();
        super.updateView();
        path = FileConfig.DIR_CACHE + "/" + videoMsg.fileName;
        //loadVideo();

    }

    @Override
    protected void onClickLayoutContainer() {
        super.onClickLayoutContainer();
        Log.e(TAG, "path==" + path);
        Intent data = new Intent();
        data.putExtra("path", path);
        data.setClass(context, PlayVideoActivity.class);
        context.startActivity(data);
    }

    private void loadVideo() {
        FileTransferManager fileTransferManager = new FileTransferManager();
        fileTransferManager.downloadFile(FileConfig.DIR_CACHE + "/" + videoMsg.fileName, videoMsg.size, new MyProgressListener(messageBean));

    }

    class MyProgressListener implements FileTransferManager.ProgressListener {
        private MessageBean messageBean;
        private int progress = 0;

        public MyProgressListener(MessageBean messageBean) {
            this.messageBean = messageBean;
        }

        @Override
        public void progressUpdated(int progress) {
            this.progress = progress;
            if (progress % 10 == 0)//不要更新太频繁
                updateMessage(progress);
        }

        @Override
        public void onFailed() {

        }

        @Override
        public void transferFinished() {
            progress = 100;
            updateMessage(progress);
            //loadPreview();
        }

        private void updateMessage(int progress) {
            videoMsg.progress = progress;
            messageBean.setMsg(videoMsg.toJson());
            MessageDao.getInstance().update(messageBean);
        }
    }

    //先判断预览图文件存不存在，不存在再根据视频获取预览图并保存
    private void loadPreview(ImageView imageView, String fileName) {
        String imageName = fileName + ".preview";
        String imagePath = FileConfig.DIR_CACHE + "/" + imageName;
        File v = new File(path);
        if (v.exists()) {
            File f = new File(imagePath);
            if (!f.exists()) {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(path);
                Bitmap bitmap = retriever.getFrameAtTime(1000 * 1000, MediaMetadataRetriever.OPTION_CLOSEST);
                try {
                    BitmapUtil.saveBitmap(imagePath, bitmap, 100);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ImageLoader.loadTriangleImage(imageView, imagePath, isLeftLayout() ? 0 : 1);
            }
        }
    }

}

