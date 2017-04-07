package com.juns.wechat.chat.ui;

import com.juns.wechat.chat.bean.MessageBean;
import com.juns.wechat.chat.bean.PictureMsg;
import com.juns.wechat.chat.xmpp.util.FileTransferManager;
import com.juns.wechat.database.dao.DbDataEvent;
import com.juns.wechat.database.dao.MessageDao;
import com.style.constant.FileConfig;

/**
 * Created by xiajun on 2017/4/7.
 */

public class PictureDownloadTask implements Runnable {
    private final String url;
    private final int fileSize;
    private final MessageBean messageBean;

    public PictureDownloadTask(String url, int fileSize, MessageBean messageBean) {
        this.url = url;
        this.fileSize = fileSize;
        this.messageBean = messageBean;

    }

    @Override
    public void run() {
        FileTransferManager fileTransferManager = new FileTransferManager();
        fileTransferManager.downloadFile(url, fileSize, new MyProgressListener(messageBean));
    }


    class MyProgressListener implements FileTransferManager.ProgressListener {
        private MessageBean messageBean;
        private int progress = 0;
        PictureMsg pictureMsg;
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
            MessageDao.getInstance().postDataChangedEvent(DbDataEvent.UPDATE, messageBean);
        }
    }
}
