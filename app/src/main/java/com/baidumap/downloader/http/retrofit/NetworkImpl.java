package com.baidumap.downloader.http.retrofit;

import com.baidumap.downloader.http.listeners.OnGetApkListListener;
import com.baidumap.downloader.http.listeners.OnGetVersionListListener;
import com.baidumap.downloader.models.ApkModel;
import com.baidumap.downloader.models.VersionModel;
import com.baidumap.downloader.models.response.ArrayResponse;

import android.util.Log;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by weilei04 on 17/2/19.
 */
public class NetworkImpl implements NetworkAction {

    private static final String TAG = "NetworkImpl";

    private static NetworkImpl networkImpl;
    private static NetClient netClient;

    public static NetworkImpl getInstance(){
        if(networkImpl == null){
            networkImpl = new NetworkImpl();
            netClient = NetClient.getInstance();
        }
        return networkImpl;
    }

    @Override
    public void getVersionList(final OnGetVersionListListener listener) {
        netClient.getVersionList(new Callback<ArrayResponse<VersionModel>>() {
            @Override
            public void onResponse(Response<ArrayResponse<VersionModel>> response, Retrofit retrofit) {
                if(response.body()!=null) {
//                    Log.d(TAG," "+response.body().getObjList());
                    listener.getVersionListSuccess(response.body().getObjList());

                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "onFailure: "+t.toString());
            }
        });
    }

    @Override
    public void getApkList(String version, final OnGetApkListListener listener) {
        netClient.getApkList(version, new Callback<ArrayResponse<ApkModel>>() {
            @Override
            public void onResponse(Response<ArrayResponse<ApkModel>> response, Retrofit retrofit) {
                if(response.body()!=null) {
//                    Log.d(TAG," "+response.body().getObjList());
                    listener.getApkListSuccess(response.body().getObjList());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "onFailure: "+t.toString());
            }
        });
    }
}
