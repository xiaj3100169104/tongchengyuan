package cn.tongchengyuan.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import cn.tongchengyuan.manager.AccountManager;
import cn.tongchengyuan.service.XmppService;
import cn.tongchengyuan.util.LogUtil;
import cn.tongchengyuan.chat.xmpp.XmppConnUtil;

/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：监听各种广播的类：网络状况变化，开机，解锁
 *
 * @since 1.6
 * Created by aa on 2015/11/20
 *******************************************************/
public class WeChatBroadCastReceiver extends BroadcastReceiver {;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogUtil.i("action : " + action);
        if(!AccountManager.getInstance().isLogin()){
            return;
        }
        if(action.equals(Intent.ACTION_BOOT_COMPLETED) || action.equals(Intent.ACTION_USER_PRESENT)){
            loginToXmpp(context);
        }else if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            onNetChanged(context);
        }
    }

    /**
     * 告诉{@link XmppService}登录
     */
    private void loginToXmpp(Context context){
        Intent intent = new Intent(context, XmppService.class);
        intent.setAction(XmppService.ACTION_LOGIN);
        context.startService(intent);
    }

    /**
     * 处理网络变化事件
     * @param context
     */
    private void onNetChanged(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null){ //没有网络的情况下
            XmppConnUtil.enableAutoReconnect(false);  //去掉自动重连
        }else{
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                XmppConnUtil.enableAutoReconnect(true); //开启xmpp重连机制
                loginToXmpp(context); //掉线的用户让其登录
            }
        }
    }
}
