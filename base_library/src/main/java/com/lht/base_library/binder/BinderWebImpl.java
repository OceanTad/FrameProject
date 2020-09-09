package com.lht.base_library.binder;

import android.os.RemoteException;
import android.util.Log;

import com.lht.base_library.IBinderWeb;
import com.lht.base_library.IWebCallBack;

public class BinderWebImpl extends IBinderWeb.Stub {

    @Override
    public void handlerAction(String actionName, String actionParams, IWebCallBack callBack) throws RemoteException {
        if (callBack != null) {
            callBack.action(actionName, actionParams);
        }
        String result = doAction(actionName, actionParams);
        if (callBack != null) {
            callBack.result(actionName, actionParams, result);
        }
    }

    private String doAction(String actionName, String actionParams) {
        Log.e("main process", actionName+"----"+actionParams);
        String result = "";
        return result;
    }

}
