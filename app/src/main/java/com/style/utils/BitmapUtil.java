package com.style.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class BitmapUtil {
	public final static String TAG = "BitmapCache";
	private static HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
	private static LruCache<String, Bitmap> mMemoryCache;
	public static int cacheSize;

	static void initConfig() {
		// 获取虚拟机可用内存（内存占用超过该值的时候，将报OOM异常导致程序崩溃）。最后除以1024是为了以kb为单位
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		Log.e(TAG, "maxMemory-----" + String.valueOf(maxMemory / 1024) + "Mib");
		// 使用可用内存的1/8来作为Memory Cache
		cacheSize = maxMemory / 8;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// 重写sizeOf()方法，使用Bitmap占用内存的kb数作为LruCache的size
				return bitmap.getByteCount() / 1024;
			}
		};
	}
	public static void getMemoryCacheSize(String path, Bitmap bmp) {
		if (!TextUtils.isEmpty(path) && bmp != null) {
			imageCache.put(path, new SoftReference<Bitmap>(bmp));
		}
	}
	public static void put(String path, Bitmap bmp) {
		if (!TextUtils.isEmpty(path) && bmp != null) {
			imageCache.put(path, new SoftReference<Bitmap>(bmp));
		}
	}

	public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	public static Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}

	public static void displayBmp(final Activity act, final ImageView imageView, final String path, final int reqWidth, final int reqHeight, final int maxResolution) {
		if (null != imageView) {
			if (imageCache.containsKey(path)) {
				SoftReference<Bitmap> reference = imageCache.get(path);
				Bitmap bmp = reference.get();
				if (bmp != null) {
					imageView.setImageBitmap(bmp);
					return;
				}
			}
			Bitmap cachebitmap = getBitmapFromMemCache(path);
			if (cachebitmap != null) {
				imageView.setImageBitmap(cachebitmap);
				return;
			}
			new Thread() {
				Bitmap thumb;

				public void run() {
					try {
						thumb = BitmapUtil.revitionImageSize(path, reqHeight, reqHeight, maxResolution);
					} catch (Exception e) {
					}
					if (null != thumb) {
						act.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								imageView.setImageBitmap(thumb);
								put(path, thumb);
								addBitmapToMemoryCache(path, thumb);
							}
						});
					}
				}
			}.start();
		}
	}

	public static Bitmap revitionImageSize(String path, int reqWidth, int reqHeight, int maxResolution) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		// int i = 0;
		Bitmap bitmap = null;
		// Log.e("options.outWidth", String.valueOf(options.outWidth));
		// Log.e("options.outHeight", String.valueOf(options.outHeight));
		in = new BufferedInputStream(new FileInputStream(new File(path)));
		// 计算 inSampleSize 的值
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		// Log.e("options.inSampleSize", String.valueOf(options.inSampleSize));
		/*
		 * ALPHA_8：每个像素占用1byte内存 ARGB_4444：每个像素占用2byte内存 ARGB_8888：每个像素占用4byte内存
		 * （默认） RGB_565：每个像素占用2byte内存
		 */
		// options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inPreferredConfig = null; /* 设置让解码器以最佳方式解码 */
		options.inJustDecodeBounds = false;// 是否只读取边界
		options.inDither = false; /* 不进行图片抖动处理 */
		/* 下面两个字段需要组合使用 */
		options.inPurgeable = true;
		options.inInputShareable = true;
		bitmap = BitmapFactory.decodeStream(in, null, options);
		// Log.e("options.outWidth", String.valueOf(bitmap.getWidth()));
		// Log.e("options.outWidth", String.valueOf(bitmap.getHeight()));
		Bitmap newBmp = BitmapUtil.changeResolution(bitmap, maxResolution);
		return newBmp;
	}

	public static Bitmap changeResolution(Bitmap oldBmp, int maxResolution) {
		float w = oldBmp.getWidth();
		float h = oldBmp.getHeight();
		float scale = w / h >= 1 ? h / w : w / h;
		if (w > maxResolution || h > maxResolution) {
			float newWidth;
			float newHeight;
			if (w >= h) {
				newWidth = maxResolution;
				newHeight = newWidth * scale;
			} else {
				newHeight = maxResolution;
				newWidth = newHeight * scale;
			}
			return Bitmap.createScaledBitmap(oldBmp, (int) newWidth, (int) newHeight, true);
		}
		return oldBmp;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// 原始图片的宽高
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			// 在保证解析出的bitmap宽高分别大于目标尺寸宽高的前提下，取可能的inSampleSize的最大值
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	/**
	 * saveBitmap
	 * 
	 * @param @param filename---完整的路径格式-包含目录以及文件名
	 * @param @param bitmap
	 * @param @param isDelete --是否只留一张
	 * @return void
	 * @throws
	 */
	public static void saveBitmap(String dirpath, String filename, Bitmap bitmap, int quality, boolean isDelete) {
		File dir = new File(dirpath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dirpath, filename);
		// 若存在即删除-默认只保留一张
		if (isDelete) {
			if (file.exists()) {
				file.delete();
			}
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
				out.flush();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
					recycle(bitmap);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void recycle(Bitmap bitmap) {
		if (bitmap != null && bitmap.isRecycled()) {
			bitmap.recycle();
		}
	}
	

	public static Bitmap drawableToBitmap (Drawable drawable) {
	    if (drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable)drawable).getBitmap();
	    }

	    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);

	    return bitmap;
	}

	public static InputStream bitmapToInputStream(Bitmap bitmap) {
	    int size = bitmap.getHeight() * bitmap.getRowBytes();
	    ByteBuffer buffer = ByteBuffer.allocate(size);
	    bitmap.copyPixelsToBuffer(buffer);
	    return new ByteArrayInputStream(buffer.array());
	}
}
