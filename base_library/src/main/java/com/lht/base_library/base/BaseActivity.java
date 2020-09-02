package com.lht.base_library.base;

import android.content.Intent;
import android.os.Bundle;
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
    private BaseViewHolder viewHolder;
    private View.OnClickListener onClickListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        presenter = createPresenter();
        if (presenter != null) {
            presenter.attachView(this);
            getLifecycle().addObserver(presenter);
        }
        setContentView(loadView());
        viewHolder = new BaseViewHolder(getWindow().getDecorView());
        getLifecycle().addObserver(viewHolder);
        initView();
        loadData();
    }

    @Override
    protected void onDestroy() {
        recycle();
        dismissLoading();
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
        } else {
            onResult(requestCode, resultCode, data);
        }
    }

    public P getPresenter() {
        return presenter;
    }

    public <T extends View> T findView(int viewId, Class<T> classType) {
        if (viewHolder != null) {
            return viewHolder.findView(viewId, classType);
        }
        return null;
    }

    public void addViewClick(int... viewIds) {
        if (onClickListener == null) {
            onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onViewClick(v);
                }
            };
        }
        if (viewIds != null && viewIds.length > 0) {
            for (int viewId : viewIds) {
                if (viewHolder != null) {
                    View view = viewHolder.findView(viewId);
                    if (view != null) {
                        view.setOnClickListener(onClickListener);
                    }
                }
            }
        }
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

    protected abstract int loadView();

    protected void initView() {
    }

    protected void loadData() {
    }

    protected abstract void recycle();

    protected abstract P createPresenter();

    protected void resumeAppSetting() {
    }

    protected void onResult(int requestCode, int resultCode, @Nullable Intent data) {
    }

}
