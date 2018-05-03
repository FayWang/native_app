package com.example.fay.uidemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.fay.uidemo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 练习下拉列表，包括构造适配器，配置数据源，增加ItemSelected监听器，实现Selected功能
 */
public class SpinnerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView text;
    Spinner spinner;
    SimpleAdapter adapter;
    List<Map<String, Object>> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);
        text = (TextView) findViewById(R.id.text);
        //初始化列表数据
        data = getData();
        text.setText("当前选择的项目是："+ data.get(0).get("text"));
        spinner = (Spinner) findViewById(R.id.spinner);
        //构造适配器
        adapter = new SimpleAdapter(this,data,R.layout.adpater_simple_listview,new String[]{"pic","text"},new int[]{R.id.imageView,R.id.textView});
        //给适配器设置下拉样式
        adapter.setDropDownViewResource(R.layout.adpater_simple_listview);
        //下拉列表配置适配器
        spinner.setAdapter(adapter);
        //下拉列表增加选择监听器
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> item= (Map<String, Object>) adapter.getItem(position);
        text.setText("当前选择的项目是： " + item.get("text"));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private List<Map<String, Object>> getData() {
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("pic", R.drawable.weibo_logo);
            map.put("text", "data " + i);
            data.add(map);
        }
        return data;
    }
}
