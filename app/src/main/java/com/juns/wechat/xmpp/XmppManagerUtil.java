package com.juns.wechat.xmpp;

import com.juns.wechat.util.LogUtil;
import com.juns.wechat.util.ThreadPoolUtil;
import com.juns.wechat.xmpp.bean.SearchResult;
import com.juns.wechat.xmpp.listener.XmppManagerListener;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


/**
 * Created by 王宗文 on 2016/6/12.
 */
public class XmppManagerUtil {
    private static final XmppManager XMPP_MANAGER = XmppManagerImpl.getInstance();

    public static boolean login(String userName, String passWord){
        return XMPP_MANAGER.login(userName, passWord);
    }

    public static void asyncLogin(final String userName, final String passWord){
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                login(userName, passWord);
            }
        });
    }

    public static void search(final String name){
        Future<List<SearchResult>> future = ThreadPoolUtil.submit(new Callable<List<SearchResult>>() {
            @Override
            public List<SearchResult> call() throws Exception {
                return XMPP_MANAGER.searchUser(name);
            }
        });
        try {
            List<SearchResult> searchResults = future.get();
            ;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


}
