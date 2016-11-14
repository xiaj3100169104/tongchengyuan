package com.juns.wechat.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.juns.wechat.R;

/**
 * Created by 王者 on 2016/9/12.
 */
public class SelectSexDialog extends Dialog {
    private RadioButton[] rbSexs;
    private RelativeLayout rlManContainer;
    private RelativeLayout rlWomanContainer;
    private int oldPosition;
    private OnItemClickListener mListener;

    public SelectSexDialog(Context context) {
        super(context);
    }

    public SelectSexDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_sex);
        rlManContainer = (RelativeLayout) findViewById(R.id.rlManContainer);
        rlWomanContainer = (RelativeLayout) findViewById(R.id.rlWomanContainer);
        rbSexs = new RadioButton[2];
        rbSexs[0] = (RadioButton) findViewById(R.id.rbMan);
        rbSexs[1] = (RadioButton) findViewById(R.id.rbWoman);

        rbSexs[oldPosition].setChecked(true);
        rbSexs[1 - oldPosition].setChecked(false);
        setListener();
    }

    public static SelectSexDialog createDialog(Context context, int position){
        SelectSexDialog selectSexDialog = new SelectSexDialog(context);
        selectSexDialog.oldPosition = position;
        return selectSexDialog;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    private void setListener(){
        rlManContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rbSexs[0].setChecked(true);
                rbSexs[1].setChecked(false);

                if(mListener != null){
                    mListener.onItemClicked(oldPosition, 0);
                }
                oldPosition = 0;
                return true;
            }
        });

        rlWomanContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rbSexs[0].setChecked(false);
                rbSexs[1].setChecked(true);

                if(mListener != null){
                    mListener.onItemClicked(oldPosition, 1);
                }
                oldPosition = 1;

                return true;
            }
        });
    }

    public interface OnItemClickListener{
        void onItemClicked(int oldPosition, int newPosition);
    }
}
