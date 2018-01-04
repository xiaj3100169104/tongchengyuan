package cn.tongchengyuan.chat.xmpp.util;

import android.util.Log;

import cn.tongchengyuan.manager.AccountManager;
import cn.tongchengyuan.chat.config.ConfigUtil;
import cn.tongchengyuan.util.FileUtil;
import cn.tongchengyuan.util.LogUtil;
import cn.tongchengyuan.chat.xmpp.XmppExceptionHandler;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

/**
 * Created by 王者 on 2016/8/19.
 */
public class FileTransferManager {
    private static final int PORT = 7700;
    private final String account = AccountManager.getInstance().getUserName();
    private int lastProgress = 0;

    public static final int ACTION_READ = 1;
    public static final int ACTION_WRITE = 2;

    public void sendFile(File file, String otherName, ProgressListener listener) {
        try {
            Socket socket = new Socket(ConfigUtil.getXmppServer(), PORT);
            OutputStream out = socket.getOutputStream();
            out.write((byte) 5);  //version
            out.write(ACTION_WRITE);

            byte[] fromData = ConfigUtil.getXmppJid(account).getBytes();
            fromData = Arrays.copyOf(fromData, 32);
            byte[] toData = ConfigUtil.getXmppJid(otherName + "").getBytes();
            toData = Arrays.copyOf(toData, 32);
            out.write(fromData);
            out.write(toData);
            String mimeType = "image/*";
            byte[] mimeTypeData = Arrays.copyOf(mimeType.getBytes(), 16);
            out.write(mimeTypeData);
            int fileNameLength = file.getName().length();
            out.write(fileNameLength);
            out.write(file.getName().getBytes());
            int fileSize = (int) file.length();
            byte[] fileSizeData = Arrays.copyOf(String.valueOf(fileSize).getBytes(), 4);
            out.write(fileSizeData);

            lastProgress = 0;
            InputStream in = FileUtil.readFile(file);
            int count = in.available();
            int wrote = 0;
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
                wrote += len;
                notifyProgressUpdated(listener, wrote, count);
            }
            in.close();
            out.flush();
            socket.shutdownOutput();

            InputStream socketIn = new DataInputStream(socket.getInputStream());
            byte[] data = new byte[8];
            socketIn.read(data);
            socket.close();
            String result = new String(data).trim();
            if ("success".equals(result)) {
                listener.transferFinished();
            }

        } catch (IOException e) {
            XmppExceptionHandler.handleIOException(e);
            listener.onFailed();
        }
    }

    public void downloadFile(String path, int fileSize, ProgressListener listener) {
        Log.e("path ==", path);
        File f = new File(path);
        if (f.exists()) {  //由于文件名都是唯一的，说明这张图片是由同一个手机上发出并在本手机上接收。
            listener.transferFinished();
            return;
        }
        if (!f.getParentFile().exists())
            f.mkdirs();
        try {
            Socket socket = new Socket(ConfigUtil.getXmppServer(), PORT);
            OutputStream out = socket.getOutputStream();
            out.write((byte) 5);  //version
            out.write(ACTION_READ);
            String fileName = f.getName();
            out.write(fileName.length());
            out.write(fileName.getBytes());
            out.flush();
            InetAddress i = socket.getLocalAddress();
            InetSocketAddress s = (InetSocketAddress) socket.getLocalSocketAddress();
            socket.shutdownOutput();

            LogUtil.i("socket write finished!");

            InputStream socketIn = new DataInputStream(socket.getInputStream());
            byte[] data = new byte[7]; //字符串末位为'\0';
            socketIn.read(data);
            String result = new String(data).trim();
            LogUtil.i("result: " + result);
            if (!"success".equals(result)) {
                listener.onFailed();
                return;
            }

            OutputStream fileOut = FileUtil.getOutputStream(f);
            if (fileOut == null) {
                listener.onFailed();
                return;
            }
            byte[] buffer = new byte[1024];
            int wrote = 0;
            int len;
            while ((len = socketIn.read(buffer)) != -1) {
                fileOut.write(buffer, 0, len);
                wrote += len;
                notifyProgressUpdated(listener, wrote, fileSize);
            }
            fileOut.close();
            socket.close();
            listener.transferFinished();
        } catch (IOException e) {
            e.printStackTrace();
            listener.onFailed();
        }
    }

    private void notifyProgressUpdated(ProgressListener listener, int wrote, int amount) {
        int progress = (int) ((((float) wrote) / amount) * 100);
        if (progress == 100 || (progress - lastProgress > 10 + 10 * Math.random())) {
            lastProgress = progress;
            listener.progressUpdated(progress);
        }
    }

    public interface ProgressListener {
        void progressUpdated(int progress);

        void transferFinished();

        void onFailed();
    }
}
