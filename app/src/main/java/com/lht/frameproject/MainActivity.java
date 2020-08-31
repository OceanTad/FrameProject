package com.lht.frameproject;

import android.Manifest;
import android.os.Environment;
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
import com.lht.base_library.http.download.DownLoadInfo;
import com.lht.base_library.http.download.DownLoadListener;
import com.lht.base_library.http.download.DownLoadManager;
import com.lht.base_library.utils.AppConfigUtil;
import com.lht.base_library.utils.LogUtil;
import com.lht.base_library.utils.MD5Util;

import java.io.File;
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
            EasyPermissions.requestPermissions(this, "adafaf", 0, Manifest.permission.READ_PHONE_STATE);
        }

        if (EasyPermissions.hasPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})) {
            downLoad();
        } else {
            EasyPermissions.requestPermissions(this, "adafaf", 0, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
        }

    }

    private void request() {
        ApiUtil.initApi("http://vrdev.sitaitongxue.com");
        ApiUtil.buildResult(ApiUtil.getApiService(ApiService.class).login(createLocalParams("phone", "13131287580", "code", "111111", "device_id", AppConfigUtil.getUuid(this))), this, Lifecycle.Event.ON_DESTROY, new ICallBack<LoginInfoBean>() {
            @Override
            public void onSuccess(LoginInfoBean s) {
                LogUtil.e("****" + s);
            }

            @Override
            public void onFailure(String str, int errorCode) {
                LogUtil.e(errorCode + "****" + str);
            }
        });
    }

    private void downLoad() {
        DownLoadInfo info = new DownLoadInfo("http://clips.vorwaerts-gmbh.de/", "big_buck_bunny.mp4", new DownLoadListener() {
            @Override
            public void onDownLoadStart(DownLoadInfo info) {
                LogUtil.e("onDownLoadStart:" + info);
            }

            @Override
            public void onDownLoadProgress(DownLoadInfo info, int progress) {
                LogUtil.e("onDownLoadProgress:" + info + "*****" + progress);
            }

            @Override
            public void onDownLoadFinish(DownLoadInfo info) {
                LogUtil.e("onDownLoadFinish:" + info);
            }

            @Override
            public void onDownLoadFail(DownLoadInfo info, Throwable throwable) {
                LogUtil.e("onDownLoadFail:" + info + "******" + throwable.getClass() + "***" + throwable.getMessage());
            }

            @Override
            public <T> void onNext(DownLoadInfo info, T t) {
                LogUtil.e("onNext:" + info + "*****" + t);
            }

            @Override
            public void onDownLoadPause(DownLoadInfo info) {
                LogUtil.e("onDownLoadPause:" + info);
            }

            @Override
            public void onDownLoadStop(DownLoadInfo info) {
                LogUtil.e("onDownLoadStop:" + info);
            }
        });
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "test1.mp4");
        info.setSavePath(file.getAbsolutePath());
        DownLoadManager.getInstance().startTask(info);
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
