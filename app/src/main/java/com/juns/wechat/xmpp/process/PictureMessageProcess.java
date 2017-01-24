package com.juns.wechat.xmpp.process;

import android.content.Context;

import com.juns.wechat.chat.bean.MessageBean;
import com.juns.wechat.chat.bean.PictureMsg;
import com.juns.wechat.xmpp.util.FileTransferManager;


/*******************************************************
 * Created by 王宗文 on 2015/11/27
 *******************************************************/
public class PictureMessageProcess extends MessageProcess {

    public PictureMessageProcess(Context context) {
        super(context);
    }

    /**
     * 先下载图片，无论下载成功还是失败都会保存进数据库
     * @param messageBean
     */
    @Override
    public void processMessage(MessageBean messageBean) {
        PictureMsg pictureMsg = (PictureMsg) messageBean.getMsgObj();
        FileTransferManager fileTransferManager = new FileTransferManager();
        fileTransferManager.downloadFile(pictureMsg.imgName, pictureMsg.size, new MyProgressListener(messageBean));
    }

    class MyProgressListener implements FileTransferManager.ProgressListener{
        private MessageBean messageBean;
        private int progress = 0;
        public MyProgressListener(MessageBean messageBean){
            this.messageBean = messageBean;
        }

        @Override
        public void progressUpdated(int progress) {
            this.progress = progress;
        }

        @Override
        public void transferFinished(boolean success) {
            if(success){
                progress = 100;
            }
            saveMessage(progress);
            noticeShow(messageBean);
        }

        private void saveMessage(int progress){
            PictureMsg pictureMsg = (PictureMsg) messageBean.getMsgObj();
            pictureMsg.progress = progress;
            messageBean.setMsg(pictureMsg.toJson());
            saveMessageToDB(messageBean);
        }
    }
}
