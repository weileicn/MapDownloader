package com.baidumap.downloader.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidumap.downloader.R;
import com.baidumap.downloader.models.Apk;
import com.baidumap.downloader.models.CategoryInfo;
import com.baidumap.downloader.models.Constants;
import com.baidumap.downloader.utils.HttpUtil;
import com.baidumap.downloader.utils.MapDownloader;
import com.baidumap.downloader.utils.NetworkUtil;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadActivity extends BaseActivity {
    View button;
    View back;
    String location = "http://oem.baidu.com/attachments/97396/BaiduMap_v9.1_samsung_1012497h_build1188_20170213.apk";
    String url = "http://lbsdevicesys.duapp.com/DeviceInfo-deleteoneitem.php";

    String mCategory;
    List<Apk> mList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ((TextView)findViewById(R.id.titlebar_title_tv)).setText(getIntent().getStringExtra("categoryName")+"(下载)");
        mCategory = getIntent().getStringExtra(CategoryInfo.KEY);
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
//                HttpUtil.getInstance().request(url, CategoryInfo.KEY, mCategory,new Callback() {
//                    @Override
//                    public void onFailure(Request request, IOException e) {
//                        Log.e("weilei", "error");
//                    }
//
//                    @Override
//                    public void onResponse(Response response) throws IOException {
//                        Log.e("weilei", "json:"+response.body().string());
//                        parseJson(response.body().string());
//                    }
//                });
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

    private void parseJson(String json) {
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String version = jsonObject.getString("version");
                String url = jsonObject.getString("url");
                Log.d("MainActivity", "id is " + id);
                Log.d("MainActivity", "version is " + version);
                Log.d("MainActivity", "url is " + url);
                Apk apk = new Apk(id,version,url);
                mList.add(apk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
