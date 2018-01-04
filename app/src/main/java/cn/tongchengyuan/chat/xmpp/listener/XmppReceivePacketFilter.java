package cn.tongchengyuan.chat.xmpp.listener;

import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Stanza;

/*******************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：${TODO}<描述这个类是干什么的>
 *
 * @since ${TODO}<创建这个类时的版本日期>
 * Created by aa on 2015/11/19
 *******************************************************/
public class XmppReceivePacketFilter implements StanzaFilter {

    @Override
    public boolean accept(Stanza stanza) {
        return true;
    }
}
