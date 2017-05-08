package com.style.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.same.city.love.R;


/**
 * 自定义通知提醒view(小圆点)
 */
public class CustomNotifyView extends View {

    /**
     * 背景颜色
     */
    private int mBackgroundColor = Color.RED;
    /**
     * 文本
     */
    private String mTitleText;
    /**
     * 文本的颜色
     */
    private int mTitleTextColor = Color.WHITE;
    /**
     * 文本的大小
     */
    // 默认设置为12sp
    private int mTitleTextSize = (int) sp2px(12);

    private Paint mCirclePaint;
    private Paint mTextPaint;
    private int textLeft;
    private int textTop;

    public CustomNotifyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomNotifyView(Context context) {
        this(context, null);
    }

    /**
     * 获得我自定义的样式属性
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CustomNotifyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        /**
         * 获得我们所定义的自定义样式属性
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomNotifyView, defStyle, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CustomNotifyView_notifyBackgroundColor:
                    mBackgroundColor = a.getColor(attr, mBackgroundColor);
                    break;
                case R.styleable.CustomNotifyView_notifyText:
                    mTitleText = a.getString(attr);
                    break;
                case R.styleable.CustomNotifyView_notifyTextColor:
                    mTitleTextColor = a.getColor(attr, mTitleTextColor);
                    break;
                case R.styleable.CustomNotifyView_notifyTextSize:
                    mTitleTextSize = a.getDimensionPixelSize(attr, mTitleTextSize);
                    break;

            }

        }
        a.recycle();
        initPaint();

      /*  this.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            }

        });*/

    }

    private void initPaint() {
        //背景画笔
        mCirclePaint = new Paint();
        mCirclePaint.setColor(mBackgroundColor);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setFilterBitmap(true);
        //文本画笔
        mTextPaint = new Paint();
        //mTextPaint.setFlags(Paint.SUBPIXEL_TEXT_FLAG);
        mTextPaint.setSubpixelText(true);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFilterBitmap(true);
        mTextPaint.setTextSize(mTitleTextSize);
        mTextPaint.setColor(mTitleTextColor);
    }

    private float sp2px(int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    /**
     * 显示数字，默认超过99显示"..."
     *
     * @param count
     */
    public void setNotifyCount(int count) {
        setNotifyCount(count, "...");
    }

    /**
     * 显示数字
     * @param count
     * @param s     超过99显示的字符
     */
    public void setNotifyCount(int count, String s) {
        if (count <= 0)
            this.setVisibility(View.GONE);
        else {
            this.setVisibility(View.VISIBLE);
            if (count > 99)
                mTitleText = s;
            else
                mTitleText = String.valueOf(count);
        }
        invalidate();
    }

    public void setNotifyText(String s) {
        mTitleText = s;
        invalidate();
    }

    public void setNotifyTextSize(int spSize) {
        mTitleTextSize = (int) sp2px(spSize);
        mTextPaint.setTextSize(mTitleTextSize);
        invalidate();
    }

    public void setNotifyTextColor(int color) {
        mTitleTextColor = color;
        mTextPaint.setColor(mTitleTextColor);
        invalidate();
    }

    public void setNotifyBackgroundColor(int color) {
        mBackgroundColor = color;
        mCirclePaint.setColor(mBackgroundColor);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /*int width = 0;
        int height = 0;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (specMode) {
            case MeasureSpec.EXACTLY:// 明确指定了
                width = getPaddingLeft() + getPaddingRight() + specSize;
                height = getPaddingTop() + getPaddingBottom() + specSize;
                break;
            case MeasureSpec.AT_MOST:// 一般为WARP_CONTENT
                width = getPaddingLeft() + getPaddingRight();
                height = getPaddingTop() + getPaddingBottom();
                break;
        }
        setMeasuredDimension(width, height);*/
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        onTextChanged();
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, getMeasuredHeight() / 2, mCirclePaint);
        if (!TextUtils.isEmpty(mTitleText))
            canvas.drawText(mTitleText, textLeft, textTop, mTextPaint);
    }

    /**
     * 文本改变重新计算文本位置
     */
    private void onTextChanged(){
        if (!TextUtils.isEmpty(mTitleText)) {
            int height = getHeight();
            int width = getWidth();
            int textLen = (int) mTextPaint.measureText(mTitleText);
            Paint.FontMetrics fm = mTextPaint.getFontMetrics();
            if (width > 0 && height > 0) {
                textLeft = (width - textLen) / 2;
                textTop = (int) (height / 2 + Math.abs(fm.ascent + fm.descent / 2) / 2);
            }
        }
    }
}
