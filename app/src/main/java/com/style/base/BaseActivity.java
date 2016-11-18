package com.style.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.juns.wechat.R;
import com.style.dialog.MaterialProgressDialog;
import com.style.utils.CommonUtil;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG = getClass().getSimpleName();
    protected Context context;
    public LayoutInflater mInflater;
    protected Integer mLayoutResID;
    protected View mContentView;
    public View statusBar;
    public AppBarLayout appBarLayout;
    public Toolbar toolbar;
    private TextView tvTitleBase;

    private MaterialProgressDialog progressDialog;
    private AlertDialog dlgPrompt;

    public enum TitleMode {
        NORMAL, CUSTOM
    }

    public TitleMode getLayoutMode() {
        return TitleMode.NORMAL;
    }

    public abstract void initData();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        context = this;
        mInflater = LayoutInflater.from(context);
        if (mLayoutResID != null)
            mContentView = mInflater.inflate(mLayoutResID, null);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  //竖屏
        customWindowOptions(getWindow());

        setContentView(mContentView);
        //ButterKnife must set after setContentView
        ButterKnife.bind(this);
        initData();
    }

    //自定义窗口属性
    protected void customWindowOptions(Window window) {
        //定义4.4以下窗口属性
        customWindowKitkat(window);
        //定义5.0以上窗口属性
        customWindowLollipop(window);
    }

    //定义5.0以上窗口属性
    protected void customWindowLollipop(Window window) {
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //设置状态栏颜色
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            //预留状态栏空间
            mContentView.setFitsSystemWindows(true);
        }
    }

    //定义4.4窗口属性
    protected void customWindowKitkat(Window window) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //去掉状态栏
            //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            mContentView.setFitsSystemWindows(false);
        }
    }

    @Override
    public void setContentView(View mContentView) {
        TitleMode mode = getLayoutMode();
        switch (mode) {
            case NORMAL:
                customTitleOptions(mContentView);
                break;
            case CUSTOM:
                break;
        }
        super.setContentView(mContentView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onClickTitleBack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onClickTitleBack() {
        finish();
    }

    protected void customTitleOptions(View mContentView) {
        //statusBar = mContentView.findViewById(R.id.status_bar);
        appBarLayout = (AppBarLayout) mContentView.findViewById(R.id.app_bar);
        toolbar = (Toolbar) mContentView.findViewById(R.id.toolbar);
        tvTitleBase = (TextView) mContentView.findViewById(R.id.tv_title_base);
        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
        }
    }

    protected void setNavigationIcon(int resId) {
        toolbar.setNavigationIcon(getResources().getDrawable(resId));
    }

    protected void setTitleCenterText(int resId) {
        setTitleCenterText(getString(resId));
    }

    protected void setTitleCenterText(String title) {
        setText(tvTitleBase, title);
    }

    protected View getTitleCenterView() {
        return tvTitleBase;
    }

    public void resumeTopViewColor() {
        statusBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        appBarLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    public void setTopViewColor(int color) {
        statusBar.setBackgroundColor(getResources().getColor(color));
        appBarLayout.setBackgroundColor(getResources().getColor(color));
    }

    public void setTopViewTransparent() {
        statusBar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        appBarLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        dismissProgressDialog();
    }

    public void showProgressDialog() {
        showProgressDialog("");
    }

    public void showProgressDialog(int msgId) {
        showProgressDialog(getString(msgId));
    }

    public void showProgressDialog(String msg) {
        if (progressDialog == null)
            progressDialog = new MaterialProgressDialog(context, R.style.Dialog_NoTitle);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void logE(String tag, String msg) {
        LogManager.logE(tag, msg);
    }

    protected void showGiveUpEditDialog(final OnGiveUpEditDialogListener listener) {
        if (dlgPrompt == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        ToastManager.showToast(context, str);
    }

    public void showToast(int resId) {
        ToastManager.showToast(context, resId);
    }

    public void showToastLong(String msg) {
        ToastManager.showToastLong(context, msg);
    }

    public void showToastLong(int msgId) {
        ToastManager.showToastLong(context, msgId);
    }

    protected void setText(TextView textView, int strId) {
        setText(textView, context.getString(strId));
    }

    protected void setText(TextView textView, String str) {
        CommonUtil.setText(textView, getNotNullText(str));
    }

    protected static String getNotNullText(String str) {
        return CommonUtil.getNotNullText(str);
    }

    protected int dip2px(float dpValue) {
        return CommonUtil.dip2px(context, dpValue);
    }

    protected int px2dip(float pxValue) {
        return CommonUtil.px2dip(context, pxValue);
    }
}
