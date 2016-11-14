package com.juns.wechat.database;

import android.database.sqlite.SQLiteDatabase;

import com.juns.wechat.bean.FriendBean;

import org.xutils.ex.DbException;

/**
 * Created by 王宗文 on 2016/6/14.
 */
public class FriendTable {
    public static final String TABLE_NAME = "wcFriend";
    public static final String CREATE_INDEX = "create unique index index_rosters on wcFriend(ownerName, contactName)";
    public static final String DELETE_INDEX = "drop index index_rosters";


    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        if (oldVersion != newVersion){
            database.execSQL(DELETE_INDEX);
            try {
                DbUtil.getDbManager().dropTable(FriendBean.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }
    
}
