package com.juns.wechat.adpter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.same.city.love.R;
import com.juns.wechat.activity.UserInfoActivity;
import com.juns.wechat.chat.bean.MessageBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.chat.bean.InviteMsg;
import com.juns.wechat.common.ViewHolder;
import com.juns.wechat.database.dao.MessageDao;
import com.juns.wechat.database.dao.UserDao;
import com.juns.wechat.net.request.HttpActionImpl;
import com.style.net.core.NetDataBeanCallback;
import com.juns.wechat.util.SyncDataUtil;
import com.juns.wechat.chat.xmpp.util.SendMessage;
import com.style.constant.Skip;
import com.style.manager.ImageLoader;

import java.util.List;

public class NewFriendsAdapter extends BaseAdapter {
    protected Context context;
    private List<MessageBean> inviteMessages;
    private UserDao userDao = UserDao.getInstance();

    public NewFriendsAdapter(Context ctx, List<MessageBean> inviteMessages) {
        context = ctx;
        this.inviteMessages = inviteMessages;
    }

    @Override
    public int getCount() {
        if (inviteMessages == null) {
            return 0;
        }
        return inviteMessages.size();
    }

    @Override
    public Object getItem(int position) {
        if (inviteMessages == null) {
            return null;
        }
        return inviteMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.layout_item_newfriend, parent, false);
        }
        ImageView ivAvatar = ViewHolder.get(convertView, R.id.ivAvatar);
        TextView txt_name = ViewHolder.get(convertView, R.id.tv_title);
        final TextView txt_add = ViewHolder.get(convertView, R.id.tvAdd);

        final MessageBean messageBean = inviteMessages.get(position);
        final UserBean userBean = userDao.findByName(messageBean.getOtherName());
        if (userBean == null) {
            //UserRequest.queryUserData(messageBean.getOtherName(), queryUserCallBack);
            HttpActionImpl.getInstance().queryPhone("queryPhone", messageBean.getOtherName(), new NetDataBeanCallback<UserBean>(UserBean.class) {
                @Override
                protected void onCodeSuccess(UserBean data) {
                    if (data != null) {
                        UserDao.getInstance().replace(data);
                        notifyDataSetChanged();
                    }
                }

                @Override
                protected void onCodeFailure(String msg) {

                }
            });
            return convertView;
        } else {
            ImageLoader.loadAvatar(context, ivAvatar, userBean.getHeadUrl());
            txt_name.setText(userBean.getShowName());
        }

        final InviteMsg inviteMsg = (InviteMsg) messageBean.getMsgObj();
        if (inviteMsg.reply == 0) {
            txt_add.setText("添加");
            txt_add.setTextColor(context.getResources().getColor(R.color.white));
            txt_add.setBackgroundResource(R.drawable.btn_bg_green);
        } else if (inviteMsg.reply == InviteMsg.Reply.ACCEPT.value) {
            txt_add.setText("已同意");
            txt_add.setTextColor(context.getResources().getColor(R.color.gray_black));
            txt_add.setBackgroundResource(R.color.white);
        } else if (inviteMsg.reply == InviteMsg.Reply.REJECT.value) {
            txt_add.setText("已拒绝");
            txt_add.setTextColor(context.getResources().getColor(R.color.gray_black));
            txt_add.setBackgroundResource(R.color.white);
        }

        txt_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriend(messageBean);
            }
        });

        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtra(Skip.KEY_USER_ID, userBean.getUserId());
                context.startActivity(intent);
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //MessageDao.getInstance().delete(messageBean.getId());
                return true;
            }
        });
        return convertView;
    }

    private void addFriend(final MessageBean message) {
        final MessageBean messageBean = message;
        UserBean userBean = userDao.getInstance().findByName(messageBean.getOtherName());
        HttpActionImpl.getInstance().addFriend("addFriend", userBean.userId, new NetDataBeanCallback() {
            @Override
            protected void onCodeSuccess() {
                InviteMsg inviteMsg = (InviteMsg) messageBean.getMsgObj();
                int reply = InviteMsg.Reply.ACCEPT.value;
                inviteMsg.reply = reply;
                messageBean.setMsg(inviteMsg.toJson());
                MessageDao.getInstance().update(messageBean);
                notifyDataSetChanged();
                sendMessageToOther(messageBean.getOtherName(), reply);

                SyncDataUtil.getInstance().syncData();
            }

            @Override
            protected void onCodeFailure(String msg) {

            }
        });
    }

    private void sendMessageToOther(String otherName, int reply) {
        String reason = "我同意了你的添加好友的请求";
        InviteMsg inviteMsg = new InviteMsg();
        UserBean userBean = userDao.findByName(otherName);
        if (userBean != null) {
            inviteMsg.userName = userBean.getShowName();
        }

        SendMessage.sendReplyInviteMsg(otherName, reply, reason);
    }

}
