package cn.tongchengyuan.fragment.msg;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.tongchengyuan.activity.MainActivity;
import cn.tongchengyuan.chat.ui.ChatActivity;
import cn.tongchengyuan.greendao.mydao.GreenDaoManager;
import cn.tongchengyuan.manager.AccountManager;
import cn.tongchengyuan.adpter.ConversationAdapter;
import cn.tongchengyuan.bean.UserBean;

import com.same.city.love.R;
import com.same.city.love.databinding.FragmentMsgBinding;
import com.style.base.BaseFragment;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.constant.Skip;
import com.style.event.EventCode;
import com.style.view.DividerItemDecoration;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//消息
public class Fragment_Msg extends BaseFragment {
    FragmentMsgBinding bd;
    private List<MsgItem> dataList;
    private ConversationAdapter adapter;
    private UserBean curUser = AccountManager.getInstance().getUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bd = DataBindingUtil.inflate(inflater, R.layout.fragment_msg, container, false);
        return bd.getRoot();
    }

    protected void initData() {
        dataList = new ArrayList<>();
        adapter = new ConversationAdapter(getActivity(), dataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        bd.recyclerView.setLayoutManager(layoutManager);
        bd.recyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity()));
        bd.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<MsgItem>() {
            @Override
            public void onItemClick(int position, MsgItem data) {
                UserBean u = data.user;
                if (u != null) {
                    Intent intent = new Intent(getContext(), ChatActivity.class);
                    intent.putExtra(Skip.KEY_USER_ID, u.userId);
                    startActivity(intent);
                }

            }
        });
        refreshData();
    }

    private void refreshData() {
        List<MsgItem> msgItems = GreenDaoManager.getInstance().getLastMessageWithEveryFriend(curUser.getUserId());
        if (msgItems == null || msgItems.isEmpty()) {
            bd.txtNochat.setVisibility(View.VISIBLE);
        } else {
            bd.txtNochat.setVisibility(View.GONE);
            Collections.sort(msgItems, new MsgItemComparator());
            dataList.clear();
            dataList.addAll(msgItems);
            adapter.notifyDataSetChanged();
        }
        setUnreadMsgNum();
    }

    private void setUnreadMsgNum() {
        int unreadNum = GreenDaoManager.getInstance().getAllUnreadMsgNum(curUser.userId);
        ((MainActivity) getActivity()).setUnreadMsgLabel(unreadNum);
    }

    @Subscriber(tag = EventCode.REFRESH_CONVERSATION_LIST)
    private void onMessageDataChanged(Object event) {
        refreshData(); //重新加载数据
    }

    public class MsgItemComparator implements Comparator<MsgItem> {
        public int compare(MsgItem o1, MsgItem o2) {
            return o2.msg.getDate().compareTo(o1.msg.getDate());
        }
    }

}
