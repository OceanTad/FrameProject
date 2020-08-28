package com.lht.base_library.broadcast;

import android.content.Context;
import android.content.IntentFilter;
import android.util.ArrayMap;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * 前后台实时监听
 * 备注：动态广播方式监听 需注册与解注册
 */
public class ForBackGroundManager {

    private static volatile ForBackGroundManager instance;

    private ForBackGroundManager() {

    }

    public static ForBackGroundManager getInstance() {
        if (instance == null) {
            synchronized (ForBackGroundManager.class) {
                if (instance == null) {
                    instance = new ForBackGroundManager();
                }
            }
        }
        return instance;
    }

    private ArrayMap<Class, ForBackGroundBroadcastReceiver> receivers = new ArrayMap<>();

    public void registerForBackGroundReceiver(Context context, Class classType, ForBackGroundListener listener) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ForBackGroundBroadcastManager.FOR_GROUND);
        intentFilter.addAction(ForBackGroundBroadcastManager.BACK_GROUND);
        ForBackGroundBroadcastReceiver receiver = new ForBackGroundBroadcastReceiver(listener);
        if (receivers.containsKey(classType)) {
            receivers.remove(classType);
        }
        receivers.put(classType, receiver);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter);
    }

    public void unregisterForBackGroundReceiver(Context context, Class classType) {
        if (receivers.containsKey(classType)) {
            ForBackGroundBroadcastReceiver receiver = receivers.get(classType);
            LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
            receivers.remove(classType);
        }
    }

    public void clearMap() {
        if (!receivers.isEmpty()) {
            receivers.clear();
        }
    }

}
