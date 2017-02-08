package com.juns.wechat.chat.im;

/**
 * Created by xiajun on 2017/1/20.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.activity.UserInfoActivity;
import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.chat.bean.MessageBean;
import com.juns.wechat.dao.FriendDao;
import com.juns.wechat.exception.UserNotFoundException;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.util.ImageLoader;
import com.juns.wechat.util.ThreadPoolUtil;
import com.juns.wechat.util.TimeUtil;
import com.juns.wechat.xmpp.util.SendMessage;
import com.style.constant.Skip;

import java.util.Date;
import java.util.List;

/**
 * 消息布局基类
 */
public abstract class BaseMsgViewHolder extends RecyclerView.ViewHolder {
    protected String TAG = getClass().getSimpleName();

    protected ImageView viewAvatar;
    protected TextView tvDate;
    protected RelativeLayout layoutContainer;
    protected ImageView ivSendState;
    protected ProgressBar sendingProgress;

    protected MessageBean messageBean;
    protected UserBean curUser;
    protected FriendBean friendBean;
    protected UserBean contactUser;

    protected Context context;
    private ViewGroup parent;

    protected abstract void updateView();

    public BaseMsgViewHolder(View view) {
        super(view);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        viewAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
        layoutContainer = (RelativeLayout) view.findViewById(R.id.rl_content_container);
        if (!isLeftLayout()) {
            ivSendState = (ImageView) view.findViewById(R.id.iv_send_failed);
            sendingProgress = (ProgressBar) view.findViewById(R.id.pb_sending);
        }

    }

    protected boolean isLeftLayout() {
        return true;
    }

    public void setData(MessageBean data) {
        this.messageBean = data;
        initUser();
    }

    private void initUser() {
        curUser = AccountManager.getInstance().getUser();
        friendBean = FriendDao.getInstance().findByOwnerAndContactName(curUser.getUserId(), messageBean.getOtherName());
        try {
            contactUser = friendBean.getContactUser();
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void updateCommonView(List list, final int position) {
        if (isShowTime(list, position, messageBean)) {
            tvDate.setVisibility(View.VISIBLE);
            tvDate.setText(TimeUtil.getRecentTime(messageBean.getDate()));
        } else {
            tvDate.setVisibility(View.GONE);
        }
        loadUrl();

        viewAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUserPhotoClick();
            }
        });
        if (messageBean.getDirection() == MessageBean.Direction.OUTGOING.value) {
            if (messageBean.getState() == MessageBean.State.SEND_FAILED.value) {
                ivSendState.setVisibility(View.VISIBLE);
                sendingProgress.setVisibility(View.GONE);
                ivSendState.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reSend();
                    }
                });
            } else if (messageBean.getState() == MessageBean.State.SEND_SUCCESS.value) {
                ivSendState.setVisibility(View.GONE);
                sendingProgress.setVisibility(View.GONE);
            } else {
                ivSendState.setVisibility(View.GONE);
                sendingProgress.setVisibility(View.VISIBLE);
            }
        }
        viewAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "avatar==" + position);
                onUserPhotoClick();
            }
        });
        layoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLayoutContainer();
            }
        });
    }

    protected void onClickLayoutContainer() {

    }

    private final long MSG_TIME_INTERVAL = 120000L; //两分钟

    /**
     * 是否显示时间
     *
     * @return
     */
    public boolean isShowTime(List list, int position, MessageBean msg) {
        long mMsgTime = msg.getDate().getTime();
        long mPrevMsgTime;
        if (0 == position) {
            mPrevMsgTime = 0;
        } else {
            MessageBean mPrevMsg = (MessageBean) list.get(position - 1);
            mPrevMsgTime = mPrevMsg.getDate().getTime();
        }
        return mMsgTime - mPrevMsgTime > MSG_TIME_INTERVAL;
    }

    public void loadUrl() {
        String url = curUser.getHeadUrl();
        if (isLeftLayout())
            url = contactUser.getHeadUrl();
        ImageLoader.loadAvatar(viewAvatar, url);
    }
    /**
     * 重发该条消息
     */
    public final void reSend() {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                SendMessage.sendMsgDirect(messageBean);
            }
        });
    }
    /**
     * 点击用户头像的事件
     */
    protected void onUserPhotoClick() {
        int userId = curUser.getUserId();
        if (isLeftLayout())
            userId = contactUser.getUserId();
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra(Skip.KEY_USER_ID, userId);
        context.startActivity(intent);
    }
    public void setContext(Context mContext, ViewGroup parent) {
        this.context = mContext;
        this.parent = parent;
    }
}
