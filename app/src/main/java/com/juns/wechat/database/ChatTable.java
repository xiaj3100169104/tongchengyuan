package com.juns.wechat.database;

import android.database.sqlite.SQLiteDatabase;

import com.juns.wechat.chat.bean.MessageBean;

import org.xutils.ex.DbException;

/*******************************************************
 * Created by 王宗文 on 2015/11/24
 *******************************************************/
public class ChatTable {
    public static final String TABLE_NAME = "wcMessage";

    public static final String CREATE_INDEX = "create index chat_index on " + TABLE_NAME
            + " (" + MessageBean.PACKET_ID + ");";

    public static final String DELETE_INDEX = "drop index chat_index";

    public enum State{
        NEW(0), SENDING(1), SEND_SUCCESS(2), SEND_FAILED(3), READ(4);

        private int state;
        State(int state){
            this.state = state;
        }

        public int value(){
            return state;
        }
    }


    public static void onUpgrade(SQLiteDatabase db, int oldVersion,
                                 int newVersion) {
        if (oldVersion != newVersion){
            db.execSQL(DELETE_INDEX);
            try {
                DbUtil.getDbManager().dropTable(MessageBean.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }
}
