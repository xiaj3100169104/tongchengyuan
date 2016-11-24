package com.juns.wechat.chat;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.juns.wechat.R;
import com.juns.wechat.common.BaseActivity;

/**
 * 展示视频内容
 * 
 * @author Administrator
 * 
 */
public class ShowVideoActivity extends BaseActivity {

	private RelativeLayout loadingLayout;
	private ProgressBar progressBar;
	private String localFilePath;
	@Override
	protected void customWindowOptions(Window window) {
		super.customWindowOptions(window);
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mLayoutResID = R.layout.activity_show_video;
		super.onCreate(savedInstanceState);

	}
	@Override
	public void initData() {

		loadingLayout = (RelativeLayout) findViewById(R.id.loading_layout);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		localFilePath = getIntent().getStringExtra("localpath");
		String remotepath = getIntent().getStringExtra("remotepath");
		String secret = getIntent().getStringExtra("secret");
		System.err.println("show video view file:" + localFilePath
				+ " remotepath:" + remotepath + " secret:" + secret);
		if (localFilePath != null && new File(localFilePath).exists()) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(localFilePath)),
					"video/mp4");
			startActivity(intent);
			finish();
		} else if (!TextUtils.isEmpty(remotepath) && !remotepath.equals("null")) {
			System.err.println("download remote video file");
			Map<String, String> maps = new HashMap<String, String>();
			if (!TextUtils.isEmpty(secret)) {
				maps.put("share-secret", secret);
			}
			downloadVideo(remotepath, maps);
		} else {

		}
	}



	public String getLocalFilePath(String remoteUrl) {
		String localPath = null;
		/*if (remoteUrl.contains("/")) {
			localPath = PathUtil.getDbManager().getVideoPath().getAbsolutePath()
					+ "/" + remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1)
					+ ".mp4";
		} else {
			localPath = PathUtil.getDbManager().getVideoPath().getAbsolutePath()
					+ "/" + remoteUrl + ".mp4";
		}*/
		return localPath;
	}

	/**
	 * 播放本地视频
	 * 
	 * @param localPath
	 *            视频路径
	 */
	private void showLocalVideo(String localPath) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(localPath)), "video/mp4");
		startActivity(intent);
		finish();
	}

	/**
	 * 下载视频文件
	 */
	private void downloadVideo(final String remoteUrl,
			final Map<String, String> header) {

		if (TextUtils.isEmpty(localFilePath)) {
			localFilePath = getLocalFilePath(remoteUrl);
		}
		if (new File(localFilePath).exists()) {
			showLocalVideo(localFilePath);
			return;
		}
		loadingLayout.setVisibility(View.VISIBLE);
		/*final HttpFileManager httpFileMgr = new HttpFileManager(this,
				EMChatConfig.getDbManager().getStorageUrl());
		final CloudOperationCallback callback = new CloudOperationCallback() {

			@Override
			public void onSuccess(String result) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						loadingLayout.setVisibility(View.GONE);
						progressBar.setProgress(0);
						showLocalVideo(localFilePath);
					}
				});
			}

			@Override
			public void onProgress(final int progress) {
				Log.d("ease", "video progress:" + progress);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						progressBar.setProgress(progress);
					}
				});

			}

			@Override
			public void onError(String msg) {
				Log.e("###", "offline file transfer error:" + msg);
				File file = new File(localFilePath);
				if (file.exists()) {
					file.delete();
				}
			}
		};

		new Thread(new Runnable() {

			@Override
			public void run() {
				httpFileMgr.downloadFile(remoteUrl, localFilePath, header,
						callback);
			}
		}).start();*/

	}

	@Override
	public void onBackPressed() {
		finish();
	}

}
