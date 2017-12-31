package com.style.album;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dmcbig.mediapicker.entity.Media;
import com.same.city.love.R;
import com.same.city.love.databinding.AdapterPublishDynamicPictureBinding;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.manager.ImageLoader;

import java.util.List;


public class DynamicPublishImageAdapter extends BaseRecyclerViewAdapter<Media> {

    private OnDeleteClickListener listener;

    public DynamicPublishImageAdapter(Context context, List<Media> list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterPublishDynamicPictureBinding bd = DataBindingUtil.inflate(mInflater, R.layout.adapter_publish_dynamic_picture, parent, false);
        return new ViewHolder(bd);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final int index = position;
        ViewHolder holder = (ViewHolder) viewHolder;
        Media media = getData(position);
        if (position != getItemCount() - 1) {
            holder.bd.ivDelete.setVisibility(View.VISIBLE);
            ImageLoader.loadPictureByUrl(mContext, holder.bd.ivActiveImages, media.path);

        } else {
            holder.bd.ivDelete.setVisibility(View.GONE);
            holder.bd.ivActiveImages.setImageResource(R.mipmap.ic_add_photo);
        }
        super.setOnItemClickListener(holder.itemView, position);

        holder.bd.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClickDelete(index);
                }
            }
        });
        holder.bd.executePendingBindings();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final AdapterPublishDynamicPictureBinding bd;

        ViewHolder(AdapterPublishDynamicPictureBinding bd) {
            super(bd.getRoot());
            this.bd = bd;
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