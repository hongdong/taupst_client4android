package com.example.taupstairs.adapter;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.taupstairs.R;
import com.example.taupstairs.bean.MessageContent;
import com.example.taupstairs.string.NormalString;

public class MessageContentAdapter extends BaseAdapter {

	private Context context;
	private List<Map<String, Object>> list;
	private SpannableString spannableString;
	
	public MessageContentAdapter(Context context, List<Map<String, Object>> list) {
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
		TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.message_content, null);
		
		Map<String, Object> item = list.get(position);
		String content = (String) item.get(MessageContent.CONTENT);
		spannableString = new SpannableString(content);
		if (position > 0) {
			spanName(item);
		} 
		spanExp(content);
		textView.setText(spannableString);
		return textView;
	}
	
	/**
	 * 名字高亮
	 * @param item	一个留言项
	 */
	private void spanName(Map<String, Object> item) {
		int reply_start = (Integer) item.get(MessageContent.REPLY_START);
		int reply_end = (Integer) item.get(MessageContent.REPLY_END);
		int receive_start = (Integer) item.get(MessageContent.RECEIVE_START);
		int receive_end = (Integer) item.get(MessageContent.RECEIVEY_END);
		int green = context.getResources().getColor(R.color.green);
		
		spannableString.setSpan(new ForegroundColorSpan(green), 
				reply_start, reply_end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		spannableString.setSpan(new StyleSpan(Typeface.BOLD), 
				reply_start, reply_end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		spannableString.setSpan(new ForegroundColorSpan(green), 
				receive_start, receive_end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		spannableString.setSpan(new StyleSpan(Typeface.BOLD), 
				receive_start, receive_end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
	}
	
	/**
	 * 表情转换
	 * @param content 留言内容
	 */
	private void spanExp(String content) {
		Pattern pattern = Pattern.compile(NormalString.Pattern.EXPRESSION);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			int start = matcher.start();
			int end = matcher.end();
			String tempString = content.substring(start + 4, end);
			int number = Integer.parseInt(tempString);
			if (number >= 0 && number < 105) {
				try {
					Field field = R.drawable.class.getDeclaredField("smiley_" + number);
					int resourceId = Integer.parseInt(field.get(null).toString());
					Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
					Bitmap smallBitmap = Bitmap.createScaledBitmap(bitmap, 30, 30, true);
					ImageSpan imageSpan = new ImageSpan(context, smallBitmap);
					spannableString.setSpan(imageSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
	}

}
