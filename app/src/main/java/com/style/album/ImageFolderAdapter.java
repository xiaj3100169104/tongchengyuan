package com.style.album;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.juns.wechat.R;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.manager.ImageLoadManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ImageFolderAdapter extends BaseRecyclerViewAdapter {



    public ImageFolderAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.adapter_album_image_bucket, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        PicBucket item = (PicBucket) getData(position);
        List<ImageItem> items = item.getImages();
        int num = 0;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isSelected()) {
                num++;
            }
        }
        String path = item.getImages().get(0).getImagePath();
        ImageLoadManager.loadNormalPicture(mContext, holder.image, path);
        setText(holder.tvName, item.getBucketName());
        setText(holder.tvCount, " (" + num + "/" + item.getImages().size() + ")");
        super.setOnItemClickListener(holder, position);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image)
        ImageView image;
        @Bind(R.id.tv_count)
        TextView tvCount;
        @Bind(R.id.tv_name)
        TextView tvName;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
