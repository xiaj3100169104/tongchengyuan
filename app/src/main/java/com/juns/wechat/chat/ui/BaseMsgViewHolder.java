package com.juns.wechat.chat.ui;

/**
 * Created by xiajun on 2017/1/20.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.juns.wechat.database.dao.FriendDao;
import com.juns.wechat.exception.UserNotFoundException;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.util.ThreadPoolUtil;
import com.juns.wechat.util.TimeUtil;
import com.juns.wechat.chat.xmpp.util.SendMessage;
import com.style.constant.Skip;
import com.style.manager.ImageLoader;

import java.util.List;

/**
 * 消息布局基类
 */
public abstract class BaseMsgViewHolder extends RecyclerView.ViewHolder {
    protected String TAG = getClass().getSimpleName();

    protected ImageView viewAvatar;
    protected TextView tvDate;
    protected ViewGroup layoutContainer;
    protected ImageView ivSendState;
    protected ProgressBar sendingProgress;
    protected TextView tvSendPercent;

    protected MessageBean messageBean;
    protected UserBean curUser;
    protected FriendBean friendBean;
    protected UserBean contactUser;

    protected Context context;
    private ViewGroup parent;
    private int position;

    public BaseMsgViewHolder(View view) {
        super(view);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        viewAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
        layoutContainer = (RelativeLayout) view.findViewById(R.id.layout_content_container);
        if (!isLeftLayout()) {
            ivSendState = (ImageView) view.findViewById(R.id.iv_send_failed);
            sendingProgress = (ProgressBar) view.findViewById(R.id.sending_progress);
            tvSendPercent = (TextView) view.findViewById(R.id.sending_percentage);

        }

    }

    protected boolean isLeftLayout() {
        return true;
    }
    public void setContext(Context mContext, ViewGroup parent) {
        this.context = mContext;
        this.parent = parent;
    }

    public void setData(MessageBean data, List list, final int position) {
        this.messageBean = data;
        this.position = position;
        initUser();
        if (isShowTime(list, position, messageBean)) {
            tvDate.setVisibility(View.VISIBLE);
            tvDate.setText(TimeUtil.getRecentTime(messageBean.getDate()));
        } else {
            tvDate.setVisibility(View.GONE);
        }
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

    protected void updateView() {

        loadUrl();
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

        if (messageBean.getDirection() == MessageBean.Direction.OUTGOING.value) {
            tvSendPercent.setVisibility(View.GONE);//默认隐藏百分比

            if (messageBean.getState() == MessageBean.State.SEND_FAILED.value) {
                onSendSucceed();
            } else if (messageBean.getState() == MessageBean.State.SEND_SUCCESS.value) {
                onSendFailed();
            } else {
                onSendOtherStatus();
            }
        }

    }

    protected void onClickLayoutContainer() {
        Log.e(TAG, "onClickLayoutContainer");
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
        ImageLoader.loadAvatar(context, viewAvatar, url);
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

    /**
     * 发送成功
     */
    protected void onSendSucceed() {
        ivSendState.setVisibility(View.VISIBLE);
        sendingProgress.setVisibility(View.GONE);
        ivSendState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reSend();
            }
        });
    }
    /**
     * 发送失败
     */
    protected void onSendFailed() {
        ivSendState.setVisibility(View.GONE);
        sendingProgress.setVisibility(View.GONE);
    }
    /**
     * 其他发送状态
     */
    protected void onSendOtherStatus() {
        ivSendState.setVisibility(View.GONE);
        sendingProgress.setVisibility(View.VISIBLE);
    }
}
