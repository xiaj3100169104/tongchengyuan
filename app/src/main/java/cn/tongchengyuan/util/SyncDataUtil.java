package cn.tongchengyuan.util;


import com.alibaba.fastjson.TypeReference;

import cn.tongchengyuan.greendao.mydao.GreenDaoManager;
import cn.tongchengyuan.manager.AccountManager;
import cn.tongchengyuan.net.request.HttpActionImpl;
import cn.tongchengyuan.bean.FriendBean;
import cn.tongchengyuan.bean.UserBean;

import com.style.net.core.NetDataBeanCallback;

import java.util.List;

/**
 * Created by 王者 on 2016/7/26.
 */
public class SyncDataUtil {

    private static SyncDataUtil mInstance;
    private Callback callback;
    private GreenDaoManager greenDao = GreenDaoManager.getInstance();

    public static SyncDataUtil getInstance() {
        if (mInstance == null) {
            mInstance = new SyncDataUtil();
        }
        return mInstance;
    }
    public void syncData() {
        //syncFriendData("");
    }

    public void syncData(String tag, Callback callback) {
        //this.callback = callback;
        //syncFriendData(tag);
    }

    private void syncFriendData(final String tag) {
        long lastModifyDate = greenDao.getLastModifyDate(AccountManager.getInstance().getUserId());
        HttpActionImpl.getInstance().syncFriendData("syncFriendData", lastModifyDate, new NetDataBeanCallback<List<FriendBean>>(new TypeReference<List<FriendBean>>() {
        }) {
            @Override
            protected void onCodeSuccess(List<FriendBean> data) {
                List<FriendBean> friendBeen = data;
                if (friendBeen != null && !friendBeen.isEmpty()) {
                    greenDao.saveFriends(friendBeen);
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
        Integer[] userIds = greenDao.getNotExistUsersInFriend(AccountManager.getInstance().getUser().getUserId());
        long lastModifyDate = greenDao.getInstance().getLastModifyDate(AccountManager.getInstance().getUserId());
        HttpActionImpl.getInstance().syncUserData("syncUserData", userIds, lastModifyDate, new NetDataBeanCallback<List<UserBean>>(new TypeReference<List<UserBean>>() {
        }) {
            @Override
            protected void onCodeSuccess(List<UserBean> data) {
                List<UserBean> userBeen = data;
                if (userBeen != null && !userBeen.isEmpty()) {
                    greenDao.saveUserList(userBeen);
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
