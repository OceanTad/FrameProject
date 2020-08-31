package com.lht.base_library.base;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lht.base_library.utils.DialogUtil;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView, EasyPermissions.PermissionCallbacks {

    private P presenter;
    private SparseArray<View> mViews = new SparseArray<>();
    private View.OnClickListener viewClickListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        presenter = createPresenter();
        if (presenter != null) {
            presenter.attachView(this);
            getLifecycle().addObserver(presenter);
        }
        loadView();
    }

    @Override
    protected void onDestroy() {
        recycle();
        dismissLoading();
        mViews.clear();
        mViews = null;
        super.onDestroy();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            resumeAppSetting();
        }
    }

    public P getPresenter() {
        return presenter;
    }

    public <T> T findView(int viewId, Class<T> classType) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = findViewById(viewId);
            mViews.append(viewId, view);
        }
        if (classType.isInstance(view)) {
            return classType.cast(view);
        }
        return null;
    }

    public void addViewClick(int viewId) {
        if (viewClickListener == null) {
            viewClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onViewClick(v);
                }
            };
        }
        View view = mViews.get(viewId);
        if (view == null) {
            view = findViewById(viewId);
            mViews.append(viewId, view);
        }
        view.setOnClickListener(viewClickListener);
    }

    private boolean isShow = false;

    @Override
    public void showLoading() {
        if (isShow) {
            return;
        }
        DialogUtil.showLoading(this);
        isShow = true;
    }

    @Override
    public void dismissLoading() {
        if (!isShow) {
            return;
        }
        DialogUtil.dismissLoading(this);
        isShow = false;
    }

    @Override
    public void showError(boolean isNoData) {
    }

    @Override
    public void dismissError() {
    }

    protected void onViewClick(View view) {
    }

    protected abstract void loadView();

    protected abstract void recycle();

    protected abstract P createPresenter();

    protected void resumeAppSetting() {
    }

    protected void onResult(int requestCode, int resultCode, @Nullable Intent data) {
    }

}
