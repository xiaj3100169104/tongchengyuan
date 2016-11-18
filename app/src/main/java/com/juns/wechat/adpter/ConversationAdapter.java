package com.juns.wechat.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.fragment.msg.MsgItemShow;
import com.juns.wechat.widget.swipe.SwipeLayout;
import com.style.base.BaseRecyclerViewAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 王者 on 2016/8/9.
 */
public class ConversationAdapter extends BaseRecyclerViewAdapter {

    public ConversationAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateItem(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_item_msg, parent, false));
    }

    @Override
    public void onBindItem(RecyclerView.ViewHolder viewHolder, int position, Object data) {
        ViewHolder holder = (ViewHolder) viewHolder;
        MsgItemShow msgItemShow = (MsgItemShow) data;
        msgItemShow.loadUrl(holder.ivAvatar);
        msgItemShow.showUnreadMsgNumber(holder.tvUnreadMsgNumber);
        msgItemShow.showTitle(holder.tvTitle);
        msgItemShow.showContent(holder.tvContent);
        msgItemShow.showTime(holder.tvTime);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
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
        @Bind(R.id.contactitem_layout)
        LinearLayout contactitemLayout;
        @Bind(R.id.swipe)
        SwipeLayout swipe;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
