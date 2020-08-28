package com.lht.base_library.base;

public interface BaseView {

    void showLoading();

    void dismissLoading();

    void showError(boolean isNoData);

    void dismissError();

}
