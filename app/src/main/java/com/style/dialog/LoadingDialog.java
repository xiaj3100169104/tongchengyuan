package com.style.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.same.city.love.R;

public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        this(context, R.style.Dialog_NoTitle);
        init();
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_loading);
        Window window = getWindow();
        WindowManager.LayoutParams wmlp = window.getAttributes();
        //wmlp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wmlp.gravity = Gravity.CENTER;
        window.setAttributes(wmlp);
        //window.setWindowAnimations(R.style.Animations_SlideInFromRight_OutToLeft);
    }

    public void setMessage(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            TextView textView = (TextView) this.findViewById(R.id.tv_loading_info);
            textView.setText(msg);
        }
    }
}
