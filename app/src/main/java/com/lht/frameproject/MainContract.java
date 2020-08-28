package com.lht.frameproject;

import com.lht.base_library.base.BasePresenter;
import com.lht.base_library.base.BaseView;

public interface MainContract {

    interface MainView extends BaseView {

    }

    abstract class MainPresenter extends BasePresenter<MainView> {

        protected abstract void ceshi();

    }

}
