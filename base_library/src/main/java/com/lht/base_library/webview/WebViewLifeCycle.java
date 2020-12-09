package com.lht.base_library.webview;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import java.lang.ref.SoftReference;

public class WebViewLifeCycle implements LifecycleObserver {

    private SoftReference<WebView> webView;

    public WebViewLifeCycle(WebView webView) {
        this.webView = new SoftReference<>(webView);
    }

    public void init() {
        if (webView != null && webView.get() != null) {
            WebSettings webSettings = webView.get().getSettings();
            if (webSettings != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                }
                webSettings.setJavaScriptEnabled(true);
                webSettings.setUseWideViewPort(true);
                webSettings.setLoadWithOverviewMode(true);
                webSettings.setSupportZoom(true);
                webSettings.setBuiltInZoomControls(true);
                webSettings.setDisplayZoomControls(false);
                webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
                webSettings.setAllowFileAccess(true);
                webSettings.setAllowContentAccess(true);
                webSettings.setAllowUniversalAccessFromFileURLs(false);
                webSettings.setAllowFileAccessFromFileURLs(false);
                webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
                webSettings.setLoadsImagesAutomatically(true);
                webSettings.setDefaultTextEncodingName("utf-8");
                webSettings.setDomStorageEnabled(true);
                webSettings.setDatabaseEnabled(true);
                webSettings.setAppCacheEnabled(true);
                webSettings.setMediaPlaybackRequiresUserGesture(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
                } else {
                    webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
                }
            }
            webView.get().setSaveEnabled(false);
            if (webView.get().isHardwareAccelerated()) {
                webView.get().setLayerType(View.LAYER_TYPE_HARDWARE, null);
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        if (webView != null && webView.get() != null) {
            webView.get().onResume();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        if (webView != null && webView.get() != null) {
            webView.get().onPause();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestory() {
        if (webView != null && webView.get() != null) {
            webView.get().stopLoading();
            if (webView.get().getHandler() != null) {
                webView.get().getHandler().removeCallbacksAndMessages(null);
            }
            webView.get().removeAllViews();
            ViewGroup mViewGroup = null;
            if ((mViewGroup = ((ViewGroup) webView.get().getParent())) != null) {
                mViewGroup.removeView(webView.get());
            }
            webView.get().setWebChromeClient(null);
            webView.get().setWebViewClient(null);
            webView.get().setTag(null);
            webView.get().clearHistory();
            webView.get().destroy();
            webView.clear();
            webView = null;
        }
    }


}
