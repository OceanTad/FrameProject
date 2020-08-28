package com.lht.frameproject;

import com.lht.base_library.base.BaseApplication;
import com.lht.base_library.http.api.ApiUtil;

public class CustomApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        if(isMainProcess()) {
            ApiUtil.initApi("https://vrdev.sitaitongxue.com");
        }
    }

}
