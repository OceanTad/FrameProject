package com.lht.base_library.webview;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;

import com.lht.base_library.IBinderWeb;
import com.lht.base_library.IWebCallBack;
import com.lht.base_library.R;
import com.lht.base_library.binder.BinderCode;
import com.lht.base_library.binder.BinderPool;
import com.lht.base_library.utils.ToastUtil;
import com.rxjava.rxlife.RxLife;

import java.lang.ref.SoftReference;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WebBinderManager {

    private static final String TAG = "WebBinderManager";

    private IBinderWeb binderWeb;
    private IWebViewClient callBack;

    private SoftReference<FragmentActivity> owner;
    private Handler handler;

    public WebBinderManager(FragmentActivity owner) {
        this.owner = new SoftReference<>(owner);
        handler = new Handler(Looper.getMainLooper());
    }

    public void init() {
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
                .lift(RxLife.lift(owner.get(), Lifecycle.Event.ON_DESTROY))
                .subscribe(new Consumer<IBinder>() {
                    @Override
                    public void accept(IBinder binder) throws Exception {
                        binderWeb = IBinderWeb.Stub.asInterface(binder);
                    }
                });
    }

    public void setCallBack(IWebViewClient callBack) {
        this.callBack = callBack;
    }

    public void post(String actionName, String actionParams) {
        if (binderWeb == null) {
            init();
        } else {
            try {
                binderWeb.handlerAction(actionName, actionParams, new IWebCallBack.Stub() {
                    @Override
                    public void action(String actionName, String actionParams) throws RemoteException {
                        Log.e("other process", "action:" + actionName + "----" + actionParams);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (!defaultAction(actionName, actionParams)) {
                                    if (callBack != null) {
                                        callBack.actionExce(actionName, actionParams);
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void result(String actionName, String actionParams, String result) throws RemoteException {
                        Log.e("other process", "result:" + actionName + "----" + actionParams);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (callBack != null) {
                                    callBack.resultExce(actionName, actionParams, result);
                                }
                            }
                        });
                    }
                });
            } catch (RemoteException e) {
                Log.e(TAG, e.getClass() + " " + e.getMessage());
            }
        }
    }

    private boolean defaultAction(String actionName, String actionParams) {
        boolean action = false;
        if (owner != null && owner.get() != null) {
            action = true;
            switch (actionName) {

                case ActionName.CALL_PHONE:
                    Intent callPhone = new Intent(Intent.ACTION_DIAL, Uri.parse(actionParams));
                    owner.get().startActivity(callPhone);
                    break;

                case ActionName.WX_APP_PAY:
                case ActionName.ALI_WAKE_UP:
                case ActionName.ALI_APP_PAY:
                case ActionName.SMS:
                case ActionName.MAIL:
                case ActionName.GEO:
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(actionParams));
                    owner.get().startActivity(intent);
                    break;

                case ActionName.WX_WEB_PAY:
                case ActionName.RETRY_LOAD:
                    if (callBack != null) {
                        callBack.webViewLoad(actionName, actionParams);
                    }
                    break;

                case ActionName.WX_UNINSTALL:
                    ToastUtil.showShort(R.string.alipay_uninstall);
                    break;

                case ActionName.ALI_UNINSTALL:
                    ToastUtil.showShort(R.string.wx_uninstall);
                    break;

                default:
                    action = false;
                    break;

            }
        }
        return action;
    }

}
