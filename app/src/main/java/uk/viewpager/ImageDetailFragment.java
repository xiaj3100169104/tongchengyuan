package uk.viewpager;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.config.ConfigUtil;
import com.style.base.BaseFragment;
import com.style.constant.FileConfig;
import com.style.manager.ImageLoader;
import com.style.net.image.ImageCallback;
import com.style.net.image.ImageManager;
import com.style.utils.FileUtil;

import butterknife.Bind;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageDetailFragment extends BaseFragment {
	@Bind(R.id.image)
	ImageView image;
	@Bind(R.id.progressbar)
	ProgressBar progressBar;
	@Bind(R.id.tv_percent)
	TextView tvPercent;
	private String imgName;
    private String url;

    public ImageDetailFragment(String imgName) {
        this.imgName = imgName;
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mLayoutResID = R.layout.fragment_dynamic_image_detail;
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected void initData() {
        String dir = FileConfig.DIR_CACHE;
        if (FileUtil.isExist(dir, imgName)) {
            fileExist(dir + "/" + imgName);
            return;
        }
        url = ConfigUtil.BASE_UPLOAD_URL + imgName;
        ImageManager.getInstance().down(url, dir, imgName, new ImageCallback() {
            @Override
            public void complete(String dir, String fileName) {
                super.complete(dir, fileName);
                fileExist(dir + "/" + fileName);
            }

            @Override
            public void start(int fileSize) {
                super.start(fileSize);
                progressBar.setVisibility(View.VISIBLE);
                tvPercent.setVisibility(View.VISIBLE);
                progressBar.setMax(100);
            }

            @Override
            public void inProgress(int fileSize, int progress, int percent) {
                super.inProgress(fileSize, progress, percent);
                progressBar.setProgress(percent);
                tvPercent.setText(percent + "%");
            }

            @Override
            public void failed(String error) {
                super.failed(error);
                progressBar.setVisibility(View.GONE);
                tvPercent.setVisibility(View.GONE);
                showToast(error);
            }
        });

    }

    private void fileExist(String path) {
        progressBar.setVisibility(View.GONE);
        tvPercent.setVisibility(View.GONE);
        logE(TAG, path);
        ImageLoader.loadBigPicture(getContext(), image, path);
       /* PhotoViewAttacher attacher = new PhotoViewAttacher(image);
        attacher.update();*/

	}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ImageManager.getInstance().cancelCallback(url);
    }
}
