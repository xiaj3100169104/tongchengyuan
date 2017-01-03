package com.juns.wechat.dynamic;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.juns.wechat.R;
import com.juns.wechat.bean.DynamicBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.common.HttpAction;
import com.juns.wechat.net.common.NetDataBeanCallback;
import com.juns.wechat.util.ImageLoader;
import com.makeramen.roundedimageview.RoundedImageView;
import com.style.base.BaseToolbarActivity;
import com.style.constant.Skip;
import com.style.utils.CommonUtil;
import com.style.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;


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
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.ptrFrame)
    PtrClassicFrameLayout ptrFrame;
    @Bind(R.id.tv_nick)
    TextView tvNick;
    @Bind(R.id.iv_avatar)
    RoundedImageView ivAvatar;

    private static List<DynamicBean> cacheList;
    private List<DynamicBean> dataList;
    private DynamicAdapter adapter;
    private int page = 1;
    private int action = ACTION_REFRESH;
    private UserBean curUser;
    private FriendCircleHelper facehelper;

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
        ImageLoader.loadAvatar(ivAvatar, curUser.getHeadUrl());
        tvNick.setText(curUser.getShowName());

        facehelper = new FriendCircleHelper(this);
        facehelper.onCreate();

        dataList = new ArrayList<>();
        adapter = new DynamicAdapter(this, dataList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setAdapter(adapter);
        ptrFrame.setMode(PtrFrameLayout.Mode.BOTH);//可改变模式
        ptrFrame.setPtrHandler(new PtrDefaultHandler2() {

            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                //updateData();
                action = ACTION_LOAD_MORE;
                getData();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //page = 1;
                //updateData();
                action = ACTION_REFRESH;
                getData();
            }

        });

        adapter.setOnClickDiscussListener(new DynamicAdapter.OnClickDiscussListener() {
            @Override
            public void OnClickSupport(int position, Object data) {

            }

            @Override
            public void OnClickComment(int position, Object data) {
                tag = COMMENT;
                resetEditText();
                CommonUtil.showSoftInput(getContext(), facehelper.etContent);

            }
        });
        facehelper.btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = facehelper.etContent.getText().toString();
                if (tag == COMMENT) {
                    //addComment2Dynamic(content);
                } else if (tag == REPLY) {
                    //addReply2Comment(curCmtIndex, content);
                } else if (tag == REPLY_REPLY) {
                    //addReply2Reply(curCmtIndex, curReplyIndex, content);
                }
            }
        });
        //防止刚进去不显示头部
        if (cacheList != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dataList.addAll(cacheList);
                    adapter.notifyDataSetChanged();
                }
            }, 500);
        }

        //缓存加载完了再执行刷新
        ptrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrFrame.autoRefresh(true);
            }
        }, 1000);
    }

    private void resetEditText() {
        facehelper.etContent.setText("");
    }

    private void getData() {
        int dynamicId = 0;
        if (action == ACTION_LOAD_MORE) {
            if (dataList.size() > 0) {
                dynamicId = dataList.get(dataList.size() - 1).getDynamicId();
            }
        }
        HttpAction.getFriendCircleDynamic(action, dynamicId, 6, new NetDataBeanCallback<List<DynamicBean>>(new TypeReference<List<DynamicBean>>() {
        }) {
            @Override
            protected void onCodeSuccess(List<DynamicBean> data) {
                ptrFrame.refreshComplete();
                if (data != null && data.size() > 0) {
                    if (action == ACTION_REFRESH) {
                        dataList.clear();
                        setFirstPageCacheData(data);
                    }
                    dataList.addAll(data);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            protected void onCodeFailure(String msg) {
                ptrFrame.refreshComplete();
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

    protected void updateData() {
        ptrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrFrame.refreshComplete();
                /*if (page == 1) {
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        list.add("item-----" + String.valueOf(i));
                    }
                    adapter.clearData();
                    adapter.addData(list);
                } else {
                    List<String> list = new ArrayList<>();
                    int size = adapter.getItemCount();
                    int count = size + 10;
                    for (int i = size; i < count; i++) {
                        list.add("item-----" + String.valueOf(i));
                    }
                    adapter.addData(list);
                }
                page++;*/
            }
        }, 1000);
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
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }
}