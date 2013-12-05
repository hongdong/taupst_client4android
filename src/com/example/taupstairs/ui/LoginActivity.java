package com.example.taupstairs.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.example.taupstairs.R;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.logic.MainService;

public class LoginActivity extends Activity implements ItaActivity {

	private ListView listView;
	private Button bn_login;
	private TextView txt_about, txt_server;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		Intent intent = new Intent(LoginActivity.this, MainService.class);
		listView = (ListView)findViewById(R.id.list_login);
		BaseAdapter adapter = new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				View view = null;
				switch (position) {
				case 0:
					view = LinearLayout.inflate(LoginActivity.this, R.layout.login_college, null);
					break;
				case 1:
					view = LinearLayout.inflate(LoginActivity.this, R.layout.login_studentid, null);
					break;
				case 2:
					view = LinearLayout.inflate(LoginActivity.this, R.layout.login_password, null);
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
				return 3;
			}
		};
		listView.setAdapter(adapter);
		startService(intent);
		MainService.addActivity(this);	//���Լ��ŵ�activitys�ļ�������
		bn_login = (Button)findViewById(R.id.bn_login);
		bn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Task task = new Task(Task.TA_LOGIN, null);
				MainService.addTask(task);
			}
		});
		txt_about = (TextView)findViewById(R.id.txt_about);
		txt_about.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("about us");
			}
		});
		txt_server = (TextView)findViewById(R.id.txt_server);
		txt_server.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("server declare");
			}
		});
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(Object... params) {
		// TODO Auto-generated method stub

	}
	
}
