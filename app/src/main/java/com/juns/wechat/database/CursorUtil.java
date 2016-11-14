package com.juns.wechat.database;

import android.database.Cursor;

import com.juns.wechat.util.TimeUtil;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * Created by 王者 on 2016/8/7.
 */
public class CursorUtil {

    public static <T> T fromCursor(Cursor cursor, Class<T> clazz){
        Table table = clazz.getAnnotation(Table.class);
        if(table == null){
            throw new IllegalArgumentException("clazz type is not accepted!");
        }
        try {
            T t = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for(Field field : fields){
                field.setAccessible(true);
                Column column = field.getAnnotation(Column.class);
                if(column == null) continue;
                Object value = getCursorValue(cursor, field, column.name());
                field.set(t, value);
            }
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object getCursorValue(Cursor cursor, Field field, String columnName){
        Class<?> fieldType = field.getType();

        if(fieldType.equals(int.class) || field.equals(Integer.class)){
            return cursor.getInt(cursor.getColumnIndex(columnName));
        }else if(fieldType.equals(String.class)){
           return cursor.getString(cursor.getColumnIndex(columnName));
        }else if(fieldType.equals(long.class) || field.equals(Long.class)){
           return cursor.getLong(cursor.getColumnIndex(columnName));
        }else if(fieldType.equals(float.class) || field.equals(Float.class)){
           return cursor.getFloat(cursor.getColumnIndex(columnName));
        }else if(fieldType.equals(double.class) || field.equals(Double.class)){
           return cursor.getDouble(cursor.getColumnIndex(columnName));
        }else if(fieldType.equals(long.class) || field.equals(Long.class)){
           return cursor.getLong(cursor.getColumnIndex(columnName));
        }else if(fieldType.equals(Date.class)){
            long time  = cursor.getLong(cursor.getColumnIndex(columnName));
            Date date = new Date(time);
            return date;
        }
        return cursor.getString(cursor.getColumnIndex(columnName));
    }
}
