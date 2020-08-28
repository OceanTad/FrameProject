package com.lht.base_library.base;

import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

public class BaseToast {

    private static volatile BaseToast instance;

    private BaseToast() {

    }

    public static BaseToast getInstance() {
        if (instance == null) {
            synchronized (BaseToast.class) {
                if (instance == null) {
                    instance = new BaseToast();
                }
            }
        }
        return instance;
    }

    private Toast toast;

    public void showText(String str, int duration, int gravity, int xOffset, int yOffset) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.getInstance(), "", duration);
        } else {
            toast.cancel();
            toast = Toast.makeText(BaseApplication.getInstance(), "", duration);
        }
        toast.setText(str);
        toast.setDuration(duration);
        toast.setGravity(gravity, xOffset, yOffset);
        toast.show();
    }

    public void showView(View view, int duration, int gravity, int xOffset, int yOffset) {
        if (view == null) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.getInstance(), "", duration);
        } else {
            toast.cancel();
            toast = Toast.makeText(BaseApplication.getInstance(), "", duration);
        }
        toast.setView(view);
        toast.setDuration(duration);
        toast.setGravity(gravity, xOffset, yOffset);
        toast.show();
    }

    public void cancel() {
        if (toast != null) {
            toast.cancel();
        }
    }

}
