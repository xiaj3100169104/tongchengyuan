package cn.tongchengyuan.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.tongchengyuan.activity.MainActivity;
import cn.tongchengyuan.manager.AccountManager;
import cn.tongchengyuan.activity.NewFriendsListActivity;
import cn.tongchengyuan.activity.UserInfoActivity;
import cn.tongchengyuan.adpter.ContactAdapter;
import cn.tongchengyuan.bean.FriendBean;
import cn.tongchengyuan.bean.UserBean;
import cn.tongchengyuan.greendao.mydao.GreenDaoManager;

import com.same.city.love.R;
import com.same.city.love.databinding.FragmentFriendsBinding;
import com.style.base.BaseFragment;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.constant.Skip;
import com.style.event.EventCode;
import com.style.utils.HanyuToPinyin;
import com.style.view.DividerItemDecoration;
import com.style.view.SideBar;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//通讯录

public class Fragment_Friends extends BaseFragment {

    FragmentFriendsBinding bd;
    private GreenDaoManager greenDao = GreenDaoManager.getInstance();
    private int unReadCount = 0;
    private List<FriendBean> dataList;
    private LinearLayoutManager layoutManager;
    private ContactAdapter adapter;
    private UserBean curUser = AccountManager.getInstance().getUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bd = DataBindingUtil.inflate(inflater, R.layout.fragment_friends, container, false);
        return bd.getRoot();
    }

    @Override
    protected void initData() {

        layoutManager = new LinearLayoutManager(this.getActivity());
        bd.recyclerView.setLayoutManager(layoutManager);
        bd.recyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity()));
        dataList = new ArrayList<>();
        adapter = new ContactAdapter(getActivity(), dataList);
        bd.recyclerView.setAdapter(adapter);
        adapter.setUnReadCount(unReadCount);
        adapter.setOnHeaderItemClickListener(new ContactAdapter.OnHeaderItemClickListener() {
            @Override
            public void onClickNewFriend() {
                greenDao.markAsRead(curUser.getUserName());
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
        bd.sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    layoutManager.smoothScrollToPosition(bd.recyclerView, null, position + 1);
                }
            }
        });

        setUnreadInviteMsgData();
        setFriendData();
    }

    private void setUnreadInviteMsgData() {
        int count = greenDao.getUnreadInviteMsgCount(curUser.getUserName());
        unReadCount = count;
        adapter.notifyItemChanged(0);
        adapter.setUnReadCount(unReadCount);
        ((MainActivity) getActivity()).setUnreadMsgLabel(count);
    }

    private void setFriendData() {
        Log.e(TAG, "setFriendData.ownerId=" + curUser.getUserId());
        List<FriendBean> list = greenDao.getMyFriends(curUser.getUserId());
        if (null != list) {
            int size = list.size();
            for (FriendBean f : list) {
                String hanzi = !TextUtils.isEmpty(f.getRemark()) ? f.getRemark() : f.nickName;
                String sortLetter = HanyuToPinyin.hanziToCapital(hanzi);
                f.setSortLetters(sortLetter);
                //Log.e(TAG, f.toString());

            }
            // 根据a-z进行排序源数据
            Collections.sort(list, new UploadPhoneComparator());
            dataList.clear();
            dataList.addAll(list);
            adapter.notifyDataSetChanged();
        }
    }

    @Subscriber(tag = EventCode.REFRESH_FRIEND_LIST)
    private void onFriendDataChanged(Object data) {
        setFriendData(); //重新加载一次数据
    }

    @Subscriber(tag = EventCode.REFRESH_NEW_FRIEND_REQUEST)
    private void onMessageDataChaned(Object data) {
        setUnreadInviteMsgData();
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
