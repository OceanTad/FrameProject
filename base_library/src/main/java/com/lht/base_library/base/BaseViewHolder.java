package com.lht.base_library.base;

import android.util.SparseArray;
import android.view.View;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import java.lang.ref.SoftReference;

public class BaseViewHolder implements LifecycleObserver {

    private SoftReference<View> rootView;
    private SparseArray<View> mViews = new SparseArray<>();

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void detachView() {
        if (mViews != null) {
            mViews.clear();
            mViews = null;
        }
        if (rootView != null) {
            rootView.clear();
            rootView = null;
        }
    }

    public BaseViewHolder(View view) {
        rootView = new SoftReference<>(view);
    }

    public View getRootView() {
        return rootView == null ? null : rootView.get();
    }

    public <T extends View> T findView(int viewId, Class<T> classType) {
        if (mViews != null) {
            View view = mViews.get(viewId);
            if (view == null) {
                if (rootView != null && rootView.get() != null) {
                    view = rootView.get().findViewById(viewId);
                }
                if (view != null) {
                    mViews.append(viewId, view);
                }
            }
            if (classType.isInstance(view)) {
                return classType.cast(view);
            }
        }
        return null;
    }

    public View findView(int viewId) {
        if (mViews != null) {
            View view = mViews.get(viewId);
            if (view == null) {
                if (rootView != null && rootView.get() != null) {
                    view = rootView.get().findViewById(viewId);
                }
                if (view != null) {
                    mViews.append(viewId, view);
                }
                return view;
            }
            return view;
        }
        return null;
    }

}
