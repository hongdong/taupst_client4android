package com.example.taupstairs.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.example.taupstairs.R;
import com.example.taupstairs.adapter.LoginBaseAdapter;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.intent.IntentInfo;
import com.example.taupstairs.logic.MainService;

public class LoginActivity extends Activity implements ItaActivity {

	private ListView listView;
	private Button bn_login;
	private TextView txt_college_name, txt_about, txt_server;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		init();
		Intent intent = new Intent(LoginActivity.this, MainService.class);
		startService(intent);
		MainService.addActivity(this);	//���Լ��ŵ�activitys�ļ�������
		bn_login = (Button)findViewById(R.id.bn_login);
		bn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Task task = new Task(Task.TA_LOGIN, null);
				MainService.addTask(task);
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
	
	@Override
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
				if (0 == arg2) {		//选择学校那一行（第一行）被click了
					Intent intent = new Intent(LoginActivity.this, SelectCollegeActivity.class);
					startActivityForResult(intent, IntentInfo.RequestCode.LOGIN_SELECTCOLLEGE);
				}
			}
		});
	}

	@Override
	public void refresh(Object... params) {

	}
	
}
