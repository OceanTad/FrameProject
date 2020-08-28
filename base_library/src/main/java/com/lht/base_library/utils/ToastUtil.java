package com.lht.base_library.utils;

import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.lht.base_library.base.BaseApplication;
import com.lht.base_library.base.BaseToast;

public class ToastUtil {

    public static void showShort(int resId) {
        String str = BaseApplication.getInstance().getResources().getString(resId);
        BaseToast.getInstance().showText(str, Toast.LENGTH_SHORT, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, (int) ScreenUtil.dp2px(50f));
    }

    public static void showShort(String str) {
        BaseToast.getInstance().showText(str, Toast.LENGTH_SHORT, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, (int) ScreenUtil.dp2px(50f));
    }

    public static void showShort(int resId, int gravity, int xOffSet, int yOffSet) {
        String str = BaseApplication.getInstance().getResources().getString(resId);
        BaseToast.getInstance().showText(str, Toast.LENGTH_SHORT, gravity, xOffSet, yOffSet);
    }

    public static void showShort(String str, int gravity, int xOffSet, int yOffSet) {
        BaseToast.getInstance().showText(str, Toast.LENGTH_SHORT, gravity, xOffSet, yOffSet);
    }

    public static void showShort(View view) {
        BaseToast.getInstance().showView(view, Toast.LENGTH_SHORT, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, (int) ScreenUtil.dp2px(50f));
    }

    public static void showShort(View view, int gravity, int xOffSet, int yOffSet) {
        BaseToast.getInstance().showView(view, Toast.LENGTH_SHORT, gravity, xOffSet, yOffSet);
    }

    public static void showLong(int resId) {
        String str = BaseApplication.getInstance().getResources().getString(resId);
        BaseToast.getInstance().showText(str, Toast.LENGTH_LONG, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, (int) ScreenUtil.dp2px(50f));
    }

    public static void showLong(String str) {
        BaseToast.getInstance().showText(str, Toast.LENGTH_LONG, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, (int) ScreenUtil.dp2px(50f));
    }

    public static void showLong(int resId, int gravity, int xOffSet, int yOffSet) {
        String str = BaseApplication.getInstance().getResources().getString(resId);
        BaseToast.getInstance().showText(str, Toast.LENGTH_LONG, gravity, xOffSet, yOffSet);
    }

    public static void showLong(String str, int gravity, int xOffSet, int yOffSet) {
        BaseToast.getInstance().showText(str, Toast.LENGTH_LONG, gravity, xOffSet, yOffSet);
    }

    public static void showLong(View view) {
        BaseToast.getInstance().showView(view, Toast.LENGTH_LONG, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, (int) ScreenUtil.dp2px(50f));
    }

    public static void showLong(View view, int gravity, int xOffSet, int yOffSet) {
        BaseToast.getInstance().showView(view, Toast.LENGTH_LONG, gravity, xOffSet, yOffSet);
    }

    public static void show(int resId, int duration) {
        String str = BaseApplication.getInstance().getResources().getString(resId);
        BaseToast.getInstance().showText(str, duration, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, (int) ScreenUtil.dp2px(50f));
    }

    public static void show(int resId, int duration, int gravity, int xOffSet, int yOffSet) {
        String str = BaseApplication.getInstance().getResources().getString(resId);
        BaseToast.getInstance().showText(str, duration, gravity, xOffSet, yOffSet);
    }

    public static void show(String str, int duration) {
        BaseToast.getInstance().showText(str, duration, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, (int) ScreenUtil.dp2px(50f));
    }

    public static void show(String str, int duration, int gravity, int xOffSet, int yOffSet) {
        BaseToast.getInstance().showText(str, duration, gravity, xOffSet, yOffSet);
    }

    public static void show(View view, int duration) {
        BaseToast.getInstance().showView(view, duration, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, (int) ScreenUtil.dp2px(50f));
    }

    public static void show(View view, int duration, int gravity, int xOffSet, int yOffSet) {
        BaseToast.getInstance().showView(view, duration, gravity, xOffSet, yOffSet);
    }

    public static void cancel() {
        BaseToast.getInstance().cancel();
    }

}
