package com.juns.wechat.database.dao;

import android.database.Cursor;

import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.database.CursorUtil;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王宗文 on 2016/6/20.
 */
public class FriendDao extends BaseDao<FriendBean> {
    private static final String GET_LAST_MODIFY_DATE =
            "SELECT max(f.modifyDate) as lastModifyDate from wcFriend f where f.ownerId = ? and " +
                    "(f.subType = 'both' or f.subType = 'from')";

    private static final String SELECT_NOT_EXIST_USER_IN_FRIEND =
            "select contactId from wcFriend where ownerId = ? and contactId not in (select userId from wcUser)";

    private static final String QUERY_MY_FRIENDS =
            "select * from wcFriend f where ownerId = ? and " +
                    "(f.subType = 'both' or f.subType = 'from') and flag != -1";

    private static FriendDao mInstance;

    public static FriendDao getInstance() {
        if (mInstance == null) {
            mInstance = new FriendDao();
        }
        return mInstance;
    }

    public List<FriendBean> getMyFriends(int ownerId) {
        SqlInfo sqlInfo = new SqlInfo(QUERY_MY_FRIENDS);
        sqlInfo.addBindArg(new KeyValue("key1", ownerId));
        List<FriendBean> friendBeen = new ArrayList<>();
        try {
            Cursor cursor = dbManager.execQuery(sqlInfo);
            while (cursor.moveToNext()) {
                FriendBean friendBean = CursorUtil.fromCursor(cursor, FriendBean.class);
                UserBean userBean = UserDao.getInstance().findByUserId(friendBean.getContactId());
                if (userBean != null) {
                    friendBean.setContactUser(userBean);
                    friendBeen.add(friendBean);
                }
            }
            closeCursor(cursor);
            return friendBeen;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public FriendBean findByOwnerAndContactName(int ownerId, int contactId) {
        WhereBuilder whereBuilder = WhereBuilder.b(FriendBean.OWNER_ID, "=", ownerId);
        whereBuilder.and(FriendBean.CONTACT_ID, "=", contactId);
        return findByParams(whereBuilder);
    }

    public FriendBean findByOwnerAndContactName(int ownerId, String contactName) {
        UserBean userBean = UserDao.getInstance().findByName(contactName);
        return findByOwnerAndContactName(ownerId, userBean.getUserId());
    }

    public long getLastModifyDate(int userId) {
        long lastModifyDate = 0;

        SqlInfo sqlInfo = new SqlInfo(GET_LAST_MODIFY_DATE);
        List<KeyValue> keyValues = new ArrayList<>();
        KeyValue keyValue1 = new KeyValue("key1", userId);
        keyValues.add(keyValue1);
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

    public Integer[] getNotExistUsersInFriend(int ownerId) {
        SqlInfo sqlInfo = new SqlInfo(SELECT_NOT_EXIST_USER_IN_FRIEND);
        List<KeyValue> keyValues = new ArrayList<>();
        KeyValue keyValue1 = new KeyValue("key1", ownerId);
        keyValues.add(keyValue1);
        sqlInfo.addBindArgs(keyValues);

        List<Integer> userNames = new ArrayList<>();
        try {
            Cursor cursor = dbManager.execQuery(sqlInfo);
            while (cursor.moveToNext()) {
                int contactedId = cursor.getInt((cursor.getColumnIndex(FriendBean.CONTACT_ID)));
                userNames.add(contactedId);
            }
            closeCursor(cursor);
            if (!userNames.isEmpty()) {
                Integer[] userNameArray = new Integer[1];
                return userNames.toArray(userNameArray);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        return null;
    }
}
