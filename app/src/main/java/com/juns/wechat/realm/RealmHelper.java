package com.juns.wechat.realm;


import com.juns.wechat.manager.AccountManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmHelper {
    private static final int REALM_VERSION = 1;

    public static RealmConfiguration getGlobalConfig() {

        return new RealmConfiguration.Builder()
                .name("weiqu.realm")
                .schemaVersion(REALM_VERSION)
                .deleteRealmIfMigrationNeeded()
                .build();
    }

    /**
     * 针对每个用户设计单独的message表
     * @return
     */
    public static RealmConfiguration getIMConfig() {
        int userId = AccountManager.getInstance().getUserId();
        return new RealmConfiguration.Builder()
                .name(userId == 0 ? "imDb" : "imDb" + userId + ".realm")
                .schemaVersion(REALM_VERSION)
                .build();
    }

    public static Realm getDefaultInstance() {
        Realm realm = Realm.getInstance(getGlobalConfig());
        return realm;
    }

    public static Realm getIMInstance() {
        Realm realm = Realm.getInstance(getIMConfig());
        return realm;
    }

}