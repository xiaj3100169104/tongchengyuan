package custom.camera2;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import test.com.ocrtest.R;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class Camera2ScanActivity extends AppCompatActivity implements View.OnClickListener, FragmentCompat.OnRequestPermissionsResultCallback {

    private static final int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90;
    private static final int SENSOR_ORIENTATION_INVERSE_DEGREES = 270;
    private static final SparseIntArray DEFAULT_ORIENTATIONS = new SparseIntArray();
    private static final SparseIntArray INVERSE_ORIENTATIONS = new SparseIntArray();

    private static final String TAG = "Camera2ScanActivity";

    private static final String[] VIDEO_PERMISSIONS = {
            Manifest.permission.CAMERA,
    };
    ///为了使照片竖直显示

    static {
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_0, 90);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_90, 0);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_180, 270);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    static {
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_0, 270);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_90, 180);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_180, 90);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_270, 0);
    }

    private TextureView mTextureView;
    private CameraDevice mCameraDevice;
    private CameraCaptureSession mPreviewSession;

    /**
     * The {@link Size} of camera preview.
     * 目前使用全屏模式
     */
    private Size mPreviewSize;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler, mainHandler;
    private ImageReader mImageReader;

    /**
     * A {@link Semaphore} to prevent the app from exiting before closing the camera.
     */
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    private ImageView iv_show;
    private ScanView mScanView;
    private CameraManager manager;
    private TextView tvResult;
    private Button btnOpenLight;
    private Button btnCloseLight;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_camera2_scan);
        mTextureView = (TextureView) findViewById(R.id.texture);
        mScanView = (ScanView) findViewById(R.id.scan_view);
        iv_show = (ImageView) findViewById(R.id.iv_show);
        tvResult = (TextView) findViewById(R.id.tv_result);
        btnOpenLight = (Button) findViewById(R.id.btn_open_light);
        btnCloseLight = (Button) findViewById(R.id.btn_close_light);
        btnOpenLight.setOnClickListener(this);
        btnCloseLight.setOnClickListener(this);
        initLooper();

    }

    //很多过程都变成了异步的了，所以这里需要一个子线程的looper
    private void initLooper() {
        mainHandler = new Handler(getMainLooper());
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mTextureView.isAvailable()) {
            Log.e("onResume", mTextureView.getWidth() + " * " + mTextureView.getHeight());
            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    @Override
    public void onPause() {
        closeCamera();
        //stopBackgroundThread();
        super.onPause();
    }

    /**
     * Stops the background thread and its {@link Handler}.
     */
    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            Log.e("onSurfaceAvailable", width + " * " + height);
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return true;
        }

        // 这个方法要注意一下，因为每有一帧画面，都会回调一次此方法
        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

    };

    /**
     * Tries to open a {@link CameraDevice}. The result is listened by `mStateCallback`.
     */
    private void openCamera(int width, int height) {
        /*if (!hasPermissionsGranted(VIDEO_PERMISSIONS)) {
            requestVideoPermissions();
            return;
        }*/

        manager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
        try {
            Log.d(TAG, "tryAcquire");
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            String cameraId = manager.getCameraIdList()[0];
            Log.d(TAG, "cameraId = " + cameraId);
            // Choose the sizes for camera preview and video recording
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            //mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            mPreviewSize = new Size(height, width);
            //注意这些size的width都比height大
            Size[] sizes = map.getOutputSizes(SurfaceTexture.class);
            //从支持的size中选一个合适的
            mPreviewSize = Utils.chooseOptimalSize(sizes, height, width);
            Log.e("mPreviewSize", mPreviewSize.getWidth() + " * " + mPreviewSize.getHeight());

            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                //mTextureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            } else {
                //mTextureView.setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
            }
            configureTransform(width, height);
            manager.openCamera(cameraId, mStateCallback, mainHandler);
        } catch (CameraAccessException e) {
            Toast.makeText(this, "Cannot access the camera.", Toast.LENGTH_SHORT).show();
            this.finish();
        } catch (NullPointerException e) {
            // Currently an NPE is thrown when the Camera2API is used but not supported on the
            // device this code runs.
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.");
        }
    }

    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            closePreviewSession();
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }

        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.");
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    /**
     * {@link CameraDevice.StateCallback} is called when {@link CameraDevice} changes its status.
     */
    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(CameraDevice cameraDevice) {
            mCameraDevice = cameraDevice;
            startPreview();
            mCameraOpenCloseLock.release();
            if (null != mTextureView) {
                configureTransform(mTextureView.getWidth(), mTextureView.getHeight());
            }
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(CameraDevice cameraDevice, int error) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

    };

    /**
     * Start the camera preview.
     */
    private void startPreview() {
        if (null == mCameraDevice || !mTextureView.isAvailable() || null == mPreviewSize) {
            return;
        }
        try {
            closePreviewSession();
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;
            //这里设置的就是预览大小
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            // 设置捕获请求为预览，这里还有拍照啊，录像等
            CaptureRequest.Builder mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            Surface previewSurface = new Surface(texture);
            mPreviewBuilder.addTarget(previewSurface);
            //此处还有很多格式，比如我所用到YUV等*/, 2/*最大的图片数，mImageReader里能获取到图片数，但是实际中是2+1张图片，就是多一张
            mImageReader = ImageReader.newInstance(mPreviewSize.getWidth(), mPreviewSize.getHeight(), ImageFormat.JPEG, 1);
            mImageReader.setOnImageAvailableListener(new MyOnImageAvailableListener(), mBackgroundHandler);
            mPreviewBuilder.addTarget(mImageReader.getSurface());
            previewSessionStateCallback = new PreviewSessionStateCallback(mPreviewBuilder);
            mCameraDevice.createCaptureSession(Arrays.asList(previewSurface, mImageReader.getSurface()), previewSessionStateCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private class MyOnImageAvailableListener implements ImageReader.OnImageAvailableListener {
        //可以在这里处理拍照得到的临时照片 例如，写入本地
        public boolean isScanning;

        /**
         * 当有一张图片可用时会回调此方法，但有一点一定要注意：
         * 一定要调用 reader.acquireNextImage()和close()方法，否则画面就会卡住！！！！！我被这个坑坑了好久！！！
         * 很多人可能写Demo就在这里打一个Log，结果卡住了，或者方法不能一直被回调。
         **/
        @Override
        public void onImageAvailable(ImageReader reader) {
            iv_show.setVisibility(View.VISIBLE);
            // 拿到拍照照片数据
            Image image = reader.acquireNextImage();
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);//由缓冲区存入字节数组
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            bitmap = Utils.rotateToDegrees(bitmap, 90);
            Log.e("初始bitmap", bitmap.getWidth() + " * " + bitmap.getHeight());
            Log.e("扫描框大小", mScanView.getWidth() + " * " + mScanView.getHeight());
            int x = (bitmap.getWidth() - mScanView.getWidth()) / 2;
            int y = (bitmap.getHeight() - mScanView.getHeight()) / 2;
            bitmap = Bitmap.createBitmap(bitmap, x, y, mScanView.getWidth(), mScanView.getHeight());
            Log.e("裁剪后bitmap", bitmap.getWidth() + " * " + bitmap.getHeight());
            if (bitmap != null) {
                final Bitmap finalBitmap = bitmap;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_show.setImageBitmap(finalBitmap);
                    }
                });
                //上一次扫描完成了再进行下一次扫描，不然界面太卡
                if (!isScanning) {
                    //开始识别
                    isScanning = true;
                    OcrUtil.ScanEnglish(finalBitmap, new MyCallBack() {
                        @Override
                        public void response(final String result) {
                            //这是区域内扫除的所有内容
                            Log.d(TAG, "扫描结果：  " + result);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvResult.setText(result);

                                }
                            });
                            isScanning = false;
                        }
                    });
                }
            }
            image.close();
        }
    }

    PreviewSessionStateCallback previewSessionStateCallback;

    //CameraCaptureSession 这个对象控制摄像头的预览或者拍照
    //setRepeatingRequest()开启预览，capture()拍照
    //StateCallback监听CameraCaptureSession的创建
    private class PreviewSessionStateCallback extends CameraCaptureSession.StateCallback {
        private CaptureRequest.Builder builder;

        public PreviewSessionStateCallback(CaptureRequest.Builder mPreviewBuilder) {
            this.builder = mPreviewBuilder;
        }

        @Override
        public void onConfigured(CameraCaptureSession session) {
            Log.e(TAG, "相机创建成功！");
            try {
                mPreviewSession = session;
                builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
                // 自动对焦
                builder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

                mPreviewSession.setRepeatingRequest(builder.build(), null, mBackgroundHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
                Log.e(TAG, "这里异常");
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            Log.e(TAG, "相机创建失败！");
        }

        public void openTorch() {
            try {
                mPreviewSession.stopRepeating();
                //这句控制闪光灯
                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
                mPreviewSession.setRepeatingRequest(builder.build(), null, mBackgroundHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        public void closeTorch() {
            try {
                mPreviewSession.stopRepeating();
                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                mPreviewSession.setRepeatingRequest(builder.build(), null, mBackgroundHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_open_light) {
            openLight();

        } else if (i == R.id.btn_close_light) {
            closeLight();

        }

    }

    private void openLight() {
        previewSessionStateCallback.openTorch();
    }

    private void closeLight() {
        previewSessionStateCallback.closeTorch();
    }

    //CameraCaptureSession.CaptureCallback监听拍照过程
    private CameraCaptureSession.CaptureCallback mSessionCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            Log.e(TAG, "这里接受到数据" + result.toString());
        }

        @Override
        public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request, CaptureResult partialResult) {

        }
    };

    /**
     * Configures the necessary {@link Matrix} transformation to `mTextureView`.
     * This method should not to be called until the camera preview size is determined in
     * openCamera, or until the size of `mTextureView` is fixed.
     *
     * @param viewWidth  The width of `mTextureView`
     * @param viewHeight The height of `mTextureView`
     */
    private void configureTransform(int viewWidth, int viewHeight) {
        int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        }
        //在主线程中执行
        mTextureView.setTransform(matrix);
    }

    /**
     * 拍照
     */
    private void takePicture() {
        if (mCameraDevice == null) return;
        // 创建拍照需要的CaptureRequest.Builder
        final CaptureRequest.Builder captureRequestBuilder;
        try {
            captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            // 将imageReader的surface作为CaptureRequest.Builder的目标
            captureRequestBuilder.addTarget(mImageReader.getSurface());
            // 自动对焦
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            // 自动曝光
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            // 获取手机方向
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            // 根据设备方向计算设置照片的方向
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, DEFAULT_ORIENTATIONS.get(rotation));
            //拍照
            CaptureRequest mCaptureRequest = captureRequestBuilder.build();
            //mCameraCaptureSession.capture(mCaptureRequest, null, childHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void closePreviewSession() {
        if (mPreviewSession != null) {
            mPreviewSession.close();
            mPreviewSession = null;
        }
    }

}
