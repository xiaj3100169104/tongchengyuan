package cn.tongchengyuan.chat.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class OfflineVideoMsgData extends MsgData {
    public static final String FILE_NAME = "fileName";
    public static final String PROGRESS = "progress";
    public static final String WIDTH =  "width";
    public static final String HEIGHT = "height";
    public static final String SIZE = "size";

    public String fileName;
    public int progress;
    public int width;
    public int height;
    public int size;

    @Override
    public JSONObject toSendJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(FILE_NAME, fileName);
            jsonObject.put(WIDTH, width);
            jsonObject.put(HEIGHT, height);
            jsonObject.put(SIZE, size);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
