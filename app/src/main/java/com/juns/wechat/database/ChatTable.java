package com.juns.wechat.database;

import android.database.sqlite.SQLiteDatabase;

import com.juns.wechat.chat.bean.MessageBean;

import org.xutils.ex.DbException;

/*******************************************************
 * Created by 王宗文 on 2015/11/24
 *******************************************************/
public class ChatTable {
    public static final String TABLE_NAME = "wcMessage";

    // boolean mappings
    public static final int INCOMING = 0;
    public static final int OUTGOING = 1;//自己的消息
    public static final int STATE_NEW = 0; // <
    public static final int STATE_SEND_SUCC = 1; // < 消息已发送
    public static final int STATE_SEND_FAIL = 2; //消息发送失败
    public static final int STATE_READ = 3; // < 消息已读
    public static final int MSG_VALID   = 1;
    public static final int MSG_INVALID = 0;

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
