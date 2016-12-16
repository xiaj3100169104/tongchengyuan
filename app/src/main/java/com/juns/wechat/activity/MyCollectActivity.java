package com.juns.wechat.activity;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.juns.wechat.R;
import com.juns.wechat.adpter.DynamicAdapter;
import com.juns.wechat.adpter.MyCollectAdapter;
import com.style.base.BaseToolbarActivity;
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
public class MyCollectActivity extends BaseToolbarActivity {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.ptrFrame)
    PtrClassicFrameLayout ptrFrame;

    private List<String> dataList;
    private MyCollectAdapter adapter;
    private int page = 1;

    @Override
    protected void onCreate(Bundle arg0) {
        mLayoutResID = R.layout.activity_my_collect;
        super.onCreate(arg0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_collect, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                //skip(DynamicPublishActivity.class);
                break;
            case R.id.add:
                skip(AddNoteActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {
        setToolbarTitle(R.string.my_collection);

        dataList = new ArrayList<>();
        adapter = new MyCollectAdapter(this, dataList);
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

    }

    protected void updateData() {
        ptrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrFrame.refreshComplete();
                if (page == 1) {
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
                page++;
            }
        }, 1000);
    }
}