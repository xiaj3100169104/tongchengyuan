package com.juns.wechat.xmpp.listener;

import com.juns.wechat.xmpp.bean.SearchResult;

import java.util.ArrayList;
import java.util.List;

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
