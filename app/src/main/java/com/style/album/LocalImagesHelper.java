package com.style.album;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.style.utils.FileUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class LocalImagesHelper {

    public static List<PicBucket> getPicBuckets(Context context) {
        List<PicBucket> bucketList = new ArrayList<>();
        List<ImageItem> list = getPictures(context);
        for (int i = 0; i < list.size(); i++) {
            ImageItem item = list.get(i);
            boolean haveBucket = false;
            for (int j = 0; j < bucketList.size(); j++) {
                PicBucket bucket = bucketList.get(j);
                if (item.getBucketName().equals(bucket.getBucketName())) {
                    haveBucket = true;// 存在父文件夹
                    bucket.getImages().add(item);
                }
            }
            if (!haveBucket) {
                PicBucket bucket = new PicBucket();
                bucket.setBucketName(item.getBucketName());
                List<ImageItem> subList = new ArrayList<ImageItem>();
                subList.add(item);
                bucket.setImages(subList);
                bucketList.add(bucket);
            }
        }
        return bucketList;
    }

    public static List<ImageItem> getPictures(Context context) {
        List<ImageItem> imageList = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (cur != null) {
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    int id = cur.getInt(cur.getColumnIndex(MediaStore.Images.Media._ID));
                    String path = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATA));
                    long time = cur.getLong(cur.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                    int size = cur.getInt(cur.getColumnIndex(MediaStore.Images.Media.SIZE));
                    String displayName = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                    String title = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.TITLE));
                    double latitude = cur.getDouble(cur.getColumnIndex(MediaStore.Images.Media.LATITUDE));
                    double longitude = cur.getDouble(cur.getColumnIndex(MediaStore.Images.Media.LONGITUDE));
                    String bucketName = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                    ImageItem imageItem = new ImageItem(path, time, size, displayName, title, latitude, longitude, bucketName, false);
                    imageItem.setTime(parserTimeToYM(time));
                    Cursor nCur = cr.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Thumbnails.IMAGE_ID + " = ?", new String[]{String.valueOf(id)}, null);
                    while (nCur.moveToNext()) {
                        String thumbnPath = nCur.getString(nCur.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
                        imageItem.setThumbnPath(thumbnPath);
                    }
                    nCur.close();
                    if (FileUtil.isExist(imageItem.getImagePath())) {
                        imageList.add(imageItem);
                    }
                }
            }
            cur.close();
        }
        return imageList;
    }

    public static String parserTimeToYM(long time) {
        System.setProperty("user.timezone", "Asia/Shanghai");
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(new Date(time * 1000L));
    }
}
