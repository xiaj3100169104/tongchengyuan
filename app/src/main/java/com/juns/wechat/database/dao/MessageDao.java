package com.juns.wechat.database.dao;

import android.database.Cursor;

import com.juns.wechat.App;
import com.juns.wechat.bean.Flag;
import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.chat.bean.MessageBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.chat.bean.Msg;
import com.juns.wechat.fragment.msg.MsgItem;
import com.juns.wechat.config.MsgType;
import com.juns.wechat.database.CursorUtil;
import com.juns.wechat.manager.AccountManager;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 王宗文 on 2016/6/20.
 */
public class MessageDao extends BaseDao<MessageBean>{

    private static MessageDao mInstance;

    private static final String SELECT_MESSAGES_BY_PAGING =
            "select * from wcMessage where myselfName = ? and otherName = ? and type < ? and flag != -1 limit ? offset ?";
    private static final String SELECT_MESSAGE_COUNT_BETWEEN_TWO_USER =
            "select count(id) as count from wcMessage where myselfName = ? and otherName = ? and type < ? and flag != -1";

    public static MessageDao getInstance(){
        if(mInstance == null){
            mInstance = new MessageDao();
        }
        return mInstance;
    }

    /**
     * 根据收到的服务器消息回执更新数据库已有消息状态
     * @param packetId
     * @param state
     * @param date 客户端的时间可能不准确，以收到的服务器时间为准
     * @return
     */
    public boolean updateMessageState(String packetId, int state, Date date){
        WhereBuilder whereBuilder = WhereBuilder.b(MessageBean.PACKET_ID, "=", packetId);
        KeyValue keyValue = new KeyValue(MessageBean.STATE, state);
        if(date == null){
            return update(whereBuilder, keyValue);
        }
        KeyValue keyValue2 = new KeyValue(MessageBean.DATE, date);
        return update(whereBuilder, keyValue, keyValue2);
    }

    public MessageBean findByPacketId(String myselfName, String packetId){
        WhereBuilder whereBuilder = WhereBuilder.b(MessageBean.PACKET_ID, "=", packetId);
        whereBuilder.and(MessageBean.MYSELF_NAME, "=", myselfName);
        return findByParams(whereBuilder);
    }

