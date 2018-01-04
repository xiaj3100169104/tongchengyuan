package cn.tongchengyuan.chat.ui;

import cn.tongchengyuan.chat.bean.MessageBean;
import cn.tongchengyuan.chat.bean.OfflineVideoMsgData;

import com.style.event.EventCode;
import com.style.event.EventManager;
import com.style.net.image.FileDownloadCallback;

/**
 * Created by xiajun on 2017/4/7.
 */

public class OfflineVideoDownloadCallback extends FileDownloadCallback {
    private MessageBean messageBean;
    private int progress = 0;
    OfflineVideoMsgData pictureMsg;

    public OfflineVideoDownloadCallback(MessageBean messageBean) {
        this.messageBean = messageBean;
        pictureMsg = (OfflineVideoMsgData) messageBean.getMsgDataObj();
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
