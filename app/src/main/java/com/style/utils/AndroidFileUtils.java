package com.style.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AndroidFileUtils {
	/**
	 * 打开裁剪图片选择器
	 * @param activity
	 * @param imagePath
	 * @param x
	 * @param y
	 * @return
	 */
	public static Uri openPicturesChooser(Activity activity, String imagePath, int x,
			int y, int requestCode) {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		File imageFile = new File(imagePath);
		if (!imageFile.getParentFile().exists()) {
			imageFile.getParentFile().mkdirs();
		}
		Uri uri = Uri.fromFile(imageFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", x);// 裁剪框比例
		intent.putExtra("aspectY", y);
		intent.putExtra("outputX", x);// 输出图片大小
		intent.putExtra("outputY", y);
		activity.startActivityForResult(intent, requestCode);
		return uri;
	}

	/**
	 * 打开图片选择器
	 * @param activity
	 */
	public static void openPicturesChooser(Activity activity, int requestCode) {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		activity.startActivityForResult(intent,	requestCode);
	}

	/**
	 * 拍照
	 * @param activity
	 */
	public static void openCamera(Activity activity, String imagePath, int requestCode) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File imageFile = new File(imagePath);
		if (!imageFile.getParentFile().exists()) {
			imageFile.getParentFile().mkdirs();
		}
		Uri imageUri = Uri.fromFile(imageFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 打开文件选择器
	 * @param activity
	 */
	public static void openFileChooser(Activity activity,int requestCode) {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	    activity.startActivityForResult(Intent.createChooser(intent, "请选择文件管理器"), requestCode);
	}
		
	/**
	 * 根据Uri得到文件
	 * 
	 * @param context
	 * @param uri
	 * @return
	 */
	public static File uriToFile(Context context, Uri uri) {
		if (uri == null) {
			return null;
		}
		File file = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor actualimagecursor = context.getContentResolver().query(uri, proj, null, null,
				null);
		int actualImageColumn = actualimagecursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		actualimagecursor.moveToFirst();
		String imgPath = actualimagecursor.getString(actualImageColumn);
		file = new File(imgPath);
		return file;
	}

	/**
	 * 根据Uri得到文件路径
	 * 
	 * @param context
	 * @param uri
	 * @return
	 */
	public static String uriToFilePath(Context context, Uri uri) {
		if (uri == null) {
			return null;
		}
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor actualimagecursor = context.getContentResolver().query(uri, proj, null, null,
				null);
		int actualImageColumn = actualimagecursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		actualimagecursor.moveToFirst();
		String imgPath = actualimagecursor.getString(actualImageColumn);
		return imgPath;
	}



	/**
	 * 
	 * @param sourceFilePath
	 * @param toFilePath
	 *            文件路径不需要传文件名字
	 * @param rewrite
	 *            是否覆盖
	 */
	public static void copyFiles(String sourceFilePath, String toFilePath,
			boolean rewrite) {
		File sourceFile = new File(sourceFilePath);
		if (!sourceFile.exists()) {
			return;
		}
		if (!sourceFile.isFile()) {
			return;
		}
		toFilePath = toFilePath + sourceFile.getName();
		File toFile = new File(toFilePath);
		if (!toFile.getParentFile().exists()) {
			toFile.getParentFile().mkdirs();
		}
		if (toFile.exists() && !rewrite) {
			return;
		}

		if (toFile.exists() && rewrite) {
			toFile.delete();
		}
		try {
			FileInputStream fosfrom = new FileInputStream(sourceFile);
			FileOutputStream fosto = new FileOutputStream(toFile);

			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c); // 将内容写到新文件当中
			}

			fosfrom.close();
			fosto.close();
		} catch (Exception ex) {
		}

	}
	
	/**
	 * 写入数据到指定文件
	 * @param filePath 文件路径
	 * @param text 写入内容
	 * @param append 是否追加内容
	 */
	public static boolean write(String filePath, String text, boolean append){
		File file = new File(filePath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		try {
			FileOutputStream fw = new FileOutputStream(filePath, append);
			byte[] byt = text.getBytes();
			fw.write(byt);
			fw.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			return false;
		} 
	}
	
	/**
	 * 放大图片
	 * 
	 * @param b
	 * @param w
	 * @param h
	 * @return
	 */
	public static Bitmap zoomIn(Bitmap b, float w, float h) {
		int width = b.getWidth();
		int height = b.getHeight();
		float sx = w / width;// 要强制转换，不转换我的在这总是死掉。
		float sy = h / height;
		Matrix matrix = new Matrix();
		matrix.postScale(sx, sy); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(b, 0, 0, width, height, matrix,
				true);
		return resizeBmp;
	}

	public static Bitmap getSizeBitmap(Context context, Uri pictureUri,
			float w, float h) {
		ContentResolver contentResolver = context.getContentResolver();

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 当为true时 允许查询图片不为 图片像素分配内存
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(contentResolver
					.openInputStream(pictureUri));
			bitmap = zoomIn(bitmap, w, h);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}

	public static Bitmap getBitmap(Context context, Uri pictureUri, float h,
			float w) {
		ContentResolver contentResolver = context.getContentResolver();

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 当为true时 允许查询图片不为 图片像素分配内存
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(
					contentResolver.openInputStream(pictureUri), null, options);
			int hRatio = (int) Math.ceil(options.outHeight / h); // 图片是高度的几倍
			int wRatio = (int) Math.ceil(options.outWidth / w); // 图片是宽度的几倍

			// 缩小到 1/ratio的尺寸和 1/ratio^2的像素
			if (hRatio > 1 || wRatio > 1) {
				if (hRatio > wRatio) {
					options.inSampleSize = hRatio;
					options.outHeight = options.outHeight / hRatio;
					options.outWidth = options.outHeight / hRatio;
				} else {
					options.inSampleSize = wRatio;
					options.outHeight = options.outHeight / wRatio;
					options.outWidth = options.outHeight / wRatio;
				}
			}
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeStream(
					contentResolver.openInputStream(pictureUri), null, options);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}

	public static Bitmap getBitmap(String filePath, float h,
			float w) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 当为true时 允许查询图片不为 图片像素分配内存
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeFile(filePath, options);
			int hRatio = (int) Math.ceil(options.outHeight / h); // 图片是高度的几倍
			int wRatio = (int) Math.ceil(options.outWidth / w); // 图片是宽度的几倍
			// System.out.println("hRatio:"+hRatio+"  wRatio:"+wRatio);
			// 缩小到 1/ratio的尺寸和 1/ratio^2的像素
			if (hRatio > 1 || wRatio > 1) {
				if (hRatio > wRatio) {
					options.inSampleSize = hRatio;
					options.outHeight = options.outHeight / hRatio;
					options.outWidth = options.outHeight / hRatio;
				} else {
					options.inSampleSize = wRatio;
					options.outHeight = options.outHeight / wRatio;
					options.outWidth = options.outHeight / wRatio;
				}
			}
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFile(filePath, options);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 将bitmap保存为图片
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String saveToSDCard(Bitmap bitmap, String filePath) {
		bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		try {
			File file = new File(filePath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 60, fos);
			fos.flush();
			fos.close();
			if (!bitmap.isRecycled()) {
				bitmap.recycle();
			}
			bitmap = null;
			return filePath;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将图片缩小另保存为
	 * @param sourcePath 源文件完整路径
	 * @param toPath 目标文件完整路径
	 * @param w
	 * @param h
	 * @return
	 */
	public static String saveToSDCard(String sourcePath,String toPath, int w, int h) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 当为true时 允许查询图片不为 图片像素分配内存
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeFile(sourcePath, options);
			int hRatio = (int) Math.ceil(options.outHeight / h); // 图片是高度的几倍
			int wRatio = (int) Math.ceil(options.outWidth / w); // 图片是宽度的几倍
			if (hRatio > 1 || wRatio > 1) {
				if (hRatio > wRatio) {
					options.inSampleSize = hRatio;
					options.outHeight = options.outHeight / hRatio;
					options.outWidth = options.outHeight / hRatio;
				} else {
					options.inSampleSize = wRatio;
					options.outHeight = options.outHeight / wRatio;
					options.outWidth = options.outHeight / wRatio;
				}
			}
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFile(sourcePath, options);
			File toFile = new File(toPath);
			if (!toFile.getParentFile().exists()) {
				toFile.getParentFile().mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(toFile);
			bitmap.compress(CompressFormat.JPEG, 60, fos);
			fos.flush();
			fos.close();
			if (!bitmap.isRecycled()) {
				bitmap.recycle();
			}
			bitmap = null;
			return toPath;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	

}
