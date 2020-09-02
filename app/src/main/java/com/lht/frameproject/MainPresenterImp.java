package com.lht.frameproject;

import android.os.Environment;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

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

public class MainPresenterImp extends MainContract.MainPresenter {

    @Override
    protected void login(LifecycleOwner owner) {
        if (getView() != null) {
            getView().showLoading();
        }
        ApiUtil.buildResult(ApiUtil.getApiService(ApiService.class).login(createLocalParams("phone", "13131287580", "code", "111111", "device_id", AppConfigUtil.getUuid(CustomApplication.getInstance()))), owner, Lifecycle.Event.ON_DESTROY, new ICallBack<LoginInfoBean>() {
            @Override
            public void onSuccess(LoginInfoBean s) {
                LogUtil.e("****" + s);
                if (getView() != null) {
                    getView().dismissLoading();
                    getView().onSuccess();
                }
            }

            @Override
            public void onFailure(String str, int errorCode) {
                LogUtil.e(errorCode + "****" + str);
                if (getView() != null) {
                    getView().dismissLoading();
                    getView().onFail();
                }
            }
        });
    }

    @Override
    protected void download() {
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
        paramsMap.put("version", AppConfigUtil.getApkVersionName(CustomApplication.getInstance()));

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

}
