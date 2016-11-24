package com.juns.wechat.activity;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.juns.wechat.R;
import com.juns.wechat.adpter.NewFriendsAdapter;
import com.juns.wechat.annotation.Id;
import com.juns.wechat.bean.MessageBean;
import com.juns.wechat.common.ToolbarActivity;
import com.juns.wechat.dao.MessageDao;
import com.juns.wechat.manager.AccountManager;

import java.util.List;

import butterknife.Bind;

//新朋友
public class NewFriendsListActivity extends ToolbarActivity {
    @Bind(R.id.lvNewFriends)
    ListView lvNewFriends;

    @Override
    public void initData() {
        initControl();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_listview;
        super.onCreate(savedInstanceState);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_with_text, menu);
        menu.getItem(0).setTitle(R.string.menu_add_friend);
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
    protected void initControl() {
        String myselfName = AccountManager.getInstance().getUserName();
        List<MessageBean> myReceivedInviteMessages =
                MessageDao.getInstance().getMyReceivedInviteMessages(myselfName);

        lvNewFriends.setAdapter(new NewFriendsAdapter(this, myReceivedInviteMessages));
    }
}
