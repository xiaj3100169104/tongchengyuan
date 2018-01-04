package cn.tongchengyuan.helper;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import static com.style.utils.CommonUtil.getScreenHeight;

/**
 * Created by xiajun on 2018/1/4.
 */

public class SoftInputListener implements ViewTreeObserver.OnGlobalLayoutListener {
    final Rect rect = new Rect();
    final int screenHeight;
    private final View decorView;
    private final View content;
    //阀值设置为屏幕高度的1/3
    int keyHeight;

    public SoftInputListener(View decorView, View content) {
        this.decorView = decorView;
        this.content = content;
        screenHeight = getScreenHeight(decorView.getContext());
        keyHeight = screenHeight / 3;

    }

    @Override
    public void onGlobalLayout() {
        Log.e("SoftInputListener", "onGlobalLayout");
        decorView.getWindowVisibleDisplayFrame(rect);
        int heightDifference = screenHeight - rect.bottom;//计算软键盘占有的高度  = 屏幕高度 - 视图可见高度
        ViewGroup.LayoutParams layoutParams = content.getLayoutParams();
        ((ViewGroup.MarginLayoutParams) layoutParams).setMargins(0, 0, 0, heightDifference);//设置rlContent的marginBottom的值为软键盘占有的高度即可
        content.requestLayout();
        if (heightDifference > keyHeight) {
            Log.e("onGlobalLayout", "监听到软键盘弹起");
            onSoftInputOpen();
        }else {
            Log.e("onGlobalLayout", "监听到软件盘关闭");
            onSoftInputClose();
        }
    }

    protected void onSoftInputClose() {

    }

    protected void onSoftInputOpen() {

    }
}
