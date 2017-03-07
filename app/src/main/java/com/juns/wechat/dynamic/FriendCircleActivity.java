package com.juns.wechat.dynamic;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.juns.wechat.R;
import com.juns.wechat.bean.CommentBean;
import com.juns.wechat.bean.DynamicBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.common.HttpAction;
import com.juns.wechat.net.common.NetDataBeanCallback;
import com.juns.wechat.util.ImageLoader;
import com.makeramen.roundedimageview.RoundedImageView;
import com.style.base.BaseToolbarActivity;
import com.style.constant.Skip;
import com.style.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2016/4/11.
 */
public class FriendCircleActivity extends BaseToolbarActivity {
    private static int ACTION_REFRESH = 0;
    private static int ACTION_LOAD_MORE = 1;
    private static final int COMMENT = 0;
    private static final int REPLY = 1;
    private static final int REPLY_REPLY = 2;

    private int tag = COMMENT;
    @Bind(R.id.list)
    LRecyclerView mRecyclerView;

    private static List cacheList;
    private List<DynamicBean> dataList;
    private DynamicAdapter mDataAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private int page = 1;
    private int action = ACTION_REFRESH;
    private UserBean curUser;
    private FriendCircleHelper faceHelper;
    private int curDynamicPosition;
    private int curCommentPosition;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle arg0) {
        mLayoutResID = R.layout.activity_friend_circle;
        super.onCreate(arg0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friend_circle, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.select:
                skipForResult(DynamicPublishActivity.class, Skip.CODE_PUBLISH_DYNAMIC);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {
        setToolbarTitle(R.string.moments);

        //Glide.with(this).load(R.drawable.pig).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivAvatar);
        curUser = AccountManager.getInstance().getUser();

        faceHelper = new FriendCircleHelper(this);
        faceHelper.onCreate();
        faceHelper.hideLayoutBottom();

        dataList = new ArrayList<>();
        mDataAdapter = new DynamicAdapter(this, dataList);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        View header = LayoutInflater.from(this).inflate(R.layout.header_friend_circle, (ViewGroup) mRecyclerView.getParent(), false);
        mLRecyclerViewAdapter.addHeaderView(header);
        HeaderViewHolder headerViewHolder = new HeaderViewHolder(header);
        ImageLoader.loadAvatar(headerViewHolder.ivAvatar, curUser.getHeadUrl());
        headerViewHolder.tvNick.setText(curUser.getShowName());

        //禁用下拉刷新功能
        mRecyclerView.setPullRefreshEnabled(true);
        //禁用自动加载更多功能
        mRecyclerView.setLoadMoreEnabled(true);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallPulse);
        mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallPulse);
        //设置头部加载颜色
        mRecyclerView.setHeaderViewColor(R.color.white, android.R.color.white, R.color.black);
        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.gray, android.R.color.darker_gray, R.color.white);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                action = ACTION_REFRESH;
                getData();
            }

            @Override
            public void onLoadMore() {
                action = ACTION_LOAD_MORE;
                getData();
            }

        });

        mDataAdapter.setOnClickDiscussListener(new DynamicAdapter.OnClickDiscussListener() {
            @Override
            public void OnClickSupport(int dynamicPosition, Object data) {

            }

            @Override
            public void OnClickComment(int position, Object data) {
                logE(TAG, "OnClickComment==" + position);
                resetEditText();
                faceHelper.showEditLayout();
                tag = COMMENT;
                curDynamicPosition = position;
            }

            @Override
            public void OnClickReply(int position, int subPosition, Object data) {
                logE(TAG, "OnClickReply==" + position + "--" + subPosition);
                //自己不能回复自己
                DynamicBean dynamicBean = (DynamicBean) dataList.get(position);
                if (dynamicBean.getCommentList().get(curCommentPosition).getCommenterId() != curUser.getUserId()) {
                    resetEditText();
                    faceHelper.showEditLayout();
                    tag = REPLY;
                    curDynamicPosition = position;
                    curCommentPosition = subPosition;
                }
            }
        });
        faceHelper.btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = faceHelper.etContent.getText().toString();
                addComment2Dynamic(content);

            }
        });
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                faceHelper.hideAllLayout();
                return false;
            }
        });

        if (cacheList != null) {
            mRecyclerView.setLoadMoreEnabled(true);
            dataList.addAll(cacheList);
            mDataAdapter.notifyDataSetChanged();
        }

        //先加载缓存，再延迟刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }, 500);
    }

    private void addComment2Dynamic(final String content) {
        final DynamicBean dynamicBean = dataList.get(curDynamicPosition);
        int replyUserId = -1;//表示直接评论动态
        if (tag == REPLY) {
            replyUserId = dynamicBean.getCommentList().get(curCommentPosition).getCommenterId();
        }
        HttpAction.addComment2Dynamic(dynamicBean.getDynamicId(), replyUserId, content, new NetDataBeanCallback<CommentBean>(CommentBean.class) {
            @Override
            protected void onCodeSuccess(CommentBean data) {
                if (data != null) {
                    faceHelper.sendComplete();
                    List<CommentBean> list = dynamicBean.getCommentList();
                    if (list == null)
                        list = new ArrayList();
                /*CommentBean commentBean = new CommentBean();
                commentBean.setCommenterId(dynamicBean.getDynamicId());
                commentBean.setUser(curUser);
                commentBean.setContent(content);*/
                    list.add(data);
                    dynamicBean.setCommentList(list);
                    mDataAdapter.notifyItemChanged(curDynamicPosition);
                }
            }

            @Override
            protected void onCodeFailure(String msg) {
                super.onCodeFailure(msg);
            }
        });
    }

    private void resetEditText() {
        faceHelper.resetEditText();
    }

    private void getData() {
        int dynamicId = 0;
        if (action == ACTION_LOAD_MORE) {
            if (dataList.size() > 1) {
                DynamicBean dynamicBean = dataList.get(dataList.size() - 1);
                dynamicId = dynamicBean.getDynamicId();
            }
        }
        HttpAction.getFriendCircleDynamic(action, dynamicId, 6, new NetDataBeanCallback<List<DynamicBean>>(new TypeReference<List<DynamicBean>>() {
        }) {
            @Override
            protected void onCodeSuccess(List<DynamicBean> data) {
                mRecyclerView.refreshComplete(5);

                if (data != null && data.size() > 0) {
                    mRecyclerView.setLoadMoreEnabled(true);

                    if (action == ACTION_REFRESH) {
                        dataList.clear();
                        setFirstPageCacheData(data);
                    }
                    dataList.addAll(data);
                    mDataAdapter.notifyDataSetChanged();
                } else {
                    //the end
                    mRecyclerView.setNoMore(true);
                }
            }

            @Override
            protected void onCodeFailure(String msg) {
                mRecyclerView.refreshComplete(5);
                showToast(msg);
            }
        });
    }

    private void setFirstPageCacheData(List<DynamicBean> data) {
        if (cacheList == null)
            cacheList = new ArrayList<>();
        cacheList.clear();
        cacheList.addAll(data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Skip.CODE_PUBLISH_DYNAMIC:
                    if (data != null) {
                        DynamicBean bean = (DynamicBean) data.getSerializableExtra("sendDynamic");
                        dataList.add(0, bean);
                        mDataAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //  If null, all callbacks and messages will be removed.
        handler.removeCallbacksAndMessages(null);
    }

    static class HeaderViewHolder {
        @Bind(R.id.tv_nick)
        TextView tvNick;
        @Bind(R.id.iv_avatar)
        RoundedImageView ivAvatar;

        public HeaderViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}