package com.juns.wechat.dynamic;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.same.city.love.R;
import com.juns.wechat.chat.adpter.ExpressionAdapter;
import com.juns.wechat.chat.adpter.ExpressionPagerAdapter;
import com.juns.wechat.chat.utils.SmileUtils;
import com.juns.wechat.chat.widght.ExpandGridView;
import com.style.utils.CommonUtil;
import com.style.view.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xj on 2016/8/7.
 */
public class FriendCircleHelper {
    private static final String TAG = "SimpleExpressionhelper";
    private static final int EMOTICONS_COUNT = 59;
    private static final String EMOTION_NAME_DELETE = "f_emotion_del_normal";
    Button btSend;
    EditText etContent;
    View layoutRoot;
    CheckBox viewSmile;
    ViewGroup layoutBottomSmile;
    LinearLayout layoutFace;
    ViewPager facePager;
    CirclePageIndicator indicator;

    private AppCompatActivity mActivity;
    Handler mHandler = new Handler();
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    private List<String> emoticonsFileNames;


    public FriendCircleHelper(AppCompatActivity mActivity) {
        this.mActivity = mActivity;
        //不能是DecorView，DecorView不能监听layout变化
        layoutRoot = mActivity.findViewById(R.id.layout_root);//mActivity.getWindow().getDecorView();
        layoutBottomSmile = (ViewGroup) layoutRoot.findViewById(R.id.layout_bottom_smile);
        etContent = (EditText) layoutRoot.findViewById(R.id.et_comment);
        viewSmile = (CheckBox) layoutRoot.findViewById(R.id.view_smile);
        btSend = (Button) layoutRoot.findViewById(R.id.bt_dis_dynamic);
        layoutFace = (LinearLayout) layoutRoot.findViewById(R.id.layout_face);
        facePager = (ViewPager) layoutRoot.findViewById(R.id.face_pager);
        indicator = (CirclePageIndicator) layoutRoot.findViewById(R.id.indicator);

        //监听软键盘显示状态
        //获取屏幕高度
        screenHeight = mActivity.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        //添加layout大小发生改变监听器
        layoutRoot.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    Log.e(TAG, "监听到软键盘弹起");
                    showLayoutBottom();
                    viewSmile.setChecked(false);
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    Log.e(TAG, "监听到软件盘关闭");
                    //如果是切换到表情面板而隐藏流量输入法，需要延迟判断表情面板是否显示，如果表情面板是关闭的，操作栏也关闭
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //如果表情面板是关闭的，操作栏也关闭
                            if (layoutFace.getVisibility() == View.GONE) {
                                viewSmile.setChecked(false);
                                //layoutBottomSmile.setVisibility(View.GONE);
                            }
                        }
                    }, 200);
                }
            }
        });
        viewSmile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    //隐藏输入法，打开表情面板
                    hiddenSoftInput();
                    //延迟显示，先让输入法隐藏
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showLayoutFace();
                        }
                    }, 100);
                } else {
                    //隐藏表情面板，打开输入法
                    hideLayoutFace();
                    showSoftInput();
                }
            }
        });

        this.etContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                viewSmile.setChecked(false);//还原表情状态
                layoutFace.setVisibility(View.GONE);
                return false;
            }
        });
        this.etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (arg0.length() > 0)
                    btSend.setEnabled(true);
                else
                    btSend.setEnabled(false);
            }
        });
    }

    public void onCreate() {
        initFace();

    }

    public void resetEditText() {
        etContent.setText("");
    }

    public void hideLayoutBottom() {
        layoutBottomSmile.setVisibility(View.GONE);
    }

    public void showLayoutBottom() {
        layoutBottomSmile.setVisibility(View.VISIBLE);
    }

    public void hideLayoutFace() {
        layoutFace.setVisibility(View.GONE);
    }

    public void showLayoutFace() {
        layoutFace.setVisibility(View.VISIBLE);
    }

    public void showEditLayout() {
        if (layoutFace.getVisibility() == View.VISIBLE)
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
        CommonUtil.hiddenSoftInput(mActivity, etContent);
    }

    public void showSoftInput() {
        CommonUtil.showSoftInput(mActivity, etContent);
    }

    private void initFace() {
        emoticonsFileNames = new ArrayList<>();
        for (int x = 0; x <= EMOTICONS_COUNT; x++) {
            String filename = "f_static_0" + x;
            emoticonsFileNames.add(filename);
        }

        List<View> views = new ArrayList<>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        View gv3 = getGridChildView(3);
        views.add(gv1);
        views.add(gv2);
        views.add(gv3);

        facePager.setAdapter(new ExpressionPagerAdapter(views));
        indicator.setViewPager(facePager);

    }

    /**
     * 获取表情的gridview的子view
     *
     * @param i
     * @return
     */
    private View getGridChildView(int i) {
        View view = View.inflate(mActivity, R.layout.expression_gridview, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        gv.setNumColumns(7);
        /*int verticalSpacing = CommonUtil.dip2px(mActivity, 20);
        gv.setVerticalSpacing(verticalSpacing);
        gv.setHorizontalSpacing(verticalSpacing);*/
        gv.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        List<String> list = new ArrayList<>();
        if (i == 1) {
            List<String> list1 = emoticonsFileNames.subList(0, 20);
            list.addAll(list1);
        } else if (i == 2) {
            list.addAll(emoticonsFileNames.subList(20, 40));
        } else if (i == 3) {
            list.addAll(emoticonsFileNames.subList(40, emoticonsFileNames.size()));
        }
        list.add(EMOTION_NAME_DELETE);
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(mActivity, 1, list);
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String filename = expressionAdapter.getItem(position);

                if (filename != EMOTION_NAME_DELETE) { // 不是删除键，显示表情
                    String fieldValue = SmileUtils.getFieldValue(filename);
                    CharSequence sequence = SmileUtils.getSmiledText(mActivity, fieldValue);
                    int index = etContent.getSelectionStart();
                    Editable edit = etContent.getEditableText();//获取EditText的文字
                    edit.insert(index, sequence);//光标所在位置插入文字
                } else { // 删除文字或者表情
                    if (!TextUtils.isEmpty(etContent.getText())) {

                        int selectionStart = etContent.getSelectionStart();// 获取光标的位置
                        if (selectionStart > 0) {
                            String body = etContent.getText()
                                    .toString();
                            String tempStr = body.substring(0,
                                    selectionStart);
                            int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                            if (i != -1) {
                                CharSequence cs = tempStr.substring(i,
                                        selectionStart);
                                if (SmileUtils.containsKey(cs.toString()))
                                    etContent.getEditableText()
                                            .delete(i, selectionStart);
                                else
                                    etContent.getEditableText()
                                            .delete(selectionStart - 1,
                                                    selectionStart);
                            } else {
                                etContent.getEditableText()
                                        .delete(selectionStart - 1,
                                                selectionStart);
                            }
                        }
                    }

                }

            }
        });
        return view;
    }

}
