package com.style.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.same.city.love.R;

public class SimpleEditDialog extends Dialog {

    private Context context;
    private EditText etContent;
    private TextView itemOk;
    private TextView itemCancel;
    private OnItemClickListener mListener;

    public SimpleEditDialog(Context context) {
        this(context, R.style.Dialog_NoTitle, "", "");
    }

    public SimpleEditDialog(Context context, String content, String hint) {
        this(context, R.style.Dialog_NoTitle, content, hint);
    }

    public SimpleEditDialog(Context context, int theme, String content, String hint) {
        super(context, theme);
        setOwnerActivity((Activity) context);
        this.context = context;
        init(content, hint);
    }

    public void init(String content, String hint) {
        setContentView(R.layout.dialog_simple_edit);
        etContent = (EditText) this.findViewById(R.id.et_content);
        itemCancel = (TextView) this.findViewById(R.id.item_cancel);
        itemOk = (TextView) this.findViewById(R.id.item_ok);
        itemOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.OnClickOk();
            }
        });
        itemCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.OnClickCancel();
                dismiss();
            }
        });
        setContent(content);
        setHint(hint);
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setGravity(Gravity.CENTER);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        window.setLayout(dm.widthPixels - 200, window.getAttributes().height);
        //window.setWindowAnimations(R.style.Animations_SlideInFromBottom_OutToBottom);

    }

    public void setOnItemClickListener(OnItemClickListener mListener) {
        if (mListener != null)
            this.mListener = mListener;
    }

    public void setContent(String content) {
        if (content != null && !"".equals(content)){
            etContent.setText(content);
            etContent.setSelection(content.length());
        }
        else
            etContent.setText("");
    }

    public void setHint(String hint) {
        if (hint != null && !"".equals(hint))
            etContent.setHint(hint);
        else
            etContent.setHint("");
    }

    public String getContent() {
        return etContent.getText().toString();
    }

    public interface OnItemClickListener {

        void OnClickOk();

        void OnClickCancel();
    }
}
