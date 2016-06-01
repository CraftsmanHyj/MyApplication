package com.hyj.demo.adapter.recycler.adapter;

import android.content.Context;

import com.hyj.demo.R;
import com.hyj.demo.adapter.recycler.bean.ChatMessage;
import com.hyj.lib.adapter.ViewHolder;
import com.hyj.lib.adapter.recycler.CommonAdapter;

import java.util.List;

/**
 * Created by zhy on 15/9/4.
 */
public class ChatAdapterForRv extends CommonAdapter<ChatMessage> {

    public ChatAdapterForRv(Context context, List<ChatMessage> datas) {
        super(context, datas);
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage msg = getItem(position);
        if (msg.isComMeg()) {
            return ChatMessage.RECIEVE_MSG;
        }
        return ChatMessage.SEND_MSG;
    }

    @Override
    public int getLayoutId(int viewType) {
        if (viewType == ChatMessage.RECIEVE_MSG) {
            return R.layout.recyclertest_from_msg;
        } else
            return R.layout.recyclertest_send_msg;
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
