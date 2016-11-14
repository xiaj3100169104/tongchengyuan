package com.juns.wechat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.juns.wechat.MainActivity;
import com.juns.wechat.R;
import com.juns.wechat.adpter.ContactAdapter;
import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.MessageBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.common.BaseFragment;
import com.juns.wechat.common.CommonUtil;
import com.juns.wechat.config.MsgType;
import com.juns.wechat.dao.DbDataEvent;
import com.juns.wechat.dao.FriendDao;
import com.juns.wechat.dao.MessageDao;
import com.juns.wechat.database.ChatTable;
import com.juns.wechat.database.FriendTable;
import com.juns.wechat.database.UserTable;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.activity.UserInfoActivity;
import com.juns.wechat.view.activity.GroupListActivity;
import com.juns.wechat.view.activity.NewFriendsListActivity;
import com.juns.wechat.view.activity.PublishUserListActivity;
import com.juns.wechat.widget.SideBar;

import org.simple.eventbus.Subscriber;

import java.util.List;

//通讯录

public class Fragment_Friends extends BaseFragment implements OnClickListener,
		OnItemClickListener {
	private View layout, layout_head;
	private ListView lvContact;
	private SideBar indexBar;
	private TextView tvDialog;
    private TextView tvUnreadInviteMsgCount;
    private FriendDao rosterDao = FriendDao.getInstance();
    private List<FriendBean> friendBeen;
    private ContactAdapter contactAdapter;

    private String account = AccountManager.getInstance().getUserName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        if(layout == null){
            layout = inflater.inflate(R.layout.fragment_friends, container, false);
            initViews();
            setUnreadInviteMsgData();
            setFriendData();
            setOnListener();
        }

		return layout;
	}

	private void initViews() {
		lvContact = (ListView) layout.findViewById(R.id.lvContact);
        contactAdapter = new ContactAdapter(getActivity());
        lvContact.setAdapter(contactAdapter);

		tvDialog = (TextView) layout.findViewById(R.id.tvDialog);
		indexBar = (SideBar) layout.findViewById(R.id.sideBar);
		indexBar.setListView(lvContact);
		indexBar.setTextView(tvDialog);

		layout_head = getActivity().getLayoutInflater().inflate(R.layout.layout_head_friend, null);
        tvUnreadInviteMsgCount = (TextView) layout_head.findViewById(R.id.tv_unread_invite_msg);
		lvContact.addHeaderView(layout_head);
	}

    private void setUnreadInviteMsgData(){
        int count = MessageDao.getInstance().getUnreadInviteMsgCount(account);
        if(count > 0){
            tvUnreadInviteMsgCount.setVisibility(View.VISIBLE);
            tvUnreadInviteMsgCount.setText(count + "");
        }else {
            tvUnreadInviteMsgCount.setVisibility(View.GONE);
        }

        ((MainActivity) getActivity()).setUnreadMsgLabel(R.id.unread_contact_number, count);
    }

	private void setFriendData() {
	    friendBeen = rosterDao.getMyFriends(account);
        contactAdapter.setData(friendBeen);
	}

	private void setOnListener() {
		lvContact.setOnItemClickListener(this);
		layout_head.findViewById(R.id.re_newfriends)
				.setOnClickListener(this);
		layout_head.findViewById(R.id.re_chatroom).setOnClickListener(this);
		layout_head.findViewById(R.id.re_public).setOnClickListener(this);
	}

    @Subscriber(tag = FriendTable.TABLE_NAME)
    private void onFriendDataChanged(DbDataEvent<FriendBean> event){
        setFriendData(); //重新加载一次数据
    }

    @Subscriber(tag = UserTable.TABLE_NAME)
    private void onUserDataChanged(DbDataEvent<UserBean> event){
        if(event.action == DbDataEvent.UPDATE || event.action == DbDataEvent.REPLACE){
            setFriendData(); //重新加载数据
        }
    }

    @Subscriber(tag = ChatTable.TABLE_NAME)
    private void onMessageDataChaned(DbDataEvent<MessageBean> event){
        if(event.data != null && !event.data.isEmpty()){
            MessageBean messageBean = event.data.get(0);
            if(messageBean.getType() == MsgType.MSG_TYPE_SEND_INVITE){
                setUnreadInviteMsgData();
            }
        }
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.re_newfriends://
            MessageDao.getInstance().markAsRead(account, MsgType.MSG_TYPE_SEND_INVITE);
			CommonUtil.startActivity(getActivity(), NewFriendsListActivity.class);
			break;
		case R.id.re_chatroom:// 群聊
			CommonUtil.startActivity(getActivity(), GroupListActivity.class);
			break;
		case R.id.re_public:// 公众号
			CommonUtil.startActivity(getActivity(), PublishUserListActivity.class);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		FriendBean rosterBean = friendBeen.get(position - 1);

        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
        intent.putExtra(UserInfoActivity.ARG_USER_NAME, rosterBean.getContactName());

        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);

	}

    @Override
    protected boolean registerEventBus() {
        return true;
    }
}
