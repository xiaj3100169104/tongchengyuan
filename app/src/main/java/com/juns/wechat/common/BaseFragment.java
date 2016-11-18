package com.juns.wechat.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import org.simple.eventbus.EventBus;

/**
 * Created by 王者 on 2016/8/7.
 */
public class BaseFragment extends com.style.base.BaseFragment{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(registerEventBus()){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(registerEventBus()){
            EventBus.getDefault().unregister(this);
        }
    }

    protected boolean registerEventBus(){
        return false;
    }
}
