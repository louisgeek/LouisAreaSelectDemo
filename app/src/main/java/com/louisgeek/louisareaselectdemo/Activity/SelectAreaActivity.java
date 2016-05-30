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
        mItems.add("Diary of a Wimpy Kid 6: Cabin Fever");
        mItems.add("Steve Jobs");
        mItems.add("Inheritance (The Inheritance Cycle)");
        mItems.add("11/22/63: A Novel");
        mItems.add("The Hunger Games");
        mItems.add("The LEGO Ideas Book");
        mItems.add("Explosive Eighteen: A Stephanie Plum Novel");
        mItems.add("Catching Fire (The Second Book of the Hunger Games)");
        mItems.add("Elder Scrolls V: Skyrim: Prima Official Game Guide");
        mItems.add("Death Comes to Pemberley");
        mItems.add("Diary of a Wimpy Kid 6: Cabin Fever");
        mItems.add("Steve Jobs");
        mItems.add("Inheritance (The Inheritance Cycle)");
        mItems.add("11/22/63: A Novel");
        mItems.add("The Hunger Games");
        mItems.add("The LEGO Ideas Book");
        mItems.add("Explosive Eighteen: A Stephanie Plum Novel");
        mItems.add("Catching Fire (The Second Book of the Hunger Games)");
        mItems.add("Elder Scrolls V: Skyrim: Prima Official Game Guide");
        mItems.add("Death Comes to Pemberley");
        Collections.sort(mItems);


    }
}
