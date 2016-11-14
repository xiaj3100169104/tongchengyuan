package com.juns.wechat.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.juns.wechat.R;

/**
 * Created by 王宗文 on 2016/7/16.
 */
public class SelectPhotoDialog extends Dialog{
    private TextView tvTakePhoto;
    private TextView tvOpenAlbum;
    private OnClickListener mListener;

    public SelectPhotoDialog(Context context) {
        super(context);
    }

    public SelectPhotoDialog(Context context, int theme) {
        super(context, theme);
    }

    public static SelectPhotoDialog createDialog(Context context, OnClickListener listener){
        SelectPhotoDialog dialog = new SelectPhotoDialog(context);
        dialog.setListener(listener);
        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alertdialog);
        tvTakePhoto = (TextView) findViewById(R.id.tv_content1);
        tvOpenAlbum = (TextView) findViewById(R.id.tv_content2);

        tvTakePhoto.setText("拍照");
        tvOpenAlbum.setText("相册");

        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.takePhoto(v);
            }
        });

        tvOpenAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.openAlbum(v);
            }
        });
    }

    public void setListener(OnClickListener listener){
        this.mListener = listener;
    }

    public interface OnClickListener{
        void takePhoto(View v);
        void openAlbum(View v);
    }
}
