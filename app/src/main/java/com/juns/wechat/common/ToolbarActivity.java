package com.juns.wechat.common;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.juns.wechat.R;
import org.simple.eventbus.EventBus;

public abstract class ToolbarActivity extends BaseActivity {
    private Toolbar toolbar;
    private TextView tvTitleBase;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        if (registerEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    protected void customTitleOptions(View mContentView) {
        toolbar = (Toolbar) mContentView.findViewById(R.id.toolbar);
        ImageView ivBaseToolbarReturn = (ImageView) mContentView.findViewById(R.id.iv_base_toolbar_Return);
        tvTitleBase = (TextView) mContentView.findViewById(R.id.tv_base_toolbar_title);
        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
        }
        ivBaseToolbarReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTitleBack();
            }
        });
    }

    protected void onClickTitleBack() {
        onFinish();
    }

    protected void setNavigationIcon(int resId) {
        toolbar.setNavigationIcon(getResources().getDrawable(resId));
    }

    protected void setToolbarTitle(String text) {
        setText(tvTitleBase, text);
    }

    protected void setToolbarTitle(int resId) {
        setText(tvTitleBase, resId);
    }

    protected boolean registerEventBus() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (registerEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }
}
