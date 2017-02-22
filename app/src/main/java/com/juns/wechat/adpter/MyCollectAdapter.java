package com.juns.wechat.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.chat.utils.SmileUtils;
import com.style.base.BaseRecyclerViewAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MyCollectAdapter extends BaseRecyclerViewAdapter {
    public MyCollectAdapter(Context mContext, List list) {
        super(mContext, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.adapter_my_collect, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final int pos = position;
        final ViewHolder holder = (ViewHolder) viewHolder;
        final String bean = (String) getData(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_avatar)
        ImageView ivAvatar;
        @Bind(R.id.tv_nike)
        TextView tvNike;
        @Bind(R.id.tv_content)
        TextView tvContent;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.iv_first)
        ImageView ivFirst;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}