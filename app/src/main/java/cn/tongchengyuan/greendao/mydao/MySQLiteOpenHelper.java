package cn.tongchengyuan.greendao.mydao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import cn.tongchengyuan.greendao.dao.DaoMaster;
import cn.tongchengyuan.greendao.dao.DynamicBeanDao;
import cn.tongchengyuan.greendao.dao.UserBasicInfoDao;

import org.greenrobot.greendao.database.Database;

/**
 * Created by xiajun on 2017/11/1.
 */

public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, DynamicBeanDao.class, UserBasicInfoDao.class);
    }
}
