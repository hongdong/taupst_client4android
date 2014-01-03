package com.example.taupstairs.ui;

import java.util.Calendar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.taupstairs.R;
import com.example.taupstairs.app.TaUpstairsApplication;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Status;
import com.example.taupstairs.imageCache.SimpleImageLoader;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.util.HttpClientUtil;
import com.example.taupstairs.util.TimeUtil;

public class TaskDetailActivity extends Activity implements ItaActivity {

	private Button btn_back, btn_refresh, btn_signup;
	private Status status;
	private Holder holder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_detail);
		MainService.addActivity(TaskDetailActivity.this);
		init();
	}
	@Override
	public void init() {
		initData();
		initView();
	}
	
	private void initData() {
		TaUpstairsApplication app = (TaUpstairsApplication) getApplication();
		status = app.getStatus();
	}
	
	/*头像，昵称，性别，发布时间，来自哪个院系、年级，
	 * 标题，内容，报酬，截止时间，报名人数*/
	private class Holder {
		public ImageView img_task_detail_photo;
		public TextView txt_task_detail_nickname;
		public ImageView img_task_detail_sex;
		public TextView txt_task_detail_releasetime;
		public TextView txt_task_detail_grade;
		public TextView txt_task_detail_department;
		
		public TextView txt_task_detail_title;
		public TextView txt_task_detail_content;
		public TextView txt_task_detail_rewards;
		public TextView txt_task_detail_endtime;
		public TextView txt_task_detail_signupcount;
		public TextView txt_task_detail_messagecount;
	}
	
	private void initView() {
		btn_back = (Button)findViewById(R.id.btn_back_task_detail);
		btn_refresh = (Button)findViewById(R.id.btn_refresh_task_detail);
		btn_signup = (Button)findViewById(R.id.btn_task_detail_signup);
		
		holder = new Holder();
		holder.img_task_detail_photo = (ImageView)findViewById(R.id.img_task_detail_photo);
		holder.txt_task_detail_nickname = (TextView)findViewById(R.id.txt_task_detail_nickname);
		holder.img_task_detail_sex = (ImageView)findViewById(R.id.img_task_detail_sex);
		holder.txt_task_detail_releasetime = (TextView)findViewById(R.id.txt_task_detail_releasetime);
		holder.txt_task_detail_grade = (TextView)findViewById(R.id.txt_task_detail_grade);
		holder.txt_task_detail_department = (TextView)findViewById(R.id.txt_task_detail_department);	
		holder.txt_task_detail_title = (TextView)findViewById(R.id.txt_task_detail_title);
		holder.txt_task_detail_content = (TextView)findViewById(R.id.txt_task_detail_content);
		holder.txt_task_detail_rewards = (TextView)findViewById(R.id.txt_task_detail_rewards);
		holder.txt_task_detail_endtime = (TextView)findViewById(R.id.txt_task_detail_endtime);
		holder.txt_task_detail_signupcount = (TextView)findViewById(R.id.txt_task_detail_signupcount);
		holder.txt_task_detail_messagecount = (TextView)findViewById(R.id.txt_task_detail_messagecount);
		
		String personSex = status.getPersonSex().trim();
		
		/*头像和昵称可能是空的。空的时候还要分男女，用上默认的*/
		if (status.getPersonPhotoUrl() != null) {
			SimpleImageLoader.showImage(holder.img_task_detail_photo, 
					HttpClientUtil.PHOTO_BASE_URL + status.getPersonPhotoUrl());
		} else {
			if (personSex.equals(Person.MALE)) {
				holder.img_task_detail_photo.setImageResource(R.drawable.default_drawable);
			} else if (personSex.equals(Person.FEMALE)) {
				holder.img_task_detail_photo.setImageResource(R.drawable.default_drawable);
			}
		}
		holder.img_task_detail_photo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(TaskDetailActivity.this, PersonDataActivity.class);
				startActivity(intent);
			}
		});
		
		if (status.getPersonNickname() != null) {
			holder.txt_task_detail_nickname.setText(status.getPersonNickname());
		} else {
			if (personSex.equals(Person.MALE)) {
				holder.txt_task_detail_nickname.setText(Person.MALE_NICKNAME);
			} else if (personSex.equals(Person.FEMALE)) {
				holder.txt_task_detail_nickname.setText(Person.FEMALE_NICKNAME);
			}
		}
		
		if (personSex.equals(Person.MALE)) {
			holder.img_task_detail_sex.setImageResource(R.drawable.icon_male);
		} else if (personSex.equals(Person.FEMALE)) {
			holder.img_task_detail_sex.setImageResource(R.drawable.icon_female);
		}
		
		String displayTime = TimeUtil.getDisplayTime(Calendar.getInstance(), status.getStatusReleaseTime());
		holder.txt_task_detail_releasetime.setText(displayTime);
		
		holder.txt_task_detail_grade.setText(status.getPersonGrade());
		holder.txt_task_detail_department.setText(status.getPersonDepartment());
		holder.txt_task_detail_title.setText(status.getStatusTitle());
		holder.txt_task_detail_content.setText(status.getStatusContent());
		holder.txt_task_detail_rewards.setText(status.getStatusRewards());
		
		String endTime = TimeUtil.getDisplayTime(Calendar.getInstance(), status.getStatusEndTime());
		holder.txt_task_detail_endtime.setText(endTime);
		
		holder.txt_task_detail_signupcount.setText(status.getStatusSignUpCount());
		holder.txt_task_detail_messagecount.setText(status.getStatusMessageCount());
		
		btn_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		btn_refresh.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});
		
		btn_signup.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});
	}
	
	@Override
	public void refresh(Object... params) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MainService.removeActivity(TaskDetailActivity.this);
	}
	
}
