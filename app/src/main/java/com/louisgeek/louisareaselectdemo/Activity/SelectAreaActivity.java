package com.louisgeek.louisareaselectdemo.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.louisgeek.louisareaselectdemo.Adapter.ContentBaseAdapter;
import com.louisgeek.louisareaselectdemo.R;
import com.woozzu.android.indexablelistview.widget.IndexableListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectAreaActivity extends AppCompatActivity {

    private List<String> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_area);
       IndexableListView idilv = (IndexableListView) findViewById(R.id.id_ilv);

        initData();

        ContentBaseAdapter adapter = new ContentBaseAdapter(mItems,this);

        idilv = (IndexableListView) findViewById(R.id.id_ilv);
        idilv.setAdapter(adapter);
        idilv.setFastScrollEnabled(true);//打开字母快速滑动的功能
    }

    private void initData() {

        mItems = new ArrayList<String>();
        mItems.add("Diary");
        mItems.add("Steve");
        mItems.add("Inheritance");
        mItems.add("1845822");
        mItems.add("The");
        mItems.add("The LEGO");
        mItems.add("Explosive");
        mItems.add("Catching");
        mItems.add("Elder");
        mItems.add("Death");
        mItems.add("Diary");
        mItems.add("Steve Jobs");
        mItems.add("Inheritance The");
        mItems.add("The Hunger Games");
        mItems.add("The LEGO Ideas");
        mItems.add("A Stephanie Plum Novel");
        mItems.add("Catching Fire)");
        mItems.add("Elder Scrolls");
        mItems.add("Death Comes");
        Collections.sort(mItems);


    }
}
