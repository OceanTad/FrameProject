package com.lht.base_library.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements BaseView, EasyPermissions.PermissionCallbacks {

    private BaseViewHolder viewHolder;
    private View.OnClickListener onClickListener;

    private boolean isVisible = false;
    private boolean isPrepared = false;

    private P presenter;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            if (isPrepared) {
                loadData();
                isPrepared = false;
                isVisible = false;
            }
        } else {
            isVisible = false;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isPrepared = false;
        isVisible = false;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPrepared = true;
        isVisible = getUserVisibleHint();
        if (isVisible) {
            loadData();
            isPrepared = false;
            isVisible = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (viewHolder == null) {
            viewHolder = new BaseViewHolder(inflater.inflate(loadView(), container));
        }
        if (viewHolder.getRootView() != null && viewHolder.getRootView().getParent() != null) {
            ((ViewGroup) viewHolder.getRootView().getParent()).removeView(viewHolder.getRootView());
        }
        presenter = createPresenter();
        if (presenter != null) {
            presenter.attachView(this);
            getLifecycle().addObserver(presenter);
        }
        initView();
        return viewHolder.getRootView();
    }

    @Override
    public void showLoading() {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showLoading();
        }
    }

    @Override
    public void dismissLoading() {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).dismissLoading();
        }
    }

    @Override
    public void showError(boolean isNoData) {

    }

    @Override
    public void dismissError() {

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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            resumeAppSetting();
        } else {
            onResult(requestCode, resultCode, data);
        }
    }

    public <T extends View> T findView(int viewId, Class<T> classType) {
        if (viewHolder != null) {
            viewHolder.findView(viewId, classType);
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
    }

    protected void onViewClick(View view) {
    }

    public abstract String getTAG();

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
