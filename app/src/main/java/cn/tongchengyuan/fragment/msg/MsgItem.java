package cn.tongchengyuan.fragment.msg;

/**
 * Created by 王者 on 2016/8/8.
 */


import cn.tongchengyuan.bean.FriendBean;
import cn.tongchengyuan.bean.UserBean;
import cn.tongchengyuan.chat.bean.MessageBean;

/**
 * 最近联系人列表展示的item
 */
public class MsgItem{
    public MessageBean msg;
    public FriendBean friendBean;
    public UserBean user;
    public int unreadMsgCount;
}
