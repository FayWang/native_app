package com.example.fay.uidemo.listview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.fay.uidemo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 练习SimpleAdapter，包括配置数据源、view展示、from表示每个item的key，to表示对应的UI控件
 */

public class SimpleListViewDemo extends AppCompatActivity implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {
    private final String TAG = "SimpleListViewDemo";
    private ListView listView;
    private SimpleAdapter adapter;
    private List<Map<String, Object>> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list_view_demo);
        listView = (ListView) findViewById(R.id.listView2);
        adapter = new SimpleAdapter(this, getData(), R.layout.simple_adpater, new String[]{"pic", "text"}, new int[]{R.id.imageView, R.id.textView});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);
    }

    private List<Map<String, Object>> getData() {
        for (int i = 0; i < 10; i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("pic",R.drawable.weibo_logo);
            map.put("text","data " + i);
            listData.add(map);
        }
        return listData;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState){
            case SCROLL_STATE_FLING:
                Map<String,Object> map = new HashMap<>();
                map.put("pic",R.drawable.weibo_logo);
                map.put("text","scorll data ");
                listData.add(map);
                adapter.notifyDataSetChanged();
                Log.i(TAG,"滑动手指未离开");
                break;
            case SCROLL_STATE_IDLE:
                Log.i(TAG,"停止滑动");
                break;
            case SCROLL_STATE_TOUCH_SCROLL:
                Log.i(TAG,"手指离开后因为惯性继续滑动");
                break;
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
