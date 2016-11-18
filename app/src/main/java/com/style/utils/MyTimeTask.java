package com.style.utils;

import android.os.AsyncTask;
import android.widget.Button;

public class MyTimeTask extends AsyncTask<Integer, Integer, String> {
	private int count;
	private Button button;
	private String text;

	public MyTimeTask(int count, Button button, String string) {
		super();
		this.count = count;
		this.button = button;
		this.text = string;
	}

	@Override
	protected String doInBackground(Integer... arg0) {
		while (count > 0) {
			try {
				count -= arg0[0];
				publishProgress(count);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return text;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		button.setText(values[0].toString());
	}

	@Override
	protected void onPostExecute(String result) {
		button.setText(result);
		button.setEnabled(true);
	}
}
