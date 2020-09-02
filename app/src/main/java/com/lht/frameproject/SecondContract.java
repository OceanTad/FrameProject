package com.lht.frameproject;

import com.lht.base_library.base.BasePresenter;
import com.lht.base_library.base.BaseView;

public interface SecondContract {

    interface SecondView extends BaseView {

    }

    abstract class SecondPresenter extends BasePresenter<SecondView> {

    }

}
