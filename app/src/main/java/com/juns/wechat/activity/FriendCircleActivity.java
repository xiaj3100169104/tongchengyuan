package com.juns.wechat.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.stream.StreamUriLoader;
import com.juns.wechat.R;
import com.juns.wechat.adpter.DynamicAdapter;
import com.juns.wechat.bean.DynamicBean;
import com.style.base.BaseToolbarActivity;
import com.style.constant.Skip;
import com.style.utils.CommonUtil;
import com.style.utils.StreamUtil;
import com.style.view.DividerItemDecoration;

import org.xutils.common.util.FileUtil;

import java.io.IOException;
import java.io.InputStream;
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

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.ptrFrame)
    PtrClassicFrameLayout ptrFrame;
    @Bind(R.id.tv_nick)
    TextView tvNick;
    @Bind(R.id.iv_avatar)
    ImageView ivAvatar;

    private List<DynamicBean> dataList;
    private DynamicAdapter adapter;
    private int page = 1;

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

        Glide.with(this).load(R.drawable.pig).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivAvatar);

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
                updateData();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                updateData();
            }

        });
        ptrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrFrame.autoRefresh(true);
            }
        }, 500);

        adapter.setOnClickDiscussListener(new DynamicAdapter.OnClickDiscussListener() {
            @Override
            public void OnClickSupport(int position, Object data) {

            }

            @Override
            public void OnClickComment(int position, Object data) {
               /* Intent intent = new Intent(FriendCircleActivity.this, DynamicCommentActivity.class);
                intent.putExtra("position", position).putExtra("dynamic", "");
                startActivity(intent);*/

            }

            @Override
            public void OnClickCollection(int position, Object data) {

            }
        });
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