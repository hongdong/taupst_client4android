package com.example.taupstairs.ui.activity;

import java.util.Calendar;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taupstairs.R;
import com.example.taupstairs.app.TaUpstairsApplication;
import com.example.taupstairs.bean.Info;
import com.example.taupstairs.bean.InfoSignUp;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.imageCache.SimpleImageLoader;
import com.example.taupstairs.listener.PersonDataListener;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.util.HttpClientUtil;
import com.example.taupstairs.util.TimeUtil;

public class InfoSignUpActivity extends Activity implements ItaActivity {

	private Button btn_back;
	private Holder holder;
	private Info info;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_signup);
		MainService.addActivity(this);
		init();
	}

	@Override
	public void init() {
		initHolder();
		initData();
		initView();
	}
	
	/*头像，昵称，性别，发布时间，来自哪个院系、年级，
	 * 留言内容，任务发布人，任务标题*/
	private class Holder {
		public ImageView img_photo;
		public TextView txt_nickname;
		public ImageView img_sex;
		public TextView txt_releasetime;
		public TextView txt_grade;
		public TextView txt_department;
		
		public TextView txt_signup_string;
		public View view;
		public TextView txt_status_nickname;
		public TextView txt_status_title;
		public TextView txt_person_phone;
		public TextView txt_person_qq;
		public TextView txt_person_email;
	}
	
	private void initHolder() {
		holder = new Holder();
		holder.img_photo = (ImageView)findViewById(R.id.img_photo);
		holder.txt_nickname = (TextView)findViewById(R.id.txt_nickname);
		holder.img_sex = (ImageView)findViewById(R.id.img_sex);
		holder.txt_releasetime = (TextView)findViewById(R.id.txt_releasetime);
		holder.txt_grade = (TextView)findViewById(R.id.txt_grade);
		holder.txt_department = (TextView)findViewById(R.id.txt_department);	
		
		holder.txt_signup_string = (TextView)findViewById(R.id.txt_info_signup_string);
		holder.view = findViewById(R.id.layout_info_signup_task);
		holder.txt_status_nickname = (TextView)findViewById(R.id.txt_info_signup_nickname);
		holder.txt_status_title = (TextView)findViewById(R.id.txt_info_signup_title);
		holder.txt_person_phone = (TextView)findViewById(R.id.txt_info_signup_phone);
		holder.txt_person_qq = (TextView)findViewById(R.id.txt_info_signup_qq);
		holder.txt_person_email = (TextView)findViewById(R.id.txt_info_signup_email);
	}
	
	private void initData() {
		TaUpstairsApplication app = (TaUpstairsApplication) getApplication();
		info = app.getInfo();
		if (null == info.getInfoSignUp()) {	//可能本地已经保存了
			doGetInfoSignUpTask();
		} else {
			displaySignUp();
		}
	}
	
	private void initView() {
		btn_back = (Button)findViewById(R.id.btn_back_info_singup_detail);
		
		SimpleImageLoader.showImage(holder.img_photo, 
				HttpClientUtil.PHOTO_BASE_URL + info.getPersonPhotoUrl());
		PersonDataListener personDataListener = new PersonDataListener(this, info.getPersonId());
		holder.img_photo.setOnClickListener(personDataListener);
		holder.txt_nickname.setText(info.getPersonNickname());
		String personSex = info.getPersonSex().trim();
		if (personSex.equals(Person.MALE)) {
			holder.img_sex.setImageResource(R.drawable.icon_male);
		} else if (personSex.equals(Person.FEMALE)) {
			holder.img_sex.setImageResource(R.drawable.icon_female);
		}
		String displayTime = TimeUtil.getDisplayTime(TimeUtil.getNow(Calendar.getInstance()), info.getInfoReleaseTime());
		holder.txt_releasetime.setText(displayTime);
		holder.txt_grade.setText(info.getPersonGrade());
		holder.txt_department.setText(info.getPersonDepartment());
		
		btn_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		holder.view.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//跳转到任务详情
			}
		});
	}
	
	/**
	 * 获取详情
	 */
	private void doGetInfoSignUpTask() {
		HashMap<String, Object> taskParams = new HashMap<String, Object>();
		taskParams.put(Info.INFO_SOURCE, info.getInfoSource());
		taskParams.put(Info.INFO_TYPE, info.getInfoType());
		taskParams.put(Task.TA_GETINFO_DETAIL_ACTIVITY, Task.TA_GETINFO_DETAIL_SIGNUP);
		Task task = new Task(Task.TA_GETINFO_DETAIL, taskParams);
		MainService.addTask(task);
	}

	@Override
	public void refresh(Object... params) {
		if (params[1] != null) {
			int taskId = (Integer) params[0];
			switch (taskId) {
			case Task.TA_GETINFO_DETAIL:
				InfoSignUp infoSignUp = (InfoSignUp) params[1];
				info.setInfoSignUp(infoSignUp);
				displaySignUp();
				break;

			default:
				break;
			}
		}
	}
	
	private void displaySignUp() {
		InfoSignUp infoSignUp = info.getInfoSignUp();
		holder.txt_signup_string.setText(infoSignUp.getSignUpString());
		holder.txt_status_nickname.setText(infoSignUp.getSignUpNickname());
		holder.txt_status_title.setText("  :  " + infoSignUp.getStatusTitle());
		String contact = infoSignUp.getPersonContact();
		char[] optional = contact.toCharArray();
		if (optional[0] != '0') {
			holder.txt_person_phone.setText(infoSignUp.getPersonPhone());
			View view = findViewById(R.id.layout_info_signup_phone);
			view.setVisibility(View.VISIBLE);
		}
		if (optional[1] != '0') {
			holder.txt_person_qq.setText(infoSignUp.getPersonQq());
			View view = findViewById(R.id.layout_info_signup_qq);
			view.setVisibility(View.VISIBLE);
		}
		if (optional[2] != '0') {
			holder.txt_person_email.setText(infoSignUp.getPersonEmail());
			View view = findViewById(R.id.layout_info_signup_email);
			view.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MainService.removeActivity(this);
	}
	
}