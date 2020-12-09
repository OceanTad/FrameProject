package com.lht.base_library.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.gyf.immersionbar.components.SimpleImmersionOwner;
import com.gyf.immersionbar.components.SimpleImmersionProxy;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements BaseView, EasyPermissions.PermissionCallbacks, SimpleImmersionOwner {

    private SimpleImmersionProxy mImmersion;

    private BaseViewHolder viewHolder;
    private View.OnClickListener onClickListener;

    private boolean isVisible = false;
    private boolean isPrepared = false;

    private P presenter;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (immersionBarEnabled()) {
            if (mImmersion == null) {
                mImmersion = new SimpleImmersionProxy(this);
            }
            mImmersion.setUserVisibleHint(isVisibleToUser);
        }
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (immersionBarEnabled()) {
            if (mImmersion == null) {
                mImmersion = new SimpleImmersionProxy(this);
            }
            mImmersion.onActivityCreated(savedInstanceState);
        }
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
            viewHolder = new BaseViewHolder(inflater.inflate(loadView(), null));
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
    public void onDestroy() {
        super.onDestroy();
        isPrepared = false;
        if (mImmersion != null) {
            mImmersion.onDestroy();
        }
        recycle();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (immersionBarEnabled()) {
            if (mImmersion == null) {
                mImmersion = new SimpleImmersionProxy(this);
            }
            mImmersion.onHiddenChanged(hidden);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (immersionBarEnabled()) {
            if (mImmersion == null) {
                mImmersion = new SimpleImmersionProxy(this);
            }
            mImmersion.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public Context getCurrentContext() {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            return ((BaseActivity) getActivity()).getCurrentContext();
        }
        return null;
    }

    @Override
    public LifecycleOwner getLifecycleOwner() {
        return getViewLifecycleOwner();
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
            new AppSettingsDialog
                    .Builder(this)
                    .setTitle("提示")
                    .setRationale("应用需要此权限，否则无法正常使用，是否打开设置")
                    .setPositiveButton("是")
                    .setNegativeButton("否")
                    .build()
                    .show();
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

    @Override
    public void initImmersionBar() {

    }

    @Override
    public boolean immersionBarEnabled() {
        return false;
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
