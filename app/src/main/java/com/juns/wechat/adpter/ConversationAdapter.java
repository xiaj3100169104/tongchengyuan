package com.juns.wechat.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.juns.wechat.R;
import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.chat.utils.SmileUtils;
import com.juns.wechat.fragment.msg.MsgItem;
import com.juns.wechat.util.TimeUtil;
import com.juns.wechat.widget.swipe.SwipeLayout;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.manager.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 王者 on 2016/8/9.
 */
public class ConversationAdapter<T> extends BaseRecyclerViewAdapter<T> {

    public ConversationAdapter(Context context, List<T> list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_item_msg, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        MsgItem msgItem = (MsgItem) getData(position);
        FriendBean f = msgItem.friendBean;
        UserBean u = msgItem.user;
        if (u != null)
            ImageLoader.loadAvatar(mContext, holder.ivAvatar, u.getHeadUrl());
        if (f != null && u != null) {
            String showName = !TextUtils.isEmpty(f.getRemark()) ? f.getRemark() : u.getShowName();
            setText(holder.tvTitle, showName);
        }
        int unreadMsgCount = msgItem.unreadMsgCount;
        if(unreadMsgCount == 0){
            holder.tvUnreadMsgNumber.setVisibility(View.GONE);
        }else {
            holder.tvUnreadMsgNumber.setVisibility(View.VISIBLE);
            holder.tvUnreadMsgNumber.setText(unreadMsgCount + "");
        }
        setText(holder.tvContent, SmileUtils.getSmiledText(mContext, msgItem.msg.getTypeDesc()));
        setText(holder.tvTime, TimeUtil.getRecentTime(msgItem.msg.getDate()));
        super.setOnItemClickListener(holder, position);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_del)
        TextView txtDel;
        @Bind(R.id.layout_back)
        LinearLayout layoutBack;
        @Bind(R.id.iv_avatar)
        ImageView ivAvatar;
        @Bind(R.id.tv_unread_msg_number)
        TextView tvUnreadMsgNumber;
        @Bind(R.id.avatar_container)
        RelativeLayout avatarContainer;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.txt_state)
        TextView txtState;
        @Bind(R.id.tv_content)
        TextView tvContent;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.contact_item_layout)
        LinearLayout contactItemLayout;
        @Bind(R.id.swipe)
        SwipeLayout swipe;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
