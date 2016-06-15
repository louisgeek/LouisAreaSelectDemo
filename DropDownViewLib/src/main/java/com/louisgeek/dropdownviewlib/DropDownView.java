package com.louisgeek.dropdownviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by louisgeek on 2016/6/5.
 */
public class DropDownView extends TextView implements View.OnClickListener {

private  Context mContext;
    private static final String TAG = "DropDownView";
    String[] items_all;
    List<String>  items_key_list;
    List<String>  items_name_list;
    View nowClickView;

    public String getDefaultText() {
        return defaultText;
    }

    String defaultText;
    public void setupNameStateList(List<Map<String, Object>> nameStateList) {
        this.dataList.clear();
        this.dataList.addAll(nameStateList);
        Log.d(TAG, "setNameStateList: "+this.dataList.size());

    }

    List<Map<String, Object>> dataList=new ArrayList<>();

    public DropDownView(Context context) {
        super(context);
        init(context);
        Log.d(TAG, "DropDownView: Context context");
    }

    public DropDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.MyDropDownView);
        int itemArray_resID = typedArray.getResourceId(R.styleable.MyDropDownView_itemArray,0);
        if (itemArray_resID!=0) {
            items_all = getResources().getStringArray(itemArray_resID);//R.array.select_dialog_items
        }
        if (items_all!=null&&items_all.length>0){
            items_key_list=new ArrayList<>();
            items_name_list=new ArrayList<>();

            for (int i = 0; i <items_all.length ; i++) {
                String[] items_key_and_name=items_all[i].split("_");//风格

                if (items_key_and_name!=null&&items_key_and_name.length>1) {//1  mean:至少2个
                    items_key_list.add(items_key_and_name[0]);
                    items_name_list.add(items_key_and_name[1]);
                }else{
                    items_key_list.add(items_all[i]);//没有_就直接都是一样的
                    items_name_list.add(items_all[i]);
                }
            }
        }

        typedArray.recycle();
        init(context);
        Log.d(TAG, "DropDownView: Context context, AttributeSet attrs");
    }

    public DropDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        Log.d(TAG, "DropDownView: Context context, AttributeSet attrs, int defStyleAttr");
    }


    private void init(Context context) {
        mContext=context;
        if (this.getText()==null||this.getText().equals("")||this.getText().equals("null")) {
            this.setText("请选择");
        }
        defaultText=this.getText().toString();//
        if (this.getPaddingTop()==0&&this.getPaddingBottom()==0&&this.getPaddingLeft()==0&&this.getPaddingRight()==0) {
            int paddingLeft_Right = dp2px(mContext, 8);
            int paddingTop_Bottom = dp2px(mContext, 5);
            this.setPadding(paddingLeft_Right, paddingTop_Bottom, paddingLeft_Right, paddingTop_Bottom);
        }
        this.setOnClickListener(this);
        this.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_down_blue_grey_400_18dp,0);
        this.setBackgroundResource(R.drawable.shape_list);
        this.setSingleLine();
        /**
         * setup min width
         */
       // int text_width= (int) this.getPaint().measureText("请选择");//当前画笔测量三个字的width
       // int text_ScaleX= (int) this.getTextScaleX();//字间距
        //
       // final int text_count=230;
        int  temp_min_width=230;//三个字 大概
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (this.getMinWidth()!=-1&&this.getMinWidth()!=0){
                temp_min_width=this.getMinWidth();
            }
        }
        this.setMinWidth(temp_min_width);

        /**
         * setup max width
         */
       // final int text_count_MAX=10;
        int  temp_max_width=520;//九个字 大概
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (this.getMaxWidth()!=-1&&this.getMaxWidth()!=0&&this.getMaxWidth()!=Integer.MAX_VALUE){
                temp_max_width=this.getMaxWidth();
            }
        }
        this.setMaxWidth(temp_max_width);
        this.setEllipsize(TextUtils.TruncateAt.END);

    }

    public int  dealTextMyW(int text_width,int text_ScaleX,int text_count){
       return text_width*text_count+text_ScaleX*(text_count-1);
    }

    public int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
    @Override
    public void onClick(View v) {
        nowClickView=v;

        if (dataList!=null&&dataList.size()>0){
            //code设置list
        }else{
            //code设置list
            if (items_name_list!=null&&items_name_list.size()>0&&items_key_list!=null&&items_key_list.size()>0){
                for (int i = 0; i < items_name_list.size(); i++) {
                    Map<String, Object> map=new HashMap<>();
                    map.put("key",items_key_list.get(i));
                    map.put("name",items_name_list.get(i));
                    dataList.add(map);
                }
            }
        }

        if (dataList!=null&&dataList.size()>0){
            DropDownPopupWindow myPopupwindow=new DropDownPopupWindow(mContext,dataList);
            myPopupwindow.showAsDropDownBelwBtnView(nowClickView);
            myPopupwindow.setOnItemSelectListener(new DropDownPopupWindow.OnItemSelectListener() {
                @Override
                public void onItemSelect(Map<String,Object> map,int pos) {
                    ((DropDownView)nowClickView).setText(map.get("name").toString());
                    nowClickView.setTag(R.id.hold_dropdown_map,map);
                    if(map.get("key")!=null) {
                        nowClickView.setTag(R.id.hold_dropdown_key, map.get("key").toString());
                    }
                    //Log.d(TAG, "onItemSelect: ssqid:"+map.get("ssqid").toString());
                    if (onItemClickListener!=null) {
                        onItemClickListener.onItemClick(map,pos);
                    }
                }
            });
        }
        Log.d(TAG, "onClick: "+dataList.size());
    }


    public  interface OnItemClickListener{
        void  onItemClick(Map<String,Object> map,int pos);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    OnItemClickListener onItemClickListener;


    public void setTextMy(String text) {
        if (text==null||text.equals("")||text.equals("null")) {
            this.setText("请选择");
        }else{
            this.setText(text);
        }
    }


    public int getPositionByName(String name){
        int pos=0;
        if (name!=null&&!name.equals("")){

            for (int i = 0; i <items_name_list.size(); i++) {
               if (items_name_list.get(i).equals(name)){
                   pos=i;
                   break;
               }
            }
        }
        return pos;
    }
    public int getPositionByKey(String key){
        int pos=0;
        if (key!=null&&!key.equals("")){

            for (int i = 0; i <items_key_list.size(); i++) {
                if (items_key_list.get(i).equals(key)){
                    pos=i;
                    break;
                }
            }
        }
        return pos;
    }

    public String getNameByKey(String key){
        int pos=getPositionByKey(key);
        return items_name_list.get(pos);
    }

    public String getKeyByName(String name){
        int pos=getPositionByName(name);
        return items_key_list.get(pos);
    }

    public String getKeyByPosition(int position){
        return items_key_list.get(position);
    }
    public String getNameByPosition(int position){
        return items_name_list.get(position);
    }
}
