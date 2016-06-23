package com.hyj.demo.listviewrefresh;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.hyj.demo.BaseActivity;
import com.hyj.demo.R;
import com.hyj.lib.listview.ListViewRefresh;

import java.util.ArrayList;
import java.util.List;

public class ListViewRfreshActivity extends BaseActivity {
    private ListViewRefresh lvContent;
    private ListViewAdapter adapter;
    private List<Integer> lDatas;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int index = lDatas.size();

            switch (msg.what) {
                case 100:
                    for (int i = index; i < index + 2; i++) {
                        lDatas.add(0, i);
                    }
                    adapter.notifyDataSetChanged();

                    lvContent.refreshComplete();
                    break;

                case 101:
                    for (int i = index; i < index + 2; i++) {
                        lDatas.add(i);
                    }
                    adapter.notifyDataSetChanged();

                    lvContent.refreshComplete();
                    lvContent.showFooter();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listviewrefresh_main);

        myInit();
    }

    private void myInit() {
        initView();
        initData();
        initListener();
    }

    private void initView() {
        lDatas = new ArrayList<Integer>();
        lvContent = (ListViewRefresh) findViewById(R.id.refreshLV);
        adapter = new ListViewAdapter(this, lDatas, R.layout.listviewrefresh_item);
        lvContent.setAdapter(adapter);
    }

    private void initData() {
        for (int i = 0; i < 5; i++) {
            lDatas.add(i);
        }
        adapter.notifyDataSetChanged();
    }

    private void initListener() {
        lvContent.setOnPullDownRefreshListener(new ListViewRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Message messenger = handler.obtainMessage();
                messenger.what = 100;
                handler.sendMessageDelayed(messenger, 3000);
            }
        });

        lvContent.setOnPullUpRefreshListener(new ListViewRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Message messenger = handler.obtainMessage();
                messenger.what = 101;
                handler.sendMessageDelayed(messenger, 3000);
            }
        });
    }
}
