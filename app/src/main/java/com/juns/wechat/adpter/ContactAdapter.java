package com.juns.wechat.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.util.ImageLoader;
import com.style.base.BaseRecyclerViewAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContactAdapter extends BaseRecyclerViewAdapter implements SectionIndexer {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private OnHeaderItemClickListener mHeaderListener;

    public ContactAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER)
            return new HeaderViewHolder(mInflater.inflate(R.layout.layout_head_friend, parent, false));
        return new ViewHolder(mInflater.inflate(R.layout.contact_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
            int count = (int) getData(0);
            if (count > 0) {
                headerViewHolder.tvUnreadInviteMsg.setVisibility(View.VISIBLE);
                headerViewHolder.tvUnreadInviteMsg.setText(count + "");
            } else {
                headerViewHolder.tvUnreadInviteMsg.setVisibility(View.GONE);
            }
            if (mHeaderListener != null) {
                headerViewHolder.layoutNewFriends.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mHeaderListener.onClickNewFriend();
                    }
                });
            }
        } else {
            ViewHolder holder = (ViewHolder) viewHolder;
            FriendBean friendBean = (FriendBean) getData(position);
            // 根据position获取分类的首字母的Char ascii值
            int section = getSectionForPosition(position);
            // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(section)) {
                holder.tvCatalog.setVisibility(View.VISIBLE);
                holder.tvCatalog.setText(friendBean.getSortLetters());
            } else {
                holder.tvCatalog.setVisibility(View.GONE);
            }

            ImageLoader.loadAvatar(holder.ivAvatar, friendBean.getHeadUrl());
            holder.tvNick.setText(friendBean.getShowName());
            super.setOnItemClickListener(holder, position);
        }
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return ((FriendBean) list.get(position)).getSortLetters().charAt(0);
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 1; i < getItemCount(); i++) {
            String sortStr = ((FriendBean) list.get(i)).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_catalog)
        TextView tvCatalog;
        @Bind(R.id.iv_avatar)
        ImageView ivAvatar;
        @Bind(R.id.tv_nick)
        TextView tvNick;
        @Bind(R.id.checkbox)
        CheckBox checkbox;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_unread_invite_msg)
        TextView tvUnreadInviteMsg;
        @Bind(R.id.layout_new_friends)
        RelativeLayout layoutNewFriends;
        @Bind(R.id.layout_chat_room)
        RelativeLayout layoutChatRoom;
        @Bind(R.id.layout_label)
        RelativeLayout layoutLabel;
        @Bind(R.id.layout_public)
        RelativeLayout layoutPublic;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnHeaderItemClickListener(OnHeaderItemClickListener mListener) {
        if (mListener != null)
            this.mHeaderListener = mListener;
    }

    public interface OnHeaderItemClickListener {
        void onClickNewFriend();
    }
}
