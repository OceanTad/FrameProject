package com.lht.base_library.rxbus;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.rxjava.rxlife.RxLife;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class RxBus {

    private static volatile RxBus instance;

    private RxBus() {
        mSubject = PublishSubject.create().toSerialized();
    }

    public static RxBus getInstance() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    private Subject<Object> mSubject;

    public void post(Object obj) {
        mSubject.onNext(obj);
    }

    public <T> void registerEvent(final LifecycleOwner owner, final Lifecycle.Event event,Class<T> classType, Consumer<T> consumer) {
        mSubject.ofType(classType)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onTerminateDetach()
                .lift(RxLife.<T>lift(owner, event))
                .subscribe(consumer);
    }

    public boolean hasRegister() {
        return mSubject.hasObservers();
    }

}
