package com.lht.base_library.http.download;

public interface DownLoadListener {

    void onDownLoadStart(DownLoadInfo info);

    void onDownLoadProgress(DownLoadInfo info, int progress);

    void onDownLoadFinish(DownLoadInfo info);

    void onDownLoadFail(DownLoadInfo info, Throwable throwable);

    <T> void onNext(DownLoadInfo info, T t);

    void onDownLoadPause(DownLoadInfo info);

    void onDownLoadStop(DownLoadInfo info);

}
