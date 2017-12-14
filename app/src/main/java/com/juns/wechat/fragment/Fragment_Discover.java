package com.juns.wechat.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.camera.ocrtest.ScanCardActivity;
//import com.juns.wechat.activity.FingerPrintActivity;
import com.juns.wechat.activity.FingerPrintActivity;
import com.same.city.love.R;
import com.juns.wechat.dynamic.FriendCircleActivity;
import com.juns.wechat.activity.QRScanActivity;
import com.style.base.BaseFragment;

import butterknife.OnClick;
import custom.camera2.Camera2ScanActivity;

public class Fragment_Discover extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutResID = R.layout.fragment_dicover;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void initData() {
        // TODO Auto-generated method stub

    }

    @OnClick(R.id.layout_discovery_dynamic) // 朋友圈
    public void friendCircle() {
        skip(FriendCircleActivity.class);
    }

    @OnClick(R.id.layout_left_menu_6)
    public void onClickEvent6() {
        skip(QRScanActivity.class);
    }

    @OnClick(R.id.layout_left_menu_7)
    public void onClickEvent7() {
        skip(ScanCardActivity.class);
    }

    @OnClick(R.id.layout_left_menu_8)
    public void onClickEvent8() {
        skip(Camera2ScanActivity.class);
    }

    @OnClick(R.id.layout_left_menu_9)
    public void onClickEvent9() {
        skip(FingerPrintActivity.class);
    }
}
