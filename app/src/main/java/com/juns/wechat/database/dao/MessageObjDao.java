package com.juns.wechat.database.dao;

import com.juns.wechat.bean.Flag;
import com.juns.wechat.chat.bean.MessageBean;
import com.juns.wechat.chat.bean.MessageObject;
import com.juns.wechat.config.MsgType;

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
}
