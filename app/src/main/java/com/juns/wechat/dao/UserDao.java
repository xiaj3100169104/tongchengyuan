package com.juns.wechat.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.database.CursorUtil;
import com.juns.wechat.database.DbUtil;
import com.juns.wechat.database.IdGenerator;
import com.juns.wechat.database.UserTable;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 王宗文 on 2016/6/20.
 */
public class UserDao extends BaseDao<UserBean>{
    private static final String GET_LAST_MODIFY_DATE =
            "SELECT max(t.modifyDate) as lastModifyDate FROM ( " +
                    "SELECT t1.* FROM (select u.* from wcUser u, wcFriend r where " +
                    "(r.ownerName = ? and u.userName = r.contactName) and (r.subType = 'both' or r.subType = 'from')) t1" +
                    " UNION SELECT u.* from wcUser u WHERE u.userName = ?) t";

    private static final String QUERY_MY_FRIENDS =
            "select * from wcUser u, wcFriend f where f.ownerName = ? and f.contactName = u.userName";

    private static UserDao mInstance;

    public static UserDao getInstance(){
        if(mInstance == null){
            mInstance = new UserDao();
        }
        return mInstance;
    }

    public List<UserBean> getMyFriends(String userName){
        SqlInfo sqlInfo = new SqlInfo(QUERY_MY_FRIENDS);
        sqlInfo.addBindArg(new KeyValue("key1", userName));
        List<UserBean> userBeen = new ArrayList<>();
        try {
            Cursor cursor = dbManager.execQuery(sqlInfo);
            while (cursor.moveToNext()){
                UserBean userBean = CursorUtil.fromCursor(cursor, UserBean.class);
                userBeen.add(userBean);
            }
            closeCursor(cursor);
            return userBeen;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserBean findByName(String userName){
        WhereBuilder whereBuilder = WhereBuilder.b(UserBean.USERNAME, "=", userName);
        return findByParams(whereBuilder);
    }

    public long getLastModifyDate(String userName){
        long lastModifyDate = 0;

        SqlInfo sqlInfo = new SqlInfo(GET_LAST_MODIFY_DATE);
        List<KeyValue> keyValues = new ArrayList<>();
        KeyValue keyValue1 = new KeyValue("key1", userName);
        KeyValue keyValue2 = new KeyValue("key2", userName);
        keyValues.add(keyValue1);
        keyValues.add(keyValue2);
        sqlInfo.addBindArgs(keyValues);

        try {
            Cursor cursor = dbManager.execQuery(sqlInfo);
            if(cursor != null && cursor.moveToNext()){
               lastModifyDate =  cursor.getLong(cursor.getColumnIndex("lastModifyDate"));
            }
            closeCursor(cursor);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return lastModifyDate;
    }

}
