package com.juns.wechat.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.chat.ui.ChatActivity;
import com.juns.wechat.greendao.mydao.GreenDaoManager;
import com.juns.wechat.helper.CommonViewHelper;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.request.HttpActionImpl;
import com.same.city.love.R;
import com.same.city.love.databinding.ActivityUserInfoBinding;
import com.style.base.BaseToolbarActivity;
import com.style.constant.Skip;
import com.style.net.core.NetDataBeanCallback;

/**
 * 用户资料
 */

public class UserInfoActivity extends BaseToolbarActivity implements OnClickListener {
    ActivityUserInfoBinding bd;

    private String userId;
    private UserBean curUser = AccountManager.getInstance().getUser();
    private FriendBean friendBean;
    private UserBean userBean;
    private String subType = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_user_info);
        super.setContentView(bd.getRoot());
        initData();
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
        setToolbarTitle("详细资料");
        bd.btnSendMsg.setOnClickListener(this);

        userId = getIntent().getStringExtra(Skip.KEY_USER_ID);
        if (userId == curUser.getUserId()) {  //查看自己的信息
            userBean = curUser;
            setData();
            return;
        }

        friendBean = GreenDaoManager.getInstance().findByOwnerAndContactName(curUser.getUserId(), userId);
        if (friendBean != null) {  //不是好友关系
            subType = friendBean.getSubType();
        }
        userBean = GreenDaoManager.getInstance().findByUserId(userId);
        if (userBean != null) {
            setData();
            return;
        }
        HttpActionImpl.getInstance().queryUserData(TAG, userId, new NetDataBeanCallback<UserBean>(UserBean.class) {
            @Override
            protected void onCodeSuccess(UserBean data) {
                if (data != null) {
                    userBean = data;
                    GreenDaoManager.getInstance().save(data);
                    setData();
                }
            }

            @Override
            protected void onCodeFailure(String msg) {
                showToast("获取用户信息失败");
            }
        });
    }

    private void setData() {
        CommonViewHelper.setUserViewInfo(userBean, bd.ivAvatar, bd.tvNickName, bd.ivSex, bd.tvUserName, true);

        if (userId == curUser.getUserId()) {
            bd.btnSendMsg.setText("个人信息");
        } else {
            if (subType == null) {
                bd.btnSendMsg.setText("加为好友");
            } else {
                bd.btnSendMsg.setText("发送消息");
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == bd.btnSendMsg.getId()) {
            if (userId == curUser.getUserId()) {
                startActivity(new Intent(this, PersonInfoShowActivity.class));
                return;
            }
            if (subType == null) {
                Intent intent = new Intent(UserInfoActivity.this, AddFriendFinalActivity.class);
                intent.putExtra(Skip.KEY_USER_ID, userBean.getUserId());
                startActivity(intent);
            } else {
                Intent intent = new Intent(UserInfoActivity.this, ChatActivity.class);
                intent.putExtra(Skip.KEY_USER_ID, userId);
                startActivity(intent);
            }
        }
    }

}
