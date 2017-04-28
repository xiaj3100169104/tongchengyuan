package com.juns.wechat.database.dao;

import com.juns.wechat.bean.Flag;
import com.juns.wechat.chat.bean.MessageBean;
import com.juns.wechat.chat.bean.MessageObject;
import com.juns.wechat.config.MsgType;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Administrator on 2017/4/25.
 */

public class MessageObjDao extends BaseObjDao<MessageObject> {
    private static final MessageObjDao INSTANCE = new MessageObjDao();

    public static MessageObjDao getInstance(){
        return INSTANCE;
    }

    private MessageObjDao(){
    }

    public RealmResults<MessageObject> getMessagesBetweenTwoUsers(Realm realm, String myselfName, String otherName){
        return realm.where(clazz).equalTo(MessageObject.MYSELF_NAME, myselfName).equalTo(MessageObject.OTHER_NAME, otherName)
                .lessThan(MessageObject.TYPE, MsgType.MSG_TYPE_SEND_INVITE)
                .notEqualTo(MessageObject.FLAG, Flag.INVALID.value())
                .findAllSorted(MessageObject.ID, Sort.ASCENDING);
    }

    public void markAsRead(Realm realm, final String myselfName, final String otherName){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<MessageObject> realmResults =
                        realm.where(clazz).equalTo(MessageObject.MYSELF_NAME, myselfName).equalTo(MessageObject.OTHER_NAME, otherName)
                        .notEqualTo(MessageObject.FLAG, Flag.INVALID.value())
                        .equalTo(MessageObject.STATE, MessageObject.State.NEW.value)
                        .findAll();
                for(MessageObject messageObject : realmResults){
                    //messageObject.setState(MessageObject.State.READ.value);
                    messageObject.setFlag(Flag.INVALID.value());
                }
            }
        });
    }
}
