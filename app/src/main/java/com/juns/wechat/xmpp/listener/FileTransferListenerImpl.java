package com.juns.wechat.xmpp.listener;

import com.juns.wechat.util.PhotoUtil;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;

import java.io.File;
import java.io.IOException;

/**
 * Created by 王宗文 on 2016/8/17.
 */
public class FileTransferListenerImpl implements FileTransferListener {
    @Override
    public void fileTransferRequest(FileTransferRequest request) {
        IncomingFileTransfer fileTransfer = request.accept();
        try {
            fileTransfer.recieveFile(new File(PhotoUtil.PHOTO_PATH, fileTransfer.getFileName()));
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
