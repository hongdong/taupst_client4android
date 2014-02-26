package com.example.taupstairs.ui.activity;

import java.util.Calendar;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.Info;
import com.example.taupstairs.bean.InfoExecTask;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.imageCache.SimpleImageLoader;
import com.example.taupstairs.listener.PersonDataListener;
import com.example.taupstairs.listener.TaskByIdListener;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.logic.TaUpstairsApplication;
import com.example.taupstairs.util.HttpClientUtil;
import com.example.taupstairs.util.TimeUtil;

public class InfoExecTaskActivity extends Activity implements ItaActivity {

	private Button btn_back;
	private Holder holder;
	private Info info;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_exec_task);
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
	 * 回复的私信内容，任务发布人，任务标题，我发给他的私信*/
	private class Holder {
		public LinearLayout layout_loading;
		public ImageView img_photo;
		public TextView txt_nickname;
		public ImageView img_sex;
		public TextView txt_releasetime;
		public TextView txt_grade;
		public TextView txt_department;
		
		public TextView txt_exec_task_string_reply;
		public View view;
		public TextView txt_status_nickname;
		public TextView txt_status_title;
		public TextView txt_exec_task_string_nickname;
		public TextView txt_exec_task_string;
	}
	
	private void initHolder() {
		holder = new Holder();
		holder.layout_loading = (LinearLayout)findViewById(R.id.layout_loading);
		
		holder.img_photo = (ImageView)findViewById(R.id.img_photo);
		holder.txt_nickname = (TextView)findViewById(R.id.txt_nickname);
		holder.img_sex = (ImageView)findViewById(R.id.img_sex);
		holder.txt_releasetime = (TextView)findViewById(R.id.txt_releasetime);
		holder.txt_grade = (TextView)findViewById(R.id.txt_grade);
		holder.txt_department = (TextView)findViewById(R.id.txt_department);	
		
		holder.txt_exec_task_string_reply = (TextView)findViewById(R.id.txt_info_exec_task_string_reply);
		holder.view = findViewById(R.id.layout_info_exec_task_task);
		holder.txt_status_nickname = (TextView)findViewById(R.id.txt_info_exec_task_nickname);
		holder.txt_status_title = (TextView)findViewById(R.id.txt_info_exec_task_title);
		holder.txt_exec_task_string_nickname = (TextView)findViewById(R.id.txt_info_exec_task_string_nickname);
		holder.txt_exec_task_string = (TextView)findViewById(R.id.txt_info_exec_task_string);
	}
	
	private void initData() {
		TaUpstairsApplication app = (TaUpstairsApplication) getApplication();
		info = app.getInfo();
		if (null == info.getInfoExecTask()) {	//可能本地已经保存了
			showProgressBar();
			doGetInfoExecTaskTask();
		} else {
			displayExecTask();
		}
	}
	
	private void initView() {
		btn_back = (Button)findViewById(R.id.btn_back_info_exec_task_detail);
		
		/*person数据的显示*/
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
	}
	
	private void showProgressBar() {
		holder.layout_loading.setVisibility(View.VISIBLE);
	}
	
	private void hideProgressBar() {
		holder.layout_loading.setVisibility(View.GONE);
	}
	
	/**
	 * 获取详情
	 */
	private void doGetInfoExecTaskTask() {
		HashMap<String, Object> taskParams = new HashMap<String, Object>();
		taskParams.put(Info.INFO_SOURCE, info.getInfoSource());
		taskParams.put(Info.INFO_TYPE, info.getInfoType());
		taskParams.put(Task.TA_GETINFO_DETAIL_ACTIVITY, Task.TA_GETINFO_DETAIL_EXECTASK);
		Task task = new Task(Task.TA_GETINFO_DETAIL, taskParams);
		MainService.addTask(task);
	}

	@Override
	public void refresh(Object... params) {
		if (params[1] != null) {
			int taskId = (Integer) params[0];
			switch (taskId) {
			case Task.TA_GETINFO_DETAIL:
				hideProgressBar();
				InfoExecTask infoExecTask = (InfoExecTask) params[1];
				info.setInfoExecTask(infoExecTask);
				displayExecTask();
				break;

			default:
				break;
			}
		}
	}
	
	/**
	 * 显示详情
	 */
	private void displayExecTask() {
		InfoExecTask infoExecTask = info.getInfoExecTask();
		holder.txt_exec_task_string_reply.setText(infoExecTask.getSignUpStringReply());
		holder.view.setOnClickListener(new TaskByIdListener(this, infoExecTask.getStatusId()));
		holder.txt_status_nickname.setText(infoExecTask.getStatusPersonNickname());
		holder.txt_status_title.setText("  :  " + infoExecTask.getStatusTitle());
		holder.txt_exec_task_string_nickname.setText(infoExecTask.getSignUpStringNickname());
		holder.txt_exec_task_string.setText("  :  " + infoExecTask.getSignUpString());
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MainService.removeActivity(this);
	}
	
}
