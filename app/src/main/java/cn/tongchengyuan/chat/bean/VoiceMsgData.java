package cn.tongchengyuan.chat.bean;


import org.json.JSONException;
import org.json.JSONObject;

/*******************************************************
 * Created by 王者 on 2015/11/24
 *******************************************************/
public class VoiceMsgData extends MsgData {
    public static final String SECONDS = "seconds";
    public static final String FILE_NAME = "fileName";
    public static final String ENCODE_STR = "encodeStr";

    public int seconds;
    public String fileName;
    public String encodeStr; //编码字符串
    public int playState; //0默认，未播放;1,已经播放过

    @Override
    public JSONObject toSendJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SECONDS, seconds);
            jsonObject.put(FILE_NAME, fileName);
            jsonObject.put(ENCODE_STR, encodeStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
