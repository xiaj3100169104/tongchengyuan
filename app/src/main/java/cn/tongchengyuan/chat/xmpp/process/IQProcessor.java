package cn.tongchengyuan.chat.xmpp.process;

import org.jivesoftware.smack.packet.IQ;

/**
 * Created by 王宗文 on 2016/6/30.
 */
public abstract class IQProcessor {
    private String element;
    private String nameSpace;

    protected IQProcessor(String element, String nameSpace){
        this.element = element;
        this.nameSpace = nameSpace;
    }

    public void initialize(){

    }

    public void destroy(){

    }

    public abstract void processIQ(IQ packet);

    public String getElement() {
        return element;
    }

    public String getNameSpace() {
        return nameSpace;
    }
}
