package com.baidumap.downloader.http.retrofit;

import com.baidumap.downloader.http.listeners.OnGetApkListListener;
import com.baidumap.downloader.http.listeners.OnGetVersionListListener;

/**
 * Created by weilei04 on 17/2/19.
 */
public interface NetworkAction {

    /**
     * 查询所有版本数据
     */
    void getVersionList(OnGetVersionListListener onGetVersionListListener);

    /**
     * 查询Apk数据
     */
    void getApkList(String version,OnGetApkListListener onGetApkListListener);


}
