package cn.tongchengyuan.bean;


import java.io.Serializable;

/**
 * Created by xiajun on 2017/5/25.
 */
public class UserPropertyBean implements Serializable {

    public static final String KEY_COMPANY_INFO = "company_info";
    public static final String KEY_MY_LABEL = "myLabel";
    public static final String KEY_INTEREST_SPORT = "interest_sport";
    public static final String KEY_INTEREST_MUSIC = "interest_music";
    public static final String KEY_INTEREST_FOOD = "interest_food";
    public static final String KEY_INTEREST_MOVIE = "interest_movie";


    public String key;
    public String value;

    public UserPropertyBean() {
    }

    public UserPropertyBean(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
