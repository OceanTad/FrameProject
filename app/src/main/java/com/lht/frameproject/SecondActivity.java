package com.lht.frameproject;

import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.lht.base_library.ARouterPath;
import com.lht.base_library.base.BaseActivity;
import com.lht.base_library.base.ProviderApplication;
import com.lht.base_library.utils.LogUtil;

@Route(path = "/app/SecondActivity")
public class SecondActivity extends BaseActivity<MainPresenterImp> {

    @Override
    protected void loadView() {
        setContentView(R.layout.activity_main);
        addViewClick(R.id.ceshi);
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
                findView(R.id.ceshi, TextView.class).setText("nihao ceshi fengzhuang");
                if (getPresenter() != null) {
                    getPresenter().ceshi();
                }
                break;
        }
    }

}
