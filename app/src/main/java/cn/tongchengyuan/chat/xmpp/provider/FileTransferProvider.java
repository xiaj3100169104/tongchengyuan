package cn.tongchengyuan.chat.xmpp.provider;

import cn.tongchengyuan.chat.xmpp.iq.FileTransferIQ;
import cn.tongchengyuan.util.LogUtil;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by Administrator on 2016/8/18.
 */
public class FileTransferProvider extends IQProvider<FileTransferIQ> {
    public static final FileTransferProvider INSTANCE = new FileTransferProvider();

    @Override
    public FileTransferIQ parse(XmlPullParser parser, int initialDepth) throws XmlPullParserException, IOException, SmackException {

        FileTransferIQ fileTransferIQ = null;
        boolean done = false;
        int event = parser.getEventType();
        while (!done) {
            if (event == XmlPullParser.START_TAG) {
                LogUtil.i("name: " + parser.getName());
                if (parser.getNamespace().equals(FileTransferIQ.NAMESPACE)) {
                    fileTransferIQ = new FileTransferIQ();
                    fileTransferIQ.setType(IQ.Type.result);
                    fileTransferIQ.setSessionID(parser.getAttributeValue(null, "id"));
                    fileTransferIQ.setMimeType(parser.getAttributeValue(null, "mime-type"));
                    fileTransferIQ.setDigest(parser.getAttributeValue(null, "digest"));
                } else if (parser.getNamespace().equals("xmpp:custom:transfer:file-desc")) {
                    String fileName = parser.getAttributeValue(null, "name");
                    LogUtil.i("fileName: " + fileName);
                    String fileSize = parser.getAttributeValue(null, "size");
                    long size = 0;
                    try {
                        size = Long.parseLong(fileSize);
                    } catch (Exception e) {
                    }
                    FileTransferIQ.File file = new FileTransferIQ.File(fileName, size);
                    fileTransferIQ.setFile(file);
                }
            } else if (event == XmlPullParser.END_TAG && parser.getName().equals(FileTransferIQ.ELEMENT)) {
                done = true;
            }
            if(!done){
                event = parser.next();
            }
        }
        return fileTransferIQ;
    }
}
