package cn.tongchengyuan.chat.xmpp.listener;

import cn.tongchengyuan.chat.xmpp.bean.SearchResult;

import java.util.ArrayList;

/**
 * Created by 王宗文 on 2016/6/12.
 */
public interface XmppManagerListener {

    void onLoginSuccess();
    void onLoginFailed(Exception e);
    void onRegisterSuccess();
    void onRegisterFailed(Exception e);
    void onSearchSuccess(ArrayList<SearchResult> searchResults);
    void onSearchFailed(Exception e);
}
