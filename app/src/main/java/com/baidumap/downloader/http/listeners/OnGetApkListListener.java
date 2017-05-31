package com.baidumap.downloader.http.listeners;

import java.util.List;

import com.baidumap.downloader.models.ApkModel;

/**
 * Created by weilei04 on 2017/2/19.
 */
public interface OnGetApkListListener {
    void getApkListSuccess(List<ApkModel> apkModelList);
}
