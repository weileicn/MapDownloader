package com.baidumap.downloader.activities;

import com.baidumap.downloader.R;

import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ((TextView)findViewById(R.id.titlebar_title_tv)).setText("关于");
    }
}
