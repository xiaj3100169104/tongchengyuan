package com.style.rxAndroid.callback;

import android.util.Log;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xiajun on 2016/10/8.
 */
public abstract class BaseRXTaskCallBack implements RXTaskCallBack {
    protected String TAG = "BaseRXTaskCallBack";

    public Subscription run() {
        Observable<Object> mObservable = Observable.fromCallable(new Callable<Object>() {

            @Override
            public Object call() {
                Log.e(TAG, "call");
                return doInBackground();
            }
        });

       return mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object object) {
                        Log.e(TAG, "onNext");
                        onNextOnUIThread(object);
                    }
                });
    }

    public abstract Object doInBackground();

    public abstract void onNextOnUIThread(Object data);

    @Override
    public void OnSuccess(Object data) {

    }

    @Override
    public void OnFailed(String message) {

    }
}
