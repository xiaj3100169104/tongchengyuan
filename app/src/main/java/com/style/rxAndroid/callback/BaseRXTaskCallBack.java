package com.style.rxAndroid.callback;

import android.util.Log;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xiajun on 2016/10/8.
 */
public abstract class BaseRXTaskCallBack{
    protected String TAG = "BaseRXTaskCallBack";

    public Disposable run() {
        Observable<Object> mObservable = Observable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() {
                Log.e(TAG, "call");
                return doInBackground();
            }
        });
      /*  Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                Log.d("所在的线程：",Thread.currentThread().getName());
                Log.d("发送的数据:", 1+"");
                e.onNext(1);
            }
        });*/
        Consumer<Object> observer = new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                Log.e(TAG, "accept");
                onNextOnUIThread(o);
            }
        };
        Disposable disposable = mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        return disposable;
    }

    protected abstract void onNextOnUIThread(Object o);

    public abstract Object doInBackground();
}
