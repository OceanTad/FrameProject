package com.lht.base_library.utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.lht.base_library.base.BaseLoadingDialog;

public class DialogUtil {

    public static void showLoading(AppCompatActivity context) {
        Fragment fragment = context.getSupportFragmentManager().findFragmentByTag("loading");
        if (fragment instanceof BaseLoadingDialog) {
            ((BaseLoadingDialog) fragment).dismiss();
        }
        new BaseLoadingDialog()
                .setTouchCancelable(false)
                .show(context.getSupportFragmentManager(), "loading");
    }

    public static void dismissLoading(AppCompatActivity context) {
        Fragment fragment = context.getSupportFragmentManager().findFragmentByTag("loading");
        if (fragment instanceof BaseLoadingDialog) {
            ((BaseLoadingDialog) fragment).dismiss();
        }
    }

}
