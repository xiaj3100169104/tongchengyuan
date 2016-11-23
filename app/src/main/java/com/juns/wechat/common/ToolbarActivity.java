package com.juns.wechat.common;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;


import com.juns.wechat.util.ToolBarUtil;

import org.simple.eventbus.EventBus;

public abstract class ToolbarActivity extends BaseActivity {
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        toolbar = ToolBarUtil.setToolBar(this);
        if(registerEventBus()){
            EventBus.getDefault().register(this);
        }
    }

    protected void setToolbarTitle(String text){
        ToolBarUtil.setTitle(this, text);
    }

    protected void setToolbarRight(int type, int resId){
        ToolBarUtil.setToolbarRight(this, type, resId);
    }

    protected boolean registerEventBus(){
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(registerEventBus()){
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
