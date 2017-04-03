package com.juns.wechat.chat.offlineVideo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.juns.wechat.R;
import com.juns.wechat.chat.offlineVideo.widget.RecorderProgressDialog;
import com.juns.wechat.chat.offlineVideo.widget.RecorderProgress;
import com.style.utils.CommonUtil;
import com.yixia.camera.MediaRecorderBase;
import com.yixia.camera.MediaRecorderNative;
import com.yixia.camera.VCamera;
import com.yixia.camera.model.MediaObject;
import com.yixia.camera.util.DeviceUtils;
import com.yixia.camera.util.FileUtils;
import com.yixia.videoeditor.adapter.UtilityAdapter;

import java.io.File;
import java.util.ArrayList;


public class RecordActivity extends Activity implements MediaRecorderBase.OnErrorListener, MediaRecorderBase.OnEncodeListener {

    /**
     * 录制最长时间
     */
    public static int RECORD_TIME_MAX = 10 * 1000;
    /**
     * 录制最小时间
     */
    public static int RECORD_TIME_MIN = 2 * 1000;

    /**
     * 按住拍偏移距离
     */
    private static float OFFSET_DRUTION = 25.0f;


    /**
     * progress 小于录制最少时间的颜色
     */
    private static int MIN_TIME_PROGRESS_COLOR = 0xFFFC2828;
    /**
     * progress 颜色
     */
    private static int PROGRESS_COLOR = 0xFF00FF00;

    /**
     * 对焦图片宽度
     */
    private int mFocusWidth;

    /**
     * 屏幕宽度
     */
    private int mWindowWidth;

    /**
     * SDK视频录制对象
     */
    private MediaRecorderBase mMediaRecorder;
    /**
     * 视频信息
     */
    private MediaObject mMediaObject;

    /**
     * 对焦动画
     */
    private Animation mFocusAnimation;

    private Long firstTime;

    private boolean isFirstTouch = true;
    private boolean isNeedFinish = true;

    private boolean mCreated;


    private boolean isCancelRecoder;
    private boolean isRecoder;

    TextView back;
    SurfaceView mSurfaceView;
    ImageView mImgRecordFocusing;
    TextView hitInfo;
    RecorderProgress mRecorderProgress;
    TextView startPlay;
    private RecorderProgressDialog progressDialog;
    private TextView tv_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //竖屏锁定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题
        setContentView(R.layout.activity_record);

        initView();

        initVideo();

