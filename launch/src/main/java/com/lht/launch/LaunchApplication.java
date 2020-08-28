package com.lht.launch;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lht.base_library.ARouterPath;
import com.lht.base_library.base.ProviderApplication;
import com.lht.base_library.utils.LogUtil;

@Route(path = ARouterPath.LAUNCH_APPLICATION)
public class LaunchApplication implements ProviderApplication {

    @Override
    public void mainInit() {
        LogUtil.e("Lht ceshi main");
    }

    @Override
    public void delayInit() {
        LogUtil.e("Lht ceshi delay");
    }

    @Override
    public void init(Context context) {
        LogUtil.e("Lht ceshi init");
    }

}
