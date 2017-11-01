package uk.viewpager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.same.city.love.R;
import com.juns.wechat.config.ConfigUtil;
import com.style.base.BaseFragment;
import com.style.constant.FileConfig;
import com.style.manager.ImageLoader;
import com.style.net.image.FileDownloadCallback;
import com.style.net.image.FileDownloadManager;
import com.style.utils.FileUtil;

import butterknife.Bind;

public class ImageDetailFragment extends BaseFragment {
    @Bind(R.id.image)
    ImageView image;
    @Bind(R.id.progressbar)
    ProgressBar progressBar;
    @Bind(R.id.tv_percent)
    TextView tvPercent;
    private String imgName;
    private String url;

    public static ImageDetailFragment newInstance(String imgName) {
        ImageDetailFragment newFragment = new ImageDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("imgName", imgName);
        newFragment.setArguments(bundle);
        return newFragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutResID = R.layout.fragment_dynamic_image_detail;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initData() {

        progressBar.setVisibility(View.GONE);
        tvPercent.setVisibility(View.GONE);
        Bundle args = getArguments();
        if (args != null) {
            logE(TAG, "args不为空");
            imgName = args.getString("imgName");
        }
        logE(TAG, "文件名" + imgName);
        ImageLoader.loadPictureByName(mContext, image, imgName);
        return;

       /* String path = FileConfig.DIR_CACHE + "/" + imgName;
        if (FileUtil.isExist(path)) {
            fileExist(path);
            return;
        }
        url = ConfigUtil.getDownUrl(imgName);
        FileDownloadManager.getInstance().down(url, path, new FileDownloadCallback() {
            @Override
            public void complete(String filePath) {
                super.complete(filePath);
                fileExist(filePath);
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
*/
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
        FileDownloadManager.getInstance().cancelCallback(url);
    }
}
