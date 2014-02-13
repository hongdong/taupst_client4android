package com.example.taupstairs.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.Message;
import com.example.taupstairs.bean.MessageContent;
import com.example.taupstairs.bean.Time;
import com.example.taupstairs.imageCache.SimpleImageLoader;
import com.example.taupstairs.listener.ByIdReplyListMessageListener;
import com.example.taupstairs.listener.ByIdReplyMessageListener;
import com.example.taupstairs.listener.PersonDataListener;
import com.example.taupstairs.ui.activity.TaskByIdActivity;
import com.example.taupstairs.util.HttpClientUtil;
import com.example.taupstairs.util.TimeUtil;

public class ByIdMessageAdapter extends BaseAdapter {

	private TaskByIdActivity context;
	private List<Message> messages;
	
	public ByIdMessageAdapter(TaskByIdActivity context, List<Message> messages) {
		this.context = context;
		this.messages = messages;
	}

	@Override
	public int getCount() {
		return messages.size();
	}

	@Override
	public Object getItem(int position) {
		return messages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		view = LayoutInflater.from(context).inflate(R.layout.message_item, null);
		Message message = messages.get(position);
		
		Holder holder = new Holder();
		holder.img_message_photo = (ImageView)view.findViewById(R.id.img_message_photo);
		holder.txt_message_nickname = (TextView)view.findViewById(R.id.txt_message_nickname);
		holder.txt_message_reply = (TextView)view.findViewById(R.id.txt_message_reply);
		holder.txt_message_time = (TextView)view.findViewById(R.id.txt_message_releasetime);
		holder.list_message_content = (ListView)view.findViewById(R.id.list_message_content);
		
		SimpleImageLoader.showImage(holder.img_message_photo, 
				HttpClientUtil.PHOTO_BASE_URL + message.getPersonPhotoUrl());
		PersonDataListener personDataListener = new PersonDataListener(context, message.getPersonId());
		holder.img_message_photo.setOnClickListener(personDataListener);
		
		holder.txt_message_nickname.setText(message.getPersonNickname());
		
		Time now = TimeUtil.getNow(Calendar.getInstance());
		String displayTime = TimeUtil.getDisplayTime(now, message.getMessageTime());
		holder.txt_message_time.setText(displayTime);
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<MessageContent> contents = message.getMessageContents();
		for (int i = 0; i < contents.size(); i++) {
			MessageContent content = contents.get(i);		
			String t = content.getContent();				
			Map<String, Object> item = new HashMap<String, Object>();
			if (i > 0) {
				String replyNickname = content.getReplyNickname();
				String receiveNickname = content.getReceiveNickname();
				String text = replyNickname + MessageContent.REPLY_TEXT + 
						receiveNickname + MessageContent.REPLY_TEMP + t;
				item.put(MessageContent.CONTENT, text);			
				item.put(MessageContent.REPLY_START, 0);
				item.put(MessageContent.REPLY_END, replyNickname.length());
				item.put(MessageContent.RECEIVE_START, 
						replyNickname.length() + MessageContent.REPLY_TEXT_LENNTH);
				item.put(MessageContent.RECEIVEY_END, 
						replyNickname.length() + MessageContent.REPLY_TEXT_LENNTH + receiveNickname.length());
			} else {	//如果是根留言不要显示 xx 回复 xx
				content.setReplyId(message.getPersonId());
				content.setReplyNickname(message.getPersonNickname());
				item.put(MessageContent.CONTENT, t);
			}
					
			list.add(item);
		}
		
		MessageContentAdapter adapter = new MessageContentAdapter(context, list);
		holder.list_message_content.setAdapter(adapter);
		
		String messageId = message.getMessageId();
		String replyId = message.getPersonId();
		String replyNickname = message.getPersonNickname();
		ByIdReplyMessageListener replyMessageListener = 
				new ByIdReplyMessageListener(context, messageId, replyId, replyNickname);
		holder.txt_message_reply.setOnClickListener(replyMessageListener);
		
		ByIdReplyListMessageListener replyListMessageListener = 
				new ByIdReplyListMessageListener(context, messageId, contents);
		holder.list_message_content.setOnItemClickListener(replyListMessageListener);
		
		return view;
	}
	
	private class Holder {
		ImageView img_message_photo;
		TextView txt_message_nickname;
		TextView txt_message_reply;
		TextView txt_message_time;
		ListView list_message_content;
	}

}
