package com.juns.wechat.database.dao;

import java.util.List;

/**
 * Created by 王宗文 on 2016/7/14.
 */
public class DbDataEvent<T>{

    public static final int SAVE = 11;
    public static final int UPDATE = 21;
    public static final int REPLACE = 31;  //对于replace，其通知的数据不带有id

    public int action;

    public List<T> data;
}
