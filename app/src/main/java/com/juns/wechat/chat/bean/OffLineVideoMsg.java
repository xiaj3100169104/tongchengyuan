package com.juns.wechat.chat.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 王者 on 2016/8/19.
 */
public class OfflineVideoMsg extends Msg {
    public static final String FILE_NAME = "fileName";
    public static final String PROGRESS = "progress";
    public static final String SIZE = "size";

    public String fileName;
    public int progress;
    public int size;

    @Override
    public JSONObject toSendJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(FILE_NAME, fileName);
            jsonObject.put(SIZE, size);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
