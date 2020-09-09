package com.lht.base_library.webview;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gyf.immersionbar.ImmersionBar;
import com.lht.base_library.R;
import com.lht.base_library.binder.BinderPool;

import java.util.List;
import java.util.concurrent.Executors;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class CustomWebActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private CustomWebFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).init();
        setContentView(R.layout.activity_custom_web);

        createFragment();

        Executors.newCachedThreadPool().submit(new Runnable() {
            @Override
            public void run() {
                BinderPool.getInstance().connectBinderPoolService(getApplicationContext());
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (fragment != null) {
            if (fragment.onKeyBack(keyCode)) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            resumeAppSetting();
        } else {
            onResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    protected void resumeAppSetting() {
    }

    protected void onResult(int requestCode, int resultCode, @Nullable Intent data) {
    }

    private void createFragment() {
        boolean html = false;
        String heard = "";
        boolean progress = true;
        String url = "";
        if (getIntent() != null) {
            html = getIntent().getBooleanExtra(CustomWebFragment.HTML, false);
            progress = getIntent().getBooleanExtra(CustomWebFragment.SHOW_PROGRESS, true);
            heard = getIntent().getStringExtra(CustomWebFragment.PAY_HEARD);
            if (TextUtils.isEmpty(heard)) {
                heard = "";
            }
            url = getIntent().getStringExtra(CustomWebFragment.WEB_VIEW_URL);
            if (TextUtils.isEmpty(url)) {
                url = "file:///android_asset/network-error.html";
            }
        }
        fragment = new CustomWebFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(CustomWebFragment.HTML, html);
        bundle.putString(CustomWebFragment.PAY_HEARD, heard);
        bundle.putBoolean(CustomWebFragment.SHOW_PROGRESS, progress);
        bundle.putString(CustomWebFragment.WEB_VIEW_URL, url);
        fragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_web_view, fragment).commitAllowingStateLoss();
    }

}
