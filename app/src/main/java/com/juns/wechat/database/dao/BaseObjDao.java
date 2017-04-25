package com.juns.wechat.database.dao;

import java.lang.reflect.ParameterizedType;

import io.realm.Realm;
import io.realm.RealmModel;

/**
 * Created by Administrator on 2017/4/25.
 */

public class BaseObjDao<T extends RealmModel> implements IObjDao<T> {
    protected Class<T> clazz;

    protected BaseObjDao(){
        this.clazz = getEntityClass();
    }

    @Override
    public T findById(Realm realm, Integer id) {
        return null;
    }

    @Override
    public void save(Realm realm, final T t) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insert(t);
            }
        });
    }

    public Class<T> getEntityClass() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> result = (Class<T>) (parameterizedType.getActualTypeArguments()[0]);// 0表示获得第一个泛型的具体类型
        return result;
    }
}
