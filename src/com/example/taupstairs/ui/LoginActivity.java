package com.example.taupstairs.ui;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taupstairs.R;
import com.example.taupstairs.adapter.LoginBaseAdapter;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.bean.User;
import com.example.taupstairs.intent.IntentInfo;
import com.example.taupstairs.logic.MainService;

public class LoginActivity extends Activity implements ItaActivity {

	private User user;
	private ListView listView;
	private Button bn_login;
	private TextView txt_college_name, txt_about, txt_server;
	private EditText edit_studentid, edit_password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		MainService.addActivity(this);	//将自己添加到activitys链表里面去
		init();
	}

	@Override
	public void init() {
		final int LIST_LOGIN_COUNT = 3;
		listView = (ListView)findViewById(R.id.list_login);
		LoginBaseAdapter adapter = new LoginBaseAdapter(LoginActivity.this, LIST_LOGIN_COUNT);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (0 == arg2) {		//“选择学校”那一行（第一行）被click了
					Intent intent = new Intent(LoginActivity.this, SelectCollegeActivity.class);
					startActivityForResult(intent, IntentInfo.RequestCode.LOGIN_SELECTCOLLEGE);
				}
			}
		});
		edit_studentid = (EditText)findViewById(R.id.edit_studentid);
		edit_password = (EditText)findViewById(R.id.edit_password);
		bn_login = (Button)findViewById(R.id.bn_login);
		bn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (txt_college_name.getText().toString().equals("点击选择")) {
					Toast.makeText(LoginActivity.this, "请选择学校", Toast.LENGTH_SHORT).show();
				} else if (edit_studentid.getText().toString().equals("")) {
					Toast.makeText(LoginActivity.this, "请输入学号", Toast.LENGTH_SHORT).show();
				} else if (edit_password.getText().toString().equals("")) {
					Toast.makeText(LoginActivity.this, "请输入教务系统密码", Toast.LENGTH_SHORT).show();
				} else {
					String userCollege = txt_college_name.getText().toString();
					String userStudentId = edit_studentid.getText().toString();
					String userPassword = edit_password.getText().toString();
					user = new User(userCollege, userStudentId, userPassword);
					loginTask(user);
				}
			}
		});
		txt_about = (TextView)findViewById(R.id.txt_about);
		txt_about.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, AboutUsActivity.class);
				startActivity(intent);
			}
		});
		txt_server = (TextView)findViewById(R.id.txt_server);
		txt_server.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, ServerDeclareActivity.class);
				startActivity(intent);
			}
		});
	}
	
	/*登录的后台任务处理*/
	private void loginTask(User user) {
		Map<String, Object> taskParams = new HashMap<String, Object>();
		taskParams.put(Task.TA_LOGIN_TASKPARAMS, user);
		Task task = new Task(Task.TA_LOGIN, taskParams);
		MainService.addTask(task);
	}
	
	/*跳转到主页面去*/
	private void jumpToHomePage() {
		Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
		startActivity(intent);
	}

	@Override
	public void refresh(Object... params) {					//有待完善
		jumpToHomePage();									//如果TA_LOGIN任务执行成功，说明可以跳到主页面去了
	}
	
	/*接收Intent返回的数据*/
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (IntentInfo.RequestCode.LOGIN_SELECTCOLLEGE == requestCode) {
			if (IntentInfo.ResultCode.SELECTCOLLEGE_LOGIN == resultCode) {
				Bundle myData = data.getExtras();
				String collegeName = myData.getString("collegeName");
				txt_college_name = (TextView)findViewById(R.id.txt_college_name);
				txt_college_name.setTextColor(Color.BLACK);
				txt_college_name.setText(collegeName);
			}
		}
	}
	
}
