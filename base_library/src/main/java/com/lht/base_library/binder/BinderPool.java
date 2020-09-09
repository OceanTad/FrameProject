package com.lht.base_library.binder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.lht.base_library.IBinderPool;

import java.util.concurrent.CountDownLatch;

public class BinderPool {

    private static final String TAG = "BinderPool";

    private static volatile BinderPool instance;

    private BinderPool() {
    }

    public static BinderPool getInstance() {
        if (instance == null) {
            synchronized (BinderPool.class) {
                if (instance == null) {
                    instance = new BinderPool();
                }
            }
        }
        return instance;
    }

    private Context mContext;

    private CountDownLatch mBindPoolLatch;
    private IBinderPool mBinderPool;

    public synchronized void connectBinderPoolService(Context context) {
        mContext = context.getApplicationContext();
        mBindPoolLatch = new CountDownLatch(1);
        Intent service = new Intent(mContext, BinderPoolService.class);
        mContext.bindService(service, mBinderPoolConnection, Context.BIND_AUTO_CREATE);
        try {
            mBindPoolLatch.await();
        } catch (InterruptedException e) {
            Log.e(TAG, "connect binder pool service error " + e.getClass() + " " + e.getMessage());
        }
    }

    public synchronized void disconnectBinderPoolService(Context context) {
        mContext = context.getApplicationContext();
        if (mBinderPoolConnection != null) {
            mContext.unbindService(mBinderPoolConnection);
        }
    }

    public IBinder queryBinder(int binderCode) {
        IBinder binder = null;
        try {
            if (mBinderPool != null) {
                binder = mBinderPool.queryBinder(binderCode);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "query binder error " + e.getClass() + " " + e.getMessage());
        }
        return binder;
    }


    private ServiceConnection mBinderPoolConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                mBinderPool.asBinder().linkToDeath(mBinderPoolDeathRecipient, 0);
            } catch (RemoteException e) {
                Log.e(TAG, "on service connected error " + e.getClass() + " " + e.getMessage());
            }
            mBindPoolLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            mBinderPool.asBinder().unlinkToDeath(mBinderPoolDeathRecipient, 0);
            mBinderPool = null;
            connectBinderPoolService(mContext);
        }
    };

}
