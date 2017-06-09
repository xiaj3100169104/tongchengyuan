package com.juns.wechat.helper;

import com.juns.wechat.bean.UserPropertyBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiajun on 2017/5/29.
 */

public class CacheDataHelper {
    static Map<Integer, List<UserPropertyBean>> userLabels = new HashMap<>();

    public static List<UserPropertyBean> getUserLabelCache(int userId) {
        if (userLabels.containsKey(userId))
            return userLabels.get(userId);
        return null;
    }

    public static void putUserLabelCache(int userId, List<UserPropertyBean> list) {
       userLabels.put(userId, list);
    }
}
