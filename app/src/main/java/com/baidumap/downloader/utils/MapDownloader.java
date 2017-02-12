package com.baidumap.downloader.utils;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by weilei04 on 2017/2/12.
 */
public class MapDownloader {
    private Context mContext;

    public MapDownloader(Context context) {
        mContext = context;
    }

    public boolean DownloadApk(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }

        return true;
    }
}
