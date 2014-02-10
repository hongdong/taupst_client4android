package com.example.taupstairs.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.MessageContent;

public class InfoMessageAdapter extends BaseAdapter {

	private Context context;
	private List<Map<String, Object>> list;
	
	public InfoMessageAdapter(Context context, List<Map<String, Object>> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.info_message_message_content, null);
		
		Map<String, Object> item = list.get(position);
		String content = (String) item.get(MessageContent.CONTENT);
		int reply_start = (Integer) item.get(MessageContent.REPLY_START);
		int reply_end = (Integer) item.get(MessageContent.REPLY_END);
		int receive_start = (Integer) item.get(MessageContent.RECEIVE_START);
		int receive_end = (Integer) item.get(MessageContent.RECEIVEY_END);
		int green = context.getResources().getColor(R.color.green);
		SpannableString spannableString = new SpannableString(content);
		spannableString.setSpan(new ForegroundColorSpan(green), 
				reply_start, reply_end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		spannableString.setSpan(new StyleSpan(Typeface.BOLD), 
				reply_start, reply_end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		spannableString.setSpan(new ForegroundColorSpan(green), 
				receive_start, receive_end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		spannableString.setSpan(new StyleSpan(Typeface.BOLD), 
				receive_start, receive_end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		textView.setText(spannableString);
		return textView;
	}

}
