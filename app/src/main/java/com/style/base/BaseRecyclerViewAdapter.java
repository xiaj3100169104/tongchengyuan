package com.style.base;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juns.wechat.R;
import com.style.utils.CommonUtil;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by XiaJun on 2015/7/2.
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public Context mContext;
    public LayoutInflater mInflater;
    public List<T> list;
    private View mHeaderView;
    private OnItemClickListener mListener;
    private OnItemLongClickListener onItemLongClickListener;

    public abstract RecyclerView.ViewHolder onCreateItem(ViewGroup parent, final int viewType);

    public abstract void onBindItem(RecyclerView.ViewHolder viewHolder, int position, T data);

    public BaseRecyclerViewAdapter(Context context, List<T> dataList) {
        this.list = dataList;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void addHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public List<T> getList() {
        return list;
    }

    public void setData(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addData(T e) {
        this.list.add(e);
        notifyDataSetChanged();
    }

    public void setData(T[] data) {
        if (data != null) {
            setData(Arrays.asList(data));
        }
    }

    public void addData(T[] data) {
        if (data != null && data.length > 0) {
            addData(Arrays.asList(data));
        }
    }

    public void addData(List<T> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public T getData(int position) {
        return list.get(position);
    }

    public void removeData(int position) {
        this.list.remove(position);
        notifyItemRemoved(getItemPosition(position));
    }

    public void removeData(T e) {
        this.list.remove(e);
        notifyDataSetChanged();
    }

    private int getItemPosition(int position) {
        return mHeaderView == null ? position : position + 1;
    }

    public void updateData(int position, T e) {
        list.set(position, e);
        notifyItemChanged(getItemPosition(position));
    }

    public void clearData() {
        this.list.clear();
        notifyDataSetChanged();
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
        return 1;
    }

    public boolean isSetOnItemClickListener() {
        return true;
    }

    public boolean isSetOnItemLongClickListener() {
        return false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER)
            return new HeaderViewHolder(mHeaderView);
        return onCreateItem(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_HEADER) return;
        //int adapterPosition = holder.getAdapterPosition();
        int layoutPosition = holder.getLayoutPosition();
        final int index = mHeaderView == null ? layoutPosition : layoutPosition - 1;
        T data = list.get(index);
        onBindItem(holder, index, data);
        if (isSetOnItemClickListener()) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null)
                        mListener.onItemClick(index, list.get(index));
                }
            });
        }
        if (isSetOnItemLongClickListener()) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null)
                        onItemLongClickListener.onItemLongClick(v, index, list.get(index));
                    return true;
                }
            });
        }
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

    public void setOnItemClickListener(OnItemClickListener mListener) {
        if (mListener != null)
            this.mListener = mListener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position, T data);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mListener) {
        if (mListener != null)
            this.onItemLongClickListener = mListener;
    }

    public interface OnItemLongClickListener<T> {
        void onItemLongClick(View itemView, int position, T data);
    }

    public void showToast(String str) {
        ToastManager.showToast(mContext, str);
    }

    public void showToast(int resId) {
        ToastManager.showToast(mContext, resId);
    }

    public void showToast(JSONObject response) {
        showToast(response.optString("msg"));
    }

    public void showToastRequestFailure() {
        showToast(R.string.request_fail);
    }


    public void logE(String tag, String msg) {
        LogManager.logE(tag, msg);
    }

    protected void setText(TextView textView, String str) {
        CommonUtil.setText(textView, str);
    }

    protected void setText(TextView textView, int strId) {
        setText(textView, mContext.getString(strId));
    }

    protected int dip2px(float dpValue) {
        return CommonUtil.dip2px(mContext, dpValue);
    }

    protected int px2dip(float pxValue) {
        return CommonUtil.px2dip(mContext, pxValue);
    }
}