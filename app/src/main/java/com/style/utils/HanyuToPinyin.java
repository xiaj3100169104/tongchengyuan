package com.style.utils;

import android.util.Log;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class HanyuToPinyin {
	private final static String TAG = "HanyuToPinyin";
	private static HanyuPinyinOutputFormat mDefaultFormat = new HanyuPinyinOutputFormat();

	private static HanyuPinyinOutputFormat getInstance() {
		if (mDefaultFormat == null) {
			mDefaultFormat = new HanyuPinyinOutputFormat();
		}
		return mDefaultFormat;
	}

	public static String converterToFirstSpell(String chines) {
		if (chines == null) {
			Log.e(TAG, "string value = null, return!");
			return "";
		}
		String pinyinName = "";

		try {
			char[] nameChar = chines.toCharArray();
			getInstance().setCaseType(HanyuPinyinCaseType.UPPERCASE);
			getInstance().setToneType(HanyuPinyinToneType.WITHOUT_TONE);

			for (int i = 0; i < nameChar.length; i++) {
				if (nameChar[i] > 128) {
					try {
						pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], getInstance())[0].charAt(0);
					} catch (BadHanyuPinyinOutputFormatCombination e) {
						Log.e(TAG, e + "");
					}
				} else {
					pinyinName += nameChar[i];
				}
			}
		} catch (Exception e) {
			Log.e(TAG, e + "");
			pinyinName = "";
		}
		return pinyinName;
	}

	public static String converterToSpell(String chines) {
		if (chines == null) {
			Log.e(TAG, "string value = null, return!");
			return "";
		}
		String pinyinName = "";
		try {
			char[] nameChar = chines.toCharArray();
			getInstance().setCaseType(HanyuPinyinCaseType.UPPERCASE);
			getInstance().setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			for (int i = 0; i < nameChar.length; i++) {
				if (nameChar[i] > 128) {
					try {
						pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], getInstance())[0];
					} catch (BadHanyuPinyinOutputFormatCombination e) {
						Log.e(TAG, e + "");
					}
				} else {
					pinyinName += nameChar[i];
				}
			}
		} catch (Exception e) {
			Log.e(TAG, e + "");
			pinyinName = "";
		}

		return pinyinName;
	}

	public static String hanziToCapital(String hanzi) {
		String sortString = "#";
		String pinyin = HanyuToPinyin.converterToSpell(hanzi);
		if ("" != pinyin) {
			String uc = pinyin.substring(0, 1).toUpperCase();
			if (uc.matches("[A-Z]")) {
				sortString = uc;
			}
		}
		return sortString;
	}
}
