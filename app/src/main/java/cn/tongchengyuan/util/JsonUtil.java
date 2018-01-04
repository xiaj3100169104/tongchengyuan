package cn.tongchengyuan.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Created by 王者 on 2016/8/3.
 */
public class JsonUtil {
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static <T> T fromJson(String json, Class<T> clazz){
        return gson.fromJson(json, clazz);
    }

    public static <T> T fromJson(String json, Type type){
        return gson.fromJson(json, type);
    }
}
