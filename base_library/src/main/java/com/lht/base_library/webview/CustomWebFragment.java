package com.lht.base_library.webview;

import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lht.base_library.R;
import com.lht.base_library.utils.NetUtil;

import java.util.HashMap;

public class CustomWebFragment extends Fragment implements IWebViewClient {

    public static final String TAG = "CustomWebFragment";

    public static final String WEB_VIEW_URL = "WEB_VIEW_URL";
    public static final String SHOW_PROGRESS = "SHOW_PROGRESS";
    public static final String HTML = "HTML";
    public static final String PAY_HEARD = "PAY_HEARD";

    private WebView webView;
    private WebViewLifeCycle webViewLifeCycle;

    private ProgressBar progressBar;

    private boolean isVideoFull = false;
    private CustomWebViewChromeClient chromeClient;
    private CustomWebViewClient client;
    private OrientationEventListener orientationEventListener;
    private WebBinderManager binderManager;

    private String webUrl = "";
    private boolean isShowProgress = true;
    private String payHeards = "";
    private boolean isHtml = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            webUrl = bundle.getString(WEB_VIEW_URL);
            isShowProgress = bundle.getBoolean(SHOW_PROGRESS, true);
            isHtml = bundle.getBoolean(HTML, false);
            payHeards = bundle.getString(PAY_HEARD);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_web, container, false);
        webView = view.findViewById(R.id.web_view);
        progressBar = view.findViewById(R.id.pd_web_view);
        progressBar.setProgress(0);
        progressBar.setVisibility(isShowProgress ? View.VISIBLE : View.GONE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        StatusBarUtil.setFitSystemStatusBar(getActivity(), progressBar);
        initOrientationListener();
        initWebView();
        if (isHtml) {
            webView.loadData(webUrl, "text/html;charset=utf-8", "utf-8");
        } else {
            webView.loadUrl(webUrl);
        }
    }

    @Override
    public void onDestroy() {
        if (orientationEventListener != null) {
            orientationEventListener.disable();
        }
        super.onDestroy();
    }

    private void initOrientationListener() {
        orientationEventListener = new OrientationEventListener(getActivity(), SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (getActivity() != null) {
                    if (isVideoFull) {
                        if (orientation < 280 && orientation > 260) {
                            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        }
                        if (orientation < 100 && orientation > 80) {
                            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                        }
                    }
                }

            }
        };
        orientationEventListener.enable();
    }

    private void initWebView() {
        binderManager = new WebBinderManager(getActivity());
        binderManager.init();
        binderManager.setCallBack(this);

        webViewLifeCycle = new WebViewLifeCycle(webView);
        webViewLifeCycle.init();
        chromeClient = new CustomWebViewChromeClient(this);
        webView.setWebChromeClient(chromeClient);

        client = new CustomWebViewClient(getActivity().getApplicationContext());
        client.setManager(binderManager);
        client.setListener(this);
        webView.setWebViewClient(client);
        webView.addJavascriptInterface(new WebJsInterface(binderManager), "jsCallAndroid");
    }

    private void callJs(String params) {
        if (webView != null) {
            if (Build.VERSION.SDK_INT < 18) {
                webView.loadUrl("javascript:androidCallJs(" + params + ")");
            } else {
                webView.evaluateJavascript("javascript:androidCallJs(" + params + ")", null);
            }

        }
    }

    public boolean onKeyBack(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            if (isVideoFull && chromeClient != null) {
                chromeClient.onHideCustomView();
                return true;
            }
            WebBackForwardList history = webView.copyBackForwardList();
            int index = history.getCurrentIndex();
            if (history.getItemAtIndex(index - 1) != null) {
                String url = history.getItemAtIndex(index - 1).getUrl();
                if ("about:blank".equals(url) || "file:///android_asset/network-error.html".equals(url)) {
                    if (index > 2) {
                        webView.goBackOrForward(index - 2);
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            //解决无网不返回问题
            if (!TextUtils.isEmpty(webView.getUrl().toString()) && (webView.getUrl().toString().equals("about:blank") || webView.getUrl().toString().equals("file:///android_asset/network-error.html"))) {
                return false;//退出H5界面
            } else {
                webView.goBack();//返回上个页面
                return true;
            }
        }
        return false;//退出H5界面
    }

    public void updateProgress(int progress) {
        if (isShowProgress && progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setProgress(progress);
        }
    }

    public WebView getWebView() {
        return webView;
    }

    public void setVideoFull(boolean isVideoFull) {
        this.isVideoFull = isVideoFull;
    }

    public String getTAG() {
        return TAG;
    }

    @Override
    public void loadFinished(String url) {
        if (isShowProgress && progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadStart(String url) {
        if (!url.equals("file:///android_asset/network-error.html") && isShowProgress && progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void webViewLoad(String action, String actionParams) {
        if (NetUtil.getNetWorkState(getActivity()) != NetUtil.NET_STATE_NO) {
            if (action.equals(ActionName.WX_WEB_PAY)) {
                if (19 == Build.VERSION.SDK_INT) {
                    if (webView != null) {
                        webView.loadDataWithBaseURL("http://winsion.net",
                                "<script>window.location.href=" + actionParams + ";</script>",
                                "text/html",
                                "utf-8",
                                null);
                    }
                } else {
                    if (webView != null) {
                        HashMap<String, String> extraHeaders = new HashMap<>();
                        extraHeaders.put("Referer", payHeards);
                        webView.loadUrl(actionParams, extraHeaders);
                    }
                }
            }
            if (action.equals(ActionName.RETRY_LOAD)) {
                if (!isHtml) {
                    callJs("\"" + webUrl + "\"");
                }
            }
        }
    }

    @Override
    public void actionExce(String actionName, String actionParams) {

    }

    @Override
    public void resultExce(String actionName, String actionParams, String result) {

    }

}
