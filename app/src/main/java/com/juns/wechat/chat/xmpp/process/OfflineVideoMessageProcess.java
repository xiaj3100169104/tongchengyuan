package com.juns.wechat.chat.xmpp.process;

import android.content.Context;

import com.juns.wechat.chat.bean.MessageBean;
import com.juns.wechat.chat.bean.OfflineVideoMsg;
import com.juns.wechat.chat.xmpp.util.FileTransferManager;
import com.style.constant.FileConfig;


/*******************************************************
 * Created by 王宗文 on 2015/11/27
 *******************************************************/
public class OfflineVideoMessageProcess extends MessageProcess {

    public OfflineVideoMessageProcess(Context context) {
        super(context);
    }

    /**
     * 先下载图片，无论下载成功还是失败都会保存进数据库
     *
     * @param messageBean
     */
    @Override
    public void processMessage(MessageBean messageBean) {
        OfflineVideoMsg pictureMsg = (OfflineVideoMsg) messageBean.getMsgObj();
        FileTransferManager fileTransferManager = new FileTransferManager();
        fileTransferManager.downloadFile(FileConfig.DIR_CACHE + "/" + pictureMsg.fileName, pictureMsg.size, new MyProgressListener(messageBean));
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
        }

        @Override
        public void transferFinished() {
            progress = 100;
            saveMessage(progress);
            noticeShow(messageBean);
        }

        @Override
        public void onFailed() {

        }

        private void saveMessage(int progress) {
            OfflineVideoMsg pictureMsg = (OfflineVideoMsg) messageBean.getMsgObj();
            pictureMsg.progress = progress;
            messageBean.setMsg(pictureMsg.toJson());
            saveMessageToDB(messageBean);
        }
    }
}
