package cn.tongchengyuan.chat.xmpp;

import cn.tongchengyuan.chat.xmpp.bean.SearchResult;
import cn.tongchengyuan.util.ThreadPoolUtil;

import org.jivesoftware.smack.packet.Stanza;

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

    public static void sendPacket(final Stanza packet){
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                XMPP_MANAGER.sendPacket(packet);
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
