package com.juns.wechat.database.dao;

import com.juns.wechat.bean.Flag;
import com.juns.wechat.chat.bean.ConversationObject;
import com.juns.wechat.chat.bean.MessageObject;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Administrator on 2017/5/3.
 */

public class ConversationObjDao extends BaseObjDao<ConversationObject> {
    private static final ConversationObjDao INSTANCE = new ConversationObjDao();

    public static ConversationObjDao getInstance(){
        return INSTANCE;
    }

    private ConversationObjDao(){
    }

    public RealmResults<ConversationObject> getAllConversations(Realm realm){
        return realm.where(clazz).notEqualTo(MessageObject.FLAG, Flag.INVALID.value())
            .findAllSorted(ConversationObject.DATE, Sort.DESCENDING);
    }
}
