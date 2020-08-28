package com.lht.base_library.base;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lht.base_library.utils.DialogUtil;

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {

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

}
