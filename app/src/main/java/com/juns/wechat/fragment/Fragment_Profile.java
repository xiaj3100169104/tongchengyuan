package com.juns.wechat.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.same.city.love.R;
import com.style.base.BaseFragment;

//我
public class Fragment_Profile extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutResID = R.layout.fragment_profile;
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    protected void initData() {
       getView().setVisibility(View.GONE);

    }

}