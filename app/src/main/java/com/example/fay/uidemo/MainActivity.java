package com.example.fay.uidemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.fay.uidemo.listview.SimpleListViewDemo;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private String[] data = {SimpleListViewDemo.class.getSimpleName()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        listView = (ListView) findViewById(R.id.listView1);
        // listview使用的布局文件，这里采用的是系统默认的listview布局
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String name = (String) listView.getItemAtPosition(position);
        Intent intent = null;
        if(SimpleListViewDemo.class.getSimpleName().equals(name)) {
            intent = new Intent(this, SimpleListViewDemo.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        }
        if(intent != null) {
            startActivity(intent);
        }
    }

}
