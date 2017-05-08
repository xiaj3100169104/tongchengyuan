package com.style.base;

import android.view.View;
import android.widget.TextView;

import com.same.city.love.R;


public abstract class BaseToolbarBtnActivity extends BaseToolbarActivity {

    private TextView viewToolbarRight;

    @Override
    protected void customTitleOptions(View mContentView) {
        super.customTitleOptions(mContentView);
        viewToolbarRight = (TextView) mContentView.findViewById(R.id.view_toolbar_right);
        viewToolbarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTitleRightView();
            }
        });
    }

    protected void onClickTitleRightView() {

    }

    public TextView getToolbarRightView() {
        return viewToolbarRight;
    }

    public void setViewToolbarRight(TextView viewToolbarRight) {
        this.viewToolbarRight = viewToolbarRight;
    }
}
