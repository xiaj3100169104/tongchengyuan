package cn.tongchengyuan.chat.ui;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.same.city.love.R;

/**
 * Created by xiajun on 2017/1/20.
 */

public class ViewHolderPictureLeft extends ViewHolderPictureBase {
    protected ProgressBar receivingProgress;
    protected TextView tvReceivePercent;
    ViewHolderPictureLeft(View view) {
        super(view);
        receivingProgress = (ProgressBar) view.findViewById(R.id.receive_progressBar);
        tvReceivePercent = (TextView) view.findViewById(R.id.receive_percentage);

    }
    @Override
    protected void updateView() {
        super.updateView();
        if (pictureMsg.progress == 100) {
            receivingProgress.setVisibility(View.GONE);
            tvReceivePercent.setVisibility(View.GONE);
        } else {
            receivingProgress.setVisibility(View.VISIBLE);
            tvReceivePercent.setVisibility(View.VISIBLE);
            tvReceivePercent.setText(pictureMsg.progress + "%");
        }

    }
}
