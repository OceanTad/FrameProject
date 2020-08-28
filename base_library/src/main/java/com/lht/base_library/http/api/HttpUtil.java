package com.lht.base_library.http.api;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.lht.base_library.http.converter.GsonHelp;
import com.lht.base_library.http.modle.BaseResponse;
import com.lht.base_library.utils.LogUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;

public class HttpUtil {

    public static <T> T parseResponse(Response response) {
        if (null == response) {
            return null;
        }
        ResponseBody body = response.body();
        if (null == body) {
            return null;
        }
        BufferedSource source = body.source();
        if (null == source) {
            return null;
        }
        try {
            source.request(Long.MAX_VALUE);
        } catch (IOException e) {
            LogUtil.e("parseResponse error " + e.getClass() + " " + e.getMessage());
        }
        String respString = source.buffer().clone().readString(Charset.defaultCharset());
        if (TextUtils.isEmpty(respString)) {
            return null;
        }
        return GsonHelp.getInstance().createGson().fromJson(respString, new TypeToken<BaseResponse<T>>() {}.getType());
    }

    public static Request buildGetRequest(Map<String, Object> params, String url) {
        StringBuilder sb = new StringBuilder(url);
        sb.append("?");
        for (Map.Entry<String, Object> p : params.entrySet()) {
            sb.append(p.getKey()).append("=").append(p.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return new Request.Builder()
                .url(sb.toString())
                .get()
                .build();
    }

    public static Request buildPostRequest(Map<String, String> params, String url) {
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> p : params.entrySet()) {
            bodyBuilder.add(p.getKey(), p.getValue());
        }
        return new Request.Builder()
                .url(url)
                .post(bodyBuilder.build())
                .build();
    }

    public static Request buildRequest(Map<String, String> params, Request request) {
        String method = request.method();
        if ("GET".equals(method)) {
            int index = request.url().toString().indexOf("?");
            StringBuilder sb = new StringBuilder(request.url().toString().substring(0, index));
            sb.append("?");
            for (Map.Entry<String, String> p : params.entrySet()) {
                sb.append(p.getKey()).append("=").append(p.getValue()).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            return request.newBuilder().url(sb.toString()).build();
        }
        if ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method) || "PATCH".equals(method)) {
            if (request.body() instanceof FormBody) {
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                for (Map.Entry<String, String> p : params.entrySet()) {
                    bodyBuilder.add(p.getKey(), p.getValue());
                }
                return request.newBuilder().method(method, bodyBuilder.build()).build();
            }
        }
        return request;
    }

    public static Map<String, String> parsePostRequest(Request request) {
        Map<String, String> params = null;
        FormBody body = null;
        try {
            body = (FormBody) request.body();
        } catch (ClassCastException e) {
            LogUtil.e("parseRequest error " + e.getClass() + " " + e.getMessage());
        }
        if (body != null) {
            int size = body.size();
            if (size > 0) {
                params = new HashMap<>();
                for (int i = 0; i < size; i++) {
                    params.put(body.name(i), body.value(i));
                }
            }
        }
        return params;
    }

    public static Map<String, String> parseGetRequest(Request request) {
        Map<String, String> params = null;
        HttpUrl url = request.url();
        Set<String> strings = url.queryParameterNames();
        if (strings != null) {
            Iterator<String> iterator = strings.iterator();
            params = new HashMap<>();
            int i = 0;
            while (iterator.hasNext()) {
                String name = iterator.next();
                String value = url.queryParameterValue(i);
                params.put(name, value);
                i++;
            }
        }
        return params;
    }

    public static Map<String, String> parseRequest(Request request) {
        String method = request.method();
        Map<String, String> params = null;
        if ("GET".equals(method)) {
            params = parseGetRequest(request);
        } else if ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method) || "PATCH".equals(method)) {
            RequestBody body = request.body();
            if (body instanceof FormBody) {
                params = parsePostRequest(request);
            }
        }
        return params;
    }

    public static <T> BaseResponse<T> executeRequest(OkHttpClient client, Request request){
        try {
            Response response = client.newCall(request).execute();
            return parseResponse(response);
        } catch (IOException e) {
            LogUtil.e("executeRequest error " + e.getClass() + " " + e.getMessage());
            return null;
        }
    }

}
