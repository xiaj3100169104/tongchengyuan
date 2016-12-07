package com.style.rxAndroid.newwork.core;

import android.text.TextUtils;

import com.style.utils.ReflectUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by xiajun on 2016/1/15.
 */
public class Params {
    private Map<String, Object> params;
    private File[] files;

    public Params() {
        params = new HashMap<>();
    }

    public Params(Object obj) {
        params = new HashMap<>();
        obj2Map(obj);
    }

    public void putObj(Object obj) {
        obj2Map(obj);
    }

    private void obj2Map(Object obj) {
        Map<String, Object> map = ReflectUtil.Object2Map(obj);
        params.putAll(map);
    }

    public void put(String key, Object obj) {
        params.put(key, obj);
    }

    public void putPaths(String[] paths) {
        if (paths != null) {
            int length = paths.length;
            files = new File[length];
            for (int i = 0; i < length; i++) {
                if (!TextUtils.isEmpty(paths[i])) {
                    files[i] = new File(paths[i]);
                }
            }
        }
    }

    public File[] getFiles() {
        return files;
    }

    public void putFiles(File[] file) {
        files = file;
    }

    public Map<String, Object> getParams() {
        return params;
    }
}
