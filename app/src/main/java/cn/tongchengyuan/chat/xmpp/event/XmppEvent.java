package cn.tongchengyuan.chat.xmpp.event;

/**
 * Created by 王宗文 on 2016/7/5.
 */
public class XmppEvent {
    public static final int SUCCESS = 0;
    public static final int FAILED = 1;
    public static final int LOGOUT = 2;

    public static final int CREATE_GROUP_SUCCESS = 100;
    public static final int CREATE_GROUP_FAILED = CREATE_GROUP_SUCCESS + 1;
    public static final int JOIN_CROUP_SUCCESS = CREATE_GROUP_FAILED + 1;
    public static final int JOIN_GROUP_FAILED = JOIN_CROUP_SUCCESS + 1;


    public int resultCode;
    private Object result;
    private Exception exception;

    public XmppEvent(int resultCode){
        this.resultCode = resultCode;
    }

    public XmppEvent(int resultCode, Exception exception) {
        this.resultCode = resultCode;
        this.exception = exception;
    }

    public XmppEvent(int resultCode, Object result){
        this.resultCode = resultCode;
        this.result = result;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
