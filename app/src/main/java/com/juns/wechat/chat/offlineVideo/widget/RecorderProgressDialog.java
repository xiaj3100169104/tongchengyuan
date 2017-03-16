package com.juns.wechat.chat.offlineVideo.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.juns.wechat.R;

public class RecorderProgressDialog extends Dialog {
    private Context mContext;
    private TextView tvMessage;
    private boolean cancelable = true;
    private RotateAnimation animation;

    public RecorderProgressDialog(Context context) {
        super(context, R.style.globalDialog);
        mContext = context;
        initView();
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public void initView() {
        setCanceledOnTouchOutside(false);
        setCancelable(cancelable);
        getWindow().setBackgroundDrawable(new BitmapDrawable((Resources) null));
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_recorder_progress, null);
        setContentView(view);
        tvMessage = (TextView) view.findViewById(R.id.tv_message);
    }

    @Override
    public void onBackPressed() {
        if (cancelable) {
            super.onBackPressed();
        }
    }

    public RecorderProgressDialog setMessage(String message) {
        tvMessage.setText(message);
        return this;
    }

    public RecorderProgressDialog setMessage(int id) {
        setMessage(mContext.getResources().getString(id));
        return this;
    }

    @Override
    public void show() {
        super.show();
//        animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        animation.setDuration(1000);
//        LinearInterpolator lin = new LinearInterpolator();
//        animation.setInterpolator(lin);
//        animation.setRepeatCount(Animation.INFINITE);
    }

    @Override
    public void dismiss() {
        if (!isValidContext()) {
            return;
        }
        super.dismiss();
    }

    private boolean isValidContext() {
        Activity a = (Activity) mContext;
        if (a.isFinishing()) {
            return false;
        } else {
            return true;
        }

    }
}
