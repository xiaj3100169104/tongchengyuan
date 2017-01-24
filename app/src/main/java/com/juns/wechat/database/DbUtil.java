package com.juns.wechat.database;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.chat.bean.MessageBean;
import com.juns.wechat.bean.UserBean;

import org.xutils.DbManager;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.sqlite.SqlInfoBuilder;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

/**
 * Created by 王宗文 on 2016/5/25
 */
public class DbUtil {
    private static final String DATABASE_NAME = "weixin.db";
    private static final int DATABASE_VERSION = 2;

    public static DbManager getDbManager() {
       return x.getDb(dbConfig);
    }

    private static DbManager.DaoConfig dbConfig = new DbManager.DaoConfig()
            .setDbName(DATABASE_NAME).setDbVersion(DATABASE_VERSION)
            .setDbOpenListener(new DbManager.DbOpenListener() {
                @Override
                public void onDbOpened(DbManager db) {
                   createTableIfNotExist(db);
                }
            })
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    SQLiteDatabase database = db.getDatabase();
                    FriendTable.onUpgrade(database, oldVersion, newVersion);
                    UserTable.onUpgrade(database, oldVersion, newVersion);
                    ChatTable.onUpgrade(database, oldVersion, newVersion);
                }
            });

    private static void createTableIfNotExist(DbManager db){
        try {
            createTableIfNotExist(db, db.getTable(UserBean.class));
            createTableIfNotExist(db, db.getTable(FriendBean.class));
            createTableIfNotExist(db, db.getTable(MessageBean.class));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private static void createTableIfNotExist(DbManager db, TableEntity<?> table) throws DbException{
        if (!table.tableIsExist()) {
            synchronized (table.getClass()) {
                if (!table.tableIsExist()) {
                    SqlInfo sqlInfo = SqlInfoBuilder.buildCreateTableSqlInfo(table);
                    db.execNonQuery(sqlInfo);
                    if(!TextUtils.isEmpty(table.getOnCreated())){
                        db.execNonQuery(table.getOnCreated());
                    }
                }
            }
        }
    }
}
