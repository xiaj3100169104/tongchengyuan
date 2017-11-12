package com.juns.wechat.database;

import android.database.Cursor;
import android.text.TextUtils;

import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.chat.bean.MessageBean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 王者 on 2016/8/7.
 */
public class CursorUtil {

    public static FriendBean fromCursor(Cursor cursor) {
        FriendBean f = null;
        while (cursor.moveToNext()) {
            f = new FriendBean();
            f.id = cursor.getString(cursor.getColumnIndexOrThrow("ID"));
            f.ownerId = cursor.getString(cursor.getColumnIndexOrThrow("OWNER_ID"));
            f.contactId = cursor.getString(cursor.getColumnIndexOrThrow("CONTACT_ID"));
            f.subType = cursor.getString(cursor.getColumnIndexOrThrow("SUB_TYPE"));
            f.remark = cursor.getString(cursor.getColumnIndexOrThrow("REMARK"));
            f.flag = cursor.getInt(cursor.getColumnIndexOrThrow("FLAG"));
            f.modifyDate = cursor.getLong(cursor.getColumnIndexOrThrow("MODIFY_DATE"));
        }
        return f;
    }

    public static MessageBean getMessageBean(Cursor cursor) {
        MessageBean f = null;
        while (cursor.moveToNext()) {
            f = new MessageBean();
            f.packetId = cursor.getString(cursor.getColumnIndexOrThrow("PACKET_ID"));
            f.myUserId = cursor.getString(cursor.getColumnIndexOrThrow("MY_USER_ID"));
            f.otherUserId = cursor.getString(cursor.getColumnIndexOrThrow("OTHER_USER_ID"));
            f.msg = cursor.getString(cursor.getColumnIndexOrThrow("MSG"));
            f.type = cursor.getInt(cursor.getColumnIndexOrThrow("TYPE"));
            f.date = new Date(cursor.getLong(cursor.getColumnIndexOrThrow("DATE")));
            f.direction = cursor.getInt(cursor.getColumnIndexOrThrow("DIRECTION"));
            f.state = cursor.getInt(cursor.getColumnIndexOrThrow("STATE"));
            f.flag = cursor.getInt(cursor.getColumnIndexOrThrow("FLAG"));

        }
        return f;
    }
}
