package com.lht.base_library.http.api;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.lht.base_library.http.callback.CallBackFactory;
import com.lht.base_library.http.callback.ICallBack;
import com.lht.base_library.http.modle.BaseResponse;
import com.lht.base_library.http.net.RetrofitManager;
import com.lht.base_library.http.net.RetryFunction;
import com.rxjava.rxlife.RxLife;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;

public class ApiUtil {

    public static void initApi(String baseUrl) {
        ApiHelp.getInstance().createApi(baseUrl);
    }

    public static void initApi(RetrofitManager.Builder builder) {
        ApiHelp.getInstance().createApi(builder);
    }

    public static OkHttpClient getHttpClient() {
        return ApiHelp.getInstance().getClient();
    }

    public static <T> T getApiService(Class<T> object) {
        return ApiHelp.getInstance().getApi(object);
    }

    public static <T> void buildResult(final Observable<BaseResponse<T>> observable, final LifecycleOwner owner, final Lifecycle.Event event, final ICallBack<T> callback) {
        observable.compose(new ObservableTransformer<BaseResponse<T>, BaseResponse<T>>() {
            @Override
            public ObservableSource<BaseResponse<T>> apply(Observable<BaseResponse<T>> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .onTerminateDetach()
                        .lift(RxLife.<BaseResponse<T>>lift(owner, event));
            }
        })
                .subscribe(CallBackFactory.getInstance().callback(callback));
    }

    public static <T> void buildResult(final Observable<BaseResponse<T>> observable, int retryCount, long retryTime, Class[] throwables, final LifecycleOwner owner, final Lifecycle.Event event, final ICallBack<T> callback) {
        observable.compose(new ObservableTransformer<BaseResponse<T>, BaseResponse<T>>() {
            @Override
            public ObservableSource<BaseResponse<T>> apply(Observable<BaseResponse<T>> upstream) {
                return upstream
                        .retryWhen(new RetryFunction.Builder().setMaxCount(retryCount).setWaitTime(retryTime).addThrowable(throwables).build())
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .onTerminateDetach()
                        .lift(RxLife.<BaseResponse<T>>lift(owner, event));
            }
        })
                .subscribe(CallBackFactory.getInstance().callback(callback));
    }

}