        mCreated = true;
        progressDialog = new RecorderProgressDialog(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        UtilityAdapter.freeFilterParser();
        UtilityAdapter.initFilterParser();

        if (mMediaRecorder == null) {
            initMediaRecorder();
        } else {
            mMediaRecorder.prepare();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        stopRecord();
        UtilityAdapter.freeFilterParser();
        if (mMediaRecorder != null)
            mMediaRecorder.release();
    }

    private void initVideo() {
        mWindowWidth = DeviceUtils.getScreenWidth(this);

        mFocusWidth = CommonUtil.dip2px(this, 64);
        try {
            mImgRecordFocusing.setImageResource(R.drawable.ms_video_focus_icon);
        } catch (OutOfMemoryError e) {
            Log.e("OutOfMemoryError", e.getMessage());
        }

        mRecorderProgress.setMaxTime(RECORD_TIME_MAX);
        mRecorderProgress.setMinRecordertime(RECORD_TIME_MIN);
        mRecorderProgress.setMinTimeProgressColor(MIN_TIME_PROGRESS_COLOR);
        mRecorderProgress.setProgressColor(PROGRESS_COLOR);


        //init Listener
        if (DeviceUtils.hasICS()) {
            mSurfaceView.setOnTouchListener(onSurfaveViewTouchListener);
        }

        startPlay.setOnTouchListener(onVideoRecoderTouchListener);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        back = (TextView) findViewById(R.id.tv_recorder_cancel);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mImgRecordFocusing = (ImageView) findViewById(R.id.img_record_focusing);
        hitInfo = (TextView) findViewById(R.id.tv_recoder_tips);
        mRecorderProgress = (RecorderProgress) findViewById(R.id.recorder_progress);
        startPlay = (TextView) findViewById(R.id.btn_press);
    }


    /**
     * 初始化拍摄SDK
     */
    private void initMediaRecorder() {
        mMediaRecorder = new MediaRecorderNative();

        mMediaRecorder.setOnErrorListener(this);
        mMediaRecorder.setOnEncodeListener(this);
        File f = new File(VCamera.getVideoCachePath());
        if (!FileUtils.checkFile(f)) {
            f.mkdirs();
        }
        String key = String.valueOf(System.currentTimeMillis());
        mMediaObject = mMediaRecorder.setOutputDirectory(key,
                VCamera.getVideoCachePath() + key);
        mMediaRecorder.setSurfaceHolder(mSurfaceView.getHolder());
        mMediaRecorder.prepare();

    }


    /**
     * 检测手否要手动对焦
     */
    private View.OnTouchListener onSurfaveViewTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mMediaRecorder == null || !mCreated) {
                return false;
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 检测是否手动对焦
                    if (checkCameraFocus(event))
                        return true;
                    break;
            }
            return true;
        }

    };

    /**
     * 点击屏幕处理是否录制
     */
    private View.OnTouchListener onVideoRecoderTouchListener = new View.OnTouchListener() {
        private float startY;
        private float moveY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (mMediaRecorder == null) {
                return false;
            }

            //第一次触摸记录时间
            if (isFirstTouch) {
                isFirstTouch = false;
                firstTime = System.currentTimeMillis();
            }

            //和第一次触摸记录时间做对比
            if (System.currentTimeMillis() - firstTime < RECORD_TIME_MAX) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        //开始录制
                        startY = event.getY();
                        startRecoder();
                        break;
                    case MotionEvent.ACTION_MOVE:

                        moveY = event.getY();
                        float drution = moveY - startY;

                        if ((drution > 0.0f) && Math.abs(drution) > OFFSET_DRUTION) {
                            //滑动取消
                            slideCancelRecoder();

                        }
                        if ((drution < 0.0f) && (Math.abs(drution) > OFFSET_DRUTION)) {
                            releaseCancelRecoder();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        //停止录制
                        stopAll();
                        if (isCancelRecoder) {
                            hideRecoderTxt();
                            removeRecoderPart();
                            return true;
                        }
                        int duration = mMediaObject.getDuration();
                        if (duration < RECORD_TIME_MIN) {
                            recoderShortTime();
                            return true;
                        }
                        //开始编码
                        startEncoding();
                        break;
                    default:
                        return true;
                }

            } else {
                if (isNeedFinish) {
                    isNeedFinish = false;
                    //停止录制
                    stopAll();
                    //编码
                    startEncoding();
                    return true;
                }
            }
            return true;

        }

    };

    private void startRecoder() {


        isCancelRecoder = false;
        hitInfo.setVisibility(View.VISIBLE);
        if (mMediaRecorder == null) {
            return;
        }
        MediaObject.MediaPart part = mMediaRecorder.startRecord();
        if (part == null) {
            return;
        }
        this.mRecorderProgress.startAnimation();
        isRecoder = true;

    }

    private void slideCancelRecoder() {

        isCancelRecoder = false;
        hitInfo.setVisibility(View.VISIBLE);

        tv_cancel.setVisibility(View.INVISIBLE);


    }


    private void releaseCancelRecoder() {
        isCancelRecoder = true;
        hitInfo.setVisibility(View.INVISIBLE);
        tv_cancel.setVisibility(View.VISIBLE);
    }


    private void stopAll() {

        hitInfo.setVisibility(View.INVISIBLE);
        tv_cancel.setVisibility(View.INVISIBLE);
        mRecorderProgress.stopAnimation();
        stopRecord();
        isRecoder = false;

    }


    private void hideRecoderTxt() {
        hitInfo.setVisibility(View.INVISIBLE);
        if (isRecoder) {
            hitInfo.setVisibility(View.VISIBLE);
            tv_cancel.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 回删已录制的视频
     */
    private void removeRecoderPart() {
        if (mMediaObject != null) {
            mMediaObject.removeAllPart();
        }
    }

    private void recoderShortTime() {

        Toast.makeText(this, "录制时间太短", Toast.LENGTH_SHORT).show();
        removeRecoderPart();
    }


    private void startEncoding() {
        mMediaRecorder.startEncoding();
    }


    /**
     * 停止录制
     */
    private void stopRecord() {
        if (mMediaRecorder != null) {
            mMediaRecorder.stopRecord();
        }
    }

    /**
     * 手动对焦
     */
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private boolean checkCameraFocus(MotionEvent event) {
        mImgRecordFocusing.setVisibility(View.GONE);
        float x = event.getX();
        float y = event.getY();
        float touchMajor = event.getTouchMajor();
        float touchMinor = event.getTouchMinor();

        Rect touchRect = new Rect((int) (x - touchMajor / 2),
                (int) (y - touchMinor / 2), (int) (x + touchMajor / 2),
                (int) (y + touchMinor / 2));
        // The direction is relative to the sensor orientation, that is, what
        // the sensor sees. The direction is not affected by the rotation or
        // mirroring of setDisplayOrientation(int). Coordinates of the rectangle
        // range from -1000 to 1000. (-1000, -1000) is the upper left point.
        // (1000, 1000) is the lower right point. The width and height of focus
        // areas cannot be 0 or negative.
        // No matter what the zoom level is, (-1000,-1000) represents the top of
        // the currently visible camera frame
        if (touchRect.right > 1000)
            touchRect.right = 1000;
        if (touchRect.bottom > 1000)
            touchRect.bottom = 1000;
        if (touchRect.left < 0)
            touchRect.left = 0;
        if (touchRect.right < 0)
            touchRect.right = 0;

        if (touchRect.left >= touchRect.right
                || touchRect.top >= touchRect.bottom)
            return false;

        ArrayList<Camera.Area> focusAreas = new ArrayList<Camera.Area>();
        focusAreas.add(new Camera.Area(touchRect, 1000));
        if (!mMediaRecorder.manualFocus(new Camera.AutoFocusCallback() {

            @Override
            public void onAutoFocus(boolean success, Camera camera) {

                mImgRecordFocusing.setVisibility(View.GONE);

            }
        }, focusAreas)) {
            mImgRecordFocusing.setVisibility(View.GONE);
        }

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mImgRecordFocusing
                .getLayoutParams();
        int left = touchRect.left - (mFocusWidth / 2);

        int top = touchRect.top - (mFocusWidth / 2);
        if (left < 0)
            left = 0;
        else if (left + mFocusWidth > mWindowWidth)
            left = mWindowWidth - mFocusWidth;
        if (top + mFocusWidth > mWindowWidth)
            top = mWindowWidth - mFocusWidth;

        lp.leftMargin = left;
        lp.topMargin = top;
        mImgRecordFocusing.setLayoutParams(lp);
        mImgRecordFocusing.setVisibility(View.VISIBLE);

        if (mFocusAnimation == null)
            mFocusAnimation = AnimationUtils.loadAnimation(this,
                    R.anim.record_focus);

        mImgRecordFocusing.startAnimation(mFocusAnimation);

        return true;
    }

    @Override
    public void onVideoError(int what, int extra) {

    }

    @Override
    public void onAudioError(int what, String message) {

    }

    @Override
    public void onEncodeStart() {

        progressDialog.setMessage("请稍后...");
        progressDialog.show();

    }

    @Override
    public void onEncodeProgress(int progress) {

    }

    @Override
    public void onEncodeComplete() {

        progressDialog.dismiss();
        String outputVideoPath = mMediaObject.getOutputVideoPath();
        Intent data = new Intent();
        data.putExtra("path", outputVideoPath);
        setResult(RESULT_OK, data);
        finish();

    }

    @Override
    public void onEncodeError() {

        progressDialog.dismiss();
        Toast.makeText(this, "视频转码失败", Toast.LENGTH_SHORT).show();

    }

}
