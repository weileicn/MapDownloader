package com.baidumap.downloader.http.retrofit;

import com.baidumap.downloader.models.ApkModel;
import com.baidumap.downloader.models.VersionModel;
import com.baidumap.downloader.models.request.BaseRequest;
import com.baidumap.downloader.models.response.ArrayResponse;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by weilei04 on 17/2/19.
 */
public interface NetApi {

    /**
     * 查询所有版本数据
     */
    @POST("/DeviceInfo-queryallinfo.php")
    Call<ArrayResponse<VersionModel>> getVersionList(@Body BaseRequest request);

    /**
     * 查询Apk数据
     */
    @POST("/DeviceInfo-addoneitem.php")
    Call<ArrayResponse<ApkModel>> getApkList(@Body BaseRequest request);
}
