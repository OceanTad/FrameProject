package com.lht.base_library.http.callback;

import com.lht.base_library.http.modle.BaseResponse;
import com.lht.base_library.http.modle.BaseResponseCode;
import com.lht.base_library.utils.LogUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

public class CallBackFactory {

    private static volatile CallBackFactory instance;

    private CallBackFactory() {

    }

    public static CallBackFactory getInstance() {
        if (instance == null) {
            synchronized (CallBackFactory.class) {
                if (instance == null) {
                    instance = new CallBackFactory();
                }
            }
        }
        return instance;
    }

    private IUnifyCallBack unifyCallBack;

    public void setUnifyCallBack(IUnifyCallBack unifyCallBack) {
        this.unifyCallBack = unifyCallBack;
    }

    public <T> Observer<BaseResponse<T>> callback(final ICallBack<T> callback) {
        return new Observer<BaseResponse<T>>() {
            @Override
            public void onSubscribe(Disposable d) {
                if (callback != null) {
                    callback.onPrepared();
                }
            }

            @Override
            public void onNext(BaseResponse<T> value) {
                if (callback != null) {
                    if (value != null && value.getCode() == BaseResponseCode.SUCCESS) {
                        callback.onSuccess(value.getData());
                    } else {
                        if (unifyCallBack != null) {
                            boolean isUnify = unifyCallBack.onUnifyDispose(value.getCode(), value.getMessage());
                            callback.onFailure(value.getMessage(), isUnify ? BaseResponseCode.NO_ACTION : value.getCode());
                        } else {
                            callback.onFailure(value.getMessage(), value.getCode());
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e("okhttp error " + e.getClass() + " " + e.getMessage());
                if (callback != null) {
                    if (e instanceof SocketTimeoutException) {
                        callback.onFailure(BaseResponseCode.TIMEOUT_MSG, BaseResponseCode.TIME_OUT_ERROR);
                    } else if (e instanceof ConnectException) {
                        callback.onFailure(BaseResponseCode.CONNECT_MSG, BaseResponseCode.CONNECT_ERROR);
                    } else if (e instanceof SSLHandshakeException) {
                        callback.onFailure(BaseResponseCode.SSL_MSG, BaseResponseCode.SSL_ERROR);
                    } else if (e instanceof UnknownHostException) {
                        callback.onFailure(BaseResponseCode.DNS_MSG, BaseResponseCode.DNS_ERROR);
                    } else if (e instanceof HttpException) {
                        int code = ((HttpException) e).code();
                        if (504 == code) {
                            callback.onFailure(BaseResponseCode.NET_MSG, BaseResponseCode.NET_ERROR);
                        } else if (404 == code) {
                            callback.onFailure(BaseResponseCode.URL_MSG, BaseResponseCode.URL_ERROR);
                        } else {
                            callback.onFailure(BaseResponseCode.HTTP_MSG, BaseResponseCode.HTTP_ERROR);
                        }
                    } else {
                        callback.onFailure(BaseResponseCode.UNKNOWN_MSG, BaseResponseCode.UNKNOWN_ERROR);
                    }
                }
            }

            @Override
            public void onComplete() {
                if (callback != null) {
                    callback.onComplete();
                }
            }
        };
    }

}
