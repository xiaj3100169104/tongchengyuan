package com.style.dialog;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.same.city.love.R;

/**
 * Created by xiajun on 2017/3/29.
 */

public class EditAlertDialog extends AlertDialog {

    private Context context;
    private AlertDialog.Builder builder;
    private OnPromptListener listener;
    private EditText etContent;
    private String content;
    private CharSequence hint;

    public EditAlertDialog(Context context) {
        super(context);
        this.context = context;

    }

    public void setData(String content, CharSequence hint) {
        this.content = content;
        this.hint = hint;

    }

    public void setTitle(String title) {
        if (title != null)
            builder.setTitle(title);
    }

    public void setMessage(String msg) {
        if (msg != null)
            builder.setMessage(msg);
    }

    private void setListener(OnPromptListener listener) {
        this.listener = listener;
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = etContent.getText().toString();
                EditAlertDialog.this.listener.onPositiveButton(s);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditAlertDialog.this.listener.onNegativeButton();
            }
        });
    }

    public void show(OnPromptListener listener) {
        builder = new AlertDialog.Builder(this.context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_simple_edit, null);
        etContent = (EditText) view.findViewById(R.id.et_content);
        builder.setView(view);
        setListener(listener);
        setData(content, hint);
        setContent(content);
        setHint(hint);
        builder.create().show();
    }

    public void dismiss() {
        if (builder != null) {
            builder.create().dismiss();
        }
    }

    public void setContent(String content) {
        if (!TextUtils.isEmpty(content)) {
            etContent.setText(content);
            etContent.setSelection(content.length());
        } else
            etContent.setText("");
    }

    public String getContent() {
        return etContent.getText().toString();
    }

    public void setHint(CharSequence hint) {
        if (!TextUtils.isEmpty(hint))
            etContent.setHint(hint);
        else
            etContent.setHint("");
    }


    public interface OnPromptListener {
        void onPositiveButton(String s);

        void onNegativeButton();
    }
}
