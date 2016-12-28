package com.juns.wechat.database;

import android.database.sqlite.SQLiteDatabase;

import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.UserBean;

import org.xutils.ex.DbException;

/**
 * Created by Administrator on 2016/6/20.
 */
public class UserTable {
    public static final String TABLE_NAME = "wcUser";


    public static final String CREATE_INDEX = "create unique index index_wcUser on " + TABLE_NAME + "(" + UserBean.USER_ID + ")";
    public static final String DELETE_INDEX = "drop index index_wcUser";


    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        if (oldVersion != newVersion){
            database.execSQL(DELETE_INDEX);
            try {
                DbUtil.getDbManager().dropTable(UserBean.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }
}
