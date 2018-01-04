package cn.tongchengyuan.chat.adpter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.same.city.love.R;
import com.same.city.love.databinding.RowExpressionBinding;
import com.style.base.BaseRecyclerViewAdapter;

import java.util.List;

public class ExpressionAdapter extends BaseRecyclerViewAdapter<String> {

    public ExpressionAdapter(Context mContext, List<String> list) {
        super(mContext, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowExpressionBinding bd = DataBindingUtil.inflate(mInflater, R.layout.row_expression, parent, false);
        return new ViewHolder(bd);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final int pos = position;
        final ViewHolder holder = (ViewHolder) viewHolder;
        String filename = getData(position);
        int resId = mContext.getResources().getIdentifier(filename, "drawable", mContext.getPackageName());

        holder.bd.ivExpression.setImageResource(resId);
        super.setOnItemClickListener(viewHolder.itemView, pos);
        holder.bd.executePendingBindings();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final RowExpressionBinding bd;

        public ViewHolder(RowExpressionBinding bd) {
            super(bd.getRoot());
            this.bd = bd;
        }
    }
}
