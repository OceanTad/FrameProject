package com.lht.base_library.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;

import com.lht.base_library.utils.Util;

import java.lang.ref.SoftReference;

public class CustomWebViewClient extends WebViewClient {

    private SoftReference<Context> context;
    private IWebViewClient listener;
    private String platformApiUrl = "";

    private WebBinderManager manager;

    public CustomWebViewClient(Context context) {
        this.context = new SoftReference<>(context);
    }

    public void setListener(IWebViewClient listener) {
        this.listener = listener;
    }

    public void setManager(WebBinderManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        String actionName = "";
        String actionParams = "";
        if (url.startsWith("tel:")) {
            actionName = ActionName.CALL_PHONE;
            actionParams = url;
        }
        if (url.startsWith("sms:")) {
            actionName = ActionName.SMS;
            actionParams = url;
        }
        if (url.startsWith("mailto:")) {
            actionName = ActionName.MAIL;
            actionParams = url;
        }
        if (url.startsWith("geo:0,0?q=")) {
            actionName = ActionName.GEO;
            actionParams = url;
        }
        if (url.startsWith("weixin://wap/pay?") || url.startsWith("https://wx.tenpay.com")) {
            if (Util.isWxInstall(context.get())) {
                if (url.startsWith("weixin://wap/pay?")) {
                    actionName = ActionName.WX_APP_PAY;
                }
                if (url.startsWith("https://wx.tenpay.com")) {
                    actionName = ActionName.WX_WEB_PAY;
                }
                actionParams = url;
            } else {
                actionName = ActionName.WX_UNINSTALL;
            }
        }
        if (url.startsWith("alipays://platformapi/startApp") || url.startsWith("https://mclient.alipay.com/h5Continue.htm?")) {
            if (Util.isAliPayInstall(context.get())) {
                if (url.startsWith("alipays://platformapi/startApp")) {
                    platformApiUrl = url;
                    actionName = ActionName.ALI_WAKE_UP;
                    actionParams = url;
                }
                if (url.startsWith("https://mclient.alipay.com/h5Continue.htm?")) {
                    actionName = ActionName.ALI_APP_PAY;
                    actionParams = platformApiUrl;
                }
            } else {
                actionName = ActionName.ALI_UNINSTALL;
            }
        }
        if (!TextUtils.isEmpty(actionName)) {
            if (actionName.equals(ActionName.ALI_UNINSTALL) || actionName.equals(ActionName.WX_UNINSTALL)) {
                return false;
            } else {
                manager.post(actionName, actionParams);
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return shouldOverrideUrlLoading(view, request.getUrl().toString());
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (listener != null) {
            listener.loadFinished(url);
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (listener != null) {
            listener.loadStart(url);
        }
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        view.loadUrl("file:///android_asset/network-error.html");
    }

}
