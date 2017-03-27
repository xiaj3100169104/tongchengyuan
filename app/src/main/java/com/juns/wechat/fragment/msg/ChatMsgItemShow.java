package com.juns.wechat.fragment.msg;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.juns.wechat.chat.ui.ChatActivity;
import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.dao.FriendDao;
import com.juns.wechat.net.common.HttpActionImpl;
import com.juns.wechat.net.common.NetDataBeanCallback;
import com.style.constant.Skip;
import com.style.manager.ImageLoader;

/**
 * Created by 王者 on 2016/8/9.
 */
public class ChatMsgItemShow extends MsgItemShow {
    private FriendBean friendBean;
    private UserBean stranger; //陌生人

    public ChatMsgItemShow(Context context, MsgItem msgItem){
        super(context, msgItem);
        friendBean = FriendDao.getInstance().findByOwnerAndContactName(myselfId, msgItem.userId);
    }

    @Override
    public void showTitle(final TextView textView) {
        if(friendBean != null){
            textView.setText(friendBean.getShowName());
        }else {  //陌生人
            HttpActionImpl.getInstance().queryUserData("queryUserData", msgItem.userId, new NetDataBeanCallback<UserBean>(UserBean.class) {
                @Override
                protected void onCodeSuccess(UserBean data) {
                    if (data != null) {
                        stranger = data;
                        textView.setText(stranger.getShowName());
                    }
                }

                @Override
                protected void onCodeFailure(String msg) {

                }
            });
           /* UserRequest.queryUserData(msgItem.userName, new QueryUserCallBack() {
                @Override
                protected void handleResponse(BaseResponse.QueryUserResponse result) {
                    stranger = result.userBean;
                    textView.setText(stranger.getShowName());
                }
            });*/
        }
    }

    @Override
    public void loadUrl(final ImageView iv) {
        if(friendBean == null){
            if(stranger != null){
                ImageLoader.loadAvatar(mContext, iv, stranger.getHeadUrl());
            }else {
                HttpActionImpl.getInstance().queryUserData("loadUrl", msgItem.userId, new NetDataBeanCallback<UserBean>(UserBean.class) {
                    @Override
                    protected void onCodeSuccess(UserBean data) {
                        if (data != null) {
                            stranger = data;
                            ImageLoader.loadAvatar(mContext, iv, stranger.getHeadUrl());
                        }
                    }

                    @Override
                    protected void onCodeFailure(String msg) {

                    }
                });
            }
        }else {
            ImageLoader.loadAvatar(mContext, iv, friendBean.getHeadUrl());
        }

    }

    @Override
    public void onItemClick() {
        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.putExtra(Skip.KEY_USER_ID, msgItem.userId);
        mContext.startActivity(intent);
    }

}
