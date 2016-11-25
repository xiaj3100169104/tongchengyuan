package com.style.rxAndroid;

import android.support.v7.app.AppCompatActivity;

import com.style.base.BaseActivity;
import com.style.rxAndroid.newwork.callback.BaseRXTaskCallBack;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * Created by xiajun on 2016/10/8.
 */
public abstract class BaseRxActivity extends AppCompatActivity {
    protected String TAG = getClass().getSimpleName();
    protected List<Subscription> mSubscriptions;

    protected Subscription runTask(BaseRXTaskCallBack callBack) {
        Subscription subscription = RXAsynTaskManager.getInstance().runTask(callBack);
        addSubscription(subscription);
        return subscription;
    }

    protected void addSubscription(Subscription subscription) {
        if (mSubscriptions == null) {
            mSubscriptions = new ArrayList<>();
        }
        mSubscriptions.add(subscription);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscriptions != null) {
            for (Subscription s : mSubscriptions) {
                if (s != null && s.isUnsubscribed()) {
                    s.unsubscribe();
                }
            }
        }
    }
}