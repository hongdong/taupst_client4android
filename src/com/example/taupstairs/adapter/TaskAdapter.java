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
import com.example.taupstairs.bean.Status;
import com.example.taupstairs.bean.Time;
import com.example.taupstairs.imageCache.SimpleImageLoader;
import com.example.taupstairs.listener.PersonDataListener;
import com.example.taupstairs.util.HttpClientUtil;
import com.example.taupstairs.util.TimeUtil;

public class TaskAdapter extends BaseAdapter {

	private Context context;
	private List<Status> listStatus;

	public TaskAdapter(Context context, List<Status> listStatus) {
		super();
		this.context = context;
		this.listStatus = listStatus;
	}

	@Override
	public int getCount() {
		return listStatus.size();
	}

	@Override
	public Object getItem(int position) {
		return listStatus.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.task_item, null);	
		Status status = listStatus.get(position);
		
		Holder holder = new Holder();
		holder.img_fm_task_photo = (ImageView) view.findViewById(R.id.img_fm_task_photo);	
		holder.txt_fm_task_nickname = (TextView) view.findViewById(R.id.txt_fm_task_nickname);
		holder.txt_fm_task_releasetime = (TextView) view.findViewById(R.id.txt_fm_task_releasetime);
		holder.txt_fm_task_title = (TextView) view.findViewById(R.id.txt_fm_task_title);
		holder.txt_fm_task_rewards = (TextView) view.findViewById(R.id.txt_fm_task_rewards);
		holder.txt_fm_task_department = (TextView) view.findViewById(R.id.txt_fm_task_department);
		holder.txt_fm_task_grade = (TextView) view.findViewById(R.id.txt_fm_task_grade);
		holder.txt_fm_task_message = (TextView) view.findViewById(R.id.txt_fm_task_message);
		holder.txt_fm_task_signup = (TextView) view.findViewById(R.id.txt_fm_task_signup);
		
		SimpleImageLoader.showImage(holder.img_fm_task_photo, 
				HttpClientUtil.PHOTO_BASE_URL + status.getPersonPhotoUrl());
		PersonDataListener personDataListener = new PersonDataListener(context, status.getPersonId());
		holder.img_fm_task_photo.setOnClickListener(personDataListener);
		
		holder.txt_fm_task_nickname.setText(status.getPersonNickname());
		
		Time now = TimeUtil.getNow(Calendar.getInstance());
		String displayTime = TimeUtil.getDisplayTime(now, status.getStatusReleaseTime());
		holder.txt_fm_task_releasetime.setText(displayTime);
		
		holder.txt_fm_task_title.setText(status.getStatusTitle());
		holder.txt_fm_task_rewards.setText(status.getStatusRewards());
		holder.txt_fm_task_department.setText(status.getPersonDepartment());
		holder.txt_fm_task_grade.setText(status.getPersonGrade());
		holder.txt_fm_task_message.setText(status.getStatusMessageCount());
		holder.txt_fm_task_signup.setText(status.getStatusSignUpCount());
		
		return view;
	}
	
	/*头像，昵称，发布时间，标题，报酬，来自哪个院系、年级，留言数，报名数*/
	private class Holder {
		ImageView img_fm_task_photo;
		TextView txt_fm_task_nickname;
		TextView txt_fm_task_releasetime;
		TextView txt_fm_task_title;
		TextView txt_fm_task_rewards;
		TextView txt_fm_task_department;
		TextView txt_fm_task_grade;
		TextView txt_fm_task_message;
		TextView txt_fm_task_signup;
	}

}
