package com.hyj.demo.adapter.recycler;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.R;
import com.hyj.demo.adapter.recycler.adapter.ChatAdapterForRv;
import com.hyj.demo.adapter.recycler.bean.ChatMessage;
import com.hyj.demo.tools.adapter.recycle.CommonAdapter;

public class MultiItemRvActivity extends BaseActivity {
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclertest_recyclerview);

        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ChatAdapterForRv adapter = new ChatAdapterForRv(this, ChatMessage.MOCK_DATAS);
        adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener<ChatMessage>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, ChatMessage o, int position) {
                Toast.makeText(MultiItemRvActivity.this, "Click:" + position + " => " + o.getContent(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, ChatMessage o, int position) {
                Toast.makeText(MultiItemRvActivity.this, "LongClick:" + position + " => " + o.getContent(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        mRecyclerView.setAdapter(adapter);
    }
}
