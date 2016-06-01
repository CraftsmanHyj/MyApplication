package com.hyj.demo.adapter.recycler.adapter;

import android.content.Context;

import com.hyj.demo.R;
import com.hyj.demo.adapter.recycler.bean.ChatMessage;
import com.hyj.lib.adapter.CommonAdapter;
import com.hyj.lib.adapter.ViewHolder;

import java.util.List;

/**
 * Created by zhy on 15/9/4.
 */
public class ChatAdapter extends CommonAdapter<ChatMessage> {

    public ChatAdapter(Context context, List<ChatMessage> lDatas) {
        super(context, lDatas);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage msg = getItem(position);
        if (msg.isComMeg()) {
            return ChatMessage.RECIEVE_MSG;
        } else {
            return ChatMessage.SEND_MSG;
        }
    }

    @Override
    public int getLayoutId(int position) {
        switch (getItemViewType(position)) {
            case ChatMessage.RECIEVE_MSG:
                return R.layout.recyclertest_from_msg;

            case ChatMessage.SEND_MSG:
                return R.layout.recyclertest_send_msg;
        }

        return super.getLayoutId(position);
    }

    @Override
    public void getItemView(ViewHolder holder, ChatMessage chatMessage) {
        if (chatMessage.isComMeg()) {
            holder.setText(R.id.chat_from_content, chatMessage.getContent());
            holder.setText(R.id.chat_from_name, chatMessage.getName());
            holder.setImageResource(R.id.chat_from_icon, chatMessage.getIcon());
        } else {
            holder.setText(R.id.chat_send_content, chatMessage.getContent());
            holder.setText(R.id.chat_send_name, chatMessage.getName());
            holder.setImageResource(R.id.chat_send_icon, chatMessage.getIcon());
        }
    }
}
