package com.lht.base_library.http.download;

import android.os.Handler;
import android.os.Looper;

import java.lang.ref.SoftReference;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

public class DownLoadSubscriber<T> implements Observer<T>, IDownLoadProgressListener {

    private Disposable disposable;

    private SoftReference<DownLoadListener> listener;
    private DownLoadInfo info;

    private Handler mainHandler;

    public DownLoadSubscriber(DownLoadInfo info) {
        this.info = info;
        listener = new SoftReference<>(info.getListener());
        mainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void update(long read, long count, boolean done) {
        if (info.getCountLength() > count) {
            read = info.getCountLength() - count + read;
        } else {
            info.setCountLength(count);
        }
        info.setReadLength(read);

        if (listener != null && listener.get() != null) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (DownState.PAUSE.equals(info.getDownState()) || DownState.STOP.equals(info.getDownState())) {
                        return;
                    }
                    info.setDownState(DownState.DOWN);
                    listener.get().onDownLoadProgress(info, (int) (info.getReadLength() * 100 / info.getCountLength()));
                }
            });
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (DisposableHelper.validate(this.disposable, d)) {
            this.disposable = d;
        }
        if (info != null) {
            info.setDownState(DownState.Start);
        }
        if (listener != null && listener.get() != null) {
            listener.get().onDownLoadStart(info);
        }
    }

    @Override
    public void onNext(T value) {
        if (listener != null && listener.get() != null) {
            listener.get().onNext(info, value);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (info != null) {
            info.setDownState(DownState.ERROR);
        }
        if (listener != null && listener.get() != null) {
            listener.get().onDownLoadFail(info, e);
        }
        DownLoadManager.getInstance().removeTask(info);
    }

    @Override
    public void onComplete() {
        if (info != null) {
            info.setDownState(DownState.FINISH);
        }
        if (listener != null && listener.get() != null) {
            listener.get().onDownLoadFinish(info);
        }
        DownLoadManager.getInstance().removeTask(info);
    }

    public void cancel() {
        Disposable s = this.disposable;
        this.disposable = DisposableHelper.DISPOSED;
        s.dispose();
    }

}