    /**
     * 获取每一个用户与这个用户聊天的最近的一条消息
     * @param userName
     */
    public List<MsgItem> getLastMessageWithEveryFriend(Integer userId, String userName){
        SqlInfo sqlInfo = new SqlInfo("select * from wcMessage where myselfName = ? and flag != -1 and type < ? group by otherName order by date");
        sqlInfo.addBindArg(new KeyValue(UserBean.USERNAME, userName));
        sqlInfo.addBindArg(new KeyValue("key2", MsgType.MSG_TYPE_SEND_INVITE));
        try {
            List<MsgItem> msgItems = new ArrayList<>();
            Cursor cursor = dbManager.execQuery(sqlInfo);
            while (cursor.moveToNext()){
                MsgItem msgItem = new MsgItem();
                MessageBean msg = CursorUtil.fromCursor(cursor, MessageBean.class);
                FriendBean friendBean = FriendDao.getInstance().findByOwnerAndContactName(userId, msg.getOtherName());
                UserBean userBean = UserDao.getInstance().findByName(msg.getOtherName());
                int unreadMsgCount = MessageDao.getInstance().getUnreadMsgNum(msg.getMyselfName(), msg.getOtherName());
                msgItem.msg = msg;
                msgItem.friendBean = friendBean;
                msgItem.user = userBean;
                msgItem.unreadMsgCount = unreadMsgCount;
                msgItems.add(msgItem);
            }
            closeCursor(cursor);
            return msgItems;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getUnreadMsgNum(String myselfName, String otherName){
        SqlInfo sqlInfo = new SqlInfo("select count(1) as count from wcMessage where myselfName = ? " +
                "and otherName = ? and direction = ? and state = ? and type < ?");
        sqlInfo.addBindArg(new KeyValue("key1", myselfName));
        sqlInfo.addBindArg(new KeyValue("key2", otherName));
        sqlInfo.addBindArg((new KeyValue("key3", MessageBean.Direction.INCOMING.value)));
        sqlInfo.addBindArg(new KeyValue("key4", MessageBean.State.NEW.value));
        sqlInfo.addBindArg(new KeyValue("key5", MsgType.MSG_TYPE_SEND_INVITE));
        try {
            Cursor cursor = dbManager.execQuery(sqlInfo);
            if(cursor.moveToNext()){
                return cursor.getInt(cursor.getColumnIndex("count"));
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取用户未读消息总数
     * @param myselfName
     * @return
     */
    public int getAllUnreadMsgNum(String myselfName){
        int unreadNum = 0;

        WhereBuilder whereBuilder = WhereBuilder.b(MessageBean.MYSELF_NAME, "=", myselfName);
        whereBuilder.and(MessageBean.DIRECTION, "=", MessageBean.Direction.INCOMING.value);
        whereBuilder.and(MessageBean.STATE, "=", MessageBean.State.NEW.value);
        whereBuilder.and(MessageBean.FLAG, "!=", Flag.INVALID.value());
        whereBuilder.and(MessageBean.TYPE, "<", MsgType.MSG_TYPE_SEND_INVITE);
        List<MessageBean> results = findAllByParams(whereBuilder);
        if(results != null) {
            unreadNum = results.size();
        }

        return unreadNum;
    }


    /**
     * 获取收到的请求添加好友的消息
     * @param myselfName
     * @return
     */
    public List<MessageBean> getMyReceivedInviteMessages(String myselfName){
        WhereBuilder whereBuilder = WhereBuilder.b(MessageBean.MYSELF_NAME, "=", myselfName);
        whereBuilder.and(MessageBean.DIRECTION, "=", MessageBean.Direction.INCOMING.value);
        whereBuilder.and(MessageBean.TYPE, "=", MsgType.MSG_TYPE_SEND_INVITE);
        List<MessageBean> messageBeen =  findAllByParams(whereBuilder);
        return messageBeen;
    }

    public int getUnreadInviteMsgCount(String myselfName){
        WhereBuilder whereBuilder = WhereBuilder.b(MessageBean.MYSELF_NAME, "=", myselfName);
        whereBuilder.and(MessageBean.DIRECTION, "=", MessageBean.Direction.INCOMING.value);
        whereBuilder.and(MessageBean.TYPE, "=", MsgType.MSG_TYPE_SEND_INVITE);
        whereBuilder.and(MessageBean.STATE, "=", MessageBean.State.NEW.value);
        List<MessageBean> messageBeen =  findAllByParams(whereBuilder);
        if(messageBeen != null){
            return messageBeen.size();
        }
        return 0;
    }

    /**
     * 分页查询两个用户之间的聊天记录
     * @param myselfName
     * @param otherName
     * @param index
     * @param size
     * @return
     */
    public List<MessageBean> getMessagesByIndexAndSize(String myselfName, String otherName, int index, int size){
        SqlInfo sqlInfo = new SqlInfo(SELECT_MESSAGES_BY_PAGING);
        sqlInfo.addBindArg(new KeyValue("key1", myselfName));
        sqlInfo.addBindArg(new KeyValue("key2", otherName));
        sqlInfo.addBindArg(new KeyValue("key3", MsgType.MSG_TYPE_SEND_INVITE));
        sqlInfo.addBindArg(new KeyValue("key4", size));
        sqlInfo.addBindArg(new KeyValue("key5", index));

        try {
            Cursor cursor = dbManager.execQuery(sqlInfo);
            List<MessageBean> messageBeanList = new ArrayList<>();
            while (cursor.moveToNext()){
                MessageBean messageBean = CursorUtil.fromCursor(cursor, MessageBean.class);
                messageBeanList.add(messageBean);
            }
            closeCursor(cursor);
            return messageBeanList;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取两个用户之间的聊天记录总数
     * @param myselfName
     * @param otherName
     * @return
     */
    public int getMessageCount(String myselfName, String otherName){
        SqlInfo sqlInfo = new SqlInfo(SELECT_MESSAGE_COUNT_BETWEEN_TWO_USER);
        sqlInfo.addBindArg(new KeyValue("key1", myselfName));
        sqlInfo.addBindArg(new KeyValue("key2", otherName));
        sqlInfo.addBindArg(new KeyValue("key3", MsgType.MSG_TYPE_SEND_INVITE));
        int count = 0;
        try {
            Cursor cursor = dbManager.execQuery(sqlInfo);
            if(cursor.moveToNext()){
                count = cursor.getInt(cursor.getColumnIndex("count"));
            }
            closeCursor(cursor);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return count;
    }

    public void markAsRead(String myselfName, String otherName){
        WhereBuilder whereBuilder = WhereBuilder.b();
        whereBuilder.and(MessageBean.MYSELF_NAME, "=", myselfName);
        whereBuilder.and(MessageBean.OTHER_NAME, "=", otherName);
        whereBuilder.and(MessageBean.FLAG, "!=", Flag.INVALID.value());
        whereBuilder.and(MessageBean.STATE, "=", MessageBean.State.NEW.value);
        whereBuilder.and(MessageBean.DIRECTION, "=", MessageBean.Direction.INCOMING.value);

        KeyValue keyValue = new KeyValue(MessageBean.STATE, MessageBean.State.READ.value);
        update(whereBuilder, keyValue);
    }

    /**
     * 将用户的某一类型消息全部标为已读
     * @param myselfName
     * @param type
     */
    public void markAsRead(String myselfName, int type){
        WhereBuilder whereBuilder = WhereBuilder.b();
        whereBuilder.and(MessageBean.MYSELF_NAME, "=", myselfName);
        whereBuilder.and(MessageBean.FLAG, "!=", Flag.INVALID.value());
        whereBuilder.and(MessageBean.STATE, "=", MessageBean.State.NEW.value);
        whereBuilder.and(MessageBean.DIRECTION, "=", MessageBean.Direction.INCOMING.value);
        whereBuilder.and(MessageBean.TYPE, "=", type);

        KeyValue keyValue = new KeyValue(MessageBean.STATE, MessageBean.State.READ.value);
        update(whereBuilder, keyValue);
    }

    /**
     * 将用户正在发送中的消息标记为失败
     * @param myselfName
     */
    public void markAsSendFailed(String myselfName){
        WhereBuilder whereBuilder = WhereBuilder.b();
        whereBuilder.and(MessageBean.MYSELF_NAME, "=", myselfName);
        whereBuilder.and(MessageBean.DIRECTION, "=", MessageBean.Direction.OUTGOING.value);
        whereBuilder.and(MessageBean.FLAG, "!=", Flag.INVALID.value());
        whereBuilder.and(MessageBean.STATE, "=", MessageBean.State.NEW.value);

        KeyValue keyValue = new KeyValue(MessageBean.STATE, MessageBean.State.SEND_FAILED.value);
        update(whereBuilder, keyValue);
    }

    public boolean delete(MessageBean t) {
        try {
            dbManager.saveOrUpdate(t);
            postDataChangedEvent(DbDataEvent.UPDATE, t);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }
}
