package com.example.fay.uidemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.fay.uidemo.activity.SimpleListViewActivity;
import com.example.fay.uidemo.activity.SpinnerActivity;
import com.example.fay.uidemo.activity.WebViewActivity;

/**
 * demo首页，展示常用控件列表，点击进入每个控件的demo页面
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private String[] data = {SimpleListViewActivity.class.getSimpleName(),
            SpinnerActivity.class.getSimpleName(),
            WebViewActivity.class.getSimpleName()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //1.绑定listView控件
        listView = (ListView) findViewById(R.id.array_list);
        //2.构造数据适配器,指定展示数据的控件布局和需要展示的数据源
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        //3.给listView配置数据适配器
        listView.setAdapter(adapter);
        //4.listView添加点击事件的监听器
        listView.setOnItemClickListener(this);
    }

    /**
     * 重写点击处理方法，根据所点击item的名字打开对应的activity
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String name = (String) listView.getItemAtPosition(position);
        Intent intent = new Intent();
        intent.setClassName(this, this.getPackageName() + ".activity." + name);
        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startActivity(intent);
    }

}
