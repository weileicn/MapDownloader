package com.baidumap.downloader.activities;

import com.baidumap.downloader.R;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DownloadActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ((TextView)findViewById(R.id.titlebar_title_tv)).setText(getIntent().getStringExtra("category")+"(下载)");
    }
}
