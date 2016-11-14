package com.juns.wechat.util;


import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.dao.FriendDao;
import com.juns.wechat.dao.UserDao;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.callback.BaseCallBack;
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

    public static void syncData(){
        syncFriendData();
    }

    private static void syncFriendData(){
        long lastModifyDate = FriendDao.getInstance().getLastModifyDate(AccountManager.getInstance().getUserName());
        FriendRequest.syncFriendData(lastModifyDate, new BaseCallBack<SyncFriendResponse>() {
            @Override
            protected void handleResponse(SyncFriendResponse result) {
                if(result.code == BaseResponse.SUCCESS){
                    List<FriendBean> friendBeen = result.friendBeans;
                    if(friendBeen != null && !friendBeen.isEmpty()){
                        FriendDao.getInstance().replace(friendBeen);
                    }
                    syncUserData();
                }
            }

        });
    }

    private static void syncUserData(){
        String[] userNames = FriendDao.getInstance().getNotExistUsersInFriend(AccountManager.getInstance().getUserName());
        long lastModifyDate = UserDao.getInstance().getLastModifyDate(AccountManager.getInstance().getUserName());
        UserRequest.syncUserData(userNames, lastModifyDate, new BaseCallBack<UserListResponse>() {
            @Override
            protected void handleResponse(UserListResponse result) {
                if(result.code == BaseResponse.SUCCESS){
                    List<UserBean> userBeen = result.userBeans;
                    if(userBeen != null && !userBeen.isEmpty()){
                        UserDao.getInstance().replace(userBeen);
                    }
                }
            }
        });
    }

}
