package com.lht.base_library.broadcast;

import android.content.Context;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class ForBackGroundBroadcastManager {

    public static final String FOR_GROUND = "com.activity.action.FOR_GROUND";
    public static final String BACK_GROUND = "com.activity.action.BACK_GROUND";

    public static void sendForGroundBroadcast(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(FOR_GROUND));
    }

    public static void sendBackGroundBroadcast(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(BACK_GROUND));
    }

}
