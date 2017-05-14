package com.juns.wechat.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.same.city.love.R;
import com.juns.wechat.dynamic.FriendCircleActivity;
import com.juns.wechat.activity.MyCollectActivity;
import com.juns.wechat.activity.MyProfileActivity;
import com.juns.wechat.activity.SettingActivity;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.database.dao.DbDataEvent;
import com.juns.wechat.database.UserTable;
import com.juns.wechat.helper.CommonViewHelper;
import com.juns.wechat.manager.AccountManager;
import com.style.base.BaseFragment;

import org.simple.eventbus.Subscriber;

import java.util.List;

import butterknife.Bind;

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