package cn.tongchengyuan.chat.xmpp.iq;

import org.jivesoftware.smack.packet.IQ;

/**
 * Created by 王宗文 on 2016/6/29.
 */
public class MarkAsReadIQ extends IQ{
    public static final String ELEMENT = "read";
    public static final String NAME_SPACE = "xmpp:custom:read";

    private String packetId;

    public MarkAsReadIQ(String packetId) {
        super(ELEMENT, NAME_SPACE);
        this.packetId  = packetId;
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.attribute("packetId", packetId);
        xml.rightAngleBracket();
        return xml;
    }

}
