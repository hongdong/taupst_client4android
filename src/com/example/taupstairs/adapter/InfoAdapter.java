package com.example.taupstairs.adapter;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.Info;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Time;
import com.example.taupstairs.imageCache.SimpleImageLoader;
import com.example.taupstairs.listener.PersonDataListener;
import com.example.taupstairs.util.HttpClientUtil;
import com.example.taupstairs.util.TimeUtil;

public class InfoAdapter extends BaseAdapter {

	private Context context;
	private List<Info> infos;
	
	public InfoAdapter(Context context, List<Info> infos) {
		this.context = context;
		this.infos = infos;
	}

	@Override
	public int getCount() {
		return infos.size();
	}

	@Override
	public Object getItem(int arg0) {
		return infos.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.info_item, null);	
		Info info = infos.get(position);
		
		Holder holder = new Holder();
		holder.img_photo = (ImageView) view.findViewById(R.id.img_fm_info_photo);
		holder.txt_nickname = (TextView) view.findViewById(R.id.txt_fm_info_nickname);
		holder.txt_releasetime = (TextView) view.findViewById(R.id.txt_fm_info_releasetime);
		holder.txt_content = (TextView) view.findViewById(R.id.txt_fm_info_content);
		
		SimpleImageLoader.showImage(holder.img_photo, HttpClientUtil.PHOTO_BASE_URL + info.getPersonPhotoUrl());
		PersonDataListener personDataListener = 
				new PersonDataListener(context, info.getPersonId(), Person.PERMISSION_HIDE);
		holder.img_photo.setOnClickListener(personDataListener);
		
		holder.txt_nickname.setText(info.getPersonNickname());
		
		Time now = TimeUtil.getNow(Calendar.getInstance());
		String displayTime = TimeUtil.getDisplayTime(now, info.getInfoReleaseTime());
		holder.txt_releasetime.setText(displayTime);
		
		holder.txt_content.setText(info.getInfoContent());
		
		return view;
	}
	
	private class Holder {
		ImageView img_photo;
		TextView txt_nickname;
		TextView txt_releasetime;
		TextView txt_content;
	}

}
