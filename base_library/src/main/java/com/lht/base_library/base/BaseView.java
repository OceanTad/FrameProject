package com.lht.base_library.base;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

public interface BaseView {

    Context getCurrentContext();

    LifecycleOwner getLifecycleOwner();

    void showLoading();

    void dismissLoading();

    void showError(boolean isNoData);

    void dismissError();

}
