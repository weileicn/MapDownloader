package com.baidumap.downloader.http.parse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import retrofit.Converter;

/**
 * Created by weilei04 on 17/2/19.
 */
public final class FastjsonConverterFactory extends Converter.Factory {

    public static FastjsonConverterFactory create() {
        return new FastjsonConverterFactory();
    }

    private FastjsonConverterFactory() {}

    @Override
    public Converter<ResponseBody, ?> fromResponseBody(Type type, Annotation[] annotations) {
        return new FastjsonResponseBodyConverter<>(type);
    }

    @Override
    public Converter<?, RequestBody> toRequestBody(Type type, Annotation[] annotations) {
        return new FastjsonRequestBodyConverter<>();
    }
}
