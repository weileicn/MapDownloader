package com.baidumap.downloader.utils;

import com.baidumap.downloader.models.Constants;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by weilei04 on 2017/2/12.
 */
public class NetworkUtil {
    public static int getNetworkType(Context inContext) {
        Context context = inContext.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo network;
            if ((network = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI)) != null && network.isConnected()) {
                return Constants.NETWORK_WIFI;
            } else if ((network = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)) != null && network.isConnected()) {
                return Constants.NETWORK_MOBILE;
            }
        }
        return Constants.NETWORK_NONE;
    }

    public static boolean isWifiNetwork(Context context) {
        return (Constants.NETWORK_WIFI == getNetworkType(context));
    }
}
