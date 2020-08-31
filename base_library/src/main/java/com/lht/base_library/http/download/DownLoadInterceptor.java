package com.lht.base_library.http.download;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class DownLoadInterceptor implements Interceptor {

    private IDownLoadProgressListener listener;

    public DownLoadInterceptor(IDownLoadProgressListener listener) {
        this.listener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return response.newBuilder().body(new DownLoadResponseBoby(response.body(), listener)).build();
    }

}
