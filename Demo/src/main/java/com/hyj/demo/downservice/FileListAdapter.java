package com.hyj.demo.downservice;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hyj.demo.R;
import com.hyj.demo.http.download.DownService;
import com.hyj.demo.http.download.FileInfo;
import com.hyj.demo.tools.adapter.CommonAdapter;
import com.hyj.demo.tools.adapter.ViewHolder;

import java.util.List;

public class FileListAdapter extends CommonAdapter<FileInfo> {

    public FileListAdapter(Context context, List<FileInfo> lDatas, int layoutItemID) {
        super(context, lDatas, layoutItemID);
    }

    @Override
    public void getItemView(ViewHolder holder, final FileInfo file) {
        holder.setText(R.id.downTvFileName, file.getFileName());
        holder.setProgress(R.id.downPbProgress, file.getProgress());

        final Button btStart = holder.getView(R.id.downBtBegin);
        final Button btPause = holder.getView(R.id.downBtPause);

        btStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btStart.setEnabled(false);
                btPause.setEnabled(true);

                Intent intent = new Intent(context, DownService.class);
                intent.setAction(DownService.ACTION_PREPARE);
                intent.putExtra(DownService.DOWNINFO, file);
                context.startService(intent);
            }
        });

        btPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btStart.setEnabled(true);
                btPause.setEnabled(false);

                Intent intent = new Intent(context, DownService.class);
                intent.setAction(DownService.ACTION_PAUSE);
                intent.putExtra(DownService.DOWNINFO, file);
                context.startService(intent);
            }
        });
    }
}