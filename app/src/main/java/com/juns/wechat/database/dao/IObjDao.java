package com.juns.wechat.database.dao;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmModel;

/**
 * Created by 王宗文 on 2016/7/6.
 */
public interface IObjDao<T extends RealmModel> {
    T findById(Realm realm, Integer id);
    void save(Realm realm, T t);
}
