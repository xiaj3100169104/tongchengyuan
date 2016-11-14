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

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER_NAME = "userName";
    public static final String COLUMN_PASSWORD = "passWord";
    public static final String COLUMN_HEAD_URL = "headUrl";
    public static final String COLUMN_SIGNATURE = "signature";
    public static final String COLUMN_SEX = "sex";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_TYPE ="type";
    public static final String COLUMN_TELEPHONE = "telephone";
    public static final String COLUMN_CREATE_DATE = "createDate";
    public static final String COLUMN_MODIFY_DATE = "modifyDate";

    public static final String CREATE_INDEX = "create unique index index_wcUser on " + TABLE_NAME + "(" + COLUMN_USER_NAME + ")";
    public static final String DELETE_INDEX = "drop index index_wcUser";



    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "userName TEXT NOT NULL, passWord TEXT NOT NULL,"
            + "headUrl TEXT, signature TEXT, sex TEXT, location TEXT, birthday TEXT, type TEXT DEFAULT 'N', " +
            " createDate timestamp default current_timestamp, modifyDate long, telephone TEXT);";



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
