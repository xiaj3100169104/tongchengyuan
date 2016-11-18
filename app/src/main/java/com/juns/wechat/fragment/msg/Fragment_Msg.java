package com.juns.wechat.fragment.msg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.juns.wechat.MainActivity;
import com.juns.wechat.R;
import com.juns.wechat.activity.ChatActivity;
import com.juns.wechat.adpter.ConversationAdapter;
import com.juns.wechat.bean.MessageBean;
import com.juns.wechat.common.BaseFragment;
import com.juns.wechat.dao.DbDataEvent;
import com.juns.wechat.dao.MessageDao;
import com.juns.wechat.database.ChatTable;
import com.juns.wechat.database.UserTable;
import com.juns.wechat.manager.AccountManager;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.view.DividerItemDecoration;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

//消息
public class Fragment_Msg extends BaseFragment {
    @Bind(R.id.iv_neterror)
    ImageView ivNeterror;
    @Bind(R.id.tv_connect_errormsg)
    TextView tvConnectErrormsg;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.txt_nochat)
    TextView txtNochat;
    private String account = AccountManager.getInstance().getUserName();
    private List<MsgItemShow> dataList;
    private ConversationAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutResID = R.layout.fragment_msg;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void initData() {
        dataList = new ArrayList<>();
        adapter = new ConversationAdapter(getActivity(), dataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object data) {
                MsgItemShow  msgItemShow = (MsgItemShow) data;
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra(ChatActivity.ARG_USER_NAME, msgItemShow.msgItem.userName);
                startActivity(intent);

            }
        });
        refreshData();
    }

    private void refreshData() {
        List<MsgItem> msgItems = MessageDao.getInstance().getLastMessageWithEveryFriend(account);
        if (msgItems == null || msgItems.isEmpty()) {
            txtNochat.setVisibility(View.VISIBLE);
        } else {
            txtNochat.setVisibility(View.GONE);
            dataList.clear();
            for (MsgItem msgItem : msgItems) {
                MsgItemShow msgItemShow = new ChatMsgItemShow(getActivity(), msgItem);
                dataList.add(msgItemShow);
            }
            adapter.notifyDataSetChanged();
        }
        setUnreadMsgNum();
    }

    private void setUnreadMsgNum() {
        int unreadNum = MessageDao.getInstance().getAllUnreadMsgNum(account);
        ((MainActivity) getActivity()).setUnreadMsgLabel(R.id.tv_unread_msg_number, unreadNum);
    }

    @Subscriber(tag = ChatTable.TABLE_NAME)
    private void onMessageDataChanged(DbDataEvent<MessageBean> event) {
        refreshData(); //重新加载数据
    }

    @Subscriber(tag = UserTable.TABLE_NAME)
    private void onUserDataChanged(DbDataEvent<MessageBean> event) {
        if (event.action == DbDataEvent.UPDATE && event.action == DbDataEvent.REPLACE) {
            refreshData(); //重新加载数据
        }
    }

    @Override
    protected boolean registerEventBus() {
        return true;
    }

}
