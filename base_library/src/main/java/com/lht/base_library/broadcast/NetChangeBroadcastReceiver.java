package com.lht.base_library.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.lht.base_library.utils.NetUtil;

public class NetChangeBroadcastReceiver extends BroadcastReceiver {

    private NetChangeStateListener listener;

    public NetChangeBroadcastReceiver(NetChangeStateListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            if (listener != null) {
                listener.onNetStateChange(NetUtil.getNetWorkState(context));
            }
        }
    }

}
