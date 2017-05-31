package com.baidumap.downloader.http.listeners;

import java.util.List;

import com.baidumap.downloader.models.VersionModel;

/**
 * Created by weilei04 on 2017/2/19.
 */
public interface OnGetVersionListListener {
    void getVersionListSuccess(List<VersionModel> versionModelList);
}
