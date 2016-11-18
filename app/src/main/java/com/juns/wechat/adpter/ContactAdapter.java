package com.juns.wechat.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.common.PingYinUtil;
import com.juns.wechat.util.ImageLoader;
import com.style.base.BaseRecyclerViewAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContactAdapter extends BaseRecyclerViewAdapter implements SectionIndexer {
    public ContactAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateItem(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.contact_item, parent, false));
    }

    @Override
    public void onBindItem(RecyclerView.ViewHolder viewHolder, int position, Object data) {
        ViewHolder holder = (ViewHolder) viewHolder;
        FriendBean friendBean = (FriendBean) data;
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
        for (int i = 0; i < getDataSize(); i++) {
            String sortStr = ((FriendBean) list.get(i)).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
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
}
