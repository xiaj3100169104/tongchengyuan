package com.juns.wechat.chat.ui;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.juns.wechat.R;
import com.juns.wechat.chat.bean.MessageBean;
import com.juns.wechat.chat.bean.PictureMsg;
import com.juns.wechat.chat.xmpp.util.FileTransferManager;
import com.juns.wechat.database.dao.MessageDao;
import com.juns.wechat.util.ThreadPoolUtil;
import com.style.constant.FileConfig;
import com.style.manager.ImageLoader;

import java.io.File;

/**
 * Created by xiajun on 2017/1/20.
 */

public class ViewHolderPictureBase extends BaseMsgViewHolder {
    protected ImageView ivPicture;
    protected PictureMsg pictureMsg;
    private String path;

    ViewHolderPictureBase(View view) {
        super(view);
        ivPicture = (ImageView) view.findViewById(R.id.ivPicture);
    }

    @Override
    protected void updateView() {
        //先得到bean，在进行其他操作
        pictureMsg = (PictureMsg) messageBean.getMsgObj();
        super.updateView();
        Log.e(TAG, "progress==" + pictureMsg.progress);
        this.path = FileConfig.DIR_CACHE + "/" + pictureMsg.imgName;
        File f = new File(path);
        if (f.exists())
            ImageLoader.loadTriangleImage(ivPicture, path, isLeftLayout() ? 0 : 1);
        else
            ThreadPoolUtil.execute(new Runnable() {
                @Override
                public void run() {
                    loadPicture();
                }
            });
    }

    @Override
    protected void onClickLayoutContainer() {
        super.onClickLayoutContainer();
    }

    private void loadPicture() {
        FileTransferManager fileTransferManager = new FileTransferManager();
        fileTransferManager.downloadFile(FileConfig.DIR_CACHE + "/" + pictureMsg.imgName, pictureMsg.size, new MyProgressListener(messageBean));

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
        public void transferFinished(boolean success) {
            if (success) {
                progress = 100;
            }
            updateMessage(progress);
        }

        private void updateMessage(int progress) {
            pictureMsg.progress = progress;
            messageBean.setMsg(pictureMsg.toJson());
            MessageDao.getInstance().update(messageBean);
        }
    }
}

