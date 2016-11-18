package com.juns.wechat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juns.wechat.MainActivity;
import com.juns.wechat.R;
import com.juns.wechat.activity.UserInfoActivity;
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
import com.juns.wechat.view.activity.GroupListActivity;
import com.juns.wechat.view.activity.NewFriendsListActivity;
import com.juns.wechat.view.activity.PublishUserListActivity;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.utils.HanyuToPinyin;
import com.style.view.DividerItemDecoration;
import com.style.view.SideBar;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

//通讯录

public class Fragment_Friends extends BaseFragment implements OnClickListener{
    @Bind(R.id.tv_unread_invite_msg)
    TextView tvUnreadInviteMsg;
    @Bind(R.id.layout_new_friends)
    RelativeLayout layoutNewFriends;
    @Bind(R.id.layout_chat_room)
    RelativeLayout layoutChatRoom;
    @Bind(R.id.layout_label)
    RelativeLayout layoutLabel;
    @Bind(R.id.layout_public)
    RelativeLayout layoutPublic;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.sideBar)
    SideBar sideBar;
    private FriendDao rosterDao = FriendDao.getInstance();
    private List<FriendBean> dataList;
    private LinearLayoutManager layoutManager;

    private ContactAdapter adapter;

    private String account = AccountManager.getInstance().getUserName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutResID = R.layout.fragment_friends;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initData() {
        layoutNewFriends.setOnClickListener(this);
        layoutChatRoom.setOnClickListener(this);
        layoutPublic.setOnClickListener(this);
        dataList = new ArrayList<>();
        adapter = new ContactAdapter(getActivity(), dataList);
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object data) {
                FriendBean rosterBean = (FriendBean) data;
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                intent.putExtra(UserInfoActivity.ARG_USER_NAME, rosterBean.getContactName());

                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);

            }
        });
        // 设置右侧触摸监听
        //sideBar.setTextView(tvDialog);
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    layoutManager.smoothScrollToPosition(recyclerView, null, position);
                }
            }
        });

        setUnreadInviteMsgData();
        setFriendData();

    }

    private void setUnreadInviteMsgData() {
        int count = MessageDao.getInstance().getUnreadInviteMsgCount(account);
        if (count > 0) {
            tvUnreadInviteMsg.setVisibility(View.VISIBLE);
            tvUnreadInviteMsg.setText(count + "");
        } else {
            tvUnreadInviteMsg.setVisibility(View.GONE);
        }

        ((MainActivity) getActivity()).setUnreadMsgLabel(R.id.unread_contact_number, count);
    }

    private void setFriendData() {
        List<FriendBean> list = rosterDao.getMyFriends(account);
        if (null != list) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                String sortLetter = HanyuToPinyin.hanziToCapital(list.get(i).getShowName());
                list.get(i).setSortLetters(sortLetter);
            }
            // 根据a-z进行排序源数据
            Collections.sort(list, new UploadPhoneComparator());
            dataList.clear();
            dataList.addAll(list);
            adapter.notifyDataSetChanged();
        }

    }

    @Subscriber(tag = FriendTable.TABLE_NAME)
    private void onFriendDataChanged(DbDataEvent<FriendBean> event) {
        setFriendData(); //重新加载一次数据
    }

    @Subscriber(tag = UserTable.TABLE_NAME)
    private void onUserDataChanged(DbDataEvent<UserBean> event) {
        if (event.action == DbDataEvent.UPDATE || event.action == DbDataEvent.REPLACE) {
            setFriendData(); //重新加载数据
        }
    }

    @Subscriber(tag = ChatTable.TABLE_NAME)
    private void onMessageDataChaned(DbDataEvent<MessageBean> event) {
        if (event.data != null && !event.data.isEmpty()) {
            MessageBean messageBean = event.data.get(0);
            if (messageBean.getType() == MsgType.MSG_TYPE_SEND_INVITE) {
                setUnreadInviteMsgData();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_new_friends://
                MessageDao.getInstance().markAsRead(account, MsgType.MSG_TYPE_SEND_INVITE);
                CommonUtil.startActivity(getActivity(), NewFriendsListActivity.class);
                break;
            case R.id.layout_chat_room:// 群聊
                CommonUtil.startActivity(getActivity(), GroupListActivity.class);
                break;
            case R.id.layout_public:// 公众号
                CommonUtil.startActivity(getActivity(), PublishUserListActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    protected boolean registerEventBus() {
        return true;
    }

    public class UploadPhoneComparator implements Comparator<FriendBean> {
        public int compare(FriendBean o1, FriendBean o2) {
            if ("#".equals(o2.getSortLetters())) {
                return -1;// o1 < o2
            } else if ("#".equals(o1.getSortLetters())) {
                return 1;// o1 > o2
            } else {
                return o1.getSortLetters().compareTo(o2.getSortLetters());
            }
        }
    }

}
