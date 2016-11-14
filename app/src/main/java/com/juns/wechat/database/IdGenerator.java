package com.juns.wechat.database;

import com.juns.wechat.App;
import com.juns.wechat.util.SharedPreferencesUtil;

/**
 * Created by 王者 on 2016/8/8.
 */
public class IdGenerator {

    public synchronized static int nextId(String table){
        int existedValue = getIdValue(table);
        int nextId = existedValue + 1;
        saveIdValue(table, nextId);
        return nextId;
    }

    private  static int getIdValue(String table){
        int value =  SharedPreferencesUtil.getIntValue(App.getInstance(), table);
        return value;
    }

    private static void saveIdValue(String table, int value){
        SharedPreferencesUtil.putIntValue(App.getInstance(), table, value);
    }
}
