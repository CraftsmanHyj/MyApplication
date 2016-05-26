package com.hyj.demo.adapter.recycler;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.R;
import com.hyj.demo.tools.adapter.ViewHolder;
import com.hyj.demo.tools.adapter.recycle.CommonAdapter;
import com.hyj.demo.tools.adapter.recycle.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private List<String> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclertest_recyclerview);

        initDatas();

        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        mRecyclerView.setAdapter(new CommonAdapter<String>(this, mDatas, R.layout.recyclertest_item_list) {
            @Override
            public void getItemView(ViewHolder holder, String s) {
                holder.setText(R.id.id_item_list_title, s);
            }
        });
    }

    private void initDatas() {
        for (int i = 'A'; i < 'z'; i++) {
            mDatas.add((char) i + "");
        }
    }
}
