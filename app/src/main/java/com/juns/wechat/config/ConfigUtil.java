package com.juns.wechat.config;

import com.juns.wechat.BuildConfig;

/**
 * ****************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * <p/>
 * This file is part of YY Cube project.
 * <p/>
 * It can not be copied and/or distributed without the express
 * permission of Yunyun Network
 * Created by wangshuai on 2015/5/13
 * *****************************************************
 */
public class ConfigUtil {
    public static final String REAL_SERVER = "http://123.206.34.158:8088";
    public static final String REAL_API_URL = REAL_SERVER + "/wechat_server";

    public static final String DEBUG_SERVER = "http://192.168.199.54:8088";
    public static final String DEBUG_API_URL = DEBUG_SERVER + "/wechat_server";


    public static String REAL_XMPP_SERVER = "123.206.34.158";
    private static String DEBUG_XMPP_SERVER = "192.168.199.59";

    private final static String  DEBUG_XMPP_DOMAIN = "wangzhe";
    public final static String  REAL_XMPP_DOMAIN = "wangzhe";

    public static final String RESOURCE = "XMPP";

    public static boolean isDebug = BuildConfig.DEBUG;

    public static String getXmppServer(){
        if (isDebug){
            return DEBUG_XMPP_SERVER;
        }
        return REAL_XMPP_SERVER;
    }

    public static String getXmppDomain(){
        if (isDebug){
            return DEBUG_XMPP_DOMAIN;
        }
        return REAL_XMPP_DOMAIN;
    }

    public static String getXmppJid(String userName){
        if (isDebug){
            return userName + "@" + DEBUG_XMPP_DOMAIN + "/" + RESOURCE;
        }
        return userName + "@" + REAL_XMPP_DOMAIN+ "/" + RESOURCE;
    }

    public static String getBaseJid(String userName){
        if (isDebug){
            return userName + "@" + DEBUG_XMPP_DOMAIN;
        }
        return userName + "@" + REAL_XMPP_DOMAIN;
    }

    public static String getUserName(String userJid){
        int index = userJid.indexOf("@");
        if (index < 1){
            return null;
        }
        userJid = userJid.substring(0, index);
        if (isDebug){
            if (userJid.contains("office")){
                userJid = userJid.replace("office", "");
            }
        }
        return userJid;
    }
}
