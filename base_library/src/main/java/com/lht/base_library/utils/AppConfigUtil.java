package com.lht.base_library.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.UUID;

public class AppConfigUtil {

    public static String getMetaData(Context context, String key) {
        String value = "";
        if (context != null && !TextUtils.isEmpty(key)) {
            try {
                ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            } catch (Exception e) {
                LogUtil.e("getMetaData error " + e.getClass() + " " + e.getMessage());
            }
        }
        return value;
    }

    @SuppressLint("MissingPermission")
    public static String getUuid(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        return uniqueId;
    }

    public static int getApkVersionCode(Context context) {
        int versionCode = 0;
        try { //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.e("getApkVersionCode error " + e.getClass() + " " + e.getMessage());
        }
        return versionCode;
    }

    public static String getApkVersionName(Context context) {
        String versionName = "";
        try { //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.e("getApkVersionName error " + e.getClass() + " " + e.getMessage());
        }
        return versionName;
    }

    public static boolean isDebug(Context context) {
        return context != null && context.getApplicationInfo() != null && (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

}
