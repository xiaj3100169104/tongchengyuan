package com.juns.wechat.chat.im;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.juns.wechat.activity.UserInfoActivity;
import com.juns.wechat.bean.Flag;
import com.juns.wechat.chat.bean.MessageBean;
import com.juns.wechat.dao.MessageDao;
import com.juns.wechat.util.ImageLoader;
import com.juns.wechat.xmpp.util.SendMessage;
import com.style.constant.Skip;

/*******************************************************
 * Created by 王者 on 2015/11/30
 *******************************************************/
public abstract class MsgViewModel implements Comparable<MsgViewModel> {
    protected MessageDao messageDao;
    protected MessageBean messageBean;
    protected String myselfName;
    protected String otherName;
    protected String myselfAvatar;
    protected String otherAvatar;
    protected Context mContext;
    protected LayoutInflater mInflater;

    private long mMsgTime, mPrevMsgTime;
    private final long MSG_TIME_INTERVAL = 120000L; //两分钟
    protected int myselfuserId;
    protected int otherUserId;

    public MsgViewModel(Context context, MessageBean messageBean) {
        messageDao = MessageDao.getInstance();
        mContext = context;
        mInflater = LayoutInflater.from(context);

        this.messageBean = messageBean;
        mMsgTime = messageBean.getDate().getTime();
    }

    /**
     * 在初始化该类及其子类后，应该马上调用这个方法设置必须值
     */
    public final void setInfo(int myselfuserId, String myselfName, String myselfAvatar, int otherUserId, String otherName, String otherAvatar) {
        this.myselfuserId = myselfuserId;
        this.myselfName = myselfName;
        this.myselfAvatar = myselfAvatar;
        this.otherUserId = otherUserId;
        this.otherName = otherName;
        this.otherAvatar = otherAvatar;
    }

    /**
     * 是否显示自己，用来判断消失是在左边还是右边显示
     *
     * @return 显示自己返回true
     */
    public final boolean isShowMyself() {
        int direction = messageBean.getDirection();
        return direction == MessageBean.Direction.OUTGOING.value;
    }

    /**
     * 返回messageEntity的Id
     *
     * @return
     */
    public final int getId() {
        return messageBean.getId();
    }

    /**
     * 是否显示时间
     *
     * @return
     */
    public final boolean isShowTime() {
        return mMsgTime - mPrevMsgTime > MSG_TIME_INTERVAL;
    }

    /**
     * 加载用户头像
     *
     * @param icon
     * @param url
     */
    public void loadUrl(ImageView icon, String url) {
        ImageLoader.loadAvatar(icon, url);
    }

    public abstract View fillView(int position, View convertView, ViewGroup viewGroup);

    /**
     * 将该条消息标记为已读
     */
    public final void markAsRead() {
        if (messageBean.getDirection() == MessageBean.Direction.OUTGOING.value) {
            return;
        }
        if (messageBean.getState() != MessageBean.State.NEW.value) {
            return;
        }
        messageBean.setState(MessageBean.State.READ.value);
        messageDao.update(messageBean);
    }

    /**
     * 返回该viewModel的类型，adapter根据类型来正确的展示
     *
     * @return
     * @see ChatAdapter#getItemViewType(int)
     */
    public abstract int getType();

    @Override
    public int compareTo(MsgViewModel another) {
        int myId = getId();
        int anotherId = another.getId();
        return myId == anotherId ? 0 : (myId > anotherId ? 1 : -1);
    }

    /**
     * 删除该条消息
     */
    public final void delete() {
        messageBean.setFlag(Flag.INVALID.value());
        messageDao.update(messageBean);
    }


    public final void setPrevMsgTime(long prevMsgTime) {
        mPrevMsgTime = prevMsgTime;
    }

    public final long getPrevMsgTime() {
        return mPrevMsgTime;
    }

    public final long getMsgTime() {
        return mMsgTime;
    }

}
