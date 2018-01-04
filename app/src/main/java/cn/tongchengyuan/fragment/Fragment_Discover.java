package cn.tongchengyuan.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.camera.ocrtest.ScanCardActivity;

import cn.tongchengyuan.activity.QRScanActivity;
import cn.tongchengyuan.dynamic.FriendCircleActivity;
import cn.tongchengyuan.activity.FingerPrintActivity;
import com.same.city.love.R;
import com.same.city.love.databinding.FragmentDicoverBinding;
import com.style.base.BaseFragment;

import custom.camera2.Camera2ScanActivity;

public class Fragment_Discover extends BaseFragment implements View.OnClickListener {

    FragmentDicoverBinding bd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bd = DataBindingUtil.inflate(inflater, R.layout.fragment_dicover, container, false);
        return bd.getRoot();
    }

    protected void initData() {
        bd.layoutDiscoveryDynamic.setOnClickListener(this);
        bd.layoutLeftMenu6.setOnClickListener(this);
        bd.layoutLeftMenu7.setOnClickListener(this);
        bd.layoutLeftMenu8.setOnClickListener(this);
        bd.layoutLeftMenu9.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.layout_discovery_dynamic:
                skip(FriendCircleActivity.class);
                break;
            case R.id.layout_left_menu_6:
                skip(QRScanActivity.class);
                break;
            case R.id.layout_left_menu_7:
                skip(ScanCardActivity.class);
                break;
            case R.id.layout_left_menu_8:
                skip(Camera2ScanActivity.class);
                break;
            case R.id.layout_left_menu_9:
                skip(FingerPrintActivity.class);
                break;
        }
    }

}
