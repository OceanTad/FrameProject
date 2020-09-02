package com.lht.frameproject;

import androidx.lifecycle.LifecycleOwner;

import com.lht.base_library.base.BasePresenter;
import com.lht.base_library.base.BaseView;

public interface MainContract {

    interface MainView extends BaseView {

        void onSuccess();

        void onFail();

    }

    abstract class MainPresenter extends BasePresenter<MainView> {

        protected abstract void login(LifecycleOwner owner);

        protected abstract void download();

    }

}
