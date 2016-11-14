package com.juns.wechat.xmpp.provider;

import com.juns.wechat.util.TimeUtil;
import com.juns.wechat.xmpp.extensionelement.TimeElement;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by 王宗文 on 2016/6/29.
 */
public class TimeProvider extends ExtensionElementProvider<TimeElement>{
    public static final TimeProvider INSTANCE = new TimeProvider();

    @Override
    public TimeElement parse(XmlPullParser parser, int initialDepth) throws XmlPullParserException, IOException, SmackException {
        TimeElement time = new TimeElement();
        time.setUtc(parser.getAttributeValue(null, "utc"));
        time.setTime(TimeUtil.format(parser.nextText(), true));
        return time;
    }
}
