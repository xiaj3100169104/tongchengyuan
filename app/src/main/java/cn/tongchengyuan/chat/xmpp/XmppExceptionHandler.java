package cn.tongchengyuan.chat.xmpp;

import cn.tongchengyuan.chat.xmpp.event.XmppEvent;
import cn.tongchengyuan.manager.AccountManager;
import cn.tongchengyuan.util.NetWorkUtil;
import cn.tongchengyuan.bean.UserBean;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.simple.eventbus.EventBus;

import java.io.IOException;

/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：各种XMPP异常的统一处理类
 *
 * @since 1.6
 * Created by 王宗文 on 2015/12/2
 *******************************************************/
public class XmppExceptionHandler {

    public static void handleSmackException(SmackException e){
        e.printStackTrace();
        if(e instanceof SmackException.NoResponseException || e instanceof SmackException.NotConnectedException
                || e instanceof SmackException.NotLoggedInException) {
            reLoginToXmpp();
        }
        XmppEvent xmppEvent = new XmppEvent(XmppEvent.FAILED, e);
        EventBus.getDefault().post(xmppEvent);
    }

    public static void handleXmppExecption(XMPPException e){
        e.printStackTrace();
        XmppEvent xmppEvent = new XmppEvent(XmppEvent.FAILED, e);
        EventBus.getDefault().post(xmppEvent);
    }

    public static void handleIOException(IOException e){
        e.printStackTrace();
        XmppEvent xmppEvent = new XmppEvent(XmppEvent.FAILED, e);
        EventBus.getDefault().post(xmppEvent);
    }

    private static long lastConnTime = 0;

    /**
     * 连接超时或者没有连接的情况下，如果在有网络的情况下，应该让其重新连接并登录。因为在抛出异常时，XMPP并不会自动重连
     */
    private static void reLoginToXmpp(){
        if(!NetWorkUtil.isNetworkAvailable()) return;
        long nowTime = System.currentTimeMillis();
        if(nowTime - lastConnTime < 20 * 1000){ //XMPP有自动重连功能，防止在这里重复连接
            return;
        }
        lastConnTime = nowTime;
        UserBean account = AccountManager.getInstance().getUser();
        XmppManagerUtil.asyncLogin(account.getUserName(), account.getPassWord());
    }
}
