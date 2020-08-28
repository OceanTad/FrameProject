package com.lht.base_library.http.net;

import com.google.gson.GsonBuilder;
import com.lht.base_library.http.converter.CustomConverterFactory;
import com.lht.base_library.http.converter.CustomRequestConverter;
import com.lht.base_library.http.converter.CustomResponseConverter;
import com.lht.base_library.http.converter.IntegerConverter;
import com.lht.base_library.http.converter.StringConverter;

import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConverterFactory {

    public static Converter.Factory createDefaultConverterFactory() {
        return GsonConverterFactory.create(new GsonBuilder()
                .registerTypeAdapter(String.class, new StringConverter())
                .registerTypeAdapter(int.class, new IntegerConverter())
                .registerTypeAdapter(Integer.class, new IntegerConverter())
                .setLenient()
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create());
    }

    public static Converter.Factory createCustomConverterFactory(CustomResponseConverter responseConverter, CustomRequestConverter requestConverter) {
        return CustomConverterFactory.create(responseConverter, requestConverter);
    }

}
