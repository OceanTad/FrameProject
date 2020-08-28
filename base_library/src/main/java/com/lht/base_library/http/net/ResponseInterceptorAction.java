package com.lht.base_library.http.net;

import okhttp3.Request;
import okhttp3.Response;

public interface ResponseInterceptorAction {

    boolean doResponseInterceptorAction(Response response);

    Request retryRequest(Request.Builder builder);

}
