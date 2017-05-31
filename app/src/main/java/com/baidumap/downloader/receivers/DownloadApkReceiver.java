
package com.baidumap.downloader.receivers;

import java.io.File;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class DownloadApkReceiver extends BroadcastReceiver {

    public static final String DOWNLOAD_COMPLETE_ACTION = "android.intent.action.DOWNLOAD_COMPLETE";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("weilei","onReceive");
        String action = intent.getAction();

        if (TextUtils.equals(action, DOWNLOAD_COMPLETE_ACTION)) {
            final DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            final DownloadManager downloadManager = (DownloadManager) context
                    .getSystemService(Context.DOWNLOAD_SERVICE);
            Cursor cursor = downloadManager.query(query);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        int fileIndex = cursor
                                .getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
                        String file = cursor.getString(fileIndex);
                        Intent installIntent = new Intent(Intent.ACTION_VIEW);
                        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        installIntent.setDataAndType(Uri.fromFile(new File(file)),
                                "application/vnd.android.package-archive");
                        context.startActivity(installIntent);
                    }
                } finally {
                    cursor.close();
                }
            }
        }
    }
}
