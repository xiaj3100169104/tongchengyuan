package cn.tongchengyuan.dynamic;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import cn.tongchengyuan.bean.CommentBean;
import cn.tongchengyuan.greendao.mydao.GreenDaoManager;
import cn.tongchengyuan.manager.AccountManager;
import cn.tongchengyuan.net.request.HttpActionImpl;
import cn.tongchengyuan.bean.DynamicBean;
import cn.tongchengyuan.bean.UserBean;

import com.same.city.love.R;
import com.same.city.love.databinding.ActivityFriendCircleBinding;
import com.same.city.love.databinding.HeaderFriendCircleBinding;
import com.style.base.BaseToolbarActivity;
import com.style.constant.Skip;
import com.style.manager.ImageLoader;
import com.style.net.core.NetDataBeanCallback;
import com.style.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;


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
     ActivityFriendCircleBinding bd;
    private FriendCircleHelper faceHelper;

    private static List cacheList;
    private List<DynamicBean> dataList;
    private DynamicAdapter mDataAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private int page = 1;
    private int action = ACTION_REFRESH;
    private UserBean curUser;
    private int curDynamicPosition;
    private int curCommentPosition;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_friend_circle);
        super.setContentView(bd.getRoot());
        initData();
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
        setAppBarLayoutTransparent();
        //Glide.with(this).load(R.drawable.pig).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivAvatar);
        curUser = AccountManager.getInstance().getUser();

        faceHelper = new FriendCircleHelper(this);
        faceHelper.onCreate();
        faceHelper.hideLayoutBottom();

        dataList = new ArrayList<>();
        mDataAdapter = new DynamicAdapter(this, dataList);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        bd.recyclerView.setAdapter(mLRecyclerViewAdapter);
        bd.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bd.recyclerView.addItemDecoration(new DividerItemDecoration(this));
        HeaderFriendCircleBinding header = DataBindingUtil.inflate(mInflater, R.layout.header_friend_circle, (ViewGroup) bd.recyclerView.getParent(), false);
        //View header = LayoutInflater.from(this).inflate(R.layout.header_friend_circle, (ViewGroup) bd.recyclerView.getParent(), false);
        mLRecyclerViewAdapter.addHeaderView(header.getRoot());
        ImageLoader.loadAvatar(this, header.ivAvatar, curUser.getHeadUrl());
        header.tvNick.setText(curUser.getShowName());

        //禁用下拉刷新功能
        bd.recyclerView.setPullRefreshEnabled(true);
        //禁用自动加载更多功能
        bd.recyclerView.setLoadMoreEnabled(true);
        bd.recyclerView.setRefreshProgressStyle(ProgressStyle.BallPulse);
        bd.recyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        bd.recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallPulse);
        //设置头部加载颜色
        bd.recyclerView.setHeaderViewColor(R.color.white, android.R.color.white, R.color.bg_refresh_view);
        //设置底部加载颜色
        bd.recyclerView.setFooterViewColor(R.color.gray, android.R.color.darker_gray, R.color.white);
        //设置底部加载文字提示
        bd.recyclerView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        bd.recyclerView.setOnRefreshListener(new OnRefreshListener() {

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

        bd.recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                faceHelper.hideAllLayout();
                return false;
            }
        });

       /* if (cacheList != null) {
            mRecyclerView.setLoadMoreEnabled(true);
            dataList.addAll(cacheList);
            mDataAdapter.notifyDataSetChanged();
        }*/

        //先加载缓存，再延迟刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }, 500);
    }

    public void addComment2Dynamic(String content) {
        final DynamicBean dynamicBean = dataList.get(curDynamicPosition);
        String replyUserId = null;//表示直接评论动态
        if (tag == REPLY) {
            replyUserId = dynamicBean.getCommentList().get(curCommentPosition).getCommenterId();
        }
        HttpActionImpl.getInstance().addComment2Dynamic(TAG, dynamicBean.getDynamicId(), replyUserId, content, new NetDataBeanCallback<CommentBean>(CommentBean.class) {
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
        String dynamicId = null;
        if (action == ACTION_LOAD_MORE) {
            if (dataList.size() > 1) {
                DynamicBean dynamicBean = dataList.get(dataList.size() - 1);
                dynamicId = dynamicBean.getDynamicId();
            }
        }
       /* HttpActionImpl.getInstance().getFriendCircleDynamic(TAG, action, dynamicId, 6, new NetDataBeanCallback<List<DynamicBean>>(new TypeReference<List<DynamicBean>>() {
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
        });*/
        int offset = 0;
        if (action == ACTION_REFRESH) {
            offset = 0;
        } else {
            offset = dataList.size();
        }
        List<DynamicBean> data = GreenDaoManager.getInstance().queryByPage(offset, 5, curUser.getUserId());
        bd.recyclerView.refreshComplete(5);
        if (data != null && data.size() > 0) {
            bd.recyclerView.setLoadMoreEnabled(true);

            if (action == ACTION_REFRESH) {
                List<CommentBean> commentList = new ArrayList<>();
                CommentBean comment0 = new CommentBean("你瞅啥？", "杜磊", "");
                CommentBean comment1 = new CommentBean("瞅你咋滴?", "Style2", "杜磊");
                CommentBean comment2 = new CommentBean("你再瞅一个试试？", "杜磊", "Style2");
                CommentBean comment3 = new CommentBean("试试就试试[(A1)]", "Style2", "杜磊");
                commentList.add(comment0);
                commentList.add(comment1);
                commentList.add(comment2);
                commentList.add(comment3);
                data.get(0).setCommentList(commentList);

                List<CommentBean> commentList2 = new ArrayList<>();
                CommentBean comment20 = new CommentBean("从前有座山[(B2)][(B2)]", "任虹锦", "");
                CommentBean comment21 = new CommentBean("山里有座庙", "石皓", "任虹锦");
                CommentBean comment22 = new CommentBean("庙里住着一个老和尚和一个小和尚", "任虹锦", "石皓");
                CommentBean comment23 = new CommentBean("老和尚对小和尚说", "石皓", "任虹锦");
                commentList2.add(comment20);
                commentList2.add(comment21);
                commentList2.add(comment22);
                commentList2.add(comment23);
                data.get(1).setCommentList(commentList2);

                dataList.clear();
                setFirstPageCacheData(data);
            }
            dataList.addAll(data);
            mDataAdapter.notifyDataSetChanged();
        } else {
            //the end
            bd.recyclerView.setNoMore(true);
        }
    }

    private void setFirstPageCacheData(List<DynamicBean> data) {
        /*if (cacheList == null)
            cacheList = new ArrayList<>();
        cacheList.clear();
        cacheList.addAll(data);*/
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
}