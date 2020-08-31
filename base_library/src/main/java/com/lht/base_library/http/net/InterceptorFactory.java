package com.lht.base_library.http.net;

import android.text.TextUtils;
import android.webkit.CookieManager;

import com.lht.base_library.utils.LogUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class InterceptorFactory {

    public static Interceptor createDefaultLogInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtil.e("okhttp", "response:" + message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    public static Interceptor createDynamicBaseUrlInterceptor(final String key) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                if (!TextUtils.isEmpty(key)) {
                    Request request = chain.request();
                    HttpUrl oldHttpUrl = request.url();
                    Request.Builder builder = request.newBuilder();
                    List<String> headerValues = request.headers(key);
                    if (headerValues != null && headerValues.size() > 0) {
                        builder.removeHeader(key);
                        if (!TextUtils.isEmpty(headerValues.get(0))) {
                            HttpUrl newBaseUrl = HttpUrl.parse(headerValues.get(0));
                            HttpUrl newHttpUrl = oldHttpUrl.newBuilder()
                                    .scheme(newBaseUrl.scheme())
                                    .host(newBaseUrl.host())
                                    .port(newBaseUrl.port())
                                    .build();
                            return chain.proceed(builder.url(newHttpUrl).build());
                        }
                    }
                }
                return chain.proceed(chain.request());
            }
        };
    }

    public static Interceptor createPublicHeaderInterceptor(final String... params) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl httpUrl = request.url().newBuilder()
                        .build();
                Request.Builder builder = new okhttp3.Request.Builder()
                        .method(request.method(), request.body())
                        .url(httpUrl)
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json;charset=UTF-8");
                if (params != null && params.length > 0) {
                    if (params.length % 2 == 0) {
                        for (int i = 0; i < params.length / 2; i++) {
                            if (!TextUtils.isEmpty(params[2 * i]) && !TextUtils.isEmpty(params[2 * i + 1])) {
                                builder.addHeader(params[2 * i], params[2 * i + 1]);
                            }
                        }
                    } else {
                        LogUtil.e("okhttp", "add public heard error");
                    }
                }
                request = builder.build();
                return chain.proceed(request);
            }
        };
    }

    public static Interceptor createResponseInterceptor(final ResponseInterceptorAction action) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                boolean isInterceptor = false;
                if (action != null) {
                    isInterceptor = action.doResponseInterceptorAction(response);
                    if (isInterceptor) {
                        Request newRequest = action.retryRequest(request.newBuilder());
                        if (newRequest != null) {
                            return chain.proceed(newRequest);
                        }
                    }
                }
                return response;
            }
        };
    }

    public static Interceptor createCookiesCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                    for (String header : originalResponse.headers("Set-Cookie")) {
                        CookieManager.getInstance().setCookie(chain.request().url().toString(), header);
                    }
                }
                return originalResponse;
            }
        };
    }

    public static Interceptor createReadCookiesInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                String cookie = CookieManager.getInstance().getCookie(chain.request().url().toString());
                builder.addHeader("Cookie", cookie == null ? "" : cookie);
                return chain.proceed(builder.build());
            }
        };
    }

}
