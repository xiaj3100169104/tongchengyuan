package com.juns.wechat.activity;


import android.os.Bundle;
import android.widget.ListView;

import com.juns.wechat.greendao.mydao.GreenDaoManager;
import com.same.city.love.R;
import com.juns.wechat.adpter.NewFriendsAdapter;
import com.juns.wechat.chat.bean.MessageBean;
import com.style.base.BaseToolbarActivity;
import com.juns.wechat.manager.AccountManager;

import java.util.List;

import butterknife.Bind;

//新朋友
public class NewFriendsListActivity extends BaseToolbarActivity {
    @Bind(R.id.lvNewFriends)
    ListView lvNewFriends;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_listview;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
        setToolbarTitle("新的朋友");
        initControl();
    }

    protected void initControl() {
        String myselfName = AccountManager.getInstance().getUserName();
        List<MessageBean> list = GreenDaoManager.getInstance().getMyReceivedInviteMessages(myselfName);
        if (list != null)
            lvNewFriends.setAdapter(new NewFriendsAdapter(this, list));
    }
}
