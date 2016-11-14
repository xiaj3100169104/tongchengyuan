package com.juns.wechat.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.common.ViewHolder;
import com.juns.wechat.fragment.msg.MsgItemShow;

/**
 * Created by 王者 on 2016/8/9.
 */
public class ConversationAdapter extends ListDataAdapter<MsgItemShow>{

    public ConversationAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.layout_item_msg, parent, false);
        }
        ImageView ivAvatar = ViewHolder.get(convertView,
                R.id.iv_avatar);
        TextView tvTitle = ViewHolder.get(convertView, R.id.tv_title);
        TextView tvContent = ViewHolder.get(convertView, R.id.tv_content);
        TextView tvTime = ViewHolder.get(convertView, R.id.tv_time);
        TextView tvUnreadLabel = ViewHolder.get(convertView,
                R.id.tv_unread_msg_number);

        final MsgItemShow msgItemShow = listData.get(position);
        msgItemShow.loadUrl(ivAvatar);
        msgItemShow.showUnreadMsgNumber(tvUnreadLabel);
        msgItemShow.showTitle(tvTitle);
        msgItemShow.showContent(tvContent);
        msgItemShow.showTime(tvTime);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgItemShow.onItemClick();
            }
        });
        return convertView;
    }

}
