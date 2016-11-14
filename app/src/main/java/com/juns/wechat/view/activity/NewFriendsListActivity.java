package com.juns.wechat.view.activity;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.juns.wechat.Constants;
import com.juns.wechat.R;
import com.juns.wechat.adpter.NewFriendsAdapter;
import com.juns.wechat.annotation.Content;
import com.juns.wechat.annotation.Id;
import com.juns.wechat.bean.MessageBean;
import com.juns.wechat.common.CommonUtil;
import com.juns.wechat.common.ToolbarActivity;
import com.juns.wechat.dao.MessageDao;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.util.LogUtil;
import com.juns.wechat.util.ToolBarUtil;

import java.util.List;

//新朋友
@Content(R.layout.activity_listview)
public class NewFriendsListActivity extends ToolbarActivity implements OnClickListener {
    @Id
	private ListView lvNewFriends;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        ToolBarUtil.setToolbarRight(this, 1, R.string.menu_add_friend);
        initControl();
	}

	protected void initControl() {
        String myselfName = AccountManager.getInstance().getUserName();
        List<MessageBean> myReceivedInviteMessages =
                MessageDao.getInstance().getMyReceivedInviteMessages(myselfName);

		lvNewFriends.setAdapter(new NewFriendsAdapter(this, myReceivedInviteMessages));
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
	/*	case R.id.tvRightText:
			CommonUtil.startActivity(this, PublicActivity.class,
					new BasicNameValuePair(Constants.NAME, "添加朋友"));
			break;
		case R.id.txt_search:
			CommonUtil.startActivity(this, PublicActivity.class,
					new BasicNameValuePair(Constants.NAME, "搜索"));
			break;*/
		default:
			break;
		}
	}
}
