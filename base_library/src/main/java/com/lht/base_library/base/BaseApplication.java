package com.lht.base_library.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.text.TextUtils;
import android.webkit.WebView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lht.base_library.R;
import com.lht.base_library.broadcast.ForBackGroundBroadcastManager;
import com.lht.base_library.utils.AppConfigUtil;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.AutoSize;

public class BaseApplication extends Application {

    private static BaseApplication instance;
    private static int count = 0;
    public static List<Activity> activityList;
    private List<ProviderApplication> applicationList;

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        
        initWebView();
        
        if (isMainProcess()) {

            instance = this;

            registerActivityLife();

            initARouter();
            loadApplication();
            init();
            delayInit();

        }
    }

    private void initWebView(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.P) {
            String processName = getProcessName();
            if(getApplicationContext().getPackageName().equals(processName)) {
                WebView.setDataDirectorySuffix(processName);
            }
        }
    }

    protected boolean isMainProcess() {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService
                (Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
                break;
            }
        }
        return getApplicationContext().getPackageName().equals(processName);
    }

    //ARouter初始化
    private void initARouter() {
        if (AppConfigUtil.isDebug(this)) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
    }

    private void init() {

        if (applicationList != null) {
            for (ProviderApplication application : applicationList) {
                if (application != null) {
                    application.mainInit();
                }
            }
        }

    }

    private void delayInit() {

        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                AutoSize.initCompatMultiProcess(getApplicationContext());
                if (applicationList != null) {
                    for (ProviderApplication application : applicationList) {
                        if (application != null) {
                            application.delayInit();
                        }
                    }
                }
                return false;
            }
        });

    }

    private void loadApplication() {
        applicationList = new ArrayList<>();
        String[] implementations = getResources().getString(R.string.build_include).split(",");
        for (String module : implementations) {
            if (!TextUtils.isEmpty(module)) {
                applicationList.add((ProviderApplication) ARouter.getInstance().build("/" + module + "/" + module).navigation());
            }
        }
    }

    private void registerActivityLife() {
        activityList = new ArrayList<>();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activityList.add(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                count++;
                if (count == 1) {
                    ForBackGroundBroadcastManager.sendForGroundBroadcast(getApplicationContext());
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                count--;
                if (count == 0) {
                    ForBackGroundBroadcastManager.sendBackGroundBroadcast(getApplicationContext());
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                activityList.remove(activity);
            }
        });
    }

    public static void clearActivity() {
        for (int i = 0; i < activityList.size(); i++) {
            activityList.get(i).finish();
        }
        activityList.clear();
    }

    public static Activity getTopActivity() {
        if (activityList != null && activityList.size() > 0) {
            for (int i = activityList.size() - 1; i >= 0; i--) {
                return activityList.get(i);
            }
        }
        return null;
    }

    public static BaseActivity getTopBaseActivity() {
        if (activityList != null && activityList.size() > 0) {
            for (int i = activityList.size() - 1; i >= 0; i--) {
                if (activityList.get(i) instanceof BaseActivity) {
                    return (BaseActivity) activityList.get(i);
                }
            }
        }
        return null;
    }

    @Override
    public void onTerminate() {
        ARouter.getInstance().destroy();
        super.onTerminate();
    }

}
