package com.style.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juns.wechat.R;
import com.style.manager.LogManager;
import com.style.manager.ToastManager;
import com.style.utils.CommonUtil;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {
    protected String TAG = getClass().getSimpleName();
    protected Context mContext;
    public LayoutInflater mInflater;
    protected Integer mLayoutResID;
    protected View rootView;

    protected boolean isViewCreated;
    protected boolean isInit;
    protected boolean isVisible;
    private boolean isLazyData;

    protected abstract void initData();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        mInflater = LayoutInflater.from(getContext());
        if (mLayoutResID != null)
            rootView = inflater.inflate(mLayoutResID, null);
        rootView.setFitsSystemWindows(false);
        //rootView = getTitleLayoutView(rootView);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        init();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
        }
        init();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            isVisible = true;
        }
        init();
    }

    private void init() {
        if (isViewCreated && !isInit) {
            initData();
            isInit = true;
        }
        if (isViewCreated && isVisible && isInit && !isLazyData) {
            lazyData();
            isLazyData = true;
        }
    }

    protected void lazyData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    protected void skip(Class<?> cls) {
        startActivity(new Intent(mContext, cls));
    }

    public void logE(String tag, String msg) {
        LogManager.logE(tag, msg);
    }

    public interface OnGiveUpEditDialogListener {
        void onPositiveButton();

        void onNegativeButton();
    }

    public void showToast(String str) {
        ToastManager.showToast(mContext, str);
    }

    public void showToast(int resId) {
        ToastManager.showToast(mContext, resId);
    }

    public void showToastLong(String msg) {
        ToastManager.showToastLong(mContext, msg);
    }

    public void showToastLong(int msgId) {
        ToastManager.showToastLong(mContext, msgId);
    }

    protected void setText(TextView textView, int strId) {
        setText(textView, mContext.getString(strId));
    }

    protected void setText(TextView textView, String str) {
        CommonUtil.setText(textView, getNotNullText(str));
    }

    protected static CharSequence getNotNullText(String str) {
        return CommonUtil.getNotNullText(str);
    }
}
