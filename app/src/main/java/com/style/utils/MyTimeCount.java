package com.style.utils;

import android.os.CountDownTimer;
import android.widget.Button;

public class MyTimeCount extends CountDownTimer {
	private Button button;
	private String countDownStr;

	public MyTimeCount(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
	}

	public void setButton(Button button, String countDownStr) {
		this.button = button;
		this.countDownStr = countDownStr;
	}

	@Override
	public void onFinish() {// 计时完毕时触发
		button.setText(countDownStr);
		button.setEnabled(true);
	}

	@Override
	public void onTick(long millisUntilFinished) {// 计时过程显示
		button.setEnabled(false);
		button.setText(String.valueOf(millisUntilFinished / 1000));
	}
}
