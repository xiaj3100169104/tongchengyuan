package com.camera.ocrtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import test.com.ocrtest.R;


public class ScanCardActivity extends AppCompatActivity {
    CameraView mCameraView;
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_card);
        mCameraView = (CameraView) findViewById(R.id.main_camera);
        mImageView = (ImageView) findViewById(R.id.main_image);
        mCameraView.setTag(mImageView);

    }
}
