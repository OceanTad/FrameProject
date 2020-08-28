package com.lht.base_library.base;

import android.content.Context;
import android.content.SharedPreferences;

public class BaseSp {

    private static volatile BaseSp instance;

    private BaseSp() {

    }

    public static BaseSp getInstance() {
        if (instance == null) {
            synchronized (BaseSp.class) {
                if (instance == null) {
                    instance = new BaseSp();
                }
            }
        }
        return instance;
    }

    private SharedPreferences sp;
    private static final String tag = "share_preference_cache";

    public SharedPreferences getSp() {
        if (sp == null) {
            sp = BaseApplication.getInstance().getSharedPreferences(tag, Context.MODE_PRIVATE);
        }
        return sp;
    }

}
