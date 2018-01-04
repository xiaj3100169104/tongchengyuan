package cn.tongchengyuan.chat.xmpp.process;

import android.content.Context;

import cn.tongchengyuan.chat.bean.MessageBean;

/*******************************************************
 * Created by 王者 on 2015/11/27
 *******************************************************/
public class UnknownTypeMessageProcess extends MessageProcess {

    public UnknownTypeMessageProcess(Context context) {
        super(context);
    }

    @Override
    public void processMessage(MessageBean messageBean) {
        super.processMessage(messageBean);
    }
}
