package com.baidumap.downloader.views;

import com.baidumap.downloader.R;

import android.content.Context;
import android.widget.FrameLayout;

/**
 * Created by weilei04 on 2017/2/11.
 */
public class BannerImage extends FrameLayout {
    //test
    public BannerImage(Context context) {
        super(context);
        setForeground(context.getResources().getDrawable(R.mipmap.banner));
    }
}
