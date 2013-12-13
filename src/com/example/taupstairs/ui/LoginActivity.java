package com.example.taupstairs.ui;

import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.taupstairs.R;
import com.example.taupstairs.bean.College;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.bean.User;
import com.example.taupstairs.intent.IntentInfo;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.util.SharedPreferencesUtil;

public class LoginActivity extends Activity implements ItaActivity {

	private User user;
	private Button bn_login;
	private String userCollegeId;
	private TextView txt_college_name, txt_about, txt_server;
	private EditText edit_studentid, edit_password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		MainService.addActivity(this);	//将自己添加到activitys链表里面去
		init();
	}

	public void init() {
		txt_college_name = (TextView)findViewById(R.id.txt_college_name);
		txt_college_name.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, SelectCollegeActivity.class);
				startActivityForResult(intent, IntentInfo.RequestCode.LOGIN_SELECTCOLLEGE);
			}
		});
		edit_studentid = (EditText)findViewById(R.id.edit_studentid);
		edit_password = (EditText)findViewById(R.id.edit_password);
		bn_login = (Button)findViewById(R.id.bn_login);
		bn_login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (txt_college_name.getText().toString().equals("点击选择")) {
					Toast.makeText(LoginActivity.this, "请选择学校", Toast.LENGTH_SHORT).show();
				} else if (edit_studentid.getText().toString().equals("")) {
					Toast.makeText(LoginActivity.this, "请输入学号", Toast.LENGTH_SHORT).show();
				} else if (edit_password.getText().toString().equals("")) {
					Toast.makeText(LoginActivity.this, "请输入教务系统密码", Toast.LENGTH_SHORT).show();
				} else {
					String userStudentId = edit_studentid.getText().toString();
					String userPassword = edit_password.getText().toString();
					user = new User(userCollegeId, userStudentId, userPassword);
					loginTask(user);
				}
			}
		});
		txt_about = (TextView)findViewById(R.id.txt_about);
		txt_about.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, AboutUsActivity.class);
				startActivity(intent);
			}
		});
		txt_server = (TextView)findViewById(R.id.txt_server);
		txt_server.setOnClickListener(new OnClickListener() {
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

	public void refresh(Object... params) {					
		String result = ((String) params[0]).trim();	//这里的字符串要去空格，不然很可能不会equals
		if (result.equals(Task.TA_LOGIN_ERROR)) {
			loginError();
		} else if(result.equals(Task.TA_LOGIN_FALSE)) {
			loginFalse();
		} else if (result.equals("true")) {
			jumpToHomePage();							//如果TA_LOGIN任务执行成功，说明可以跳到主页面去了
		} else {
			System.out.println("未知错误");
		}
	}
	
	/*未连接网络或服务器响应异常*/
	private void loginError() {
		Toast.makeText(LoginActivity.this, 
				"未连接网络或服务器响应异常", 
				Toast.LENGTH_SHORT).show();
	}
	
	/*登录信息错误，登录失败*/
	private void loginFalse() {
//		txt_college_name.setText("点击选择");
		edit_studentid.setText("");
		edit_password.setText("");
		Toast.makeText(LoginActivity.this, 
				"用户信息错误，请从新选择学校并填写学号和密码", 
				Toast.LENGTH_SHORT).show();
	}
	
	/*跳转到主页面去*/
	private void jumpToHomePage() {
		//跳转前要把登录账户保存为默认账户，下次直接从logo跳到主界面，就不要再次登录了
		SharedPreferencesUtil.saveDefaultUser(LoginActivity.this, user);	
		Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
		startActivity(intent);
		finish();
	}
	
	/*接收Intent返回的数据*/
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (IntentInfo.RequestCode.LOGIN_SELECTCOLLEGE == requestCode) {
			if (IntentInfo.ResultCode.SELECTCOLLEGE_LOGIN == resultCode) {
				Bundle myData = data.getExtras();
				userCollegeId = myData.getString(College.COLLEGE_ID);
				String collegeName = myData.getString(College.COLLEGE_NAME);
				txt_college_name.setTextColor(Color.BLACK);
				txt_college_name.setText(collegeName);
			}
		}
	}
	
}
