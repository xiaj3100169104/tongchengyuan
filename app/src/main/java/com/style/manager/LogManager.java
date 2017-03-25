package com.style.manager;

import android.text.TextUtils;
import android.util.Log;

public class LogManager {
	private static boolean bothNotNull(String tag, String msg) {
		if (!TextUtils.isEmpty(tag) && !TextUtils.isEmpty(msg)) {
			return true;
		}
		return false;
	}

	public static void logE(String tag, String msg) {
		if (bothNotNull(tag, msg)) {
			Log.e(tag, msg);
		}
	}
}
