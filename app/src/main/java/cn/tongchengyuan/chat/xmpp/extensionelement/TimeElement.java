package cn.tongchengyuan.chat.xmpp.extensionelement;

import cn.tongchengyuan.util.TimeUtil;

import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Stanza;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by 王宗文 on 2016/8/2.
 */
public class TimeElement implements ExtensionElement{
    public static final String ELEMENT = "time";
    public static final String NAME_SPACE = "xmpp:custom:time";

    public static final TimeElement INSTANCE = new TimeElement();

    private String utc = TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT, Locale.getDefault());
    private Date time;

    @Override
    public String getNamespace() {
        return NAME_SPACE;
    }

    @Override
    public String getElementName() {
        return ELEMENT;
    }

    @Override
    public CharSequence toXML() {
        return "<time xmlns = '" + NAME_SPACE + "' utc='" + utc + "'>" + TimeUtil.format(time, true) + "</time>";
    }

    public static TimeElement from(Stanza packet){
        return packet.getExtension(ELEMENT, NAME_SPACE);
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getUtc() {
        return utc;
    }

    public void setUtc(String utc) {
        this.utc = utc;
    }
}
