package com.louisgeek.louisareaselectdemo.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.promeg.pinyinhelper.Pinyin;
import com.louisgeek.louisareaselectdemo.Adapter.AreaSelectRecyclerViewAdapter;
import com.louisgeek.louisareaselectdemo.Bean.SelectAreaBean;
import com.louisgeek.louisareaselectdemo.MyPinyinBannerView;
import com.louisgeek.louisareaselectdemo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MySelectAreaActivity extends AppCompatActivity {
    List<SelectAreaBean> selectAreaBeanList;
    RecyclerView id_rv;
    private static final String TAG = "MySelectAreaActivity";

/*    public static String[] stringCitys = new String[]{
            "合肥", "张家界", "宿州", "淮北", "阜阳", "蚌埠", "淮南", "滁州",
            "马鞍山", "芜湖", "铜陵", "安庆", "安阳", "黄山", "六安", "巢湖",
            "池州", "宣城", "亳州", "明光", "天长", "桐城", "宁国",
            "徐州", "连云港", "宿迁", "淮安", "盐城", "扬州", "泰州",
            "南通", "镇江", "常州", "无锡", "苏州", "江阴", "宜兴",
            "邳州", "新沂", "金坛", "溧阳", "常熟", "张家港", "太仓",
            "昆山", "吴江", "如皋", "通州", "海门", "启东", "大丰",
            "东台", "高邮", "仪征", "江都", "扬中", "句容", "丹阳",
            "兴化", "姜堰", "泰兴", "靖江", "福州", "南平", "三明",
            "复兴", "高领", "共兴", "柯家寨", "匹克", "匹夫", "旗舰", "启航",
            "如阳", "如果", "科比", "韦德", "诺维斯基", "麦迪", "乔丹", "姚明"
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_select_area);
/**
 * 重庆待处理。。。
 */
        initData();

        final TextView id_tv_sticky_header_view = (TextView) findViewById(R.id.id_tv_sticky_header_view);
        //id_tv_sticky_header_view.setBackgroundColor(ContextCompat.getColor(this,R.color.colorAccent));
        final MyPinyinBannerView idmpbv = (MyPinyinBannerView) findViewById(R.id.id_mpbv);
        TextView textshowbox = (TextView) findViewById(R.id.text_showbox);
        idmpbv.setTextShowBox(textshowbox);
        idmpbv.setSelectAreaBeanList(selectAreaBeanList);
        idmpbv.setOnSlidedListener(new MyPinyinBannerView.OnSlidedListener() {
            @Override
            public void onSlided(String pinyinFirst) {
                dealListstayWhere(pinyinFirst);
            }
        });
        id_rv = (RecyclerView) findViewById(R.id.id_rv);
        id_rv.setLayoutManager(new LinearLayoutManager(this));
        AreaSelectRecyclerViewAdapter areaSelectRecyclerViewAdapter = new AreaSelectRecyclerViewAdapter(selectAreaBeanList);
        areaSelectRecyclerViewAdapter.setOnItemClickListener(new AreaSelectRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                Toast.makeText(MySelectAreaActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
            }
        });
        id_rv.setAdapter(areaSelectRecyclerViewAdapter);
        id_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //Log.d(TAG, "onScrolled: dx:" + dx + ",dy:" + dy);

                /**
                 * 头部停留   参考自网络
                 */
                //返回指定位置的childView，这里也就是取得item的头部布局stickyInfoView，x,y只要是id_tv_sticky_header_view里面就行
                View stickyInfoView = recyclerView.findChildViewUnder(
                        id_tv_sticky_header_view.getMeasuredWidth() / 2, id_tv_sticky_header_view.getMeasuredHeight()/2);
                if (stickyInfoView != null && stickyInfoView.getContentDescription() != null) {
                    id_tv_sticky_header_view.setText(stickyInfoView.getContentDescription().toString());
                }
                //返回的是固定在屏幕上方那个id_tv_sticky_header_view下面1像素位置的RecyclerView的item，
                // 根据这个item来更新id_tv_sticky_header_view要translate多少距离。
                Log.d(TAG, "onScrolled: id_tv_sticky_header_view.getMeasuredHeight()"+id_tv_sticky_header_view.getMeasuredHeight());
                View nextInfoView = recyclerView.findChildViewUnder(
                        id_tv_sticky_header_view.getMeasuredWidth() / 2, id_tv_sticky_header_view.getMeasuredHeight() + 1);
                //如果tag为HAS_STICKY_VIEW，表示当前item需要展示头部布局，那么根据这个item的getTop和id_tv_sticky_header_view的
                // 高度相差的距离来滚动id_tv_sticky_header_view；如果tag为NONE_STICKY_VIEW，
                // 表示当前item不需要展示头部布局，那么就不会引起id_tv_sticky_header_view的滚动。
                if (nextInfoView != null && nextInfoView.getTag() != null) {
                    int moveInfoViewStatus = (int) nextInfoView.getTag();
                    //view 顶部距离Y
                    int dealtY = nextInfoView.getTop() - id_tv_sticky_header_view.getMeasuredHeight();
                    Log.d(TAG, "onScrolled: dealtY:" + dealtY + ",getTop:" + nextInfoView.getTop());
                    if (moveInfoViewStatus == AreaSelectRecyclerViewAdapter.HAS_STICKY_VIEW) {
                        if (nextInfoView.getTop() > 0) {
                            //nextInfoView距离最顶部还有距离
                            //位移
                            id_tv_sticky_header_view.setTranslationY(dealtY);
                            Log.d(TAG, "onScrolled: dealtY:"+dealtY);
                        } else {
                            //nextInfoView已经往上移动到或超出最顶部区域了
                            //归位
                            id_tv_sticky_header_view.setTranslationY(0);
                            Log.d(TAG, "onScrolled: dealtY: setTranslationY(0)");
                        }
                    } else if (moveInfoViewStatus == AreaSelectRecyclerViewAdapter.NONE_STICKY_VIEW) {
                        //nextInfoView 没有头部 不做移动改变
                        //归位
                        id_tv_sticky_header_view.setTranslationY(0);
                        Log.d(TAG, "onScrolled: NONE_STICKY_VIEW: setTranslationY(0)");
                    }else if(moveInfoViewStatus == AreaSelectRecyclerViewAdapter.FIRST_STICKY_VIEW){
                        //
                        Log.d(TAG, "onScrolled: FIRST_STICKY_VIEW");
                    }
                }

            }
        });
    }

    private void dealListstayWhere(String pinyinFirst) {
        for (int i = 0; i < selectAreaBeanList.size(); i++) {
            String firstPinyin = selectAreaBeanList.get(i).getPinyin().charAt(0) + "";
            if (firstPinyin.equals(pinyinFirst)) {
                id_rv.smoothScrollToPosition(i);
                break;
            }
        }
    }

    private void initData() {
        //
        selectAreaBeanList = new ArrayList<>();
        for (int i = 0; i < SelectAreaBean.provinceArray.length; i++) {
            SelectAreaBean selectAreaBean = new SelectAreaBean();
            selectAreaBean.setID(i);
            selectAreaBean.setAreaID("key_" + i);
            selectAreaBean.setName(SelectAreaBean.provinceArray[i]);
            selectAreaBean.setPinyin(parseChineseToPinyin(SelectAreaBean.provinceArray[i]));
            selectAreaBeanList.add(selectAreaBean);
        }
        //比较器
        PinyinComparator pinyinComparator = new PinyinComparator();
        //排序
        Collections.sort(selectAreaBeanList, pinyinComparator);
    }

    /**
     * /**
     * 如果c为汉字，则返回大写拼音；如果c不是汉字，则返回String.valueOf(c)
     * Pinyin.toPinyin(char c)
     * c为汉字，则返回true，否则返回false
     * Pinyin.isChinese(char c)
     *
     * @param inStr
     * @return
     */
    private String parseChineseToPinyin(String inStr) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < inStr.length(); i++) {
            stringBuilder.append(Pinyin.toPinyin(inStr.charAt(i)));
        }
        return stringBuilder.toString();
    }


    public class PinyinComparator implements Comparator<SelectAreaBean> {
        @Override
        public int compare(SelectAreaBean first, SelectAreaBean second) {
            return first.getPinyin().compareTo(second.getPinyin());
        }
    }

}
