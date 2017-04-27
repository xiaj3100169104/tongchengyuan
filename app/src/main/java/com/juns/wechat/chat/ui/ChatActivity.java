package com.juns.wechat.chat.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.juns.wechat.Constants;
import com.juns.wechat.R;
import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.chat.bean.MessageBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.chat.xmpp.util.SendMessage;
import com.juns.wechat.database.dao.DbDataEvent;
import com.juns.wechat.database.dao.FriendDao;
import com.juns.wechat.database.ChatTable;
import com.juns.wechat.database.dao.MessageDao;
import com.juns.wechat.database.dao.UserDao;
import com.juns.wechat.exception.UserNotFoundException;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.util.LogUtil;
import com.juns.wechat.util.ThreadPoolUtil;
import com.style.base.BaseToolbarActivity;
import com.style.constant.Skip;
import com.style.dialog.PromptDialog;

import org.simple.eventbus.Subscriber;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

//聊天页面
public class ChatActivity extends BaseToolbarActivity {


    public static final int CHATTYPE_SINGLE = 1;

    public static final String COPY_IMAGE = "EASEMOBIMG";

    private static final int SIZE = 10;
    @Bind(R.id.list)
    LRecyclerView mRecyclerView;

    private ChatInputManager chatInputManager;
    private ChatActivityHelper chatActivityHelper;
    private ChatAdapter mDataAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private UserBean account = AccountManager.getInstance().getUser();
    private FriendBean friendBean;
    private UserBean contactUser;
    private boolean mFirstLoad = true; //是否第一次加载数据
    private List<MessageBean> msgViewModels;
    private Handler mHandler = new Handler();

    private int chatType;
    public String playMsgId;
    private int contactId;
    private String contactName;

    private PromptDialog reSendDialog;
    private PromptDialog deleteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_chat;
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_right_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.avatar_select:
                //goonNext();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra(Skip.KEY_USER_ID);

    }

    public void initData() {
        // 判断单聊还是群聊
        chatType = getIntent().getIntExtra(Constants.TYPE, CHATTYPE_SINGLE);
        if (chatType == CHATTYPE_SINGLE) { // 单聊
        } else {// 群聊
        }

        contactId = getIntent().getIntExtra(Skip.KEY_USER_ID, 0);
        friendBean = FriendDao.getInstance().findByOwnerAndContactName(account.getUserId(), contactId);
        contactUser = UserDao.getInstance().findByUserId(friendBean.getContactId());
        if (contactUser == null) {
            finish();
            return;
        }

        contactName = contactUser.getUserName();
        String showName = !TextUtils.isEmpty(friendBean.getRemark()) ? friendBean.getRemark() : contactUser.getShowName();

        setToolbarTitle(showName);

        chatInputManager = new ChatInputManager(this, contactName);
        chatInputManager.onCreate();
        chatActivityHelper = new ChatActivityHelper(this, contactName);
        chatActivityHelper.onCreate();

        msgViewModels = new ArrayList<>();
        mDataAdapter = new ChatAdapter(this, msgViewModels);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLoadMoreEnabled(false);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallPulse);
        mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        //mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallPulse);
        //设置头部加载颜色
        mRecyclerView.setHeaderViewColor(R.color.white, android.R.color.white, R.color.colorAccent);

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                int queryIndex = chatActivityHelper.getQueryIndex();
                if (queryIndex < 0) {
                    mRecyclerView.refreshComplete(5);
                } else {
                    chatActivityHelper.loadMessagesFromDb();
                }
            }

            @Override
            public void onLoadMore() {

            }
        });

        chatActivityHelper.loadMessagesFromDb();

    }

    /**
     * {@link ChatActivityHelper#loadMessagesFromDb()}方法完成之后调用
     */
    public void loadDataComplete(boolean hasNewData) {
        mRecyclerView.refreshComplete(5);
        if (hasNewData) {
            mDataAdapter.notifyDataSetChanged();  //ChatActivityHelper已经更新数据源
        }
        //第一次加载数据，listView要滚动到底部，下拉刷新加载出来的数据，不用滚动到底部
        if (mFirstLoad) {
            scrollListViewToBottom();
            mFirstLoad = false;
        }
    }

    public void refreshOneData(boolean scroll2bottom) {
        mDataAdapter.notifyDataSetChanged();
        if (scroll2bottom) {
            scrollListViewToBottom();
        }
    }

    public List<MessageBean> getMsgViewModels() {
        return msgViewModels;
    }

    private void scrollListViewToBottom() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.smoothScrollToPosition(mDataAdapter.getItemCount());
            }
        });
    }

    /***
     * 监听数据库中消息表数据的变化
     *
     * @param event
     * @see ChatActivityHelper#processOneMessage(List, MessageBean, int)
     */
    @Subscriber(tag = ChatTable.TABLE_NAME)
    private void onDdDataChanged(DbDataEvent<MessageBean> event) {
        if (event.data == null || event.data.isEmpty()) return;
        LogUtil.i("data: " + event.data.toString() + "action: " + event.action);
        MessageBean messageBean = event.data.get(0);
        chatActivityHelper.processOneMessage(msgViewModels, messageBean, event.action);
    }

    /**
     * onActivityResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        chatInputManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     *  清空聊天记录
     */
    public void emptyHistory() {
        String st5 = getResources().getString(R.string.Whether_to_empty_all_chats);

    }

    @Override
    protected void onPause() {
        super.onPause();
        ChatMediaPlayer.getInstance().release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatInputManager.onDestroy();
        chatActivityHelper.onDestroy();
    }

    public void reSend(final MessageBean messageBean) {
        reSendDialog = new PromptDialog(getContext());
        reSendDialog.setMessage("确定要重发这条消息？");
        reSendDialog.setListener(new PromptDialog.OnPromptListener() {
            @Override
            public void onPositiveButton() {
                SendMessage.reSendMsg(messageBean);
            }

            @Override
            public void onNegativeButton() {
                reSendDialog.dismiss();
            }
        });
        reSendDialog.show();
    }

    public void delete(final MessageBean messageBean) {
        deleteDialog = new PromptDialog(getContext());
        deleteDialog.setMessage("确定要删除这条消息？");
        deleteDialog.setListener(new PromptDialog.OnPromptListener() {
            @Override
            public void onPositiveButton() {
                MessageDao.getInstance().deleteOne(messageBean);

            }

            @Override
            public void onNegativeButton() {
                deleteDialog.dismiss();
            }
        });
        deleteDialog.show();
    }
}
