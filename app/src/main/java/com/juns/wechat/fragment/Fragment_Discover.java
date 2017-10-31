package com.juns.wechat.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.same.city.love.R;
import com.juns.wechat.chat.voice.CallVoiceBaseActivity;
import com.juns.wechat.dynamic.FriendCircleActivity;
import com.juns.wechat.activity.QRScanActivity;
import com.style.base.BaseFragment;

import butterknife.Bind;
import butterknife.OnClick;

public class Fragment_Discover extends BaseFragment {

    @Bind(R.id.txt_pengyouquan)
    TextView txtPengyouquan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutResID = R.layout.fragment_dicover;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void initData() {
        // TODO Auto-generated method stub

    }

    @OnClick(R.id.txt_pengyouquan) // 朋友圈
    public void friendCircle() {
        skip(FriendCircleActivity.class);
    }

}
