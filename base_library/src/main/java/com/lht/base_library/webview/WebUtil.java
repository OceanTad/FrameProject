package com.lht.base_library.webview;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.lht.base_library.IBinderWeb;
import com.lht.base_library.IWebCallBack;
import com.lht.base_library.binder.BinderCode;
import com.lht.base_library.binder.BinderPool;
import com.lht.base_library.binder.BinderWebImpl;
import com.rxjava.rxlife.RxLife;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WebUtil {

    public static void startWebActivity(Context context, String url, String payHeard, boolean isShowProgress, boolean isHtml) {
        Intent intent = new Intent(context, CustomWebActivity.class);
        intent.putExtra(CustomWebFragment.WEB_VIEW_URL, url);
        intent.putExtra(CustomWebFragment.PAY_HEARD, payHeard);
        intent.putExtra(CustomWebFragment.SHOW_PROGRESS, isShowProgress);
        intent.putExtra(CustomWebFragment.HTML, isHtml);
        context.startActivity(intent);
    }

    public static void startWebActivity(Context context, String url) {
        Intent intent = new Intent(context, CustomWebActivity.class);
        intent.putExtra(CustomWebFragment.WEB_VIEW_URL, url);
        intent.putExtra(CustomWebFragment.PAY_HEARD, "");
        intent.putExtra(CustomWebFragment.SHOW_PROGRESS, true);
        intent.putExtra(CustomWebFragment.HTML, false);
        context.startActivity(intent);
    }

    public static void binderHandleAction(String actionName, String actionParams, IWebCallBack callBack, LifecycleOwner owner) {
        Observable.create(new ObservableOnSubscribe<IBinder>() {
            @Override
            public void subscribe(ObservableEmitter<IBinder> e) throws Exception {
                e.onNext(BinderPool.getInstance().queryBinder(BinderCode.BINDER_WEB));
            }
        })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onTerminateDetach()
                .lift(RxLife.lift(owner, Lifecycle.Event.ON_DESTROY))
                .subscribe(new Consumer<IBinder>() {
                    @Override
                    public void accept(IBinder binderWeb) throws Exception {
                        if (binderWeb != null) {
                            try {
                                IBinderWeb iBinderWeb = IBinderWeb.Stub.asInterface(binderWeb);
                                iBinderWeb.handlerAction(actionName, actionParams, new IWebCallBack.Stub() {
                                    @Override
                                    public void action(String actionName, String actionParams) throws RemoteException {

                                    }

                                    @Override
                                    public void result(String actionName, String actionParams, String result) throws RemoteException {

                                    }
                                });
                                Log.e("binderHandleAction", "----" + binderWeb);
                                ((BinderWebImpl) binderWeb).handlerAction(actionName, actionParams, new IWebCallBack.Stub() {
                                    @Override
                                    public void action(String actionName, String actionParams) throws RemoteException {

                                    }

                                    @Override
                                    public void result(String actionName, String actionParams, String result) throws RemoteException {

                                    }
                                });
                            } catch (RemoteException e) {
                                Log.e("binderHandleAction", e.getClass() + " " + e.getMessage());
                            }
                        }
                    }
                });
    }

}
