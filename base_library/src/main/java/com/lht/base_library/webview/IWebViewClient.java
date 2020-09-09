package com.lht.base_library.webview;

public interface IWebViewClient {

    void loadFinished(String url);

    void loadStart(String url);

    void webViewLoad(String action, String actionParams);

    void actionExce(String actionName, String actionParams);

    void resultExce(String actionName, String actionParams, String result);

}
