package com.camera.ocrtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sikang on 2017/4/21.
 */

public class CameraView extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private final String TAG = "CameraView";
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private boolean isPreviewOn;
    //默认预览尺寸
    private int imageWidth = 1920;
    private int imageHeight = 1080;
    //帧率
    private int frameRate = 30;

    public CameraView(Context context) {
        super(context);
        init();
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHolder = getHolder();
        //设置SurfaceView 的SurfaceHolder的回调函数
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //Surface创建时开启Camera
        openCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //设置Camera基本参数
        if (mCamera != null)
            initCameraParams();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            release();
        } catch (Exception e) {
        }
    }

    private boolean isScanning = false;

    /**
     * Camera帧数据回调用
     */
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        //识别中不处理其他帧数据
        if (!isScanning) {
            isScanning = true;
            try {
                //获取Camera预览尺寸
                Camera.Size size = camera.getParameters().getPreviewSize();
                Log.v(TAG, "Camera.Size getPreviewSize:  width: " + size.width + " height: " + size.height);

                //将帧数据转为bitmap
                YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
                if (image != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, stream);
                    Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
                    //这里返回的照片默认横向的，先将图片旋转90度
                    bmp = rotateToDegrees(bmp, 90);
                    Log.v(TAG, "getBitmapSize:  width: " + size.width + " height: " + size.height);

                    //然后裁切出需要的区域，具体区域要和UI布局中配合，这里取图片正中间，宽度取图片的一半，高度这里用的适配数据，可以自定义
                    bmp = bitmapCrop(bmp, (1080 - 750) / 2, (1920 - 72 - 150) / 2, 750, 150);
                    if (bmp == null)
                        return;
                    //将裁切的图片显示出来（测试用，需要为CameraView  setTag（ImageView））
                    ImageView imageView = (ImageView) getTag();
                    imageView.setImageBitmap(bmp);
                    stream.close();
                    //开始识别
                    OcrUtil.ScanEnglish(bmp, new MyCallBack() {
                        @Override
                        public void response(String result) {
                            //这是区域内扫除的所有内容
                            Log.d("scantest", "扫描结果：  " + result);
                            if (!TextUtils.isEmpty(getTelnum(result))) {
                                //检索结果中是否包含手机号
                                Log.d("scantest", "手机号码：  " + getTelnum(result));
                                isScanning = true;
                            } else
                                isScanning = false;
                        }
                    });
                }
            } catch (Exception ex) {
                isScanning = false;
            }
        }
    }


    /**
     * 获取字符串中的手机号
     */
    public String getTelnum(String sParam) {

        if (sParam.length() <= 0)
            return "";
        Pattern pattern = Pattern.compile("(1|861)(3|5|8)\\d{9}$*");
        Matcher matcher = pattern.matcher(sParam);
        StringBuffer bf = new StringBuffer();
        while (matcher.find()) {
            bf.append(matcher.group()).append(",");
        }
        int len = bf.length();
        if (len > 0) {
            bf.deleteCharAt(len - 1);
        }
        return bf.toString();
    }

    /**
     * Bitmap裁剪
     *
     * @param bitmap The bitmap we are subsetting
     * @param x      The x coordinate of the first pixel in source
     * @param y      The y coordinate of the first pixel in source
     * @param width  The number of pixels in each row
     * @param height The number of rows
     * @param height 高
     */
    public static Bitmap bitmapCrop(Bitmap bitmap, int x, int y, int width, int height) {
        if (null == bitmap || width <= 0 || height < 0) {
            return null;
        }
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();
        if (widthOrg >= width && heightOrg >= height) {
            try {
                bitmap = Bitmap.createBitmap(bitmap, x, y, width, height);
            } catch (Exception e) {
                return null;
            }
        }
        return bitmap;
    }

    /**
     * 图片旋转
     *
     * @param tmpBitmap
     * @param degrees
     * @return
     */
    public static Bitmap rotateToDegrees(Bitmap tmpBitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degrees);
        return Bitmap.createBitmap(tmpBitmap, 0, 0, tmpBitmap.getWidth(), tmpBitmap.getHeight(), matrix, true);
    }


    /**
     * 摄像头配置
     */
    public void initCameraParams() {
        stopPreview();
        Camera.Parameters camParams = mCamera.getParameters();
        List<Camera.Size> sizes = camParams.getSupportedPreviewSizes();
        for (int i = 0; i < sizes.size(); i++) {
            if ((sizes.get(i).width >= imageWidth && sizes.get(i).height >= imageHeight) || i == sizes.size() - 1) {
                imageWidth = sizes.get(i).width;
                imageHeight = sizes.get(i).height;
//                Log.v(TAG, "Changed to supported resolution: " + imageWidth + "x" + imageHeight);
                break;
            }
        }
        camParams.setPreviewSize(imageWidth, imageHeight);
//        camParams.setPictureSize(imageWidth, imageHeight);
        Log.v(TAG, "setPreviewSize width: " + imageWidth + " height: " + imageHeight + " frameRate: " + frameRate);

        camParams.setPreviewFrameRate(frameRate);
//        Log.v(TAG, "Preview Framerate: " + camParams.getPreviewFrameRate());

        mCamera.setParameters(camParams);
        //取到的图像默认是横向的，这里旋转90度，保持和预览画面相同
        mCamera.setDisplayOrientation(90);
        // Set the holder (which might have changed) again
        startPreview();
    }

    /**
     * 开始预览
     */
    public void startPreview() {
        try {
            mCamera.setPreviewCallback(this);
            mCamera.setPreviewDisplay(mHolder);//set the surface to be used for live preview
            mCamera.startPreview();
            mCamera.autoFocus(autoFocusCB);
        } catch (IOException e) {
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 停止预览
     */
    public void stopPreview() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
        }
    }

    /**
     * 打开指定摄像头
     */
    public void openCamera() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int cameraId = 0; cameraId < Camera.getNumberOfCameras(); cameraId++) {
            Camera.getCameraInfo(cameraId, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                try {
                    mCamera = Camera.open(cameraId);
                } catch (Exception e) {
                    if (mCamera != null) {
                        mCamera.release();
                        mCamera = null;
                    }
                }
                break;
            }
        }
    }

    /**
     * 摄像头自动聚焦
     */
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            postDelayed(doAutoFocus, 3000);
        }
    };
    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (mCamera != null) {
                try {
                    mCamera.autoFocus(autoFocusCB);
                } catch (Exception e) {
                }
            }
        }
    };

    /**
     * 释放
     */
    public void release() {
        if (isPreviewOn && mCamera != null) {
            isPreviewOn = false;
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

}
