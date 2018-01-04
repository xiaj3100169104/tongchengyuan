package cn.tongchengyuan.chat.xmpp;

import cn.tongchengyuan.chat.config.ConfigUtil;
import cn.tongchengyuan.chat.xmpp.extensionelement.TimeElement;
import cn.tongchengyuan.chat.xmpp.iq.FileTransferIQ;
import cn.tongchengyuan.chat.xmpp.listener.FileTransferListenerImpl;
import cn.tongchengyuan.chat.xmpp.process.IQRouter;
import cn.tongchengyuan.chat.xmpp.process.IQUserInfoProcessor;
import cn.tongchengyuan.chat.xmpp.provider.FileTransferProvider;
import cn.tongchengyuan.chat.xmpp.provider.TimeProvider;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.delay.packet.DelayInformation;
import org.jivesoftware.smackx.delay.provider.DelayInformationProvider;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.ping.android.ServerPingWithAlarmManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;


/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：初始化XmppConnection 的各种配置
 *
 * @since 1.6
 * Created by 王宗文 on 2015/11/19
 *******************************************************/
public class XmppConnUtil {
    private static AbstractXMPPConnection xmppConnection;
    private static final int SERVER_PORT = 5222;
    public static String SERVER_NAME = ConfigUtil.getXmppDomain();
    private static final String SERVER_HOST = ConfigUtil.getXmppServer();
    private static final int CONN_TIME_OUT = 10 * 1000;
    private static final String RESOURCE = ConfigUtil.RESOURCE;

    public static AbstractXMPPConnection getXmppConnection(){
        if(xmppConnection == null){
            synchronized (XmppConnUtil.class){
                initConnection();
            }
        }
        return xmppConnection;
    }

    private static void initConnection() {
        XMPPTCPConnectionConfiguration configuration = XMPPTCPConnectionConfiguration.builder().setConnectTimeout(CONN_TIME_OUT).
                setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setServiceName(SERVER_NAME).setHost(SERVER_HOST).setPort(SERVER_PORT)
                .setResource(RESOURCE).setDebuggerEnabled(true)
                .setSendPresence(false).build();
        xmppConnection = new XMPPTCPConnection(configuration);

        /** 自动向服务器发送ping pong 消息，保持socket连接*/
        ServerPingWithAlarmManager.getInstanceFor(xmppConnection).setEnabled(true);
        DeliveryReceiptManager.getInstanceFor(xmppConnection).autoAddDeliveryReceiptRequests();
        enableAutoReconnect(true);

        initProvider();
        initIQProcessor();
    }

    /**
     * 是否开启自动重连机制
     * @param reConnect
     */
    public static void enableAutoReconnect(boolean reConnect){
        if(xmppConnection == null){
            synchronized (XmppConnUtil.class){
                initConnection();
            }
        }
        if(reConnect){
           ReconnectionManager.getInstanceFor(xmppConnection).enableAutomaticReconnection();
        }else {
           ReconnectionManager.getInstanceFor(xmppConnection).disableAutomaticReconnection();
        }
    }

    public static void shutDownConn(){
        if(xmppConnection != null){
            xmppConnection.disconnect();
            enableAutoReconnect(false);
            xmppConnection = null;
        }
    }

    private static void initProvider() {
        ProviderManager.addExtensionProvider(DelayInformation.ELEMENT, DelayInformation.NAMESPACE,
               DelayInformationProvider.INSTANCE);

        ProviderManager.addExtensionProvider(TimeElement.ELEMENT, TimeElement.NAME_SPACE,
               TimeProvider.INSTANCE);

        FileTransferManager.getInstanceFor(xmppConnection).addFileTransferListener(new FileTransferListenerImpl());

        ProviderManager.addIQProvider(FileTransferIQ.ELEMENT, FileTransferIQ.NAMESPACE, FileTransferProvider.INSTANCE);
    }

    private static void initIQProcessor(){
        IQRouter.getInstance().addIQProcessor(new IQUserInfoProcessor());
    }

}
