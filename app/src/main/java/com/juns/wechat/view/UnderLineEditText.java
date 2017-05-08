package com.juns.wechat.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

import com.same.city.love.R;

/**
 * Created by 王宗文 on 2016/6/20.
 */
public class UnderLineEditText extends EditText {
    private Paint mPaint;

    public UnderLineEditText(Context context) {
        this(context, null);
    }

    public UnderLineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(getResources().getColor(R.color.app_color_green));
        canvas.drawLine(0, this.getHeight() - 1, this.getWidth(),
                this.getHeight() - 1, mPaint);
    }
}
