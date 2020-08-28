package com.lht.frameproject;

import com.lht.base_library.utils.LogUtil;

public class MainPresenterImp extends MainContract.MainPresenter {

    @Override
    protected void ceshi() {
        if (getView() != null) {
            LogUtil.e("View zhu ce le ");
        }
    }

}
