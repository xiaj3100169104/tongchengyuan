package com.juns.wechat.chat.offlineVideo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Fussen on 2016/10/27.
 * <p>
 * 进度条
 */

public class RecorderProgress extends View {


    private Paint mPaint = new Paint();

    private volatile State mState = State.PAUSE;

    private float maxTime = 10000.0F;
    private float minTime = 2000.0F;

    private int progressColor = 0xFF00FF00;

    private int minTimeProgressColor = 0xFFFC2828;

    private long currTime;

    private Context mContext;


    public RecorderProgress(Context context) {
        super(context);
        init(context);
    }


    public RecorderProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RecorderProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        this.mContext = context;
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(minTimeProgressColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        long time = System.currentTimeMillis();

        if (mState == State.START) {
            int measuredWidth = getMeasuredWidth();
            float top = measuredWidth / 2.0f / maxTime;

            float tmp = (time - currTime);

            if (tmp >= minTime) {
                mPaint.setColor(progressColor);
            }

            top *= tmp;

            if (top < measuredWidth / 2.0f) {
                canvas.drawRect(top, 0.0f, measuredWidth - top, getMeasuredHeight(), mPaint);
                invalidate();
            }
        } else {
            return;
        }
        canvas.drawRect(0.0f, 0.0f, 0.0f, getMeasuredHeight(), mPaint);
    }


    public int getMinTimeProgressColor() {
        return minTimeProgressColor;
    }

    public void setMinTimeProgressColor(int minTimeProgressColor) {
        this.minTimeProgressColor = minTimeProgressColor;
    }

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    public void setMinRecordertime(float midTime) {
        this.minTime = midTime;
    }

    public void setMaxTime(float maxTime) {
        this.maxTime = maxTime;
    }

    public void startAnimation() {
        if (mState != State.START) {
            mState = State.START;
            this.currTime = System.currentTimeMillis();
            invalidate();
            setVisibility(VISIBLE);
            mPaint.setColor(minTimeProgressColor);
        }
    }

    public void stopAnimation() {
        if (mState != State.PAUSE) {
            mState = State.PAUSE;
            setVisibility(INVISIBLE);
        }
    }


    enum State {
        START(1, "start"),
        PAUSE(2, "pause");

        State(int code, String message) {
            this.code = code;
            this.message = message;
        }

        private int code;
        private String message;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }


}
