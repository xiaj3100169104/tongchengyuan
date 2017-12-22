package com.style.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.same.city.love.R;

public abstract class BaseToolbarActivity extends BaseActivity {
    private Toolbar toolbar;
    private TextView tvTitleBase;
    private ImageView ivBaseToolbarReturn;

    protected void customTitleOptions(View mContentView) {
        toolbar = (Toolbar) mContentView.findViewById(R.id.toolbar);
        ivBaseToolbarReturn = (ImageView) mContentView.findViewById(R.id.iv_base_toolbar_Return);
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

    public Toolbar getToolbar() {
        return toolbar;
    }

    public ImageView getIvBaseToolbarReturn() {
        return ivBaseToolbarReturn;
    }

    protected void onClickTitleBack() {
        onBackFinish();
    }

    protected void setNavigationIcon(int resId) {
        toolbar.setNavigationIcon(getResources().getDrawable(resId));
    }

    protected void setToolbarTitle(String text) {
        tvTitleBase.setText(getNotNullText(text));
    }

    protected void setToolbarTitle(int resId) {
        setToolbarTitle(getString(resId));
    }


}
