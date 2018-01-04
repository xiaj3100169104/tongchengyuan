package cn.tongchengyuan.chat.xmpp.provider;

import cn.tongchengyuan.bean.UserBean;
import cn.tongchengyuan.chat.xmpp.iq.IQUserInfo;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by 王宗文 on 2016/6/29.
 */
public class UserInfoProvider extends IQProvider<IQUserInfo> {
    public static final UserInfoProvider INSTANCE = new UserInfoProvider();

    @Override
    public IQUserInfo parse(XmlPullParser parser, int initialDepth) throws XmlPullParserException, IOException, SmackException {
        int eventType = parser.getEventType();
        IQUserInfo iqUserInfo = new IQUserInfo();
        UserBean userBean = null;
        while (eventType != XmlPullParser.END_DOCUMENT){
            if(eventType == XmlPullParser.START_TAG){
                if(parser.getNamespace().equals(IQUserInfo.NAME_SPACE)){
                    userBean = new UserBean();
/*                    userBean.setUserName(parser.getAttributeValue(null, UserBean.USERNAME));
                    userBean.setNickName(parser.getAttributeValue(null, UserBean.NICKNAME));
                    userBean.setHeadUrl(parser.getAttributeValue(null, UserBean.HEADURL));
                    userBean.setBirthday(parser.getAttributeValue(null, UserBean.BIRTHDAY));
                    userBean.setLocation(parser.getAttributeValue(null, UserBean.LOCATION));
                    userBean.setSex( parser.getAttributeValue(null, UserBean.SEX));
                    userBean.setSignature(parser.getAttributeValue(null, UserBean.SIGNATURE));
                    userBean.setTelephone(parser.getAttributeValue(null, UserBean.TELEPHONE));
                    userBean.setType(parser.getAttributeValue(null, UserBean.TYPE));*/
                }else if(parser.getName().equals("error")){
                    int errorCode = Integer.parseInt(parser.getAttributeValue(null, "code"));
                    String errorDetail = parser.nextText();
                    iqUserInfo.setErrorCode(errorCode);
                    iqUserInfo.setErrorDetail(errorDetail);
                }
            }
            eventType = parser.next();
        }
        iqUserInfo.setUserBean(userBean);
        return iqUserInfo;
    }
}
