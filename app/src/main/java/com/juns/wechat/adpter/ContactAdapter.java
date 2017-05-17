package com.juns.wechat.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.same.city.love.R;
import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.UserBean;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.manager.ImageLoader;
import com.style.view.CustomNotifyView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContactAdapter extends BaseRecyclerViewAdapter<FriendBean> implements SectionIndexer {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private OnHeaderItemClickListener mHeaderListener;
    private int unReadCount = 0;

    public ContactAdapter(Context context, List<FriendBean> list) {
        super(context, list);
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
        notifyItemChanged(0);
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
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
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int layoutPosition) {
        if (getItemViewType(layoutPosition) == TYPE_HEADER) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
            int count = getUnReadCount();
            if (count > 0) {
                headerViewHolder.tvUnreadInviteMsg.setVisibility(View.VISIBLE);
                headerViewHolder.tvUnreadInviteMsg.setNotifyCount(count);
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
            int position = layoutPosition - 1;
            FriendBean friendBean = getData(position);
            // 根据position获取分类的首字母的Char ascii值
            int section = getSectionForPosition(position);
            // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(section)) {
                holder.tvCatalog.setVisibility(View.VISIBLE);
                holder.tvCatalog.setText(friendBean.getSortLetters());
            } else {
                holder.tvCatalog.setVisibility(View.GONE);
            }
            UserBean u = friendBean.contactUser;
            ImageLoader.loadAvatar(mContext, holder.ivAvatar, u.getHeadUrl());
            String showName = !TextUtils.isEmpty(friendBean.getRemark()) ? friendBean.getRemark() : u.getShowName();
            setText(holder.tvNick, showName);

            super.setOnItemClickListener(holder, position);
        }
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return getList().get(position).getSortLetters().charAt(0);
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getList().size(); i++) {
            String sortStr = getList().get(i).getSortLetters();
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
        @Bind(R.id.view_notify_msg)
        CustomNotifyView tvUnreadInviteMsg;
        @Bind(R.id.layout_new_friends)
        RelativeLayout layoutNewFriends;

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
