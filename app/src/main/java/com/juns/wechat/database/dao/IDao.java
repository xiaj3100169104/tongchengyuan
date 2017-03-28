package com.juns.wechat.database.dao;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;

import java.util.List;
import java.util.Map;

/**
 * Created by 王宗文 on 2016/7/6.
 */
public interface IDao<T> {
    T findById(Integer id);
    T findByParams(WhereBuilder whereBuilder);
    List<T> findAllByParams(WhereBuilder whereBuilder);
    void save(T t);
    void save(List<T> list);
    boolean replace(T t);
    boolean replace(List<T> list);
    boolean update(T t);
    boolean delete(Integer id);
    boolean update(WhereBuilder whereBuilder, KeyValue... keyValuePairs);
}
