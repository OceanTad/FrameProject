package com.lht.base_library.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ForBackGroundBroadcastReceiver extends BroadcastReceiver {

    private ForBackGroundListener listener;

    public ForBackGroundBroadcastReceiver(ForBackGroundListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && ForBackGroundBroadcastManager.FOR_GROUND.equals(intent.getAction())) {
            if (listener != null) {
                listener.onFroGround();
            }
        }
        if (intent != null && ForBackGroundBroadcastManager.BACK_GROUND.equals(intent.getAction())) {
            if (listener != null) {
                listener.onBackGround();
            }
        }
    }

}
