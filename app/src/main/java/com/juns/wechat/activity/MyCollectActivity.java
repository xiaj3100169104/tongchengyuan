package com.juns.wechat.activity;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.same.city.love.R;
import com.style.base.BaseToolbarActivity;


/**
 * Created by Administrator on 2016/4/11.
 */
public class MyCollectActivity extends BaseToolbarActivity {

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

    }
}