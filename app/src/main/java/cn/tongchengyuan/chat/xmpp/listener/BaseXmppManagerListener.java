package cn.tongchengyuan.chat.xmpp.listener;

import cn.tongchengyuan.chat.xmpp.bean.SearchResult;

import java.util.ArrayList;

/**
 * Created by 王宗文 on 2016/6/13.
 */
public  class BaseXmppManagerListener implements XmppManagerListener {
    @Override
    public void onLoginSuccess() {

    }

    @Override
    public void onLoginFailed(Exception e) {

    }

    @Override
    public void onRegisterSuccess() {

    }

    @Override
    public void onRegisterFailed(Exception e) {

    }

    @Override
    public void onSearchSuccess(ArrayList<SearchResult> searchResults) {

    }

    @Override
    public void onSearchFailed(Exception e) {

    }
}
