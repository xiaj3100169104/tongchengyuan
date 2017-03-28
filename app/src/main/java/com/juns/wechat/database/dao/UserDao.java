package com.juns.wechat.database.dao;

import android.database.Cursor;

import com.juns.wechat.bean.UserBean;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王宗文 on 2016/6/20.
 */
public class UserDao extends BaseDao<UserBean> {
    private static final String GET_LAST_MODIFY_DATE =
            "SELECT max(t.modifyDate) as lastModifyDate FROM ( " +
                    "SELECT t1.* FROM (select u.* from wcUser u, wcFriend r where " +
                    "(r.ownerId = ? and u.userId = r.contactId) and (r.subType = 'both' or r.subType = 'from')) t1" +
                    " UNION SELECT u.* from wcUser u WHERE u.userId = ?) t";

    private static final String QUERY_MY_FRIENDS =
            "select * from wcUser u, wcFriend f where f.ownerId = ? and f.contactId = u.userId";

    private static UserDao mInstance;

    public static UserDao getInstance() {
        if (mInstance == null) {
            mInstance = new UserDao();
        }
        return mInstance;
    }

    public UserBean findByName(String userName) {
        WhereBuilder whereBuilder = WhereBuilder.b(UserBean.USERNAME, "=", userName);
        return findByParams(whereBuilder);
    }

    public UserBean findByUserId(int userId) {
        WhereBuilder whereBuilder = WhereBuilder.b(UserBean.USER_ID, "=", userId);
        return findByParams(whereBuilder);
    }

    public long getLastModifyDate(int userId) {
        long lastModifyDate = 0;

        SqlInfo sqlInfo = new SqlInfo(GET_LAST_MODIFY_DATE);
        List<KeyValue> keyValues = new ArrayList<>();
        KeyValue keyValue1 = new KeyValue("key1", userId);
        KeyValue keyValue2 = new KeyValue("key2", userId);
        keyValues.add(keyValue1);
        keyValues.add(keyValue2);
        sqlInfo.addBindArgs(keyValues);

        try {
            Cursor cursor = dbManager.execQuery(sqlInfo);
            if (cursor != null && cursor.moveToNext()) {
                lastModifyDate = cursor.getLong(cursor.getColumnIndex("lastModifyDate"));
            }
            closeCursor(cursor);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return lastModifyDate;
    }

}
