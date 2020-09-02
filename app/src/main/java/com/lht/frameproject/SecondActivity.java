package com.lht.frameproject;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lht.base_library.base.BaseActivity;

@Route(path = "/app/SecondActivity")
public class SecondActivity extends BaseActivity<SecondPresenterImp> implements SecondContract.SecondView {

    @Override
    protected int loadView() {
        return R.layout.activity_second;
    }

    @Override
    protected void recycle() {

    }

    @Override
    protected SecondPresenterImp createPresenter() {
        return new SecondPresenterImp();
    }

}
