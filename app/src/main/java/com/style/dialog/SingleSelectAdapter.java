package com.style.dialog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.same.city.love.R;
import com.style.base.BaseRecyclerViewAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SingleSelectAdapter extends BaseRecyclerViewAdapter<String> {

    public SingleSelectAdapter(Context mContext, List<String> list) {
        super(mContext, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.adapter_dialog_single_select, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final int pos = position;
        final ViewHolder holder = (ViewHolder) viewHolder;
        final String bean = getData(position);
        holder.tvItem.setText(bean);
        setOnItemClickListener(holder, pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_item)
        TextView tvItem;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}