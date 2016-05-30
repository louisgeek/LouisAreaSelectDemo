package com.louisgeek.louisareaselectdemo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.List;

/**
 * Created by louisgeek on 2016/5/30.
 */
public class ContentBaseAdapter extends BaseAdapter implements SectionIndexer {
    private  List<String> mItems;
private Context context;

    public ContentBaseAdapter(List<String> items, Context context) {
        mItems = items;
        this.context = context;
    }

    private final String sectionStr = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
   private  String[] sections;


    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
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
            convertView= LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1,parent,false);
            viewHolder.mTextView= (TextView) convertView.findViewById(android.R.id.text1);

            convertView.setTag(viewHolder);
        }else
        {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.mTextView.setText(mItems.get(position));
        return convertView;
    }


    class ViewHolder{
    TextView mTextView;
    }

    /**
     * 主要就是返回一个数组,数组中为右侧排序列中应该显示的每一个元素,对于字母排序一般我们使用#ABCDEFGHIJKLMNOPQRSTUVWXYZ.
     * @return
     */
    @Override
    public Object[] getSections() {

        sections= new String[sectionStr.length()];
        for (int i = 0; i < sectionStr.length(); i++)
        {
            sections[i] = String.valueOf(sectionStr.charAt(i));
        }

        return sections;
    }

    /**
     *  当前list中,哪一行和目前选中的selection匹配,返回的位置就是listview会跳转到的位置
     * @param sectionIndex
     * @return
     */
    @Override
    public int getPositionForSection(int sectionIndex) {
        // If there is no item for current section, previous section will be selected
        // 如果当前部分没有item，则之前的部分将被选择
        for (int i = sectionIndex; i >= 0; i--) {
            //循环mItems
            for (int j = 0; j < getCount(); j++) {
                if (i == 0) {
                    // For numeric section
                    // 字符串第一个字符与1~9之间的数字进行匹配
                    for (int k = 0; k <= 9; k++) {
                        if (String.valueOf(k).equals(String.valueOf(getItem(j).toString().charAt(0)))){
                            return j;}
                    }
                } else {
                    // A~Z
                    if (String.valueOf(sectionStr.charAt(i)).equals(String.valueOf(getItem(j).toString().charAt(0))))
                    {
                        return j;
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }
}
