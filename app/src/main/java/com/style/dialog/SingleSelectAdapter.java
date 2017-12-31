package com.style.dialog;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.same.city.love.R;
import com.same.city.love.databinding.AdapterDialogSingleSelectBinding;
import com.style.base.BaseRecyclerViewAdapter;

import java.util.List;


public class SingleSelectAdapter extends BaseRecyclerViewAdapter<String> {

    public SingleSelectAdapter(Context mContext, List<String> list) {
        super(mContext, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterDialogSingleSelectBinding bd = DataBindingUtil.inflate(mInflater, R.layout.adapter_dialog_single_select, parent, false);
        return new ViewHolder(bd);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final int pos = position;
        final ViewHolder holder = (ViewHolder) viewHolder;
        final String bean = getData(position);
        holder.bd.tvItem.setText(bean);
        setOnItemClickListener(holder.itemView, pos);
        holder.bd.executePendingBindings();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AdapterDialogSingleSelectBinding bd;

        public ViewHolder(AdapterDialogSingleSelectBinding bd) {
            super(bd.getRoot());
            this.bd = bd;
        }
    }
}