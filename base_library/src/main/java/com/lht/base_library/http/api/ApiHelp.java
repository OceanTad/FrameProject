package com.lht.base_library.http.api;

import android.util.ArrayMap;

import com.lht.base_library.http.net.RetrofitManager;

import retrofit2.Retrofit;

public class ApiHelp {

    private static volatile ApiHelp instance;

    private ApiHelp() {

    }

    public static ApiHelp getInstance() {
        if (instance == null) {
            synchronized (ApiHelp.class) {
                if (instance == null) {
                    instance = new ApiHelp();
                }
            }
        }
        return instance;
    }

    private Retrofit retrofit;
    private ArrayMap<Class, Object> apiMap = new ArrayMap<>();

    protected void createApi(String baseUrl) {
        retrofit = new RetrofitManager.Builder(baseUrl).builder().create();
    }

    protected void createApi(RetrofitManager.Builder builder) {
        retrofit = builder.builder().create();
    }

    protected <T> T getApi(Class<T> object) {
        if (apiMap.containsKey(object)) {
            return object.cast(apiMap.get(object));
        }
        T obj = retrofit.create(object);
        apiMap.put(object, obj);
        return obj;
    }

}
