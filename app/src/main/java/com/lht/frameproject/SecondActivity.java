package com.lht.frameproject;

import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lht.base_library.base.BaseActivity;
import com.lht.base_library.rxbus.RxBus;
import com.lht.base_library.utils.LogUtil;

import io.reactivex.functions.Consumer;

@Route(path = "/app/SecondActivity")
public class SecondActivity extends BaseActivity<MainPresenterImp> {

    @Override
    protected void loadView() {
        setContentView(R.layout.activity_main);
        addViewClick(R.id.ceshi);

        RxBus.getInstance().registerEvent(this, Lifecycle.Event.ON_DESTROY, Integer.class, new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                LogUtil.e("main ceshi "+integer);
            }
        });

    }

    @Override
    protected void recycle() {

    }

    @Override
    protected MainPresenterImp createPresenter() {
        return new MainPresenterImp();
    }

    @Override
    protected void onViewClick(View view) {
        super.onViewClick(view);
        switch (view.getId()) {
            case R.id.ceshi:
                findView(R.id.ceshi, TextView.class).setText("怎么了");
//                if (getPresenter() != null) {
//                    getPresenter().ceshi();
//                }
                RxBus.getInstance().post(2000);
                break;
        }
    }

}
