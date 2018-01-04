package cn.tongchengyuan.adpter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import cn.tongchengyuan.bean.FriendBean;
import cn.tongchengyuan.fragment.msg.MsgItem;
import cn.tongchengyuan.bean.UserBean;
import cn.tongchengyuan.chat.utils.SmileUtils;

import com.same.city.love.R;
import com.same.city.love.databinding.LayoutItemMsgBinding;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.manager.ImageLoader;
import com.style.utils.MyDateUtil;

import java.util.List;

/**
 * Created by 王者 on 2016/8/9.
 */
public class ConversationAdapter extends BaseRecyclerViewAdapter {

    public ConversationAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutItemMsgBinding bd = DataBindingUtil.inflate(mInflater, R.layout.layout_item_msg, parent, false);
        return new ViewHolder(bd);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        MsgItem msgItem = (MsgItem) getData(position);
        FriendBean f = msgItem.friendBean;
        UserBean u = msgItem.user;
        if (u != null) {
            logE(TAG, u.getHeadUrl());
            ImageLoader.loadAvatar(mContext, holder.bd.ivAvatar, u.getHeadUrl());
        }
        if (f != null && u != null) {
            String showName = !TextUtils.isEmpty(f.getRemark()) ? f.getRemark() : u.getShowName();
            holder.bd.tvTitle.setText(getNotNullText(showName));
        }
        int unreadMsgCount = msgItem.unreadMsgCount;
        if(unreadMsgCount == 0){
            holder.bd.tvUnreadMsgNumber.setVisibility(View.GONE);
        }else {
            holder.bd.tvUnreadMsgNumber.setVisibility(View.VISIBLE);
            holder.bd.tvUnreadMsgNumber.setText(unreadMsgCount + "");
        }
        holder.bd.tvContent.setText(getNotNullText(SmileUtils.getInstance().getSmiledText(msgItem.msg.getTypeDesc())));
        holder.bd.tvTime.setText(getNotNullText(MyDateUtil.getTimeConversationString(msgItem.msg.getDate().getTime())));
        super.setOnItemClickListener(holder.itemView, position);
        holder.bd.executePendingBindings();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final LayoutItemMsgBinding bd;

        ViewHolder(LayoutItemMsgBinding bd) {
            super(bd.getRoot());
            this.bd = bd;
        }
    }
}
