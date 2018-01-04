package cn.tongchengyuan.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.same.city.love.R;

/**
 * Created by 王宗文 on 2016/7/16.
 */
public class SelectPhotoDialog extends Dialog{
    private Button tvTakePhoto;
    private Button tvOpenAlbum;
    private OnItemClickListener mListener;
    private Context context;

    public SelectPhotoDialog(Context context) {
        this(context, R.style.Dialog_NoTitle);
    }

    public SelectPhotoDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        setOwnerActivity((Activity) context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_sex);
        tvTakePhoto = (Button) findViewById(R.id.item_camera);
        tvOpenAlbum = (Button) findViewById(R.id.item_photo);

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
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setGravity(Gravity.CENTER);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        window.setLayout(dm.widthPixels-200, window.getAttributes().height);
       // window.setWindowAnimations(R.style.Animations_SlideInFromBottom_OutToBottom);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public interface OnItemClickListener{
        void takePhoto(View v);
        void openAlbum(View v);
    }
}
