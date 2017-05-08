package com.juns.wechat.dynamic;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.same.city.love.R;
import com.style.base.BasePopupWindow;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xiajun on 2017/1/5.
 */

public class CommentPopupWindow extends BasePopupWindow {
    @Bind(R.id.tv_support)
    TextView tvSupport;
    @Bind(R.id.ll_support)
    LinearLayout llSupport;
    @Bind(R.id.ll_comment)
    LinearLayout llComment;

    public CommentPopupWindow(Context context, ViewGroup rootView, int resId) {
        super(context, rootView, resId);
    }
}
