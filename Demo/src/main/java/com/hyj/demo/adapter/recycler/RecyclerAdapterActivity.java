package com.hyj.demo.adapter.recycler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.R;
import com.hyj.lib.adapter.CommonAdapter;
import com.hyj.lib.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecyclerAdapterActivity extends BaseActivity {
    private List<String> mDatas = new ArrayList<>(Arrays.asList("MultiItem ListView",
            "RecyclerView",
            "MultiItem RecyclerView", "RecyclerViewWithHeader"));
    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclertest_main);

        mListView = ((ListView) findViewById(R.id.id_listview_list));
        mListView.setAdapter(new CommonAdapter<String>(this, mDatas, R.layout.recyclertest_item_list) {
            @Override
            public void getItemView(ViewHolder holder, String item) {
                holder.setText(R.id.id_item_list_title, item);
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                    default:
                        intent = new Intent(RecyclerAdapterActivity.this, MultiItemListViewActivity.class);
                        break;
                    case 1:
                        intent = new Intent(RecyclerAdapterActivity.this, RecyclerViewActivity.class);
                        break;
                    case 2:
                        intent = new Intent(RecyclerAdapterActivity.this, MultiItemRvActivity.class);
                        break;
                    case 3:
                        intent = new Intent(RecyclerAdapterActivity.this, RvWidthHeaderActivity.class);
                        break;

                }
                if (intent != null)
                    startActivity(intent);
            }
        });
    }
}
