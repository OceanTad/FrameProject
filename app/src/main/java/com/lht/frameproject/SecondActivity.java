package com.lht.frameproject;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.lht.base_library.ARouterPath;
import com.lht.base_library.base.BaseActivity;
import com.lht.base_library.webview.WebUtil;

@Route(path = "/app/SecondActivity")
public class SecondActivity extends BaseActivity<SecondPresenterImp> implements SecondContract.SecondView {

    @Override
    protected int loadView() {
        return R.layout.activity_second;
    }

    @Override
    protected void initView() {
        super.initView();
        addViewClick(R.id.ceshi);
    }

    @Override
    protected void recycle() {

    }

    @Override
    protected SecondPresenterImp createPresenter() {
        return new SecondPresenterImp();
    }

    @Override
    protected void onViewClick(View view) {
        super.onViewClick(view);
        if (view.getId() == R.id.ceshi) {
            WebUtil.startWebActivity(this, "https://xw.qq.com/?f=qqcom");
        }
    }
}
