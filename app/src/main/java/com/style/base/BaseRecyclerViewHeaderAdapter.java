package com.style.base;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by XiaJun on 2015/7/2.
 */
public abstract class BaseRecyclerViewHeaderAdapter<T> extends BaseRecyclerViewAdapter<T> {
    public static final int TYPE_HEADER = -1;
    private View mHeaderView;

    public BaseRecyclerViewHeaderAdapter(Context context, List<T> dataList) {
        super(context, dataList);
    }

    public abstract RecyclerView.ViewHolder onCreateItemNormal(ViewGroup parent, final int viewType);

    public abstract void onBindItemNormal(RecyclerView.ViewHolder viewHolder, int position, T data);

    public void addHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    private int getItemPosition(int position) {
        return mHeaderView == null ? position : position + 1;
    }

    public int getDataSize() {
        return list.size();
    }

    @Override
    public int getItemCount() {
        int size = getDataSize();
        int count = mHeaderView == null ? size : size + 1;
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mHeaderView != null)
            return TYPE_HEADER;
        return getViewType(position);
    }

    //返回除header类型的其他view类型
    public int getViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateItem(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER)
            return new HeaderViewHolder(mHeaderView);
        return onCreateItemNormal(parent, viewType);
    }

    @Override
    public void onBindItem(RecyclerView.ViewHolder viewHolder, int position, T data) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_HEADER) return;
        //int adapterPosition = viewHolder.getAdapterPosition();
        int layoutPosition = viewHolder.getLayoutPosition();
        final int index = mHeaderView == null ? layoutPosition : layoutPosition - 1;
        T dataNormal = list.get(index);
        onBindItemNormal(viewHolder, index, dataNormal);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEADER
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            if (holder.getLayoutPosition() == 0 && holder.getItemViewType() == TYPE_HEADER) {
                p.setFullSpan(true);
            } else {
                p.setFullSpan(false);
            }
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
}