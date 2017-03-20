package com.juns.wechat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.juns.wechat.R;
import com.juns.wechat.activity.MainActivity;
import com.juns.wechat.activity.NewFriendsListActivity;
import com.juns.wechat.activity.UserInfoActivity;
import com.juns.wechat.adpter.ContactAdapter;
import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.chat.bean.MessageBean;
import com.juns.wechat.config.MsgType;
import com.juns.wechat.dao.DbDataEvent;
import com.juns.wechat.dao.FriendDao;
import com.juns.wechat.dao.MessageDao;
import com.juns.wechat.database.ChatTable;
import com.juns.wechat.database.FriendTable;
import com.juns.wechat.database.UserTable;
import com.juns.wechat.manager.AccountManager;
import com.style.base.BaseBusFragment;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.constant.Skip;
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

public class Fragment_Friends extends BaseBusFragment {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.sideBar)
    SideBar sideBar;
    private FriendDao friendDao = FriendDao.getInstance();
    private int unReadCount = 0;
    private List<FriendBean> dataList;
    private LinearLayoutManager layoutManager;

    private ContactAdapter adapter;

    private String account = AccountManager.getInstance().getUserName();
    private int accountId = AccountManager.getInstance().getUserId();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutResID = R.layout.fragment_friends;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initData() {

        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity()));
        dataList = new ArrayList<>();
        adapter = new ContactAdapter(getActivity(), dataList);
        recyclerView.setAdapter(adapter);
        adapter.setUnReadCount(unReadCount);
        adapter.setOnHeaderItemClickListener(new ContactAdapter.OnHeaderItemClickListener() {
            @Override
            public void onClickNewFriend() {
                MessageDao.getInstance().markAsRead(account, MsgType.MSG_TYPE_SEND_INVITE);
                startActivity(new Intent(getActivity(), NewFriendsListActivity.class));
            }
        });
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<FriendBean>() {
            @Override
            public void onItemClick(int position, FriendBean data) {
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                intent.putExtra(Skip.KEY_USER_ID, data.getContactId());
                getContext().startActivity(intent);

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
                    layoutManager.smoothScrollToPosition(recyclerView, null, position + 1);
                }
            }
        });

        setUnreadInviteMsgData();
        setFriendData();
    }

    private void setUnreadInviteMsgData() {
        int count = MessageDao.getInstance().getUnreadInviteMsgCount(account);
        unReadCount = count;
        adapter.notifyItemChanged(0);
        adapter.setUnReadCount(unReadCount);
        ((MainActivity) getActivity()).setUnreadMsgLabel(R.id.unread_contact_number, count);
    }

    private void setFriendData() {
        Log.e(TAG, "setFriendData");
        List<FriendBean> list = friendDao.getMyFriends(accountId);
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
