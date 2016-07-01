package com.hyj.demo.adapter.recycler;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.R;
import com.hyj.lib.adapter.ViewHolder;
import com.hyj.lib.adapter.recycler.CommonAdapter;
import com.hyj.lib.adapter.recycler.DividerItemDecoration;
import com.hyj.lib.adapter.recycler.SectionAdapter;

import java.util.ArrayList;
import java.util.List;

public class RvWidthHeaderActivity extends BaseActivity {
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

        SectionAdapter<String> adapter = new SectionAdapter<String>(this, R.layout.recyclertest_item_list, mDatas) {
            @Override
            public int sectionHeaderLayoutId() {
                return R.layout.recyclertest_header;
            }

            @Override
            public int sectionTitleTextViewId() {
                return R.id.id_header_title;
            }

            @Override
            public String getTitle(String s) {
                return s.substring(0, 1);
            }

            @Override
            public void getItemView(ViewHolder holder, String s) {
                holder.setText(R.id.id_item_list_title, s);
            }
        };
        adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener<String>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, String o, int position) {
                Toast.makeText(RvWidthHeaderActivity.this, "Click:" + position + " => " + o, Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    private void initDatas() {
        for (int i = 1; i < 3; i++) {
            mDatas.add("A" + i);
        }

        for (int i = 1; i < 6; i++) {
            mDatas.add("B" + i);
        }

        for (int i = 1; i < 7; i++) {
            mDatas.add("C" + i);
        }

        for (int i = 1; i < 9; i++) {
            mDatas.add("D" + i);
        }
    }
}
