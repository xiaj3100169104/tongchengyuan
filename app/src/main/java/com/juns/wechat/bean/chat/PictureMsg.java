package com.juns.wechat.bean.chat;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 王者 on 2016/8/19.
 */
public class PictureMsg extends Msg {
    public static final String IMG_NAME = "imgName";
    public static final String PROGRESS = "progress";
    public static final String WIDTH =  "width";
    public static final String HEIGHT = "height";
    public static final String SIZE = "size";

    public String imgName;
    public int progress;
    public int width;
    public int height;
    public int size;

    @Override
    public JSONObject toSendJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(IMG_NAME, imgName);
            jsonObject.put(WIDTH, width);
            jsonObject.put(HEIGHT, height);
            jsonObject.put(SIZE, size);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
