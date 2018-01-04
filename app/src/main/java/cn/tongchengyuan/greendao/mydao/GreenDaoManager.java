package cn.tongchengyuan.greendao.mydao;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import cn.tongchengyuan.bean.UserBasicInfo;
import cn.tongchengyuan.bean.UserExtendInfo;
import cn.tongchengyuan.fragment.msg.MsgItem;
import cn.tongchengyuan.greendao.dao.DaoMaster;
import cn.tongchengyuan.greendao.dao.DaoSession;
import cn.tongchengyuan.greendao.dao.DynamicBeanDao;
import cn.tongchengyuan.greendao.dao.FriendBeanDao;
import cn.tongchengyuan.greendao.dao.MessageBeanDao;
import cn.tongchengyuan.greendao.dao.UserBasicInfoDao;
import cn.tongchengyuan.bean.DynamicBean;
import cn.tongchengyuan.bean.FriendBean;
import cn.tongchengyuan.bean.UserBean;
import cn.tongchengyuan.chat.bean.MessageBean;
import cn.tongchengyuan.greendao.dao.UserBeanDao;
import cn.tongchengyuan.greendao.dao.UserExtendInfoDao;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GreenDaoManager {
    private static final String TAG = "GreenDaoManager";
    public static final String DB_NAME = "green.db";
    public static final int DB_VERSION = 1;//降版本会报错

    private static GreenDaoManager mInstance;
    private Context mContext;
    private MySQLiteOpenHelper devOpenHelper;
    private DaoSession daoSession;
    private UserBeanDao userBeanDao;
    private UserBasicInfoDao userBasicDao;
    private UserExtendInfoDao userPropertyDao;
    private FriendBeanDao friendBeanDao;
    private DynamicBeanDao dynamicBeanDao;
    private MessageBeanDao messageBeanDao;

    //避免同时获取多个实例
    public synchronized static GreenDaoManager getInstance() {
        if (mInstance == null) {
            mInstance = new GreenDaoManager();
        }
        return mInstance;
    }

    public void initialize(Context context) {
        mContext = context;
        devOpenHelper = new MySQLiteOpenHelper(mContext, DB_NAME, null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        daoSession = daoMaster.newSession();
        userBeanDao = daoSession.getUserBeanDao();
        userBasicDao = daoSession.getUserBasicInfoDao();
        userPropertyDao = daoSession.getUserExtendInfoDao();
        friendBeanDao = daoSession.getFriendBeanDao();
        dynamicBeanDao = daoSession.getDynamicBeanDao();
        messageBeanDao = daoSession.getMessageBeanDao();

    }

    private Database getDatabase() {
        return daoSession.getDatabase();
    }

    /**
     * 关闭数据库，操作完成后必须调用
     */
    public void closeDB() {
        devOpenHelper.close();
    }

    public void clearGreenTable() {
        //greenBeanDao.deleteAll();
    }


    public void saveDynamic(DynamicBean o) {
        dynamicBeanDao.insertOrReplace(o);
    }


    public void saveDynamic(List<DynamicBean> list) {
        dynamicBeanDao.insertOrReplaceInTx(list);
    }

    public void update(DynamicBean o) {
        dynamicBeanDao.update(o);
    }

    public void delete(String userId) {

    }

    public List<DynamicBean> queryAll() {
        List<DynamicBean> list = dynamicBeanDao.loadAll();
        logList(list);

        return list;
    }

    public List<DynamicBean> queryAsc() {
        List<DynamicBean> list = dynamicBeanDao.queryBuilder()  // 查询 User
                //.where(Properties.FirstName.eq("Joe"))  // 首名为 Joe
                .orderAsc(DynamicBeanDao.Properties.CreateDate)  // 末名升序排列
                .list();  // 返回集合
        logList(list);
        return list;
    }

    private void logList(List<DynamicBean> list) {
        if (list != null && list.size() > 0) {
            for (DynamicBean b : list) {
                Log.e(TAG, b.toString());
            }
        }
    }

    public List<DynamicBean> queryByPage(int offset, int limit, String publisherId) {
        List<DynamicBean> list = dynamicBeanDao.queryBuilder()
                .where(DynamicBeanDao.Properties.PublisherId.eq(publisherId))
                .offset(offset).limit(limit)
                .orderDesc(DynamicBeanDao.Properties.CreateDate)  // 末名升序排列
                .list();  // 返回集合
        logList(list);

        return list;
    }

    public void save(UserBasicInfo u) {
        userBasicDao.insertOrReplace(u);
    }

    public UserBasicInfo queryUserBasic(String userId) {
        List<UserBasicInfo> list = userBasicDao.queryBuilder()
                .where(UserBasicInfoDao.Properties.UserId.eq(userId))
                .list();  // 返回集合
        //logList(list);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }

    public UserExtendInfo queryUserProperty(String userId) {
        List<UserExtendInfo> list = userPropertyDao.queryBuilder()
                .where(UserExtendInfoDao.Properties.UserId.eq(userId))
                .list();  // 返回集合
        //logList(list);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }

    public void save(UserExtendInfo u) {
        userPropertyDao.insertOrReplace(u);
    }
     /*public List<DynamicBean> queryWhereOr() {
        List<DynamicBean> list = greenBeanDao.queryBuilder()  // 查询 User
                //.whereOr(DynamicBeanDao.Properties.Name.eq("name1"), DynamicBeanDao.Properties.Phone.eq("phone2"))  // 首名为 Joe
                .orderDesc(DynamicBeanDao.Properties.Id)  // 末名升序排列
                .list();  // 返回集合
        logList(list);
        return list;
    }

    public List<DynamicBean> queryWhereBetween() {
        List<DynamicBean> list = greenBeanDao.queryBuilder()  // 查询 User
                .where(DynamicBeanDao.Properties.Id.between("0", "5"))  // 首名为 Joe
                .orderDesc(DynamicBeanDao.Properties.Id)  // 末名升序排列
                .list();  // 返回集合
        logList(list);
        return list;
    }*/

    private static final String GET_LAST_MODIFY_DATE =
            "SELECT max(f.MODIFY_DATE) as lastModifyDate from FRIEND_BEAN f where f.OWNER_ID = ? and " +
                    "(f.SUB_TYPE = 'both' or f.SUB_TYPE = 'from')";

    private static final String SELECT_NOT_EXIST_USER_IN_FRIEND =
            "select CONTACT_ID from FRIEND_BEAN where CONTACT_ID = ? and CONTACT_ID not in (select USER_ID from USER_BEAN)";

    public void saveFriends(List<FriendBean> data) {
        friendBeanDao.insertOrReplaceInTx(data);
    }

    public List<FriendBean> queryAllFriend() {
        List<FriendBean> list = friendBeanDao.loadAll();
        return list;
    }

    public List<FriendBean> getMyFriends(String ownerId) {
        String QUERY_MY_FRIENDS = "select * from FRIEND_BEAN f inner join USER_BEAN u where f.OWNER_ID = ? and " +
                "f.CONTACT_ID = U.USER_ID and (f.SUB_TYPE = 'both' or f.SUB_TYPE = 'from') and FLAG != -1";
        String[] selectionArgs = {ownerId};
        List<FriendBean> list = new ArrayList<>();
        Cursor cursor = getDatabase().rawQuery(QUERY_MY_FRIENDS, selectionArgs);
        while (cursor.moveToNext()) {
            FriendBean f = CursorUtil.fromCursor(cursor);
            f.nickName = cursor.getString(cursor.getColumnIndexOrThrow("NICK_NAME"));
            f.headUrl = cursor.getString(cursor.getColumnIndexOrThrow("HEAD_URL"));
            list.add(f);
        }
        closeCursor(cursor);
        return list;
    }

    public FriendBean findByOwnerAndContactName(String ownerId, String contactId) {
        List<FriendBean> list = friendBeanDao.queryBuilder()
                .where(FriendBeanDao.Properties.OwnerId.eq(ownerId))
                .where(FriendBeanDao.Properties.ContactId.eq(contactId))
                .list();  // 返回集合
        //logList(list);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }

    public long getLastModifyDate(String userId) {
        long lastModifyDate = 0;
        Cursor cursor = getDatabase().rawQuery(GET_LAST_MODIFY_DATE, new String[]{userId});
        if (cursor != null && cursor.moveToNext()) {
            lastModifyDate = cursor.getLong(cursor.getColumnIndex("lastModifyDate"));
        }
        closeCursor(cursor);
        return lastModifyDate;
    }

    public Integer[] getNotExistUsersInFriend(String ownerId) {
        List<Integer> userNames = new ArrayList<>();
        Cursor cursor = getDatabase().rawQuery(SELECT_NOT_EXIST_USER_IN_FRIEND, new String[]{ownerId});
        while (cursor.moveToNext()) {
            int contactedId = cursor.getInt((cursor.getColumnIndex("CONTACT_ID")));
            userNames.add(contactedId);
        }
        closeCursor(cursor);
        if (!userNames.isEmpty()) {
            Integer[] userNameArray = new Integer[1];
            return userNames.toArray(userNameArray);
        }
        return null;
    }

    protected void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }


    private static final String GET_LAST_MODIFY_DATE_USER =
            "SELECT max(t.MODIFY_DATE) as lastModifyDate FROM ( " +
                    "SELECT t1.* FROM (select u.* from USER_BEAN u, FRIEND_BEAN r where " +
                    "(r.OWNER_ID = ? and u.USER_ID = r.CONTACT_ID) and (r.SUB_TYPE = 'both' or r.SUB_TYPE = 'from')) t1" +
                    " UNION SELECT u.* from USER_BEAN u WHERE u.USER_ID = ?) t";

    public void save(UserBean u) {
        userBeanDao.insertOrReplace(u);
    }

    public void saveUserList(List<UserBean> data) {
        userBeanDao.insertOrReplaceInTx(data);
    }

    public UserBean findByUserId(String userId) {
        List<UserBean> list = userBeanDao.queryBuilder()
                .where(UserBeanDao.Properties.UserId.eq(userId))
                .list();  // 返回集合
        //logList(list);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }

    public List<UserBean> queryAllUser() {
        List<UserBean> list = userBeanDao.loadAll();
        return list;
    }

    public long getUserLastModifyDate(String userId) {
        long lastModifyDate = 0;

        String u = String.valueOf(userId);
        String[] s = {u, u};
        Cursor cursor = getDatabase().rawQuery(GET_LAST_MODIFY_DATE_USER, s);
        if (cursor != null && cursor.moveToNext()) {
            lastModifyDate = cursor.getLong(cursor.getColumnIndex("lastModifyDate"));
        }
        closeCursor(cursor);

        return lastModifyDate;
    }


    private static final String SELECT_MESSAGES_BY_PAGING =
            "select * from MESSAGE_BEAN where MY_USER_ID = ? and OTHER_USER_ID = ? and TYPE < ? and FLAG != -1 limit ? offset ?";
    private static final String COUNT_OF_TWO_USER =
            "select count(PACKET_ID) as count from MESSAGE_BEAN where MY_USER_ID = ? and OTHER_USER_ID = ? and TYPE < ? and FLAG != -1";

    public void save(MessageBean u) {
        messageBeanDao.insertOrReplace(u);
    }

    /**
     * 根据收到的服务器消息回执更新数据库已有消息状态
     *
     * @param packetId
     * @param state
     * @param date     客户端的时间可能不准确，以收到的服务器时间为准
     * @return
     */
    public void updateMessageState(String packetId, int state, Date date) {
        String sql = "UPDATE MESSAGE_BEAN SET STATE=?" + " WHERE PACKET_ID=?";
        if (date == null) {
            getDatabase().execSQL(sql, new String[]{String.valueOf(state)});
            return;
        }
        String sql2 = "UPDATE MESSAGE_BEAN SET STATE=?,DATE=?" + " WHERE PACKET_ID=?";
        Object[] s = {state, date.getTime()};
        getDatabase().execSQL(sql2, s);
    }

    public MessageBean findByPacketId(String packetId) {
        List<MessageBean> list = messageBeanDao.queryBuilder()
                .where(MessageBeanDao.Properties.PacketId.eq(packetId))
                .list();  // 返回集合
        //logList(list);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }

    /**
     * 获取每一个用户与这个用户聊天的最近的一条消息
     *
     * @param userId
     */
    public List<MsgItem> getLastMessageWithEveryFriend(String userId) {
        String sql = "select * from MESSAGE_BEAN where MY_USER_ID = ? and FLAG != -1 and TYPE < ? group by OTHER_USER_ID order by DATE";
        String[] s = {userId, String.valueOf(MessageBean.Type.SEND_INVITE)};
        List<MsgItem> msgItems = new ArrayList<>();
        Cursor cursor = getDatabase().rawQuery(sql, s);
        while (cursor.moveToNext()) {
            MsgItem msgItem = new MsgItem();
            MessageBean msg = CursorUtil.getMessageBean(cursor);
            FriendBean friendBean = findByOwnerAndContactName(userId, msg.getOtherUserId());
            UserBean userBean = findByUserId(msg.getOtherUserId());
            int unreadMsgCount = getUnreadMsgNum(msg.getMyUserId(), msg.getOtherUserId());
            msgItem.msg = msg;
            msgItem.friendBean = friendBean;
            msgItem.user = userBean;
            msgItem.unreadMsgCount = unreadMsgCount;
            msgItems.add(msgItem);
        }
        closeCursor(cursor);
        return msgItems;
    }

    public int getUnreadMsgNum(String myUserId, String otherUserId) {
        String sql = "select count(1) as count from MESSAGE_BEAN where MY_USER_ID = ? " +
                "and OTHER_USER_ID = ? and DIRECTION = ? and STATE = ? and TYPE < ?";
        String i = String.valueOf(MessageBean.Direction.INCOMING);
        String j = String.valueOf(MessageBean.State.NEW);
        String k = String.valueOf(MessageBean.Type.SEND_INVITE);
        String[] s = {myUserId, otherUserId, i, j, k};
        Cursor cursor = getDatabase().rawQuery(sql, s);
        if (cursor.moveToNext()) {
            return cursor.getInt(cursor.getColumnIndex("count"));
        }
        return 0;
    }

    /**
     * 获取用户未读消息总数
     *
     * @param myUserId
     * @return
     */
    public int getAllUnreadMsgNum(String myUserId) {
        int unreadNum = 0;
        String sql = "select count(1) as count from MESSAGE_BEAN where MY_USER_ID = ? " +
                "and DIRECTION = ? and STATE = ? and FLAG != ? and TYPE < ?";
        String v1 = String.valueOf(MessageBean.Direction.INCOMING);
        String v2 = String.valueOf(MessageBean.State.NEW);
        String v3 = String.valueOf(MessageBean.Flag.INVALID);
        String v4 = String.valueOf(MessageBean.Type.SEND_INVITE);

        String[] s = {String.valueOf(myUserId), v1, v2, v3, v4};
        Cursor cursor = getDatabase().rawQuery(sql, s);
        if (cursor.moveToNext()) {
            return cursor.getInt(cursor.getColumnIndex("count"));
        }
        return unreadNum;
    }


    /**
     * 获取收到的请求添加好友的消息
     *
     * @param myselfName
     * @return
     */
    public List<MessageBean> getMyReceivedInviteMessages(String myselfName) {
        /*WhereBuilder whereBuilder = WhereBuilder.b(MessageBean.MYSELF_NAME, "=", myselfName);
        whereBuilder.and(MessageBean.DIRECTION, "=", MessageBean.Direction.INCOMING.value);
        whereBuilder.and(MessageBean.TYPE, "=", MsgType.MSG_TYPE_SEND_INVITE);
        List<MessageBean> messageBeen = findAllByParams(whereBuilder);
        return messageBeen;*/
        return null;
    }

    public int getUnreadInviteMsgCount(String myselfName) {
       /* WhereBuilder whereBuilder = WhereBuilder.b(MessageBean.MYSELF_NAME, "=", myselfName);
        whereBuilder.and(MessageBean.DIRECTION, "=", MessageBean.Direction.INCOMING.value);
        whereBuilder.and(MessageBean.TYPE, "=", MsgType.MSG_TYPE_SEND_INVITE);
        whereBuilder.and(MessageBean.STATE, "=", MessageBean.State.NEW.value);
        List<MessageBean> messageBeen = findAllByParams(whereBuilder);
        if (messageBeen != null) {
            return messageBeen.size();
        }*/
        return 0;
    }

    /**
     * 分页查询两个用户之间的聊天记录
     *
     * @param myUserId
     * @param otherUserId
     * @param index
     * @param size
     * @return
     */
    public List<MessageBean> getMessagesByIndexAndSize(String myUserId, String otherUserId, int index, int size) {
        String v1 = myUserId;
        String v2 = otherUserId;
        String v3 = String.valueOf(MessageBean.Type.SEND_INVITE);
        String v4 = String.valueOf(size);
        String v5 = String.valueOf(index);

        String[] s = {v1, v2, v3, v4, v5};

        Cursor cursor = getDatabase().rawQuery(SELECT_MESSAGES_BY_PAGING, s);
        List<MessageBean> messageBeanList = new ArrayList<>();
        while (cursor.moveToNext()) {
            MessageBean messageBean = CursorUtil.getMessageBean(cursor);
            messageBeanList.add(messageBean);
        }
        closeCursor(cursor);
        return messageBeanList;
    }

    /**
     * 获取两个用户之间的聊天记录总数
     *
     * @param myUserId
     * @param otherUserId
     * @return
     */
    public int getMessageCount(String myUserId, String otherUserId) {
        int count = 0;
        String v1 = myUserId;
        String v2 = otherUserId;
        String v3 = String.valueOf(MessageBean.Type.SEND_INVITE);
        String[] s = {v1, v2, v3};
        Cursor cursor = getDatabase().rawQuery(COUNT_OF_TWO_USER, s);
        if (cursor.moveToNext()) {
            count = cursor.getInt(cursor.getColumnIndex("count"));
        }
        closeCursor(cursor);
        return count;
    }

    public void markAsRead(String myUserId, String otherUserId) {
        String sql = "update MESSAGE_BEAN set STATE = ? where MY_USER_ID = ? " +
                "and OTHER_USER_ID = ? and FLAG != ? and STATE = ? and DIRECTION = ?";
        Object[] s = {MessageBean.State.READ, myUserId, otherUserId, MessageBean.Flag.INVALID, MessageBean.State.NEW, MessageBean.Direction.INCOMING};
        getDatabase().execSQL(sql, s);
    }

    /**
     * 将某一用户的所有消息标为已读
     *
     * @param myUserId
     */
    public void markAsRead2All(String myUserId) {
        String sql = "update MESSAGE_BEAN set STATE = ? where MY_USER_ID = ? " +
                "and FLAG != ? and STATE = ? and DIRECTION = ?";
        Object[] s = {MessageBean.State.READ, myUserId, MessageBean.Flag.INVALID, MessageBean.State.NEW, MessageBean.Direction.INCOMING};
        getDatabase().execSQL(sql, s);
    }

    /**
     * 将用户的添加好友请求类型消息全部标为已读
     *
     * @param myselfName
     */
    public void markAsRead(String myselfName) {
       /* WhereBuilder whereBuilder = WhereBuilder.b();
        whereBuilder.and(MessageBean.MYSELF_NAME, "=", myselfName);
        whereBuilder.and(MessageBean.FLAG, "!=", Flag.INVALID.value());
        whereBuilder.and(MessageBean.STATE, "=", MessageBean.State.NEW.value);
        whereBuilder.and(MessageBean.DIRECTION, "=", MessageBean.Direction.INCOMING.value);
        whereBuilder.and(MessageBean.TYPE, "=", , Msg.MSG_TYPE_SEND_INVITE);

        KeyValue keyValue = new KeyValue(MessageBean.STATE, MessageBean.State.READ.value);
        update(whereBuilder, keyValue);*/
    }

    /**
     * 将用户正在发送中的消息标记为失败
     *
     * @param myselfName
     */
    public void markAsSendFailed(String myselfName) {
       /* WhereBuilder whereBuilder = WhereBuilder.b();
        whereBuilder.and(MessageBean.MYSELF_NAME, "=", myselfName);
        whereBuilder.and(MessageBean.DIRECTION, "=", MessageBean.Direction.OUTGOING.value);
        whereBuilder.and(MessageBean.FLAG, "!=", Flag.INVALID.value());
        whereBuilder.and(MessageBean.STATE, "=", MessageBean.State.NEW.value);

        KeyValue keyValue = new KeyValue(MessageBean.STATE, MessageBean.State.SEND_FAILED.value);
        update(whereBuilder, keyValue);*/
    }

    /**
     * 将某一用户发送的所有消息标为发送成功
     *
     * @param myUserId
     */
    public void markAsSendSucceed(String myUserId) {
        String sql = "update MESSAGE_BEAN set STATE = ? where MY_USER_ID = ? " +
                "and DIRECTION = ?";
        Object[] s = {MessageBean.State.SEND_SUCCESS, myUserId, MessageBean.Direction.OUTGOING};
        getDatabase().execSQL(sql, s);
    }

    public void delete(MessageBean t) {
        messageBeanDao.delete(t);
    }
}
