package com.baidumap.downloader.http.parse;

import java.io.IOException;
import java.nio.charset.Charset;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import retrofit.Converter;

/**
 * Created by weilei04 on 17/2/19.
 */
public final class FastjsonRequestBodyConverter<T> implements Converter<T, RequestBody> {

    private static final String TAG = "Fastjson";

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");


    public FastjsonRequestBodyConverter() {
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        return RequestBody.create(MEDIA_TYPE, JSON.toJSONString(value).getBytes(UTF_8));
    }
}
