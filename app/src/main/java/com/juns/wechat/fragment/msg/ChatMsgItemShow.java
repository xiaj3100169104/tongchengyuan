package com.juns.wechat.fragment.msg;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.juns.wechat.activity.ChatActivity;
import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.dao.FriendDao;
import com.juns.wechat.net.callback.QueryUserCallBack;
import com.juns.wechat.net.common.HttpAction;
import com.juns.wechat.net.common.NetDataBeanCallback;
import com.juns.wechat.net.request.UserRequest;
import com.juns.wechat.net.response.BaseResponse;
import com.juns.wechat.util.ImageLoader;
import com.style.constant.Skip;

/**
 * Created by 王者 on 2016/8/9.
 */
public class ChatMsgItemShow extends MsgItemShow {
    private FriendBean friendBean;
    private UserBean stranger; //陌生人

    public ChatMsgItemShow(Context context, MsgItem msgItem){
        super(context, msgItem);
    }

    @Override
    public void showTitle(final TextView textView) {
        friendBean = FriendDao.getInstance().findByOwnerAndContactName(myselfName, msgItem.userName);
        if(friendBean != null){
            textView.setText(friendBean.getShowName());
        }else {  //陌生人
            HttpAction.queryUserData(msgItem.userName, new NetDataBeanCallback<UserBean>(UserBean.class) {
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
                ImageLoader.loadAvatar(iv, stranger.getHeadUrl());
            }else {
                HttpAction.queryUserData(msgItem.userName, new NetDataBeanCallback<UserBean>(UserBean.class) {
                    @Override
                    protected void onCodeSuccess(UserBean data) {
                        if (data != null) {
                            stranger = data;
                            ImageLoader.loadAvatar(iv, stranger.getHeadUrl());
                        }
                    }

                    @Override
                    protected void onCodeFailure(String msg) {

                    }
                });
                /*UserRequest.queryUserData(msgItem.userName, new QueryUserCallBack() {
                    @Override
                    protected void handleResponse(BaseResponse.QueryUserResponse result) {
                        stranger = result.userBean;
                        ImageLoader.loadAvatar(iv, stranger.getHeadUrl());
                    }
                });*/
            }
        }else {
            ImageLoader.loadAvatar(iv, friendBean.getHeadUrl());
        }

    }

    @Override
    public void onItemClick() {
        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.putExtra(Skip.KEY_USER_NAME, msgItem.userName);
        mContext.startActivity(intent);
    }

}
