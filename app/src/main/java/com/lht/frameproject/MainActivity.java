package com.lht.frameproject;

import android.Manifest;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;

import com.lht.base_library.base.BaseActivity;
import com.lht.base_library.broadcast.ForBackGroundListener;
import com.lht.base_library.broadcast.ForBackGroundManager;
import com.lht.base_library.broadcast.NetChangeManager;
import com.lht.base_library.broadcast.NetChangeStateListener;
import com.lht.base_library.rxbus.RxBus;
import com.lht.base_library.utils.LogUtil;
import com.rxjava.rxlife.RxLife;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class MainActivity extends BaseActivity<MainPresenterImp> implements MainContract.MainView {

    @Override
    protected void recycle() {
        ForBackGroundManager.getInstance().unregisterForBackGroundReceiver(this, getClass());
        NetChangeManager.getInstance().unregisterForBackGroundReceiver(this, getClass());
    }

    @Override
    protected MainPresenterImp createPresenter() {
        return new MainPresenterImp();
    }

    @Override
    public void onViewClick(View view) {
        super.onViewClick(view);
        switch (view.getId()) {
            case R.id.ceshi:
                startActivity(new Intent(this, SecondActivity.class));
                break;
            case R.id.ceshiyixia:
                RxBus.getInstance().post("1000");
                break;
        }
    }

    @Override
    protected int loadView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        findView(R.id.ceshi, TextView.class).setText("ce shi yi xia");
        addViewClick(R.id.ceshi, R.id.ceshiyixia);
        registerListener();
        registerRxbus();
//        countDown();
    }

    @Override
    protected void loadData() {
        super.loadData();
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_PHONE_STATE)) {
            if (getPresenter() != null) {
                getPresenter().login(this);
            }
        } else {
            EasyPermissions.requestPermissions(this, "adafaf", 0, Manifest.permission.READ_PHONE_STATE);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
        if (requestCode == 0) {
            if (getPresenter() != null) {
                getPresenter().login(this);
            }
        } else if (requestCode == 1) {
            if (getPresenter() != null) {
                getPresenter().download();
            }
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        super.onPermissionsDenied(requestCode, perms);
        LogUtil.e("quan xian ju jue");
    }

    private void registerListener() {
        ForBackGroundManager.getInstance().registerForBackGroundReceiver(this, getClass(), new ForBackGroundListener() {
            @Override
            public void onFroGround() {
                LogUtil.e("onFroGround");
            }

            @Override
            public void onBackGround() {
                LogUtil.e("onBackGround");
            }
        });

        NetChangeManager.getInstance().registerNetChangeReceiver(this, getClass(), new NetChangeStateListener() {
            @Override
            public void onNetStateChange(int state) {
                LogUtil.e("onNetStateChange:" + state);
            }
        });
    }

    private void registerRxbus() {
        RxBus.getInstance().registerEvent(this, Lifecycle.Event.ON_DESTROY, String.class, new Consumer<String>() {
            @Override
            public void accept(String event) throws Exception {
                if (EasyPermissions.hasPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})) {
                    if (getPresenter() != null) {
                        getPresenter().download();
                    }
                } else {
                    EasyPermissions.requestPermissions(new PermissionRequest.Builder(MainActivity.this, 1, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
                            .setRationale("需要权限")
                            .setPositiveButtonText("确定")
                            .setNegativeButtonText("取消")
                            .setTheme(R.style.Theme_AppCompat_Dialog)
                            .build());
                }
            }
        });
    }

    private void countDown() {
        Observable.intervalRange(0, 60, 0, 1000, TimeUnit.MILLISECONDS)
                .lift(RxLife.lift(this, Lifecycle.Event.ON_DESTROY))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtil.e("倒计时------" + aLong);
                    }
                });
    }

    @Override
    public void onSuccess() {
        LogUtil.e("main onSuccess");
    }

    @Override
    public void onFail() {
        LogUtil.e("main onFail");
    }

}
