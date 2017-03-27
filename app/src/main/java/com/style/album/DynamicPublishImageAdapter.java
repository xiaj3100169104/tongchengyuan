package com.style.album;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.juns.wechat.R;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.manager.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class DynamicPublishImageAdapter extends BaseRecyclerViewAdapter {

    private OnDeleteClickListener listener;

    public DynamicPublishImageAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.adapter_publish_dynamic_picture, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final int index = position;
        ViewHolder holder = (ViewHolder) viewHolder;
        String path = (String) getData(position);
        if (position != getItemCount() - 1) {
            holder.ivDelete.setVisibility(View.VISIBLE);
            ImageLoader.loadPicture(mContext, holder.ivActiveImages, path);

        } else {
            holder.ivDelete.setVisibility(View.GONE);
            holder.ivActiveImages.setImageResource(R.mipmap.ic_add_photo);
        }
        super.setOnItemClickListener(holder, position);

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClickDelete(index);
                }
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_active_images)
        ImageView ivActiveImages;
        @Bind(R.id.iv_delete)
        ImageView ivDelete;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        if (listener != null) {
            this.listener = listener;
        }
    }

    public interface OnDeleteClickListener {
        void onItemClickDelete(int position);
    }

}