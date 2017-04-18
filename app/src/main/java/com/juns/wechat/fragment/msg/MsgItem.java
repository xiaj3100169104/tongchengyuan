package com.juns.wechat.fragment.msg;

/**
 * Created by 王者 on 2016/8/8.
 */


import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.chat.bean.MessageBean;

/**
 * 最近联系人列表展示的item
 */
public class MsgItem{
    public MessageBean msg;
    public FriendBean friendBean;
    public UserBean user;
    public int unreadMsgCount;
}
