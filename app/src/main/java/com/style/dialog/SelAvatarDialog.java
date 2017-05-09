package com.style.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.same.city.love.R;

public class SelAvatarDialog extends Dialog {

    private Button bt_takePhoto;
    private Button bt_selPhoto;
    private OnItemClickListener mListener;
    private Button bt_selCancel;

    public SelAvatarDialog(Context context) {
        this(context, R.style.Dialog_NoTitle);
    }

    public SelAvatarDialog(Context context, int theme) {
        super(context, theme);
        setOwnerActivity((Activity) context);
        init(context);
    }

    public void init(Context context) {
        setContentView(R.layout.dialog_sel_avatar);
        bt_takePhoto = (Button) this.findViewById(R.id.item_camera);
        bt_selPhoto = (Button) this.findViewById(R.id.item_photo);
        bt_selCancel = (Button) this.findViewById(R.id.item_cancel);
        bt_takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.OnClickCamera();
                dismiss();
            }
        });
        bt_selPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.OnClickPhoto();
                dismiss();
            }
        });
        bt_selCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.OnClickCancel();
                dismiss();
            }
        });
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setGravity(Gravity.BOTTOM);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        window.setLayout(dm.widthPixels, window.getAttributes().height);
        window.setWindowAnimations(R.style.Animations_SlideInFromBottom_OutToBottom);
    }

    public void setOnItemClickListener(OnItemClickListener mListener) {
        if (mListener != null)
            this.mListener = mListener;
    }

    public interface OnItemClickListener {
        void OnClickCamera();

        void OnClickPhoto();

        void OnClickCancel();
    }
}
