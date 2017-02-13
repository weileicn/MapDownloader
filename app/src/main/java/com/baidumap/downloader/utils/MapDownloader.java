package com.baidumap.downloader.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by weilei04 on 2017/2/12.
 */
public class MapDownloader extends AsyncTask{
    private Context mContext;
    private String mUrl;

    public MapDownloader(Context context) {
        mContext = context;
    }

    public boolean DownloadApk(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        mUrl = url;
        execute();
        Toast.makeText(mContext, "开始下载...", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        DownloaderUtils.download(mContext,mUrl);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
