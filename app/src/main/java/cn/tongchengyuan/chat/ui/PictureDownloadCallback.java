package cn.tongchengyuan.chat.ui;

import cn.tongchengyuan.chat.bean.MessageBean;
import cn.tongchengyuan.chat.bean.PictureMsgData;

import com.style.event.EventCode;
import com.style.event.EventManager;
import com.style.net.image.FileDownloadCallback;

/**
 * Created by xiajun on 2017/4/7.
 */

public class PictureDownloadCallback extends FileDownloadCallback {
    private MessageBean messageBean;
    private int progress = 0;
    PictureMsgData pictureMsg;

    public PictureDownloadCallback(MessageBean messageBean) {
        this.messageBean = messageBean;
        pictureMsg = (PictureMsgData) messageBean.getMsgDataObj();
    }

    @Override
    public void inProgress(int fileSize, int progress, int percent) {
        this.progress = percent;
        if (percent % 10 == 0)//不要更新太频繁
            updateMessage(percent);
    }

    @Override
    public void complete(String filePath) {
        progress = 100;
        updateMessage(progress);
    }

    private void updateMessage(int progress) {
        pictureMsg.progress = progress;
        messageBean.setMsg(pictureMsg.toJson());
        EventManager.getDefault().post(EventCode.REFRESH_ONE_MESSAGE, messageBean);
    }
}
