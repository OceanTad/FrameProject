package com.lht.base_library.http.api;

import android.util.ArrayMap;

import com.lht.base_library.http.net.RetrofitManager;

import okhttp3.OkHttpClient;
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
    private OkHttpClient client;
    private ArrayMap<Class, Object> apiMap = new ArrayMap<>();

    protected void createApi(String baseUrl) {
        RetrofitManager manager = new RetrofitManager.Builder(baseUrl).builder();
        retrofit = manager.create();
        client = manager.getClient();
    }

    protected void createApi(RetrofitManager.Builder builder) {
        RetrofitManager manager = builder.builder();
        retrofit = builder.builder().create();
        client = manager.getClient();
    }

    protected <T> T getApi(Class<T> object) {
        if (apiMap.containsKey(object)) {
            return object.cast(apiMap.get(object));
        }
        T obj = retrofit.create(object);
        apiMap.put(object, obj);
        return obj;
    }

    protected OkHttpClient getClient(){
        return client;
    }

}
