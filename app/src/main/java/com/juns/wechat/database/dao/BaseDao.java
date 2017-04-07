package com.juns.wechat.database.dao;

import android.database.Cursor;

import com.juns.wechat.bean.Flag;
import com.juns.wechat.database.DbUtil;
import com.juns.wechat.util.LogUtil;


import org.simple.eventbus.EventBus;
import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.ColumnEntity;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王宗文 on 2016/7/6.
 */
public abstract class BaseDao<T> implements IDao<T> {
    protected DbManager dbManager;
    private Class<T> clazz;
    private TableEntity<T> tableEntity;

    public BaseDao(){
        dbManager = DbUtil.getDbManager();
        clazz = getEntityClass();
        try {
            tableEntity = dbManager.getTable(clazz);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    @Override
    public T findById(Integer id) {
        try {
            return dbManager.findById(clazz, id);
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public T findByParams(WhereBuilder whereBuilder) {
        List<T> results = findAllByParams(whereBuilder);
        if(results != null  && results.size() > 0){
            return results.get(0);
        }
        return null;
    }

    @Override
    public List<T> findAllByParams(WhereBuilder whereBuilder) {
        try {
            Selector<T> selector = dbManager.selector(clazz);
            whereBuilder.and("flag", "!=", Flag.INVALID.value());
            selector.where(whereBuilder);
            return selector.findAll();
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void save(T t) {
        try {
            dbManager.saveBindingId(t);
            postDataChangedEvent(DbDataEvent.SAVE, t);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(List<T> list) {
        try {
            dbManager.saveBindingId(list);
            postDataChangedEvent(DbDataEvent.SAVE, list);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean replace(T t) {
        try {
            dbManager.replace(t);

            postDataChangedEvent(DbDataEvent.REPLACE, t);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean replace(List<T> list) {
        try {
            dbManager.replace(list);
            postDataChangedEvent(DbDataEvent.REPLACE, list);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(T t) {
        try {
            dbManager.saveOrUpdate(t);
            postDataChangedEvent(DbDataEvent.UPDATE, t);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 并不会执行真正的删除，只是将flag标记置为1，要求所有表有这个通用字段
     * @param id
     * @return
     */
    @Override
    public boolean delete(Integer id) {
        try {
            TableEntity<T> tableEntity = dbManager.getTable(clazz);
            ColumnEntity idColumn = tableEntity.getId();
            String idName = idColumn.getName();
            WhereBuilder whereBuilder = WhereBuilder.b();
            whereBuilder.and(idName, "=", id);
            KeyValue keyValue = new KeyValue("flag", Flag.INVALID.value());
            update(whereBuilder, keyValue);

        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(WhereBuilder whereBuilder, KeyValue... keyValuePairs) {
        List<T> list = findAllByParams(whereBuilder);
        try {
            int updatedRow = dbManager.update(clazz, whereBuilder, keyValuePairs);
            LogUtil.i("updatedRow: " + updatedRow);

            if(updatedRow >= 1){  //需要将更新后的数据查询出来
                WhereBuilder builder = WhereBuilder.b();
                ColumnEntity idColumn = tableEntity.getId();
                String idName = idColumn.getName();
                for(T t : list){
                    int id = (int) idColumn.getFieldValue(t);
                    builder.and(idName, "=", id);
                }

                list = findAllByParams(builder);
                postDataChangedEvent(DbDataEvent.UPDATE, list);
            }
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    public final void postDataChangedEvent(int action, T data){
        List<T> list = new ArrayList<>();
        list.add(data);
        postDataChangedEvent(action, list);
    }

    public final void postDataChangedEvent(int action, List<T> datas){
        DbDataEvent<T> dbDataEvent = new DbDataEvent();
        dbDataEvent.action = action;
        dbDataEvent.data = datas;
        String tag = getTableName();
        EventBus.getDefault().post(dbDataEvent, tag);
    }

    private String getTableName(){
        return tableEntity.getName();
    }

    public Class<T> getEntityClass() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> result = (Class<T>) (parameterizedType.getActualTypeArguments()[0]);// 0表示获得第一个泛型的具体类型
        return result;
    }

    protected void closeCursor(Cursor cursor){
        if(cursor != null){
            cursor.close();
        }
    }
}
