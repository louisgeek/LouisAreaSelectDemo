package com.louisgeek.louisareaselectdemo.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.louisgeek.louisareaselectdemo.Bean.SelectAreaBean;
import com.louisgeek.louisareaselectdemo.R;

import java.util.List;

/**
 * Created by louisgeek on 2016/8/15.
 */
public class AreaSelectRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final int FIRST_STICKY_VIEW = 1;
    public static final int HAS_STICKY_VIEW = 2;
    public static final int NONE_STICKY_VIEW = 3;


    public AreaSelectRecyclerViewAdapter(List<SelectAreaBean> selectAreaBeanList) {
        mSelectAreaBeanList = selectAreaBeanList;
    }

    List<SelectAreaBean> mSelectAreaBeanList;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item,parent,false);
        return new AreaSelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        AreaSelectViewHolder areaSelectViewHolder= (AreaSelectViewHolder) holder;
        areaSelectViewHolder.tvName.setText(mSelectAreaBeanList.get(position).getName());
        areaSelectViewHolder.rlContentWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v,position);
            }
        });




        String nowPinyinFirst=mSelectAreaBeanList.get(position).getPinyin().charAt(0)+"";

        //
        areaSelectViewHolder.itemView.setContentDescription(nowPinyinFirst);
        //
        if (position == 0) {
            areaSelectViewHolder.tvStickyHeader.setVisibility(View.VISIBLE);
            areaSelectViewHolder.tvStickyHeader.setText(nowPinyinFirst);
            areaSelectViewHolder.itemView.setTag(FIRST_STICKY_VIEW);
        }else{
            String previousPinyinFirst=mSelectAreaBeanList.get(position-1).getPinyin().charAt(0)+"";
            if (previousPinyinFirst.equals(nowPinyinFirst)){
                areaSelectViewHolder.tvStickyHeader.setVisibility(View.GONE);
                areaSelectViewHolder.itemView.setTag(NONE_STICKY_VIEW);
            }else{
                areaSelectViewHolder.tvStickyHeader.setVisibility(View.VISIBLE);
                areaSelectViewHolder.tvStickyHeader.setText(nowPinyinFirst);
                areaSelectViewHolder.itemView.setTag(HAS_STICKY_VIEW);
            }

        }


    }

    @Override
    public int getItemCount() {
        return mSelectAreaBeanList.size();
    }


    class AreaSelectViewHolder extends RecyclerView.ViewHolder{
        TextView tvStickyHeader, tvName;
        RelativeLayout rlContentWrapper;
        public AreaSelectViewHolder(View itemView) {
            super(itemView);
            tvStickyHeader = (TextView) itemView.findViewById(R.id.id_tv_sticky_header_view);
            rlContentWrapper = (RelativeLayout) itemView.findViewById(R.id.id_rl_content_wrapper);
            tvName = (TextView) itemView.findViewById(R.id.id_tv_name);
        }
    }

    public interface OnItemClickListener{
        void  onItemClick(View v,int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    OnItemClickListener onItemClickListener;

}
