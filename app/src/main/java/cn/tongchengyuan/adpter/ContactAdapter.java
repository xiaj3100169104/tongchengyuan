package cn.tongchengyuan.adpter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;

import cn.tongchengyuan.bean.FriendBean;
import com.same.city.love.R;
import com.same.city.love.databinding.ContactItemBinding;
import com.same.city.love.databinding.LayoutHeadFriendBinding;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.manager.ImageLoader;

import java.util.List;

public class ContactAdapter extends BaseRecyclerViewAdapter<FriendBean> implements SectionIndexer {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private OnHeaderItemClickListener mHeaderListener;
    private int unReadCount = 0;

    public ContactAdapter(Context context, List<FriendBean> list) {
        super(context, list);
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
        notifyItemChanged(0);
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            LayoutHeadFriendBinding bd = DataBindingUtil.inflate(mInflater, R.layout.layout_head_friend, parent, false);
            return new HeaderViewHolder(bd);
        }
        ContactItemBinding bd = DataBindingUtil.inflate(mInflater, R.layout.contact_item, parent, false);
        return new ViewHolder(bd);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int layoutPosition) {
        if (getItemViewType(layoutPosition) == TYPE_HEADER) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
            int count = getUnReadCount();
            if (count > 0) {
                headerViewHolder.bd.viewNotifyMsg.setVisibility(View.VISIBLE);
                headerViewHolder.bd.viewNotifyMsg.setNotifyCount(count);
            } else {
                headerViewHolder.bd.viewNotifyMsg.setVisibility(View.GONE);
            }
            if (mHeaderListener != null) {
                headerViewHolder.bd.layoutNewFriends.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mHeaderListener.onClickNewFriend();
                    }
                });
            }
            headerViewHolder.bd.executePendingBindings();
        } else {
            ViewHolder holder = (ViewHolder) viewHolder;
            int position = layoutPosition - 1;
            FriendBean f = getData(position);
            // 根据position获取分类的首字母的Char ascii值
            int section = getSectionForPosition(position);
            // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(section)) {
                holder.bd.tvCatalog.setVisibility(View.VISIBLE);
                holder.bd.tvCatalog.setText(f.getSortLetters());
            } else {
                holder.bd.tvCatalog.setVisibility(View.GONE);
            }
            ImageLoader.loadAvatar(mContext, holder.bd.ivAvatar, f.headUrl);
            String showName = !TextUtils.isEmpty(f.getRemark()) ? f.getRemark() : f.nickName;
            holder.bd.tvNick.setText(getNotNullText(showName));
            super.setOnItemClickListener(holder.itemView, position);
            holder.bd.executePendingBindings();
        }
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return getList().get(position).getSortLetters().charAt(0);
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getList().size(); i++) {
            String sortStr = getList().get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ContactItemBinding bd;

        ViewHolder(ContactItemBinding bd) {
            super(bd.getRoot());
            this.bd = bd;
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final LayoutHeadFriendBinding bd;

        HeaderViewHolder(LayoutHeadFriendBinding bd) {
            super(bd.getRoot());
            this.bd = bd;
        }
    }

    public void setOnHeaderItemClickListener(OnHeaderItemClickListener mListener) {
        if (mListener != null)
            this.mHeaderListener = mListener;
    }

    public interface OnHeaderItemClickListener {
        void onClickNewFriend();
    }
}
