package com.lht.base_library.http.converter;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class CustomConverterFactory extends Converter.Factory {

    public static CustomConverterFactory create(CustomResponseConverter responseConverter, CustomRequestConverter requestConverter) {
        return new CustomConverterFactory(responseConverter, requestConverter);
    }

    private final CustomResponseConverter responseConverter;
    private final CustomRequestConverter requestConverter;

    private CustomConverterFactory(CustomResponseConverter responseConverter, CustomRequestConverter requestConverter) {
        this.requestConverter = requestConverter;
        this.responseConverter = responseConverter;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = GsonHelp.getInstance().createGson().getAdapter(TypeToken.get(type));
        requestConverter.setAdapter(adapter);
        return requestConverter;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = GsonHelp.getInstance().createGson().getAdapter(TypeToken.get(type));
        responseConverter.setAdapter(adapter);
        return responseConverter;
    }
}
