package com.lht.base_library.http.converter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Converter;

public class CustomRequestConverter<T> implements Converter<T, RequestBody> {
    protected static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    protected static final Charset UTF_8 = Charset.forName("UTF-8");

    protected TypeAdapter<T> adapter;

    public void setAdapter(TypeAdapter<T> adapter){
        this.adapter = adapter;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        Buffer buffer = new Buffer();
        Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
        JsonWriter jsonWriter = GsonHelp.getInstance().createGson().newJsonWriter(writer);
        adapter.write(jsonWriter, value);
        jsonWriter.close();
        return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
    }

}
