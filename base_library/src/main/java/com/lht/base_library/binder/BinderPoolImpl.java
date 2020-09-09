package com.lht.base_library.binder;

import android.os.IBinder;
import android.os.RemoteException;

import com.lht.base_library.IBinderPool;

public class BinderPoolImpl extends IBinderPool.Stub {

    @Override
    public IBinder queryBinder(int binderCode) throws RemoteException {
        IBinder binder = null;
        switch (binderCode) {
            case BinderCode.BINDER_WEB:
                binder = new BinderWebImpl();
                break;
        }
        return binder;
    }

}
