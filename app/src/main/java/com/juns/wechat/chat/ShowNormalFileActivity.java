package com.juns.wechat.chat;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.widget.ProgressBar;

import com.juns.wechat.R;
import com.style.base.BaseActivity;

public class ShowNormalFileActivity extends BaseActivity {
	private ProgressBar progressBar;
	private File file;

	@Override
	public void initData() {
		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		/*final FileMessageBody messageBody = getIntent().getParcelableExtra(
				"msgbody");
		file = new File(messageBody.getLocalUrl());*/
		// set head map
		final Map<String, String> maps = new HashMap<String, String>();
		/*if (!TextUtils.isEmpty(messageBody.getSecret())) {
			maps.put("share-secret", messageBody.getSecret());
		}*/
		// 下载文件
		new Thread(new Runnable() {
			public void run() {
				/*HttpFileManager fileManager = new HttpFileManager(
						ShowNormalFileActivity.this, EMChatConfig.getDbManager()
								.getStorageUrl());
				fileManager.downloadFile(messageBody.getRemoteUrl(),
						messageBody.getLocalUrl(), maps,
						new CloudOperationCallback() {

							@Override
							public void onSuccess(String result) {
								runOnUiThread(new Runnable() {
									public void run() {
										FileUtils.openFile(file,
												ShowNormalFileActivity.this);
										finish();
									}
								});
							}

							@Override
							public void onProgress(final int progress) {
								runOnUiThread(new Runnable() {
									public void run() {
										progressBar.setProgress(progress);
									}
								});
							}

							@Override
							public void onError(final String msg) {
								runOnUiThread(new Runnable() {
									public void run() {
										if (file != null && file.exists()
												&& file.isFile())
											file.delete();
										String str4 = getResources()
												.getString(
														R.string.Failed_to_download_file);
										Toast.makeText(
												ShowNormalFileActivity.this,
												str4 + msg, Toast.LENGTH_SHORT)
												.show();
										finish();
									}
								});
							}
						});
*/
			}
		}).start();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mLayoutResID = R.layout.activity_show_file;
		super.onCreate(savedInstanceState);

	}
}
