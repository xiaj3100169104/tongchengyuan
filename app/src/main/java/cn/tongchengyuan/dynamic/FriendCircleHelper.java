package cn.tongchengyuan.dynamic;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;

import cn.tongchengyuan.chat.adpter.ExpressionPagerAdapter;
import cn.tongchengyuan.chat.ui.CommonExpressionView;
import cn.tongchengyuan.helper.SoftInputListener;

import com.same.city.love.databinding.ActivityFriendCircleBinding;
import com.style.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xj on 2016/8/7.
 */
public class FriendCircleHelper {
    private static final String TAG = "FriendCircleHelper";

    private FriendCircleActivity mActivity;
    private final ActivityFriendCircleBinding bd;

    Handler mHandler = new Handler();
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;


    public FriendCircleHelper(FriendCircleActivity mActivity) {
        this.mActivity = mActivity;
        this.bd = mActivity.bd;
        //监听软键盘显示状态
        //获取屏幕高度
        screenHeight = mActivity.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        //添加layout大小发生改变监听器//不能是DecorView，DecorView不能监听layout变化
       /* bd.layoutRoot.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    Log.e(TAG, "监听到软键盘弹起");
                    showLayoutBottom();
                    bd.bottom.viewSmile.setChecked(false);
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    Log.e(TAG, "监听到软件盘关闭");
                    //如果是切换到表情面板而隐藏流量输入法，需要延迟判断表情面板是否显示，如果表情面板是关闭的，操作栏也关闭
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //如果表情面板是关闭的，操作栏也关闭
                            if (bd.bottom.layoutFace.getVisibility() == View.GONE) {
                                bd.bottom.viewSmile.setChecked(false);
                                //layoutBottomSmile.setVisibility(View.GONE);
                            }
                        }
                    }, 200);
                }
            }
        });*/

        bd.bottom.etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (arg0.length() > 0)
                    bd.bottom.btDisDynamic.setEnabled(true);
                else
                    bd.bottom.btDisDynamic.setEnabled(false);
            }
        });
        bd.bottom.btDisDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = bd.bottom.etComment.getText().toString();
                FriendCircleHelper.this.mActivity.addComment2Dynamic(content);

            }
        });
        final View decorView = mActivity.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new SoftInputListener(decorView, bd.layoutRoot) {
            @Override
            protected void onSoftInputOpen() {
                //showLayoutBottom();
                hideLayoutFace();
            }

            @Override
            protected void onSoftInputClose() {
                //如果是切换到表情面板而隐藏流量输入法，需要延迟判断表情面板是否显示，如果表情面板是关闭的，操作栏也关闭
                /*mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //如果表情面板是关闭的，操作栏也关闭
                        if (bd.bottom.layoutFace.getVisibility() == View.GONE) {
                            bd.bottom.viewSmile.setChecked(false);
                            //layoutBottomSmile.setVisibility(View.GONE);
                        }
                    }
                }, 200);*/
            }
        });
        bd.bottom.viewSmile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bd.bottom.viewSmile.isSelected()) {
                    //隐藏表情面板，打开输入法
                    hideLayoutFace();
                    showSoftInput();
                } else {
                    showLayoutFace();
                }
            }
        });

        bd.bottom.etComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideLayoutFace();
                return false;
            }
        });
    }

    public void onCreate() {
        initFace();

    }

    public void resetEditText() {
        bd.bottom.etComment.setText("");
    }

    public void hideLayoutBottom() {
        bd.bottom.layoutEdit.setVisibility(View.GONE);
    }

    public void showLayoutBottom() {
        bd.bottom.layoutEdit.setVisibility(View.VISIBLE);
    }

    public void hideLayoutFace() {
        bd.bottom.viewSmile.setSelected(false);//还原表情状态
        bd.bottom.layoutFace.setVisibility(View.GONE);
    }

    public void showLayoutFace() {
        //隐藏输入法，打开表情面板
        hiddenSoftInput();
        //延迟显示，先让输入法隐藏
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bd.bottom.viewSmile.setSelected(true);
                bd.bottom.layoutFace.setVisibility(View.VISIBLE);
            }
        }, 100);
    }

    public void showEditLayout() {
        if (bd.bottom.layoutFace.getVisibility() == View.VISIBLE)
            return;
        hideLayoutFace();
        showLayoutBottom();
        showSoftInput();
    }

    public void hideAllLayout() {
        hiddenSoftInput();
        hideLayoutFace();
        hideLayoutBottom();
    }

    public void sendComplete() {
        resetEditText();
        hideLayoutFace();
        showLayoutBottom();
        hiddenSoftInput();
    }

    public void hiddenSoftInput() {
        CommonUtil.hiddenSoftInput(mActivity, bd.bottom.etComment);
    }

    public void showSoftInput() {
        CommonUtil.showSoftInput(mActivity, bd.bottom.etComment);
    }

    private void initFace() {
        List<View> views = new ArrayList<>();
        View gv1 = CommonExpressionView.getStickerView(mActivity, bd.bottom.etComment);
        views.add(gv1);

        bd.bottom.facePager.setAdapter(new ExpressionPagerAdapter(views));
        bd.bottom.indicator.setViewPager(bd.bottom.facePager);

    }

}
