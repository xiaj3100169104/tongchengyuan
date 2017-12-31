package com.style.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.same.city.love.R;


/**
 * Created by xiajun on 2017/1/5.
 */

public abstract class BasePopupWindow extends PopupWindow {
    private Context mContext;

    public BasePopupWindow(Context context, View mContentView) {
        super();
        mContext = context;
        setContentView(mContentView);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.divider_transparent));
    }

    public BasePopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }
}
