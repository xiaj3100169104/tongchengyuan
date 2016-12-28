package com.juns.wechat.util;


import com.alibaba.fastjson.TypeReference;
import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.dao.FriendDao;
import com.juns.wechat.dao.UserDao;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.callback.BaseCallBack;
import com.juns.wechat.net.common.HttpAction;
import com.juns.wechat.net.common.NetDataBeanCallback;
import com.juns.wechat.net.request.FriendRequest;
import com.juns.wechat.net.request.UserRequest;
import com.juns.wechat.net.response.BaseResponse;
import com.juns.wechat.net.response.SyncFriendResponse;
import com.juns.wechat.net.response.UserListResponse;

import java.util.List;

/**
 * Created by 王者 on 2016/7/26.
 */
public class SyncDataUtil {

    public static void syncData() {
        syncFriendData();
    }

    private static void syncFriendData() {
        long lastModifyDate = FriendDao.getInstance().getLastModifyDate(AccountManager.getInstance().getUserId());
        HttpAction.syncFriendData(lastModifyDate, new NetDataBeanCallback<List<FriendBean>>(new TypeReference<List<FriendBean>>() {
        }) {
            @Override
            protected void onCodeSuccess(List<FriendBean> data) {
                List<FriendBean> friendBeen = data;
                if (friendBeen != null && !friendBeen.isEmpty()) {
                    FriendDao.getInstance().replace(friendBeen);
                }
                syncUserData();
            }

            @Override
            protected void onCodeFailure(String msg) {

            }
        });
       /* FriendRequest.syncFriendData(lastModifyDate, new BaseCallBack<SyncFriendResponse>() {
            @Override
            protected void handleResponse(SyncFriendResponse result) {
                if (result.code == BaseResponse.SUCCESS) {
                    List<FriendBean> friendBeen = result.friendBeans;
                    if (friendBeen != null && !friendBeen.isEmpty()) {
                        FriendDao.getInstance().replace(friendBeen);
                    }
                    syncUserData();
                }
            }

        });*/
    }

    private static void syncUserData() {
        Integer[] userIds = FriendDao.getInstance().getNotExistUsersInFriend(AccountManager.getInstance().getUser().getUserId());
        long lastModifyDate = UserDao.getInstance().getLastModifyDate(AccountManager.getInstance().getUserId());
        HttpAction.syncUserData(userIds, lastModifyDate, new NetDataBeanCallback<List<UserBean>>(new TypeReference<List<UserBean>>() {
        }) {
            @Override
            protected void onCodeSuccess(List<UserBean> data) {
                List<UserBean> userBeen = data;
                if (userBeen != null && !userBeen.isEmpty()) {
                    UserDao.getInstance().replace(userBeen);
                }
            }

            @Override
            protected void onCodeFailure(String msg) {

            }
        });
       /* UserRequest.syncUserData(userIds, lastModifyDate, new BaseCallBack<UserListResponse>() {
            @Override
            protected void handleResponse(UserListResponse result) {
                if(result.code == BaseResponse.SUCCESS){
                    List<UserBean> userBeen = result.userBeans;
                    if(userBeen != null && !userBeen.isEmpty()){
                        UserDao.getInstance().replace(userBeen);
                    }
                }
            }
        });*/
    }

}
