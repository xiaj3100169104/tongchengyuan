package uk.viewpager;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.juns.wechat.R;
import com.juns.wechat.util.LogUtil;
import com.style.manager.ImageLoader;

import org.xutils.common.Callback;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageDetailFragment extends Fragment {
	private String mImageUrl;
	private ImageView mImageView;
	private ProgressBar progressBar;
	private PhotoViewAttacher attacher;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.fragment_dynamic_image_detail, container, false);
		mImageView = (ImageView) v.findViewById(R.id.image);
		progressBar = (ProgressBar) v.findViewById(R.id.loading);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ImageLoader.loadBigPicture(getActivity(), mImageView, mImageUrl);
		/*ImageLoader.loadBigPicture(mImageView, mImageUrl, new Callback.CommonCallback<Drawable>() {
			@Override
			public void onSuccess(Drawable result) {
				LogUtil.i("success!");
				attacher = new PhotoViewAttacher(mImageView);
				attacher.update();
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {

			}

			@Override
			public void onCancelled(CancelledException cex) {

			}

			@Override
			public void onFinished() {

			}
		});*/
	}
}
