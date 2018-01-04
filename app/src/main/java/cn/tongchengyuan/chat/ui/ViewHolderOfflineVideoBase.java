package cn.tongchengyuan.chat.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.same.city.love.R;
import cn.tongchengyuan.chat.bean.OfflineVideoMsgData;
import cn.tongchengyuan.chat.config.ConfigUtil;
import com.style.constant.FileConfig;
import com.style.lib.media.video.PlayVideoActivity;
import com.style.manager.ImageLoader;
import com.style.net.image.FileDownloadManager;
import com.style.utils.BitmapUtil;
import com.style.utils.CommonUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by xiajun on 2017/1/20.
 */

public class ViewHolderOfflineVideoBase extends BaseMsgViewHolder {
    protected ImageView ivPicture;
    protected OfflineVideoMsgData videoMsg;
    protected String path;
    protected String imagePath;
    private String url;

    ViewHolderOfflineVideoBase(View view) {
        super(view);
        ivPicture = (ImageView) view.findViewById(R.id.ivPicture);
    }

    @Override
    protected void updateView() {
        //先得到bean，在进行其他操作
        videoMsg = (OfflineVideoMsgData) messageBean.getMsgDataObj();
        float width = videoMsg.width;
        float height = videoMsg.height;
        float scale = height / width;
        if (scale >= 1) {             //高大于宽
            width = CommonUtil.dip2px(context, 80);
            height = width * scale;
        } else {
            width = CommonUtil.dip2px(context, 160);
            height = width * scale;
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivPicture.getLayoutParams();
        params.width = (int) width;
        params.height = (int) height;

        path = FileConfig.DIR_CACHE + "/" + videoMsg.fileName;
        imagePath = path + ".image";
        this.url = ConfigUtil.getMsgDownUrl(videoMsg.fileName);

        super.updateView();

        this.loadPreview();
    }

    @Override
    protected void onClickLayoutContainer() {
        super.onClickLayoutContainer();
        Log.e(TAG, "path==" + path);
        Intent data = new Intent();
        data.putExtra("path", path);
        data.setClass(context, PlayVideoActivity.class);
        context.startActivity(data);
    }

    //先判断预览图文件存不存在，不存在再根据视频获取预览图并保存
    protected void loadPreview() {
        File f = new File(imagePath);
        if (!f.exists()) {
            File v = new File(path);
            if (v.exists()) {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(path);
                Bitmap bitmap = retriever.getFrameAtTime(1000 * 1000, MediaMetadataRetriever.OPTION_CLOSEST);
                try {
                    BitmapUtil.saveBitmap(imagePath, bitmap, 100);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                FileDownloadManager.getInstance().down(url, path, new OfflineVideoDownloadCallback(messageBean));
            }
        }
        ImageLoader.loadTriangleImage(ivPicture, imagePath, isLeftLayout() ? 0 : 1);
    }

}

