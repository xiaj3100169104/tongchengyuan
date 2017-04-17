package com.style.view.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.style.view.libprogressbar.R;

/**
 * 百分比圆环形进度 View
 */
public class CirclePercentView extends View {
    private final static String DEFAULT_PERCENT_TAG = "%";

    //总体大小
    private int mHeight;
    private int mWidth;
    //圆的半径
    private int mRadius;
    //圆心坐标
    private float x;
    private float y;
    //不确定进度
    private boolean mIndeterminate = true;
    //大圆颜色
    private int mBigColor = 0xffE5E5E5;
    //扇形进度颜色
    private int mFinishColor = 0xff00BFFF;
    //进度条宽度
    private int mStripeWidth;
    //进度
    private int mCurPercent = 0;
    //进度开始角度
    private int mStartAngle = 270;
    //要画的弧度跨度
    private int mSweepAngle;
    //小圆的颜色
    private int mSmallColor = 0xffffffff;
    //中心文本颜色
    private int mTextColor = 0xff333333;
    //中心文本字体大小
    private float mCenterTextSize;
    //百分比后缀
    private String percentTag = DEFAULT_PERCENT_TAG;
    //中心文本
    private String centerText = "";
    //是否显示中心文本
    private boolean showText = false;
    //旋转速度:1-10,默认5
    private int speed = 5;
    //外圆画笔
    private Paint bigCirclePaint;
    //扇形进度画笔
    private Paint sectorPaint;
    //内圆画笔
    private Paint smallCirclePaint;
    //中心文本画笔
    private Paint textPaint;

    public CirclePercentView(Context context) {
        this(context, null);
    }

    public CirclePercentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePercentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CirclePercentView, defStyleAttr, 0);
        mStripeWidth = a.getDimensionPixelSize(R.styleable.CirclePercentView_stripeWidth, Utils.dp2px(context, 8));
        mCurPercent = a.getInteger(R.styleable.CirclePercentView_percent, mCurPercent);
        mBigColor = a.getColor(R.styleable.CirclePercentView_bigColor, mBigColor);
        mFinishColor = a.getColor(R.styleable.CirclePercentView_finishColor, mFinishColor);
        mSmallColor = a.getColor(R.styleable.CirclePercentView_smallColor, mSmallColor);
        mTextColor = a.getColor(R.styleable.CirclePercentView_textColor, mTextColor);
        mCenterTextSize = a.getDimension(R.styleable.CirclePercentView_centerTextSize, Utils.sp2px(context, 20));
        mRadius = a.getDimensionPixelSize(R.styleable.CirclePercentView_radius, Utils.dp2px(context, 50));
        mIndeterminate = a.getBoolean(R.styleable.CirclePercentView_indeterminate, mIndeterminate);

        //如果是确定进度，默认显示中心文本
        if (!mIndeterminate)
            showText = true;
        initPaint();
    }

    private void initPaint() {
        bigCirclePaint = new Paint();
        bigCirclePaint.setAntiAlias(true);
        bigCirclePaint.setColor(mBigColor);

        sectorPaint = new Paint();
        sectorPaint.setColor(mFinishColor);
        sectorPaint.setAntiAlias(true);

        smallCirclePaint = new Paint();
        smallCirclePaint.setAntiAlias(true);
        smallCirclePaint.setColor(mSmallColor);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(mTextColor);
        textPaint.setTextSize(mCenterTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取测量大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            mRadius = widthSize / 2;
            x = widthSize / 2;
            y = heightSize / 2;
            mWidth = widthSize;
            mHeight = heightSize;
        }

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            mWidth = (mRadius * 2);
            mHeight = (mRadius * 2);
            x = mRadius;
            y = mRadius;

        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mIndeterminate)//设置不确定角度跨度
            mSweepAngle = 90;
        else
            mSweepAngle = (int) (getCurPercent() * 3.6);
        //绘制外圆
        canvas.drawCircle(x, y, mRadius, bigCirclePaint);

        //饼状图
        RectF rect = new RectF(0, 0, mWidth, mHeight);
        canvas.drawArc(rect, mStartAngle, mSweepAngle, true, sectorPaint);

        //绘制内圆
        canvas.drawCircle(x, y, mRadius - mStripeWidth, smallCirclePaint);

        //绘制文本
        if (showText) {
            setCenterText(mCurPercent + getPercentTag());
            String text = getCenterText();
            float textLength = textPaint.measureText(text);
            float textHeight = textPaint.descent();// + textPaint.ascent();
            canvas.drawText(text, x - textLength / 2, y + textHeight, textPaint);
        }
        if (mIndeterminate) {//不确定进度，不断改变起始角度mSweepAngle
            mStartAngle += speed;
            if (mStartAngle > 359)//超过最大角度置0
                mStartAngle = 0;
            invalidate();
        }
    }


    public void startAnimation() {
        if (mIndeterminate) {
            RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(500);//设置动画持续时间
            animation.setRepeatCount(5);//设置重复次数
            setAnimation(animation);
            super.startAnimation(animation);

        }
    }

    public String getPercentTag() {
        return percentTag;
    }

    public void setPercentTag(String percentTag) {
        this.percentTag = percentTag;
    }

    public String getCenterText() {
        return centerText;
    }

    public void setCenterText(String centerText) {
        this.centerText = centerText;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    //设置进度并立即刷新
    public void setCurPercent(int percent) {
        if (percent <= 100) {
            mCurPercent = percent;
            this.invalidate();
        }
    }

    public int getCurPercent() {
        return mCurPercent;
    }

    //带动画渐进到指定进度
    public void setPercentWithAnimation(final int percent) {
        if (percent > 100) {
            throw new IllegalArgumentException("percent must less than 100!");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                int sleepTime = 1;
                for (int i = 0; i <= percent; i++) {
                    if (i % 20 == 0) {
                        sleepTime += 2;
                    }
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mCurPercent = i;
                    postInvalidate();
                }
            }
        }).start();
    }
}
