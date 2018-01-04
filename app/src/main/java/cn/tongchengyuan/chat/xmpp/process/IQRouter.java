package cn.tongchengyuan.chat.xmpp.process;

import org.jivesoftware.smack.packet.IQ;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 王宗文 on 2016/6/30.
 */
public final class IQRouter {
    private Map<String, IQProcessor> namespace2Processor;

    private static IQRouter mInstance;

    public synchronized static IQRouter getInstance(){
        if(mInstance == null){
            mInstance = new IQRouter();
        }
        return mInstance;
    }

    private IQRouter(){
        namespace2Processor = new ConcurrentHashMap<>();
    }

    public void routeIQ(IQ packet){
        String nameSpace = packet.getChildElementNamespace();
        for(String key : namespace2Processor.keySet()){
            if(key.equals(nameSpace)){
                namespace2Processor.get(key).processIQ(packet);
            }
        }
    }

    public void addIQProcessor(IQProcessor iqProcessor){
        namespace2Processor.put(iqProcessor.getNameSpace(), iqProcessor);
        iqProcessor.initialize();
    }

    public void removeIQProcessor(IQProcessor iqProcessor){
        namespace2Processor.remove(iqProcessor.getNameSpace());
        iqProcessor.destroy();
    }
}
