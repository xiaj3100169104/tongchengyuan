package cn.tongchengyuan.manager;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * Created by xiajun on 2017/11/23.
 */

public class MemoryManager {
    public final static String TAG = "MemoryManager";

    private static HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
    private static LruCache<String, Bitmap> mMemoryCache;
    public static int cacheSize;

    static void initConfig() {
        // 获取虚拟机可用内存（内存占用超过该值的时候，将报OOM异常导致程序崩溃）。最后除以1024是为了以kb为单位
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024 / 1024);
        Log.e(TAG, "maxMemory-----" + String.valueOf(maxMemory) + "Mib");
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
}
