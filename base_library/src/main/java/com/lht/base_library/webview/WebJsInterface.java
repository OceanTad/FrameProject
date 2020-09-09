package com.lht.base_library.webview;

import android.annotation.SuppressLint;
import android.webkit.JavascriptInterface;

public class WebJsInterface {

    private WebBinderManager manager;

    public WebJsInterface(WebBinderManager manager) {
        this.manager = manager;
    }

    @SuppressLint("JavascriptInterface")
    @JavascriptInterface
    public void post(String actionName, String actionParams) {
        if (manager != null) {
            manager.post(actionName, actionParams);
        }
    }

    @SuppressLint("JavascriptInterface")
    @JavascriptInterface
    public void post(String actionName) {
        if (manager != null) {
            manager.post(actionName, "");
        }
    }

}
