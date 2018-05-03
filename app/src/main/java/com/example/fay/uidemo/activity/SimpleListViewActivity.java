package com.example.fay.uidemo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.SimpleAdapter;

import com.example.fay.uidemo.R;
import com.example.fay.uidemo.utils.LogUtil;
import com.example.fay.uidemo.view.HeaderListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 练习SimpleAdapter，包括构造数据适配器、view展示、滑动展示新更多数据
 * 实现下拉刷新数据的功能
 */

public class SimpleListViewActivity extends AppCompatActivity implements AbsListView.OnScrollListener, HeaderListView.IRefreshListener {
    private final String TAG = SimpleListViewActivity.class.getSimpleName();
    private HeaderListView listView;
    private SimpleAdapter adapter;
    private List<Map<String, Object>> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_listview);
        //1.绑定listView
        listView = (HeaderListView) findViewById(R.id.header_listView);
        //2.listView设置刷新监听器
        listView.setRefreshListener(this);
        //3.构造数据适配器，from表示每个item的key数组，to表示对应的UI控件的id数组
        adapter = new SimpleAdapter(this, getData(), R.layout.adpater_simple_listview, new String[]{"pic", "text"}, new int[]{R.id.imageView, R.id.textView});
        //4.listView配置数据适配器
        listView.setAdapter(adapter);
        //5.listView添加滑动事件的监听器
        listView.setOnScrollListener(this);
    }

    /**
     * 初始化数据对象
     * @return
     */
    private List<Map<String, Object>> getData() {
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("pic", R.drawable.weibo_logo);
            map.put("text", "data " + i);
            listData.add(map);
        }
        return listData;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case SCROLL_STATE_TOUCH_SCROLL:
                Map<String, Object> map = new HashMap<>();
                map.put("pic", R.drawable.weibo_logo);
                map.put("text", "pull data ");
                listData.add(map);
                adapter.notifyDataSetChanged();
                LogUtil.i(TAG, "滑动状态且手指未离开");
                break;
            case SCROLL_STATE_IDLE:
                LogUtil.i(TAG, "停止滑动");
                break;
            case SCROLL_STATE_FLING:
                LogUtil.i(TAG, "手指离开后因为惯性继续滑动");
                break;
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    /**
     * 完成下拉数据部分的填充
     */
    private void loadRefreshData() {
        for (int i = 0; i < 2; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("pic", R.drawable.weibo_logo);
            map.put("text", "new data ");
            listData.add(0,map);
        }
    }

    /**
     * 通过headerView下拉更新数据
     */
    @Override
    public void onRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadRefreshData();
                adapter.notifyDataSetChanged();
                listView.refreshComplete();
            }
        }, 2000);
    }
}
