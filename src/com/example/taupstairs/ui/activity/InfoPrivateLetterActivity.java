package com.example.taupstairs.ui.activity;

import java.util.HashMap;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.taupstairs.R;
import com.example.taupstairs.bean.Info;
import com.example.taupstairs.bean.InfoPrivateLetter;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.imageCache.SimpleImageLoader;
import com.example.taupstairs.listener.PersonDataListener;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.logic.TaUpstairsApplication;
import com.example.taupstairs.util.HttpClientUtil;
import com.example.taupstairs.util.TimeUtil;

public class InfoPrivateLetterActivity extends Activity implements ItaActivity {

	private Button btn_back;
	private Holder holder;
	private Info info;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_private_letter);
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
	 * 私信内容*/
	private class Holder {
		public LinearLayout layout_loading;
		public ImageView img_photo;
		public TextView txt_nickname;
		public ImageView img_sex;
		public TextView txt_releasetime;
		public TextView txt_grade;
		public TextView txt_department;
		public TextView txt_letter;
		public TextView txt_reply;
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
		holder.txt_letter = (TextView)findViewById(R.id.txt_info_private_letter_letter);
		holder.txt_reply = (TextView)findViewById(R.id.txt_info_private_letter_reply);
	}
	
	private void initData() {
		TaUpstairsApplication app = (TaUpstairsApplication) getApplication();
		info = app.getInfo();
		if (null == info.getInfoPrivateLetter()) {	//可能本地已经保存了
			showProgressBar();
			doGetInfoPrivateLetterTask();
		} else {
			displayPritaveLetter();
		}
	}
	
	private void initView() {
		btn_back = (Button)findViewById(R.id.btn_back_info_private_letter);
		
		/*person数据的显示*/
		SimpleImageLoader.showImage(holder.img_photo, 
				HttpClientUtil.PHOTO_BASE_URL + info.getPersonPhotoUrl());
		PersonDataListener personDataListener = 
				new PersonDataListener(this, info.getPersonId(), Person.PERMISSION_PUBLIC);
		holder.img_photo.setOnClickListener(personDataListener);
		holder.txt_nickname.setText(info.getPersonNickname());
		String personSex = info.getPersonSex().trim();
		if (personSex.equals(Person.MALE)) {
			holder.img_sex.setImageResource(R.drawable.icon_male);
		} else if (personSex.equals(Person.FEMALE)) {
			holder.img_sex.setImageResource(R.drawable.icon_female);
		}
		String displayTime = TimeUtil.getDisplayTime(TimeUtil.getNow(), info.getInfoReleaseTime());
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
	private void doGetInfoPrivateLetterTask() {
		HashMap<String, Object> taskParams = new HashMap<String, Object>();
		taskParams.put(Info.INFO_SOURCE, info.getInfoSource());
		taskParams.put(Info.INFO_TYPE, info.getInfoType());
		taskParams.put(Task.TA_GETINFO_DETAIL_ACTIVITY, Task.TA_GETINFO_DETAIL_PRIVATELETTER);
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
				InfoPrivateLetter infoPrivateLetter = (InfoPrivateLetter) params[1];
				info.setInfoPrivateLetter(infoPrivateLetter);
				displayPritaveLetter();
				break;

			default:
				break;
			}
		}
	}
	
	private void displayPritaveLetter() {
		holder.txt_letter.setText(info.getInfoPrivateLetter().getLetter());
		holder.txt_reply.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(InfoPrivateLetterActivity.this, SendPrivateLetterActivity.class);
				intent.putExtra(Person.PERSON_ID, info.getPersonId());
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MainService.removeActivity(this);
	}

}
