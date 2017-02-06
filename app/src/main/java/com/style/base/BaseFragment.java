package com.style.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juns.wechat.R;
import com.style.dialog.MaterialProgressDialog;
import com.style.manager.ToastManager;
import com.style.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
public abstract class BaseFragment extends Fragment {
    protected String TAG = getClass().getSimpleName();
    protected Context mContext;
    public LayoutInflater mInflater;
    protected Integer mLayoutResID;
    protected View rootView;

    public MaterialProgressDialog pd;
    protected boolean isVisible;
    protected boolean isPrepare;
    protected boolean isInit;
    public List<String> testdataList;
    private AlertDialog dlgPrompt;

    protected abstract void initData();

    protected abstract void onLazyLoad();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        mInflater = LayoutInflater.from(getContext());
        if (mLayoutResID != null)
            rootView = inflater.inflate(mLayoutResID, null);
        rootView.setFitsSystemWindows(false);
        //rootView = getTitleLayoutView(rootView);
        ButterKnife.bind(this, rootView);
        initTestData();
        return rootView;
    }

    public void initTestData() {
        testdataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            testdataList.add(String.valueOf(i));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!isPrepare) {
            mContext = getActivity();
            initData();
            isPrepare = true;
            if (isVisible && !isInit) {
                onLazyLoad();
                isInit = true;
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            init();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            isVisible = true;
            init();
        }
        super.onHiddenChanged(hidden);
    }

    private void init() {
        if (isPrepare && !isInit) {
            onLazyLoad();
            isInit = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissProgressDialog();
        ButterKnife.unbind(this);
    }
    protected void skip(Class<?> cls) {
        startActivity(new Intent(mContext, cls));
    }

    public void showProgressDialog() {
        showProgressDialog("");
    }

    public void showProgressDialog(String msg) {
        if (pd == null)
            pd = new MaterialProgressDialog(mContext, R.style.Dialog_NoTitle);
        pd.setCanceledOnTouchOutside(false);
        if (!TextUtils.isEmpty(msg))
            pd.setMessage(msg);
        pd.show();
    }

    public void showProgressDialog(int msgId) {
        showProgressDialog(getString(msgId));
    }

    public void dismissProgressDialog() {
        if (pd != null) {
            if (pd.isShowing())
                pd.dismiss();
        }
    }
    protected void showGiveUpEditDialog(final OnGiveUpEditDialogListener listener) {
        if (dlgPrompt == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("放弃编辑");
            builder.setMessage("确定要放弃此次编辑吗？");
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onPositiveButton();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onNegativeButton();
                }
            });
            dlgPrompt = builder.create();
        }
        dlgPrompt.show();
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

    protected int dip2px(float dpValue) {
        return CommonUtil.dip2px(mContext, dpValue);
    }

    protected int px2dip(float pxValue) {
        return CommonUtil.px2dip(mContext, pxValue);
    }
}
