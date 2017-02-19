package com.baidumap.downloader.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidumap.downloader.R;
import com.baidumap.downloader.models.Apk;
import com.baidumap.downloader.models.CategoryInfo;
import com.baidumap.downloader.models.Constants;
import com.baidumap.downloader.utils.MapDownloader;
import com.baidumap.downloader.utils.NetworkUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadFragment extends Fragment {

    View button;
    View back;
    String location = "http://180.149.144.140/mcdull/app/MapAssistants.apk";
    String url = "http://lbsdevicesys.duapp.com/DeviceInfo-deleteoneitem.php";

    String mCategory;
    List<Apk> mList = new ArrayList<>();

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public DownloadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_download, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((TextView)getActivity().findViewById(R.id.titlebar_title_tv)).setText(getActivity().getIntent().getStringExtra
                ("categoryName")+"(下载)");
        mCategory = getActivity().getIntent().getStringExtra(CategoryInfo.KEY);
        back = getActivity().findViewById(R.id.titlebar_back_btn);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        button = getActivity().findViewById(R.id.button);
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void downloadMap(String location) {
        if (!TextUtils.isEmpty(location)) {
            int networkType = NetworkUtil.getNetworkType(getActivity());
            if (networkType == Constants.NETWORK_WIFI) {
                MapDownloader downloader = new MapDownloader(getActivity());
                PackageManager pm = getActivity().getPackageManager();
                boolean permission = (PackageManager.PERMISSION_GRANTED ==
                                              pm.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", "com.baidumap.downloader"));
                if (permission) {
                    downloader.DownloadApk(location);
                }else {
                    //                    Toast.makeText(this,"没有权限",Toast.LENGTH_SHORT).show();
                    requestStoragePermissions(getActivity());
                }
            } else if (networkType == Constants.NETWORK_MOBILE) {
                //                showDialog( );
            } else if (networkType == Constants.NETWORK_NONE) {
                Toast.makeText(getActivity(), "请连接网络后重新尝试", Toast.LENGTH_SHORT).show();
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

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    private void requestStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}
