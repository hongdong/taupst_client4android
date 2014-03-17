package com.example.taupstairs.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.imageCache.SimpleImageLoader;
import com.example.taupstairs.listener.LargrPhotoListener;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.services.PersonService;
import com.example.taupstairs.util.HttpClientUtil;
import com.example.taupstairs.util.SharedPreferencesUtil;

public class PersonDataActivity extends Activity implements ItaActivity {

	private Button btn_back, btn_private_letter;
	private Holder holder;
	private String personId, permission;
	private Person person;
	private TextView txt_setting;
	private static final String LIST_LEFT = "left";
	private static final String LIST_RIGHT = "right";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_data);
		MainService.addActivity(this);
		init();
	}
	
	@Override
	public void init() {
		initHolder();
		initData();
		initView();
		testData();
	}
	
	private void initHolder() {
		holder = new Holder();
		holder.layout_loading = (LinearLayout)findViewById(R.id.layout_loading);
		holder.img_photo = (ImageView) findViewById(R.id.img_person_data_photo);
		holder.txt_nickname = (TextView) findViewById(R.id.txt_person_data_nickname);
		holder.txt_praise = (TextView) findViewById(R.id.txt_person_data_praise);
		holder.txt_signature = (TextView) findViewById(R.id.txt_person_data_signature);
	}
	
	private void initData() {
		personId = getIntent().getStringExtra(Person.PERSON_ID);
		permission = getIntent().getStringExtra(Person.PERMISSION);
	}
	
	private void initView() {
		btn_back = (Button)findViewById(R.id.btn_back_person_data);
		btn_private_letter = (Button)findViewById(R.id.btn_private_letter);
		txt_setting = (TextView)findViewById(R.id.txt_person_data_setting);
		
		btn_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		btn_private_letter.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(PersonDataActivity.this, SendPrivateLetterActivity.class);
				intent.putExtra(Person.PERSON_ID, personId);
				startActivity(intent);
			}
		});
		txt_setting.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(PersonDataActivity.this, UpdataUserdataRealActivity.class);
				startActivity(intent);
			}
		});
	}
	
	private void testData() {
		String myId = SharedPreferencesUtil.getDefaultUser(this).getUserId();
		if (personId.equals(myId)) {
			PersonService personService = new PersonService(this);
			person = personService.getPersonById(myId);
			displayPersonVariable(person);
			displayPersonBase(person);
		} else {
			doGetUserDataTask();
		}
	}
	
	private void showProgressBar() {
		holder.layout_loading.setVisibility(View.VISIBLE);
	}
	
	private void hideProgressBar() {
		holder.layout_loading.setVisibility(View.GONE);
	}
	
	private void doGetUserDataTask() {
		showProgressBar();
		Map<String, Object> taskParams = new HashMap<String, Object>();
		taskParams.put(Task.TA_GETUSERDATA_ACTIVITY, Task.TA_GETUSERDATA_ACTIVITY_PERSONDATA);
		taskParams.put(Task.TA_GETUSERDATA_TASKPARAMS, personId);
		Task task = new Task(Task.TA_GETUSERDATA, taskParams);
		MainService.addTask(task);
	}
	
	@Override
	public void refresh(Object... params) {
		if (params[1] != null) {
			int taskId = (Integer) params[0];
			switch (taskId) {
			case Task.TA_GETUSERDATA:
				hideProgressBar();
				person = (Person) params[1];
				displayPerson(person);
				break;

			default:
				break;
			}
		} else {
			
		}
	}
	
	private void displayPerson(Person person) {
		displayPersonVariable(person);
		if (person.getPermission().equals(Person.PERMISSION_PUBLIC) 
				|| permission.equals(Person.PERMISSION_PUBLIC)) {
			displayPersonBase(person);
		}
	}
	
	private class Holder {
		public LinearLayout layout_loading;
		public ImageView img_photo;
		public TextView txt_nickname;
		public TextView txt_praise;
		public TextView txt_signature;
	}
	
	private void displayPersonVariable(Person person) {
		SimpleImageLoader.showImage(holder.img_photo, 
				HttpClientUtil.PHOTO_BASE_URL + person.getPersonPhotoUrl());
		holder.img_photo.setOnClickListener(new LargrPhotoListener(this, person.getPersonPhotoUrl()));
		holder.txt_nickname.setText(person.getPersonNickname());
		holder.txt_praise.setText(person.getPersonPraise() + "  ");
		holder.txt_signature.setText(person.getPersonSignature());
	}
	
	private void displayPersonBase(Person person) {
		ListView list_base = (ListView) findViewById(R.id.list_person_data_base);
		String[] baseLeft = new String[] {"院系:", "年级:", "专业:", "姓名:", "性别:", };
		String[] baseRight = new String[] {person.getPersonFaculty(), person.getPersonYear(),
				person.getPersonSpecialty(), person.getPersonName(), person.getPersonSex()};
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		for (int i = 0; i < baseLeft.length; i++) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put(LIST_LEFT, baseLeft[i]);
			item.put(LIST_RIGHT, baseRight[i]);
			list.add(item);
		}
		SimpleAdapter base_adapter = new SimpleAdapter(this, list, R.layout.person_data_base, 
				new String[] {LIST_LEFT, LIST_RIGHT, }, new int[] {R.id.txt_base_left, R.id.txt_base_right});
		list_base.setAdapter(base_adapter);			//把五个基本资料显示出来
		list_base.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MainService.removeActivity(this);
	}
	
}
