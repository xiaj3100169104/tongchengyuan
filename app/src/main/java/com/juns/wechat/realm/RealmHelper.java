package com.juns.wechat.realm;


import com.juns.wechat.manager.AccountManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmHelper {

    public static RealmConfiguration getGlobalConfig() {

        return new RealmConfiguration.Builder()
                .name("weiqu.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
    }

    public static RealmConfiguration getIMConfig() {
        int userId = AccountManager.getInstance().getUserId();
        return new RealmConfiguration.Builder()
                .name(userId == 0 ? "imDb" : "imDb" + userId + ".realm")
                .schemaVersion(1)
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