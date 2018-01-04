package cn.tongchengyuan.chat.xmpp.iq;

import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.StringUtils;
import org.jxmpp.util.XmppDateTime;

import java.util.Date;
import java.util.Random;

/**
 * Created by 王者 on 2016/8/17.
 */
public class FileTransferIQ extends IQ{
    public static final String ELEMENT = "transfer";
    public static final String NAMESPACE = "xmpp:custom:transfer";


    private static Random random = new Random();

    private String id;

    private String mimeType;

    private String digest;

    private File file;

    public FileTransferIQ() {
        super(ELEMENT, NAMESPACE);
        id = "file_" + Math.abs(random.nextLong());
    }

    public void setSessionID(final String id) {
        this.id = id;
    }

    public String getSessionID() {
        return id;
    }

    public void setMimeType(final String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setFile(final File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.optAttribute("id", getSessionID());
        xml.optAttribute("mime-type", getMimeType());
        xml.rightAngleBracket();

        // Add the file section if there is one.
        xml.optAppend(file.toXML());

        return xml;
    }

    public static class File implements ExtensionElement {

        private final String name;

        private final long size;

        private String hash;

        private Date date;

        private String desc;

        private boolean isRanged;

        /**
         * Constructor providing the name of the file and its size.
         *
         * @param name The name of the file.
         * @param size The size of the file in bytes.
         */
        public File(final String name, final long size) {
            if (name == null) {
                throw new NullPointerException("name cannot be null");
            }

            this.name = name;
            this.size = size;
        }

        /**
         * Returns the file's name.
         *
         * @return Returns the file's name.
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the file's size.
         *
         * @return Returns the file's size.
         */
        public long getSize() {
            return size;
        }

        /**
         * Sets the MD5 sum of the file's contents
         *
         * @param hash The MD5 sum of the file's contents.
         */
        public void setHash(final String hash) {
            this.hash = hash;
        }

        /**
         * Returns the MD5 sum of the file's contents
         *
         * @return Returns the MD5 sum of the file's contents
         */
        public String getHash() {
            return hash;
        }

        /**
         * Sets the date that the file was last modified.
         *
         * @param date The date that the file was last modified.
         */
        public void setDate(Date date) {
            this.date = date;
        }

        /**
         * Returns the date that the file was last modified.
         *
         * @return Returns the date that the file was last modified.
         */
        public Date getDate() {
            return date;
        }

        /**
         * Sets the description of the file.
         *
         * @param desc The description of the file so that the file reciever can
         *             know what file it is.
         */
        public void setDesc(final String desc) {
            this.desc = desc;
        }

        /**
         * Returns the description of the file.
         *
         * @return Returns the description of the file.
         */
        public String getDesc() {
            return desc;
        }

        /**
         * True if a range can be provided and false if it cannot.
         *
         * @param isRanged True if a range can be provided and false if it cannot.
         */
        public void setRanged(final boolean isRanged) {
            this.isRanged = isRanged;
        }

        /**
         * Returns whether or not the initiator can support a range for the file
         * tranfer.
         *
         * @return Returns whether or not the initiator can support a range for
         *         the file tranfer.
         */
        public boolean isRanged() {
            return isRanged;
        }

        public String getElementName() {
            return "file";
        }

        public String getNamespace() {
            return "xmpp:custom:transfer:file-desc";
        }

        public String toXML() {
            StringBuilder buffer = new StringBuilder();

            buffer.append("<").append(getElementName()).append(" xmlns=\"")
                    .append(getNamespace()).append("\" ");

            if (getName() != null) {
                buffer.append("name=\"").append(StringUtils.escapeForXML(getName())).append("\" ");
            }

            if (getSize() > 0) {
                buffer.append("size=\"").append(getSize()).append("\" ");
            }

            if (getDate() != null) {
                buffer.append("date=\"").append(XmppDateTime.formatXEP0082Date(date)).append("\" ");
            }

            if (getHash() != null) {
                buffer.append("hash=\"").append(getHash()).append("\" ");
            }

            if ((desc != null && desc.length() > 0) || isRanged) {
                buffer.append(">");
                if (getDesc() != null && desc.length() > 0) {
                    buffer.append("<desc>").append(StringUtils.escapeForXML(getDesc())).append("</desc>");
                }
                if (isRanged()) {
                    buffer.append("<range/>");
                }
                buffer.append("</").append(getElementName()).append(">");
            }
            else {
                buffer.append("/>");
            }
            return buffer.toString();
        }
    }
}
