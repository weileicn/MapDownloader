package com.baidumap.downloader.activities;

import java.util.Map;

import com.baidumap.downloader.R;
import com.baidumap.downloader.network.Constants;
import com.baidumap.downloader.utils.MapDownloader;
import com.baidumap.downloader.utils.NetworkUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadActivity extends BaseActivity {
    View button;
    View back;
    String location = "http://180.149.144.140/mcdull/app/MapAssistants.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ((TextView)findViewById(R.id.titlebar_title_tv)).setText(getIntent().getStringExtra("category")+"(下载)");
        back = findViewById(R.id.titlebar_back_btn);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadMap(location);
                Log.d("weilei","onClick");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void downloadMap(String location) {
        if (!TextUtils.isEmpty(location)) {
            int networkType = NetworkUtil.getNetworkType(this);
            if (networkType == Constants.NETWORK_WIFI) {
                MapDownloader downloader = new MapDownloader(this);
                downloader.DownloadApk(location);
            } else if (networkType == Constants.NETWORK_MOBILE) {
//                showDialog( );
            } else if (networkType == Constants.NETWORK_NONE) {
                Toast.makeText(this, "请连接网络后重新尝试", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void  startMap() {

    }
}
