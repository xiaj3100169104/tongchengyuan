package cn.tongchengyuan.chat.xmpp.iq;

import cn.tongchengyuan.bean.UserBean;

import org.jivesoftware.smack.packet.IQ;

/**
 * Created by 王宗文 on 2016/6/29.
 */
public class IQUserInfo extends IQ{
    public static final String ELEMENT = "userinfo";
    public static final String NAME_SPACE = "xmpp:custom:userinfo";

    private UserBean userBean;
    private int errorCode;
    private String errorDetail;

    public IQUserInfo() {
        super(ELEMENT, NAME_SPACE);
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        if(userBean != null){
            if(getType() == Type.get){
                xml.attribute("userName", userBean.getUserName() == null ? "" : userBean.getUserName());
            }
        }
        xml.rightAngleBracket();
        return xml;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
