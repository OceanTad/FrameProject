package com.lht.base_library.broadcast;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.ArrayMap;

/**
 * 网络实时监听
 * state：1 wifi 2 mobile -1 无网络
 * 备注：动态广播方式监听 需注册与解注册
 */
public class NetChangeManager {

    private static volatile NetChangeManager instance;

    private NetChangeManager() {

    }

    public static NetChangeManager getInstance() {
        if (instance == null) {
            synchronized (NetChangeManager.class) {
                if (instance == null) {
                    instance = new NetChangeManager();
                }
            }
        }
        return instance;
    }

    private ArrayMap<Class, NetChangeBroadcastReceiver> receivers = new ArrayMap<>();

    public void registerNetChangeReceiver(Context context, Class classType, NetChangeStateListener listener) {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        NetChangeBroadcastReceiver receiver = new NetChangeBroadcastReceiver(listener);
        if (receivers.containsKey(classType)) {
            receivers.remove(classType);
        }
        receivers.put(classType, receiver);
        context.registerReceiver(receiver, intentFilter);
    }

    public void unregisterForBackGroundReceiver(Context context, Class classType) {
        if (receivers.containsKey(classType)) {
            NetChangeBroadcastReceiver receiver = receivers.get(classType);
            context.unregisterReceiver(receiver);
            receivers.remove(classType);
        }
    }

    public void clearMap() {
        if (!receivers.isEmpty()) {
            receivers.clear();
        }
    }

}
