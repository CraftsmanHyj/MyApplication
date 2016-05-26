package com.hyj.demo.adapter.recycler;

import android.os.Bundle;
import android.widget.ListView;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.R;
import com.hyj.demo.adapter.recycler.adapter.ChatAdapter;
import com.hyj.demo.adapter.recycler.bean.ChatMessage;

public class MultiItemListViewActivity extends BaseActivity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclertest_main);

        mListView = (ListView) findViewById(R.id.id_listview_list);
        mListView.setDivider(null);
        mListView.setAdapter(new ChatAdapter(this, ChatMessage.MOCK_DATAS));
    }
}
