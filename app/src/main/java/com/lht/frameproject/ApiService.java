package com.lht.frameproject;

import com.lht.base_library.http.modle.BaseResponse;

import java.util.Map;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface ApiService {

    @GET("/ref/account/phone/login")
    Observable<BaseResponse<String>> login(@QueryMap Map<String, String> params);

}
