package com.juns.wechat.util;


import com.alibaba.fastjson.TypeReference;
import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.database.dao.FriendDao;
import com.juns.wechat.database.dao.UserDao;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.request.HttpActionImpl;
import com.style.net.core.NetDataBeanCallback;

import java.util.List;

/**
 * Created by 王者 on 2016/7/26.
 */
public class SyncDataUtil {

    private static SyncDataUtil mInstance;
    private Callback callback;

    public static SyncDataUtil getInstance() {
        if (mInstance == null) {
            mInstance = new SyncDataUtil();
        }
        return mInstance;
    }
    public void syncData() {
        syncFriendData("");
    }

    public void syncData(String tag, Callback callback) {
        this.callback = callback;
        syncFriendData(tag);
    }

    private void syncFriendData(final String tag) {
        long lastModifyDate = FriendDao.getInstance().getLastModifyDate(AccountManager.getInstance().getUserId());
        HttpActionImpl.getInstance().syncFriendData("syncFriendData", lastModifyDate, new NetDataBeanCallback<List<FriendBean>>(new TypeReference<List<FriendBean>>() {
        }) {
            @Override
            protected void onCodeSuccess(List<FriendBean> data) {
                List<FriendBean> friendBeen = data;
                if (friendBeen != null && !friendBeen.isEmpty()) {
                    FriendDao.getInstance().replace(friendBeen);
                }
                syncUserData(tag);
            }

            @Override
            protected void onCodeFailure(String msg) {
                if (callback != null) {
                    callback.onFailure();
                    callback = null;
                }
            }
        });
    }

    private void syncUserData(String tag) {
        Integer[] userIds = FriendDao.getInstance().getNotExistUsersInFriend(AccountManager.getInstance().getUser().getUserId());
        long lastModifyDate = UserDao.getInstance().getLastModifyDate(AccountManager.getInstance().getUserId());
        HttpActionImpl.getInstance().syncUserData("syncUserData", userIds, lastModifyDate, new NetDataBeanCallback<List<UserBean>>(new TypeReference<List<UserBean>>() {
        }) {
            @Override
            protected void onCodeSuccess(List<UserBean> data) {
                List<UserBean> userBeen = data;
                if (userBeen != null && !userBeen.isEmpty()) {
                    UserDao.getInstance().replace(userBeen);
                }
                if (callback != null) {
                    callback.onSuccess();
                    callback = null;
                }
            }

            @Override
            protected void onCodeFailure(String msg) {
                if (callback != null) {
                    callback.onFailure();
                    callback = null;
                }
            }
        });
    }

    public interface Callback {

        void onSuccess();

        void onFailure();
    }
}
