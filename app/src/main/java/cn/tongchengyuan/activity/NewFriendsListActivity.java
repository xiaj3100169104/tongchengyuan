package cn.tongchengyuan.activity;


import android.databinding.DataBindingUtil;
import android.os.Bundle;

import cn.tongchengyuan.adpter.NewFriendsAdapter;
import cn.tongchengyuan.chat.bean.MessageBean;
import cn.tongchengyuan.greendao.mydao.GreenDaoManager;
import cn.tongchengyuan.manager.AccountManager;

import com.same.city.love.R;
import com.same.city.love.databinding.ActivityListviewBinding;
import com.style.base.BaseToolbarActivity;

import java.util.List;


//新朋友
public class NewFriendsListActivity extends BaseToolbarActivity {
    ActivityListviewBinding bd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_listview);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    public void initData() {
        setToolbarTitle("新的朋友");

        String myselfName = AccountManager.getInstance().getUserName();
        List<MessageBean> list = GreenDaoManager.getInstance().getMyReceivedInviteMessages(myselfName);
        if (list != null)
            bd.lvNewFriends.setAdapter(new NewFriendsAdapter(this, list));
    }
}
