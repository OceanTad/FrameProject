package com.lht.base_library.http.net;

import android.text.TextUtils;

import com.lht.base_library.base.BaseApplication;
import com.lht.base_library.utils.AppConfigUtil;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RetrofitManager {

    private String baseUrl;
    private List<Interceptor> interceptors;
    private Converter.Factory converterFactory;
    private Cache cache;
    private SSLSocketFactory sslSocketFactory;
    private CustomHostNameVerifier customHostNameVerifier;
    private int readTimeOut;
    private int connectTimeOut;

    private RetrofitManager(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.cache = builder.cache;
        this.customHostNameVerifier = builder.customHostNameVerifier;
        this.readTimeOut = builder.readTimeOut;
        this.connectTimeOut = builder.connectTimeOut;
        builderInterceptor(builder);
        builderConverterFactory(builder);
        builderSslSocketFactory(builder);
    }

    private void builderInterceptor(Builder builder) {
        interceptors = new ArrayList<>();
        if (AppConfigUtil.isDebug(BaseApplication.getInstance())) {
            interceptors.add(InterceptorFactory.createDefaultLogInterceptor());
        }
        interceptors.add(InterceptorFactory.createCookiesCacheInterceptor());
        interceptors.add(InterceptorFactory.createReadCookiesInterceptor());
        if (!TextUtils.isEmpty(builder.baseUrlKey)) {
            interceptors.add(InterceptorFactory.createDynamicBaseUrlInterceptor(builder.baseUrlKey));
        }
        interceptors.add(InterceptorFactory.createPublicHeaderInterceptor(builder.headerParams));
        interceptors.addAll(builder.interceptors);
    }

    private void builderConverterFactory(Builder builder) {
        if (builder.converterFactory == null) {
            converterFactory = ConverterFactory.createDefaultConverterFactory();
        } else {
            converterFactory = builder.converterFactory;
        }
    }

    private void builderSslSocketFactory(Builder builder) {
        if (builder.sslSocketFactory == null) {
            this.sslSocketFactory = SslSocketFactory.createDefaultSsl();
        } else {
            this.sslSocketFactory = builder.sslSocketFactory;
        }
    }

    public Retrofit create() {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(converterFactory)
                .client(buildClient())
                .build();
    }

    public OkHttpClient buildClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (!AppConfigUtil.isDebug(BaseApplication.getInstance())) {
            builder.proxy(Proxy.NO_PROXY);
        }

        builder.readTimeout(readTimeOut, TimeUnit.MILLISECONDS);
        if (sslSocketFactory != null) {
            builder.sslSocketFactory(sslSocketFactory);
        }
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                if (customHostNameVerifier != null) {
                    return customHostNameVerifier.verifyHost(s, sslSession);
                } else {
                    HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                    return hv.verify(s, sslSession);
                }
            }
        })
                .connectTimeout(connectTimeOut, TimeUnit.MILLISECONDS);

        if (cache != null) {
            builder.cache(cache);
        }

        for (Interceptor interceptor : interceptors) {
            if (interceptor != null) {
                builder.addInterceptor(interceptor);
            }
        }

        return builder.build();
    }

    public static class Builder {

        private String baseUrl;
        private List<Interceptor> interceptors;
        private Converter.Factory converterFactory;

        private String baseUrlKey;
        private String[] headerParams;

        private Cache cache;
        private SSLSocketFactory sslSocketFactory;
        private CustomHostNameVerifier customHostNameVerifier;
        private int readTimeOut = 10000;
        private int connectTimeOut = 15000;

        public Builder(String url) {
            baseUrl = url;
            interceptors = new ArrayList<>();
        }

        public Builder addConverterFactory(Converter.Factory converterFactory) {
            this.converterFactory = converterFactory;
            return this;
        }

        public Builder addCache(Cache cache) {
            this.cache = cache;
            return this;
        }

        public Builder addSslFactory(SSLSocketFactory sslSocketFactory) {
            this.sslSocketFactory = sslSocketFactory;
            return this;
        }

        public Builder addHostNameVerifier(CustomHostNameVerifier customHostNameVerifier) {
            this.customHostNameVerifier = customHostNameVerifier;
            return this;
        }

        public Builder setReadTimeOut(int readTimeOut) {
            this.readTimeOut = readTimeOut;
            return this;
        }

        public Builder setConnectTimeOut(int connectTimeOut) {
            this.connectTimeOut = connectTimeOut;
            return this;
        }

        public Builder setDynamicBaseUrlKey(String key) {
            if (!TextUtils.isEmpty(key)) {
                baseUrlKey = key;
            }
            return this;
        }

        public Builder addPublicHeaders(String... params) {
            if (params != null && params.length > 0) {
                headerParams = params;
            }
            return this;
        }

        public RetrofitManager builder() {
            return new RetrofitManager(this);
        }

    }

}
