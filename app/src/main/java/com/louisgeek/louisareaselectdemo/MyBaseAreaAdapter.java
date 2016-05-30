package com.louisgeek.louisareaselectdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by louisgeek on 2016/5/30.
 */
public class MyBaseAreaAdapter extends BaseAdapter{

    List<Area> areaList;
    Context mContext;

    public MyBaseAreaAdapter(List<Area> areaList, Context context) {
        this.areaList = areaList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return areaList.size();
    }

    @Override
    public Object getItem(int position) {
        return areaList.get(position);
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
            convertView= LayoutInflater.from(mContext).inflate(android.R.layout.simple_spinner_item,parent,false);
            viewHolder.text1= (TextView) convertView.findViewById(android.R.id.text1);

            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.text1.setText(areaList.get(position).getAreaName());

        return convertView;
    }

    class ViewHolder{
        TextView text1;
    }
}
