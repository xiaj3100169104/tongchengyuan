package cn.tongchengyuan.chat.ui;

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

import cn.tongchengyuan.activity.UserInfoActivity;
import cn.tongchengyuan.chat.bean.MessageBean;
import cn.tongchengyuan.greendao.mydao.GreenDaoManager;
import cn.tongchengyuan.util.TimeUtil;
import cn.tongchengyuan.bean.UserBean;

import com.same.city.love.R;
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
    protected ImageView ivSendFailed;
    protected ProgressBar sendingProgress;
    protected TextView tvSendPercent;

    protected MessageBean messageBean;
    protected UserBean user;

    protected Context context;
    private ViewGroup parent;
    private int position;

    public BaseMsgViewHolder(View view) {
        super(view);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        viewAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
        layoutContainer = (RelativeLayout) view.findViewById(R.id.layout_content_container);
        if (!isLeftLayout()) {
            ivSendFailed = (ImageView) view.findViewById(R.id.iv_send_failed);
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
        String userId;
        if (isLeftLayout())
            userId = messageBean.getOtherUserId();
        else userId = messageBean.getMyUserId();
        this.user = GreenDaoManager.getInstance().findByUserId(userId);

        if (isShowTime(list, position, messageBean)) {
            tvDate.setVisibility(View.VISIBLE);
            tvDate.setText(TimeUtil.getRecentTime(messageBean.getDate()));
        } else {
            tvDate.setVisibility(View.GONE);
        }
    }

    protected void updateView() {
        if (user != null)
            ImageLoader.loadAvatar(context, viewAvatar, user.getHeadUrl());

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
        layoutContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onLongClickLayoutContainer();
                return true;
            }
        });

        if (!isLeftLayout()) {
            tvSendPercent.setVisibility(View.GONE);//默认隐藏百分比,文件消息才需要显示
            if (messageBean.getState() == MessageBean.State.SEND_FAILED) {
                onSendFailed();
            } else if (messageBean.getState() == MessageBean.State.SEND_SUCCESS) {
                onSendSucceed();
            } else {
                onSending();
            }
        }

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

    /**
     * 点击用户头像的事件
     */
    protected void onUserPhotoClick() {
        if (user!=null){
            Intent intent = new Intent(context, UserInfoActivity.class);
            intent.putExtra(Skip.KEY_USER_ID, user.userId);
            context.startActivity(intent);
        }
    }

    protected void onSending() {
        ivSendFailed.setVisibility(View.GONE);
        sendingProgress.setVisibility(View.VISIBLE);
    }

    protected void onSendSucceed() {
        ivSendFailed.setVisibility(View.GONE);
        sendingProgress.setVisibility(View.GONE);
        tvSendPercent.setVisibility(View.GONE);
    }

    protected void onSendFailed() {
        ivSendFailed.setVisibility(View.VISIBLE);
        sendingProgress.setVisibility(View.GONE);
        tvSendPercent.setVisibility(View.GONE);
        ivSendFailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reSend();
            }
        });
    }

    /**
     * 重发该条消息
     */
    public void reSend() {
        Log.e(TAG, "reSend");
        ChatActivity chatActivity = (ChatActivity) context;
        chatActivity.reSend(messageBean);
    }

    protected void onClickLayoutContainer() {
        Log.e(TAG, "onClickLayoutContainer");

    }

    protected void onLongClickLayoutContainer() {
        Log.e(TAG, "onLongClickLayoutContainer");
        ChatActivity chatActivity = (ChatActivity) context;
        chatActivity.delete(messageBean);
    }
}
