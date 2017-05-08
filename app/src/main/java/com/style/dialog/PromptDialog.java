package com.style.dialog;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.same.city.love.R;

/**
 * Created by xiajun on 2017/3/29.
 */

public class PromptDialog {

    private Context context;
    private AlertDialog.Builder builder;
    private AlertDialog dlgPrompt;
    private OnPromptListener listener;

    public PromptDialog(Context context) {
        this.context = context;
        builder = new AlertDialog.Builder(this.context);
    }


    public void setTitle(String title) {
        if (title != null)
            builder.setTitle(title);
    }

    public void setMessage(String msg) {
        if (msg != null)
            builder.setMessage(msg);
    }

    public void setListener(OnPromptListener listener) {
        if (builder != null) {
            this.listener = listener;
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PromptDialog.this.listener.onPositiveButton();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PromptDialog.this.listener.onNegativeButton();
                }
            });
        }
    }

    public void show() {
        if (builder != null) {
            dlgPrompt = builder.create();
            dlgPrompt.show();
        }
    }

    public void dismiss() {
        if (dlgPrompt != null) {
            dlgPrompt.dismiss();
        }
    }

    public interface OnPromptListener {
        void onPositiveButton();

        void onNegativeButton();
    }
}
