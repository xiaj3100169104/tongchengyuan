package com.juns.wechat.homePage.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.chat.voice.CallVoiceBaseActivity;
import com.juns.wechat.dynamic.FriendCircleActivity;
import com.juns.wechat.activity.QRScanActivity;
import com.style.base.BaseFragment;

import butterknife.Bind;
import butterknife.OnClick;

public class Fragment_Dicover extends BaseFragment {

    @Bind(R.id.txt_pengyouquan)
    TextView txtPengyouquan;
    @Bind(R.id.txt_saoyisao)
    TextView txtSaoyisao;
    @Bind(R.id.txt_yaoyiyao)
    TextView txtYaoyiyao;
    @Bind(R.id.txt_nearby)
    TextView txtNearby;
    @Bind(R.id.txt_piaoliuping)
    TextView txtPiaoliuping;
    @Bind(R.id.txt_shop)
    TextView txtShop;
    @Bind(R.id.txt_game)
    TextView txtGame;

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
    @OnClick(R.id.txt_saoyisao) // 扫一扫
    public void saoYiSao() {
        skip(QRScanActivity.class);
    }
    @OnClick(R.id.txt_yaoyiyao) // 摇一摇
    public void yaoYiYao() {
        skip(CallVoiceBaseActivity.class);
    }
    @OnClick(R.id.txt_nearby)
    public void nearby() {

    }
}
