package com.lht.frameproject;

import android.Manifest;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;

import com.lht.base_library.base.BaseActivity;
import com.lht.base_library.broadcast.ForBackGroundListener;
import com.lht.base_library.broadcast.ForBackGroundManager;
import com.lht.base_library.broadcast.NetChangeManager;
import com.lht.base_library.broadcast.NetChangeStateListener;
import com.lht.base_library.http.api.ApiUtil;
import com.lht.base_library.http.callback.ICallBack;
import com.lht.base_library.utils.AppConfigUtil;
import com.lht.base_library.utils.LogUtil;
import com.lht.base_library.utils.MD5Util;

import java.util.Map;
import java.util.TreeMap;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity<MainPresenterImp> {

    @Override
    protected void loadView() {
        setContentView(R.layout.activity_main);
        addViewClick(R.id.ceshi);
//        showLoading();
        ForBackGroundManager.getInstance().registerForBackGroundReceiver(this, getClass(), new ForBackGroundListener() {
            @Override
            public void onFroGround() {
                LogUtil.e("onFroGround");
            }

            @Override
            public void onBackGround() {
                LogUtil.e("onBackGround");
            }
        });

        NetChangeManager.getInstance().registerNetChangeReceiver(this, getClass(), new NetChangeStateListener() {
            @Override
            public void onNetStateChange(int state) {
                LogUtil.e("onNetStateChange:" + state);
            }
        });

        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_PHONE_STATE)) {
            request();
        } else {
            EasyPermissions.requestPermissions(this,"adafaf",0,Manifest.permission.READ_PHONE_STATE);
        }

    }

    private void request(){
        ApiUtil.initApi("http://vrdev.sitaitongxue.com");
        ApiUtil.buildResult(ApiUtil.getApiService(ApiService.class).login(createLocalParams("phone", "13131287580", "code", "111111", "device_id", AppConfigUtil.getUuid(this))), this, Lifecycle.Event.ON_DESTROY, new ICallBack<String>() {
            @Override
            public void onSuccess(String s) {
                LogUtil.e("****" + s);
            }

            @Override
            public void onFailure(String str, int errorCode) {
                LogUtil.e(errorCode + "****" + str);
            }
        });
    }

    private Map<String, String> createLocalParams(String... args) {

        TreeMap<String, String> paramsMap = new TreeMap<>();
        paramsMap.put("appkey", "de158f35d459f481f0ba9309a309162c");
        paramsMap.put("channel", "5001001015");
        paramsMap.put("version", AppConfigUtil.getApkVersionName(this));

        for (int i = 0; i < args.length; i++) {
            paramsMap.put(args[i], args[++i]);
        }

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            sb.append(entry.getKey())
                    .append('=')
                    .append(entry.getValue());
        }
        sb.append('_').append("a0b5224d564feecdf2b5019d1c836964");
        paramsMap.put("sign", MD5Util.getMD5String(sb.toString()));

        return paramsMap;
    }

    @Override
    protected void recycle() {
        ForBackGroundManager.getInstance().unregisterForBackGroundReceiver(this, getClass());
        NetChangeManager.getInstance().unregisterForBackGroundReceiver(this, getClass());
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
