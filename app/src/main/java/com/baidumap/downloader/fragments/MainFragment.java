package com.baidumap.downloader.fragments;

import java.util.ArrayList;
import java.util.List;

import com.baidumap.downloader.R;
import com.baidumap.downloader.activities.AboutActivity;
import com.baidumap.downloader.activities.DownloadActivity;
import com.baidumap.downloader.views.AutoScrollViewPager;
import com.baidumap.downloader.views.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements View.OnClickListener{
    AutoScrollViewPager viewPager = null;
    List<View> mList = new ArrayList<>();
    List<View> indicatorList = new ArrayList<>();
    LinearLayout banner_indicator;
    View mapCateItem1;
    View mapCateItem2;
    View mapCateItem3;
    View mapCateItem4;
    View mapAboutItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewPager = (AutoScrollViewPager) getActivity().findViewById(R.id.auto_viewpager);
        banner_indicator = (LinearLayout) getActivity().findViewById(R.id.banner_indicator);
        mapCateItem1 = getActivity().findViewById(R.id.map_category_item1);
        mapCateItem2 = getActivity().findViewById(R.id.map_category_item2);
        mapCateItem3 = getActivity().findViewById(R.id.map_category_item3);
        mapCateItem4 = getActivity().findViewById(R.id.map_category_item4);
        mapAboutItem = getActivity().findViewById(R.id.map_about_item);

        mapCateItem1.setOnClickListener(this);
        mapCateItem2.setOnClickListener(this);
        mapCateItem3.setOnClickListener(this);
        mapCateItem4.setOnClickListener(this);
        mapAboutItem.setOnClickListener(this);

        updateIndicator();
        initViewPager();
        ((TextView)getActivity().findViewById(R.id.titlebar_title_tv)).setText("主页");
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPager.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewPager.onPause();
    }

    void initViewPager() {
        ImageView image1 = (ImageView) LayoutInflater.from(getActivity()).inflate(R.layout.banner_image,null);
        image1.setImageDrawable(getResources().getDrawable(R.drawable.banner_image1));
        mList.add(image1);
        ImageView image2 = (ImageView) LayoutInflater.from(getActivity()).inflate(R.layout.banner_image,null);
        image2.setImageDrawable(getResources().getDrawable(R.drawable.banner_image2));
        mList.add(image2);
        ImageView image3 = (ImageView) LayoutInflater.from(getActivity()).inflate(R.layout.banner_image,null);
        image3.setImageDrawable(getResources().getDrawable(R.drawable.banner_image3));
        mList.add(image3);

        viewPager.setAdapter(new PagerAdapter() {

            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(mList.get(position));
                return mList.get(position);
            }

        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int var1, float var2, int var3) {

            }

            @Override
            public void onPageSelected(int var1) {
                for (int i = 0; i < indicatorList.size(); i++) {
                    if (i == var1) {
                        ImageView point_normal = (ImageView) indicatorList.get(i).findViewById(R.id.point_normal);
                        ImageView point_selected = (ImageView) indicatorList.get(i).findViewById(R.id.point_selected);
                        point_normal.setVisibility(View.GONE);
                        point_selected.setVisibility(View.VISIBLE);
                    } else {
                        ImageView point_normal = (ImageView) indicatorList.get(i).findViewById(R.id.point_normal);
                        ImageView point_selected = (ImageView) indicatorList.get(i).findViewById(R.id.point_selected);
                        point_normal.setVisibility(View.VISIBLE);
                        point_selected.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int var1) {

            }
        });
    }

    void initIndicatorList() {
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.indicator_item,null);

        View view2 = LayoutInflater.from(getActivity()).inflate(R.layout.indicator_item,null);

        View view3 = LayoutInflater.from(getActivity()).inflate(R.layout.indicator_item,null);

        indicatorList.add(view1);
        indicatorList.add(view2);
        indicatorList.add(view3);
    }

    void updateIndicator() {
        indicatorList.clear();
        banner_indicator.removeAllViews();
        initIndicatorList();
        int size = indicatorList.size();
        if (size <= 1) {
            if (banner_indicator.getVisibility() != View.GONE) {
                banner_indicator.setVisibility(View.GONE);
            }
            return;
        } else if (banner_indicator.getVisibility() != View.VISIBLE) {
            banner_indicator.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < size; i++) {
            banner_indicator.addView(indicatorList.get(i));
            if (i == 0) {
                indicatorList.get(i).setSelected(true);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.map_category_item1:
                startActivity(new Intent(getActivity(), DownloadActivity.class));
                break;
            case R.id.map_category_item2:
                startActivity(new Intent(getActivity(), DownloadActivity.class));
                break;
            case R.id.map_category_item3:
                startActivity(new Intent(getActivity(), DownloadActivity.class));
                break;
            case R.id.map_category_item4:
                startActivity(new Intent(getActivity(), DownloadActivity.class));
                break;
            case R.id.map_about_item:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            default:
                break;
        }
    }
}
