package cn.tongchengyuan.dynamic;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import cn.tongchengyuan.chat.adpter.ExpressionPagerAdapter;
import cn.tongchengyuan.chat.ui.CommonExpressionView;
import cn.tongchengyuan.helper.SoftInputListener;

import com.same.city.love.R;
import com.style.utils.CommonUtil;
import com.style.view.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 夏军 on 2016/8/7.
 */
public class PublishDynamicHelper {
    private static final String TAG = "PublishDynamicHelper";
    EditText etContent;
    View layoutRoot;
    Button viewSmile;
    LinearLayout layoutBottomSmile;
    LinearLayout layoutFace;
    ViewPager facePager;
    CirclePageIndicator indicator;

    private AppCompatActivity mActivity;
    Handler mHandler = new Handler();
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;


    public PublishDynamicHelper(final AppCompatActivity mActivity, EditText etContent) {
        this.mActivity = mActivity;
        this.etContent = etContent;
        //不能是DecorView，DecorView不能监听layout变化
        layoutRoot = mActivity.findViewById(R.id.layout_root);//mActivity.getWindow().getDecorView();
        layoutBottomSmile = (LinearLayout) layoutRoot.findViewById(R.id.layout_bottom_smile);
        viewSmile = layoutRoot.findViewById(R.id.view_smile);
        layoutFace = (LinearLayout) layoutRoot.findViewById(R.id.layout_face);
        facePager = (ViewPager) layoutRoot.findViewById(R.id.face_pager);
        indicator = (CirclePageIndicator) layoutRoot.findViewById(R.id.indicator);

        //监听软键盘显示状态
        //获取屏幕高度
        screenHeight = mActivity.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        //添加layout大小发生改变监听器
        /*layoutRoot.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    Log.e(TAG, "监听到软键盘弹起");
                    layoutBottomSmile.setVisibility(View.VISIBLE);
                    viewSmile.setChecked(false);
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    Log.e(TAG, "监听到软件盘关闭");
                    //如果是切换到表情面板而隐藏流量输入法，需要延迟判断表情面板是否显示，如果表情面板是关闭的，操作栏也关闭
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //如果表情面板是关闭的，操作栏也关闭
                            if (layoutFace.getVisibility() == View.GONE){
                                viewSmile.setChecked(false);
                                layoutBottomSmile.setVisibility(View.GONE);
                            }
                        }
                    }, 200);
                }
            }
        });*/
        final View decorView = mActivity.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new SoftInputListener(decorView, layoutRoot) {
            @Override
            protected void onSoftInputOpen() {
                closeEmotionContainer();
                //showSoftInput();
            }

            @Override
            protected void onSoftInputClose() {
                //如果是切换到表情面板而隐藏流量输入法，需要延迟判断表情面板是否显示，如果表情面板是关闭的，操作栏也关闭
               /* mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //如果表情面板是关闭的，操作栏也关闭
                        if (layoutFace.getVisibility() == View.GONE) {
                            viewSmile.setSelected(false);
                            layoutBottomSmile.setVisibility(View.GONE);
                        }
                    }
                }, 200);*/
            }
        });
        viewSmile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewSmile.isSelected()) {
                    closeEmotionContainer();
                    showSoftInput();
                } else {
                    openEmotionContainer();
                }
            }
        });

        this.etContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                closeEmotionContainer();
                //showSoftInput();
                // 这句话说的意思告诉父View我自己的事件我自己处理
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    private void openEmotionContainer() {
        //先隐藏输入法,然后延迟显示
        hiddenSoftInput();
        //延迟显示，先让输入法隐藏
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewSmile.setSelected(true);
                layoutFace.setVisibility(View.VISIBLE);
            }
        }, 100);

    }

    private void closeEmotionContainer() {
        viewSmile.setSelected(false);//还原表情状态
        layoutFace.setVisibility(View.GONE);
    }

    public void hiddenSoftInput() {
        CommonUtil.hiddenSoftInput(mActivity);
    }

    public void showSoftInput() {
        CommonUtil.showSoftInput(mActivity, PublishDynamicHelper.this.etContent);
    }

    public void onCreate() {
        initFace();

    }

    private void initFace() {
        List<View> views = new ArrayList<>();
        View gv = CommonExpressionView.getStickerView(mActivity, etContent);
        views.add(gv);
        facePager.setAdapter(new ExpressionPagerAdapter(views));
        indicator.setViewPager(facePager);

    }

}
