package cn.tongchengyuan.dynamic;

import android.content.Context;

import com.same.city.love.databinding.PopupwindowDiscussOptionBinding;
import com.style.base.BasePopupWindow;

/**
 * Created by xiajun on 2017/1/5.
 */

public class CommentPopupWindow extends BasePopupWindow {
    public final PopupwindowDiscussOptionBinding bd;

    public CommentPopupWindow(Context mContext, PopupwindowDiscussOptionBinding bd) {
        super(mContext, bd.getRoot());
        this.bd = bd;
    }
}
