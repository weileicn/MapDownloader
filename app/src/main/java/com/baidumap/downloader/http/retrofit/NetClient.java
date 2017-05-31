package com.baidumap.downloader.http.retrofit;

import java.net.CookieManager;
import java.net.CookiePolicy;

import com.baidumap.downloader.http.parse.FastjsonConverterFactory;
import com.baidumap.downloader.models.ApkModel;
import com.baidumap.downloader.models.VersionModel;
import com.baidumap.downloader.models.request.BaseRequest;
import com.baidumap.downloader.models.request.VersionRequest;
import com.baidumap.downloader.models.response.ArrayResponse;
import com.squareup.okhttp.OkHttpClient;

import retrofit.Callback;
import retrofit.Retrofit;

/**
 * Created by weilei04 on 17/2/19.
 */
public class NetClient {

    private static final String BASE_URL = "http://lbsdevicesys.duapp.com";
    private static NetClient sInstance;
    private final NetApi mAPI;
    private static OkHttpClient sOkHttpClient;

    private NetClient() {
        sOkHttpClient = new OkHttpClient();
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        sOkHttpClient.setCookieHandler(cookieManager);
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(sOkHttpClient)
                .addConverterFactory(FastjsonConverterFactory.create())
                .build();
        mAPI = adapter.create(NetApi.class);
    }

    public static NetClient getInstance() {
        if (sInstance == null) {
            sInstance = new NetClient();
        }
        return sInstance;
    }

    /**
     * 查询所有版本数据
     */
    public void getVersionList(Callback<ArrayResponse<VersionModel>> callback) {
        BaseRequest request = new BaseRequest();
        mAPI.getVersionList(request).enqueue(callback);
    }

    /**
     * 查询Apk数据
     */
    public void getApkList(String version ,Callback<ArrayResponse<ApkModel>> callback) {
        VersionRequest request = new VersionRequest(version);
        mAPI.getApkList(request).enqueue(callback);
    }

}
