package com.style.constant;


/**
 * 避免广播action重复混乱，广播action最好统一放在这里
 * Created by xiajun on 2016/11/25.
 */

public class MyAction {
    /**
     * 广播action
     */
    public static final String ACTION_CALL_CONNECTING = "action.call.connected";
    public static final String ACTION_CALL_CONNECT_FAILED = "action.call.out.failed";
    public static final String ACTION_CALL_CONNECTED = "action.call.connected";
    public static final String ACTION_CALL_HANGUP = "action.call.hangup";
    public static final String ACTION_CALL_TIME_UPDATE = "action.call.time.update";
    public static final String ACTION_CALL_END = "action.call.end";

    public static final String ACTION_REFRESH_CONVERSATION = "action.refresh.conversation";

    public static final String ACTION_FILE_PREPARE_DOWNLOAD = "action.file.prepare.download";
    public static final String ACTION_FILE_GET_FAIL = "action.file.get.fail";
    public static final String ACTION_FILE_CREATE_FAIL = "action.file.create.fail";
    public static final String ACTION_FILE_DOWNING = "action.file.downing";
    public static final String ACTION_FILE_CANCEL_DOWNLOAD = "action.file.cancel.download";
    public static final String ACTION_FILE_DOWN_COMPLETE = "action.file.down.complete";
    public static final String ACTION_FILE_DOWN_FAIL = "action.file.down.fail";

}
