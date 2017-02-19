package com.baidumap.downloader.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidumap.downloader.R;
import com.baidumap.downloader.utils.CategoryInfo;
import com.baidumap.downloader.utils.Constants;
import com.baidumap.downloader.utils.MapDownloader;
import com.baidumap.downloader.utils.NetworkUtil;

public class DownloadFragment extends Fragment {

    View button;
    View back;
    String location = "http://180.149.144.140/mcdull/app/MapAssistants.apk";

    String mCategory;

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

    private void downloadMap(final String location) {
        if (!TextUtils.isEmpty(location)) {
            int networkType = NetworkUtil.getNetworkType(getActivity());
            if (networkType == Constants.NETWORK_WIFI) {
                download(location);
            } else if (networkType == Constants.NETWORK_MOBILE) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        download(location);
                    }
                });
                builder.setCancelable(true);
                builder.setTitle("提示");
                builder.setMessage("当前网络为移动数据网络，下载过程中会产生流量资费，是否继续下载吗？");
                builder.show();
            } else if (networkType == Constants.NETWORK_NONE) {
                Toast.makeText(getActivity(), "请连接网络后重新尝试", Toast.LENGTH_SHORT).show();
            }
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

    private void download(String location) {
        MapDownloader downloader = new MapDownloader(getActivity());
        PackageManager pm = getActivity().getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                                      pm.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", "com.baidumap.downloader"));
        if (permission) {
            downloader.DownloadApk(location);
        }else {
            //Toast.makeText(this,"没有权限",Toast.LENGTH_SHORT).show();
            requestStoragePermissions(getActivity());
        }
    }

}
