package com.juns.wechat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.juns.wechat.R;
import com.juns.wechat.annotation.Click;
import com.juns.wechat.annotation.Content;
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

/**
 * 用户资料
 */
@Content(R.layout.activity_user_info)
public class UserInfoActivity extends ToolbarActivity implements OnClickListener {
    public static final String ARG_USER_NAME = "user_name";

    @Id
    private ImageView ivAvatar;
    @Id
	private TextView tvNickName;
    @Id
    private TextView tvUserName;
    @Id
    private Button btnSendMsg;
    @Extra(name = ARG_USER_NAME)
	private String userName;

    private UserBean account = AccountManager.getInstance().getUser();
    private FriendBean friendBean;
    private UserBean userBean;
    private String subType = null;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setToolbarRight(2, R.drawable.icon_more);
        initData();
	}

	protected void initData() {
        if(userName.equals(account.getUserName())){  //查看自己的信息
            userBean = account;
            setData();
            return;
        }

		friendBean = FriendDao.getInstance().findByOwnerAndContactName(account.getUserName(), userName);
        if (friendBean == null){  //不是好友关系
            UserRequest.queryUserData(userName, queryUserCallBack);
        }else {
            subType = friendBean.getSubType();
            try {
                userBean = friendBean.getContactUser();
                setData();
            } catch (UserNotFoundException e) {
                e.printStackTrace();
            }
        }
	}

    private void setData(){
        tvNickName.setText(userBean.getShowName());
        tvUserName.setText("微信号：" + userBean.getUserName());
        ImageLoader.loadAvatar(ivAvatar, userBean.getHeadUrl());

        if(userName.equals(account.getUserName())){
            findViewById(R.id.ivRightBtn).setVisibility(View.GONE); //隐藏右边按钮
            btnSendMsg.setText("个人信息");
        }else {
            if(subType == null){
                btnSendMsg.setText("加为好友");
            }else {
                btnSendMsg.setText("发送消息");
            }
        }
    }

	private QueryUserCallBack queryUserCallBack = new QueryUserCallBack() {
        @Override
        protected void handleResponse(BaseResponse.QueryUserResponse result) {
            if(result.code == 0){
                userBean = result.userBean;
                setData();
            }else {
                handleFailed(result);
            }

        }

        protected void handleFailed(BaseResponse.QueryUserResponse result) {
            ToastUtil.showToast("该用户不存在", Toast.LENGTH_SHORT);
        }
    };

	@Click(viewId = R.id.btnSendMsg)
	public void onClick(View v) {
        if(userName.equals(account.getUserName())){
            startActivity(new Intent(this, MyProfileActivity.class));
            return;
        }
	    if(subType == null){
            Intent intent = new Intent(UserInfoActivity.this, AddFriendFinalActivity.class);
            intent.putExtra(AddFriendFinalActivity.ARG_USER_NAME, userName);
            startActivity(intent);
        }else {
            Intent intent = new Intent(UserInfoActivity.this, ChatActivity.class);
            intent.putExtra(ChatActivity.ARG_USER_NAME, userName);
            startActivity(intent);
        }
	}

}
