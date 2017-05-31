
package com.baidumap.downloader.utils;

import com.baidumap.downloader.R;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

public class DownloaderUtils {

    /**
     * Download APK file from URL
     *
     * @param context
     * @param url http url
     * @return
     */
    public static long download(Context context, String url) {
        Uri resource = Uri.parse(url);
        Request request = new Request(resource);
        request.setAllowedNetworkTypes(Request.NETWORK_MOBILE| Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(false);
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap
                .getFileExtensionFromUrl(url));
        request.setMimeType(mimeString);
        request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setVisibleInDownloadsUi(true);
        String filename = getDownloadApkFileName(resource);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
        DownloadManager downloadManager = (DownloadManager) context
                .getSystemService(Context.DOWNLOAD_SERVICE);
        long id = 0;
        try {
            id = downloadManager.enqueue(request);
        }catch (Exception e){
            Toast.makeText(context, R.string.system_downloader_error, Toast.LENGTH_LONG).show();
        }
        return id;
    }

    private static String getDownloadApkFileName(Uri resource) {
        String filename = null;
        if (resource != null) {
            filename = resource.getLastPathSegment();
            if (filename == null) {
                filename = "update.apk";
            } else if (!filename.endsWith(".apk")) {
                filename = filename + ".apk";
            }
        }
        return filename;
    }

    // if the APP we have selected is downloading, return true
//    public static boolean isDownloadProcessing(Context context, long downloadId) {
//        boolean result = false;
//        if (downloadId >= 0 && context != null) {
//            int status = getDownloadStatus(context, downloadId);
//            if (status == DownloadManager.STATUS_PENDING ||
//                    status == DownloadManager.STATUS_RUNNING) {
//                result = true;
//            }
//        }
//        return result;
//    }

//    public static int getDownloadStatus(Context context, long downloadId) {
//        int status = -1;
//        DownloadManager downloadManager = (DownloadManager) context
//                .getSystemService(Context.DOWNLOAD_SERVICE);
//        DownloadManager.Query query = new DownloadManager.Query();
//        query.setFilterById(downloadId);
//        Cursor cursor = downloadManager.query(query);
//        if (cursor != null) {
//            try {
//                if (cursor.moveToFirst()) {
//                    status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
//                }
//            } finally {
//                cursor.close();
//            }
//        }
//        return status;
//    }

}