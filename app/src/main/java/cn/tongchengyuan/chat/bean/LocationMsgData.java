package cn.tongchengyuan.chat.bean;


import org.json.JSONException;
import org.json.JSONObject;

public class LocationMsgData extends MsgData {
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String ADDRESS = "address";

    public double latitude;
    public double longitude;
    public String address;

    @Override
    public JSONObject toSendJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(LATITUDE, latitude);
            jsonObject.put(LONGITUDE, longitude);
            jsonObject.put(ADDRESS, address);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
