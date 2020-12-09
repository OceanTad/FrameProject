package com.lht.base_library.base;


import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import java.lang.ref.SoftReference;

public abstract class BasePresenter<V extends BaseView> implements LifecycleObserver {

    private SoftReference<V> mView;

    public void attachView(V view) {
        mView = new SoftReference<>(view);
        init();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void detachView() {
        if (mView != null) {
            mView.clear();
            mView = null;
        }
        destroy();
    }

    protected V getView() {
        if (mView != null) {
            return mView.get();
        }
        return null;
    }

    protected void init() {
    }

    protected void destroy() {
    }

}
