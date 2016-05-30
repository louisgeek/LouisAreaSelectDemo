package com.louisgeek.louisareaselectdemo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.louisgeek.louisareaselectdemo.Bean.Province;

import java.util.List;

/**
 * Created by louisgeek on 2016/5/30.
 */
public class MyBaseProvinceAdapter extends BaseAdapter {
    List<Province> provinceList;
    Context mContext;

    public MyBaseProvinceAdapter(List<Province> provinceList, Context context) {
        this.provinceList = provinceList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return provinceList.size();
    }

    @Override
    public Object getItem(int position) {
        return provinceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView=LayoutInflater.from(mContext).inflate(android.R.layout.simple_spinner_item,parent,false);
            viewHolder.text1= (TextView) convertView.findViewById(android.R.id.text1);

            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.text1.setText(provinceList.get(position).getProvinceName());

        return convertView;
    }

    class ViewHolder{
        TextView text1;
    }
}
