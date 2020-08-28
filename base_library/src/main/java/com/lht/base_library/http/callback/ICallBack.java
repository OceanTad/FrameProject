package com.lht.base_library.http.callback;

public abstract class ICallBack<T> {

    protected boolean isShowToast = true;

    public ICallBack<T> isShowToast(boolean isShowToast) {
        this.isShowToast = isShowToast;
        return this;
    }

    public void onPrepared() {
    }

    public void onComplete() {
    }

    public abstract void onSuccess(T t);

    public abstract void onFailure(String str, int errorCode);

}
