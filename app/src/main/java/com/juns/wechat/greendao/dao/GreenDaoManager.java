package com.juns.wechat.greendao.dao;

import android.content.Context;
import android.util.Log;

import com.juns.wechat.bean.DynamicBean;
import com.juns.wechat.bean.UserBasicInfo;
import com.juns.wechat.bean.UserExtendInfo;
import com.juns.wechat.bean.UserPropertyBean;

import java.util.List;

public class GreenDaoManager {
    private static final String TAG = "GreenDaoManager";
    public static final String DB_NAME = "green.db";
    public static final int DB_VERSION = 1;//降版本会报错

    private static GreenDaoManager mInstance;
    private Context mContext;
    private MySQLiteOpenHelper devOpenHelper;
    private DaoSession daoSession;
    private DynamicBeanDao greenBeanDao;
    private UserBasicInfoDao userBasicDao;
    private UserExtendInfoDao userPropertyDao;

    //避免同时获取多个实例
    public synchronized static GreenDaoManager getInstance() {
        if (mInstance == null) {
            mInstance = new GreenDaoManager();
        }
        return mInstance;
    }

    public void initialize(Context context) {
        mContext = context;
        devOpenHelper = new MySQLiteOpenHelper(mContext, DB_NAME, null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        daoSession = daoMaster.newSession();
        greenBeanDao = daoSession.getDynamicBeanDao();
        userBasicDao = daoSession.getUserBasicInfoDao();
        userPropertyDao = daoSession.getUserExtendInfoDao();
    }

    /**
     * 关闭数据库，操作完成后必须调用
     */
    public void closeDB() {
        devOpenHelper.close();
    }

    public void clearGreenTable() {
        greenBeanDao.deleteAll();
    }


    public void insert(DynamicBean o) {
        greenBeanDao.insertOrReplace(o);
    }


    public void insert(List<DynamicBean> list) {
        greenBeanDao.insertOrReplaceInTx(list);
    }

    public void update(DynamicBean o) {
        greenBeanDao.update(o);
    }

    public void delete(String userId) {

    }

    public List<DynamicBean> queryAll() {
        List<DynamicBean> list = greenBeanDao.loadAll();
        logList(list);

        return list;
    }

    public List<DynamicBean> queryAsc() {
        List<DynamicBean> list = greenBeanDao.queryBuilder()  // 查询 User
                //.where(Properties.FirstName.eq("Joe"))  // 首名为 Joe
                .orderAsc(DynamicBeanDao.Properties.CreateDate)  // 末名升序排列
                .list();  // 返回集合
        logList(list);
        return list;
    }

    private void logList(List<DynamicBean> list) {
        if (list != null && list.size() > 0) {
            for (DynamicBean b : list) {
                Log.e(TAG, b.toString());
            }
        }
    }

    public List<DynamicBean> queryByPage(int offset, int limit, int publisherId) {
        List<DynamicBean> list = greenBeanDao.queryBuilder()
                .where(DynamicBeanDao.Properties.PublisherId.eq(publisherId))
                .offset(offset).limit(limit)
                .orderDesc(DynamicBeanDao.Properties.CreateDate)  // 末名升序排列
                .list();  // 返回集合
        logList(list);

        return list;
    }

    public void save(UserBasicInfo u) {
        userBasicDao.insertOrReplace(u);
    }

    public UserBasicInfo queryUserBasic(Integer userId) {
        List<UserBasicInfo> list = userBasicDao.queryBuilder()
                .where(UserBasicInfoDao.Properties.UserId.eq(userId))
                .list();  // 返回集合
        //logList(list);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }

    public UserExtendInfo queryUserProperty(Integer userId) {
        List<UserExtendInfo> list = userPropertyDao.queryBuilder()
                .where(UserExtendInfoDao.Properties.UserId.eq(userId))
                .list();  // 返回集合
        //logList(list);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }

    public void save(UserExtendInfo u) {
        userPropertyDao.insertOrReplace(u);
    }
     /*public List<DynamicBean> queryWhereOr() {
        List<DynamicBean> list = greenBeanDao.queryBuilder()  // 查询 User
                //.whereOr(DynamicBeanDao.Properties.Name.eq("name1"), DynamicBeanDao.Properties.Phone.eq("phone2"))  // 首名为 Joe
                .orderDesc(DynamicBeanDao.Properties.Id)  // 末名升序排列
                .list();  // 返回集合
        logList(list);
        return list;
    }

    public List<DynamicBean> queryWhereBetween() {
        List<DynamicBean> list = greenBeanDao.queryBuilder()  // 查询 User
                .where(DynamicBeanDao.Properties.Id.between("0", "5"))  // 首名为 Joe
                .orderDesc(DynamicBeanDao.Properties.Id)  // 末名升序排列
                .list();  // 返回集合
        logList(list);
        return list;
    }*/
}
