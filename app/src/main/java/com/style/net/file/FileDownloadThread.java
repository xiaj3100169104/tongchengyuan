package com.style.net.file;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;


public class FileDownloadThread extends Thread {

    private static final String TAG = FileDownloadThread.class.getSimpleName();

    /** 当前下载是否完成 */
    private boolean isCompleted = false;
    /** 当前下载文件长度 */
    private int downloadLength = 0;
    /** 文件保存路径 */
    private File file;
    /** 文件下载路径 */
    private URL url;
    /** 下载始位置 */
    private int startPos;
    /** 下载结束位置 */
    private int endPos;

    public FileDownloadThread(URL url, File file, int startPos, int endPos) {
        this.url = url;
        this.file = file;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    @Override
    public void run() {

        BufferedInputStream bis = null;
        RandomAccessFile raf = null;

        try {
            URLConnection conn = url.openConnection();
            conn.setAllowUserInteraction(true);
            conn.setRequestProperty("Charset", "UTF-8");

            //设置当前线程下载的起点、终点
            System.out.println(Thread.currentThread().getName() + "  bytes=" + startPos + "-" + endPos);
            conn.setRequestProperty("Range", "bytes=" + startPos + "-" + endPos);

            byte[] buffer = new byte[1024];
            bis = new BufferedInputStream(conn.getInputStream());

            raf = new RandomAccessFile(file, "rwd");
            raf.seek(startPos);
            int len;
            while ((len = bis.read(buffer, 0, 1024)) != -1) {
                raf.write(buffer, 0, len);
                downloadLength += len;
            }
            isCompleted = true;
            Log.d(TAG, getName()+" is finished,all size:" + downloadLength);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 线程文件是否下载完毕
     */
    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * 线程下载文件长度
     */
    public int getDownloadLength() {
        return downloadLength;
    }

}