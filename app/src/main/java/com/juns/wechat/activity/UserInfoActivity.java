package com.juns.wechat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.juns.wechat.R;
import com.juns.wechat.annotation.Extra;
import com.juns.wechat.annotation.Id;
import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.common.ToolbarActivity;
import com.juns.wechat.dao.FriendDao;
import com.juns.wechat.exception.UserNotFoundException;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.callback.QueryUserCallBack;
import com.juns.wechat.net.request.UserRequest;
import com.juns.wechat.net.response.BaseResponse;
import com.juns.wechat.util.ImageLoader;
import com.juns.wechat.util.ToastUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 用户资料
 */

public class UserInfoActivity extends ToolbarActivity implements OnClickListener {
    public static final String ARG_USER_NAME = "user_name";
    @Bind(R.id.ivAvatar)
    ImageView ivAvatar;
    @Bind(R.id.tvNickName)
    TextView tvNickName;
    @Bind(R.id.ivSex)
    ImageView ivSex;
    @Bind(R.id.tvUserName)
    TextView tvUserName;
    @Bind(R.id.btnSendMsg)
    Button btnSendMsg;

    @Extra(name = ARG_USER_NAME)
    private String userName;

    private UserBean account = AccountManager.getInstance().getUser();
    private FriendBean friendBean;
    private UserBean userBean;
    private String subType = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_user_info;
        super.onCreate(savedInstanceState);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.avatar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_text_only:
                //goonNext();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initData() {
        if (userName.equals(account.getUserName())) {  //查看自己的信息
            userBean = account;
            setData();
            return;
        }

        friendBean = FriendDao.getInstance().findByOwnerAndContactName(account.getUserName(), userName);
        if (friendBean == null) {  //不是好友关系
            UserRequest.queryUserData(userName, queryUserCallBack);
        } else {
            subType = friendBean.getSubType();
            try {
                userBean = friendBean.getContactUser();
                setData();
            } catch (UserNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void setData() {
        tvNickName.setText(userBean.getShowName());
        tvUserName.setText("微信号：" + userBean.getUserName());
        ImageLoader.loadAvatar(ivAvatar, userBean.getHeadUrl());

        if (userName.equals(account.getUserName())) {
            btnSendMsg.setText("个人信息");
        } else {
            if (subType == null) {
                btnSendMsg.setText("加为好友");
            } else {
                btnSendMsg.setText("发送消息");
            }
        }
    }

    private QueryUserCallBack queryUserCallBack = new QueryUserCallBack() {
        @Override
        protected void handleResponse(BaseResponse.QueryUserResponse result) {
            if (result.code == 0) {
                userBean = result.userBean;
                setData();
            } else {
                handleFailed(result);
            }

        }

        protected void handleFailed(BaseResponse.QueryUserResponse result) {
            ToastUtil.showToast("该用户不存在", Toast.LENGTH_SHORT);
        }
    };

    @OnClick(R.id.btnSendMsg)
    public void onClick(View v) {
        if (userName.equals(account.getUserName())) {
            startActivity(new Intent(this, MyProfileActivity.class));
            return;
        }
        if (subType == null) {
            Intent intent = new Intent(UserInfoActivity.this, AddFriendFinalActivity.class);
            intent.putExtra(AddFriendFinalActivity.ARG_USER_NAME, userName);
            startActivity(intent);
        } else {
            Intent intent = new Intent(UserInfoActivity.this, ChatActivity.class);
            intent.putExtra(ChatActivity.ARG_USER_NAME, userName);
            startActivity(intent);
        }
    }

}
