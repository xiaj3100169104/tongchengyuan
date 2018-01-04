package cn.tongchengyuan.chat.xmpp.process;

import cn.tongchengyuan.chat.xmpp.iq.IQUserInfo;

import org.jivesoftware.smack.packet.IQ;

/**
 * Created by 王宗文 on 2016/6/30.
 */
public class IQUserInfoProcessor extends IQProcessor {
    private static final String element = IQUserInfo.ELEMENT;
    private static final String nameSpace = IQUserInfo.NAME_SPACE;

    public IQUserInfoProcessor() {
        super(element, nameSpace);
    }

    @Override
    public void processIQ(IQ packet) {

    }
}
