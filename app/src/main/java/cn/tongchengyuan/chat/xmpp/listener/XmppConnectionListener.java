package cn.tongchengyuan.chat.xmpp.listener;


import android.text.TextUtils;

import cn.tongchengyuan.greendao.mydao.GreenDaoManager;
import cn.tongchengyuan.manager.AccountManager;
import cn.tongchengyuan.util.LogUtil;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.StreamError;

/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：
 *
 * @since 1.6
 * Created by 王宗文 on 2015/11/19
 *******************************************************/
public class XmppConnectionListener implements ConnectionListener {
    @Override
    public void connected(XMPPConnection connection) {}

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
    }

    @Override
    public void connectionClosed() {
        LogUtil.i("connectionClosed!");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        if(e != null && !TextUtils.isEmpty(e.getMessage())){
            LogUtil.i("connectionClose!OnError : " + e.getMessage());
        }

        GreenDaoManager.getInstance().markAsSendFailed(AccountManager.getInstance().getUserName());

        if(e instanceof XMPPException.StreamErrorException){
            XMPPException.StreamErrorException exception = (XMPPException.StreamErrorException) e;
            StreamError streamError = exception.getStreamError();
            StreamError.Condition condition = streamError.getCondition();
            if(condition.toString().equals("conflict")){
                AccountManager.getInstance().logOut();
            }
        }
    }

    @Override
    public void reconnectionSuccessful() {

    }

    @Override
    public void reconnectingIn(int seconds) {

    }

    @Override
    public void reconnectionFailed(Exception e) {

    }
}
