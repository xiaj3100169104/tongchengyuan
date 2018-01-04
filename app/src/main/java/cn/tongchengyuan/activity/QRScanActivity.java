package cn.tongchengyuan.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.same.city.love.R;
import com.same.city.love.databinding.ActivityQrScanBinding;
import com.style.base.BaseToolbarActivity;
import com.style.constant.Skip;
import com.style.utils.CommonUtil;
import com.style.utils.FileUtil;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * 定制化显示扫描界面
 */
public class QRScanActivity extends BaseToolbarActivity {

    ActivityQrScanBinding bd;
    private CaptureFragment captureFragment;
    public static boolean isOpen = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_qr_scan);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friend_circle, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.select:
                if (CommonUtil.isSDcardAvailable()) {
                    CommonUtil.selectPhoto(this);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {
        setToolbarTitle("二维码");
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.fragment_qr_scan_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();

        initView();
    }

    private void initView() {
        bd.linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOpen) {
                    CodeUtils.isLightEnable(true);
                    isOpen = true;
                } else {
                    CodeUtils.isLightEnable(false);
                    isOpen = false;
                }

            }
        });
    }

    private void onGetQRcodeSuccess(Bitmap mBitmap, String result) {
        logE(TAG, "二维码==" + result);
        startActivity(new Intent(this, UserInfoActivity.class).putExtra(Skip.KEY_USER_ID, result));
        finish();
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            onGetQRcodeSuccess(mBitmap, result);
        }

        @Override
        public void onAnalyzeFailed() {
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理二维码扫描结果
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Skip.CODE_TAKE_ALBUM:
                    if (data != null) {
                        try {
                            showProgressDialog();
                            Uri uri = data.getData();
                            String path = FileUtil.UriToRealFilePath(this, uri);
                            CodeUtils.analyzeBitmap(path, new CodeUtils.AnalyzeCallback() {
                                @Override
                                public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                                    dismissProgressDialog();
                                    onGetQRcodeSuccess(mBitmap, result);
                                }

                                @Override
                                public void onAnalyzeFailed() {

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }
}
