package custom.camera2;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import test.com.ocrtest.R;

/**
 * 自定义组件实现,扫描功能
 */
public final class ScanView extends View {

    // 扫描框的宽度
    private int mWidth;
    // 扫描框的高度
    private int mHeight;
    // 扫描框边界颜色
    private int borderColor = 0x88ffffff;
    // 扫描框边角颜色
    private int cornerColor = 0xff45DDDD;
    // 扫描框边角宽度
    int corWidth;
    // 扫描框边角高度度
    int corHeight;
    // 扫描线移动的y
    private int scanLineTop;
    // 扫描线每次刷新y增量
    private int scan_velocity = 5;
    //刷新时间间隔
    private long refresh_time = 10L;
    //边框画笔
    private Paint borderPaint;
    //边角画笔
    private Paint paint;
    //扫描线画笔
    private Paint scanLinePaint;

    public ScanView(Context context) {
        this(context, null);
    }

    public ScanView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);

    }

    public ScanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        corWidth = Utils.dp2px(context, 20);
        corHeight = Utils.dp2px(context, 5);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ScanView);
        corWidth = (int) ta.getDimension(R.styleable.ScanView_xj_corner_width, corWidth);
        corHeight = (int) ta.getDimension(R.styleable.ScanView_xj_corner_height, corHeight);
        cornerColor = ta.getColor(R.styleable.ScanView_xj_corner_color, cornerColor);
        scan_velocity = ta.getInt(R.styleable.ScanView_xj_scan_speed, scan_velocity);
        refresh_time = ta.getInt(R.styleable.ScanView_xj_refresh_time, (int) refresh_time);
        ta.recycle();
        initPaint();
    }

    private void initPaint() {
        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setDither(true);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(5);
        paint = new Paint();
        paint.setColor(cornerColor);
        paint.setStyle(Paint.Style.FILL);
        scanLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scanLinePaint.setAntiAlias(true);
        scanLinePaint.setDither(true);
        scanLinePaint.setColor(cornerColor);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        setMeasuredDimension(mWidth, mHeight);

    }

    @Override
    public void onDraw(Canvas canvas) {
        // 画矩形边框
        canvas.drawRect(0, 0, mWidth, mHeight, borderPaint);
        drawFrameBounds(canvas);
        drawScanLight(canvas);
        postInvalidateDelayed(refresh_time, 0, 0, mWidth, mHeight);
    }

    //绘制边角
    private void drawFrameBounds(Canvas canvas) {
        // 左上角
        canvas.drawRect(0, 0, corWidth, corHeight, paint);
        canvas.drawRect(0, 0, corHeight, corWidth, paint);
        // 右上角
        canvas.drawRect(mWidth - corWidth, 0, mWidth, corHeight, paint);
        canvas.drawRect(mWidth - corHeight, 0, mWidth, corWidth, paint);
        // 左下角
        canvas.drawRect(0, mHeight - corHeight, corWidth, mHeight, paint);
        canvas.drawRect(0, mHeight - corWidth, corHeight, mHeight, paint);
        // 右下角
        canvas.drawRect(mWidth - corWidth, mHeight - corHeight, mWidth, mHeight, paint);
        canvas.drawRect(mWidth - corHeight, mHeight - corWidth, mWidth, mHeight, paint);
    }

    // 绘制移动扫描线
    private void drawScanLight(Canvas canvas) {
        //移动到了底部
        if (scanLineTop >= mHeight - scan_velocity) {
            scanLineTop = 0;
        }
        canvas.drawRect(0, scanLineTop, mWidth, scanLineTop + scan_velocity, scanLinePaint);
        scanLineTop += scan_velocity;
    }
}
