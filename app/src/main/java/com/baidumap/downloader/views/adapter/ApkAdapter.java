package com.baidumap.downloader.views.adapter;

import java.util.List;

import com.baidumap.downloader.R;
import com.baidumap.downloader.models.ApkModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by weilei04 on 2017/2/20.
 */
public class ApkAdapter extends BaseAdapter {
    private Context mContext;
    private List<ApkModel> apkModelList;

    public ApkAdapter(Context context ,List<ApkModel> apkModelList) {
        mContext = context;
        this.apkModelList = apkModelList;
    }

    @Override
    public int getCount() {
        return apkModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return apkModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view;
        ApkModel apkModel = (ApkModel) getItem(i);
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.map_about_item,null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.map_category_item_text);
            convertView.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(apkModel.getBranch());
        return view;
    }

    class ViewHolder{
        public TextView textView;
    }
}
