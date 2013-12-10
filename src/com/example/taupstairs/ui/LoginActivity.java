package com.example.taupstairs.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.example.taupstairs.R;
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
		BaseAdapter adapter = new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				View view = null;
				switch (position) {
				case 0:
					view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.login_college, null);
					view.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(LoginActivity.this, SelectCollegeActivity.class);
							startActivityForResult(intent, IntentInfo.RequestCode.LOGIN_SELECTCOLLEGE);
						}
					});	
					break;
				case 1:
					view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.login_studentid, null);
					break;
				case 2:
					view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.login_password, null);
					break;
				default:
					break;
				}
				return view;
			}
			
			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return LIST_LOGIN_COUNT;
			}
		};
		listView.setAdapter(adapter);
	}

	@Override
	public void refresh(Object... params) {

	}
	
}
