package com.example.taupstairs.ui.activity;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
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
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.bean.User;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.services.RankService;
import com.example.taupstairs.services.StatusService;
import com.example.taupstairs.string.IntentString;
import com.example.taupstairs.string.JsonString;
import com.example.taupstairs.util.SharedPreferencesUtil;

public class LoginActivity extends Activity implements ItaActivity {

	private User user;
	private Button btn_login;
	private String collegeId, collegeName, collegeCaptchaUrl;
	private ProgressDialog progressDialog;
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
		View view = findViewById(R.id.layout_login_college);
		txt_college_name = (TextView)findViewById(R.id.txt_college_name);
		edit_studentid = (EditText)findViewById(R.id.edit_studentid);
		edit_password = (EditText)findViewById(R.id.edit_password);
		btn_login = (Button)findViewById(R.id.btn_login);
		txt_about = (TextView)findViewById(R.id.txt_about);
		txt_server = (TextView)findViewById(R.id.txt_server);
		progressDialog = new ProgressDialog(this);
		
		view.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, SelectCollegeActivity.class);
				startActivityForResult(intent, IntentString.RequestCode.LOGIN_SELECTCOLLEGE);
			}
		});
		btn_login.setOnClickListener(new OnClickListener() {
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
					user = new User(collegeId, userStudentId, userPassword);
					loginTask(user);
				}
			}
		});
		txt_about.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, AboutUsActivity.class);
				startActivity(intent);
			}
		});
		txt_server.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, ServerDeclareActivity.class);
				startActivity(intent);
			}
		});
	}
	
	/*登录放到后台处理*/
	private void loginTask(User user) {
		progressDialog.setCancelable(false);
		progressDialog.setMessage("    正在登录...");
		progressDialog.show();
		HashMap<String, Object> taskParams = new HashMap<String, Object>(1);
		taskParams.put(Task.TA_LOGIN_TASKPARAMS, user);
		Task task = new Task(Task.TA_LOGIN, taskParams);
		MainService.addTask(task);
	}

	public void refresh(Object... params) {		
		int taskId = (Integer) params[0];
		switch (taskId) {
		case Task.TA_LOGIN:
			String result = ((String) params[1]).trim();	//这里的字符串要去空格，不然很可能不会equals
			refreshLogin(result);
			break;
			
		case Task.TA_GETUSERDATA:
			progressDialog.dismiss();
			Person person = (Person) params[1];
			if (person.getPersonNickname() != null) {
				jumpToHomePage();
			} else {
				jumpToCompleteUserdata(person.getPersonSex());
			}
			break;

		default:
			break;
		}
	}
	
	/*
	 * 
	 */
	private void refreshLogin(String result) {
		if (result.equals(Task.TA_NO)) {				//返回no表示没有网络
			progressDialog.dismiss();
			loginNoNet();
		} else {
			try {
				JSONObject loginJsonObject = new JSONObject(result);
				String isLogined = loginJsonObject.getString(JsonString.Login.IS_LOGINED).trim();
				if(isLogined.equals(Task.TA_FALSE)) {
					progressDialog.dismiss();
					String state = loginJsonObject.getString(JsonString.Login.STATE).trim();
					if (state.equals(JsonString.Login.STATE_OK)) {
						loginFalse();
					} else if (state.equals(JsonString.Login.STATE_NO)) {
						loginServerFalse();
					}
				} else if (isLogined.equals(Task.TA_TRUE)) {
					user.setUserId(loginJsonObject.getString(JsonString.Login.USERS_ID));
					/*至关重要的一步，保存后下次会自动跳到主页面*/
					SharedPreferencesUtil.saveDefaultUser(LoginActivity.this, user);
					doGetUserDataTask();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*未连接网络或服务器响应异常*/
	private void loginNoNet() {
		Toast.makeText(LoginActivity.this, 
				"没网络啊！！！亲", 
				Toast.LENGTH_SHORT).show();
	}
	
	/*登录信息错误，登录失败*/
	private void loginFalse() {
		edit_password.setText("");
		Toast.makeText(LoginActivity.this, 
				"    用户信息错误\n请从新选择与填写", 
				Toast.LENGTH_SHORT).show();

	}
	
	/*服务器网络异常*/
	private void loginServerFalse() {
		Toast.makeText(LoginActivity.this, 
				"我勒个去！网络异常", 
				Toast.LENGTH_SHORT).show();

	}
	
	/*从服务器获取Person信息*/
	private void doGetUserDataTask() {
		HashMap<String, Object> taskParams = new HashMap<String, Object>(1);
		taskParams.put(Task.TA_GETUSERDATA_ACTIVITY, Task.TA_GETUSERDATA_ACTIVITY_LOGIN);
		taskParams.put(Task.TA_GETUSERDATA_TASKPARAMS, user.getUserId());
		Task task = new Task(Task.TA_GETUSERDATA, taskParams);
		MainService.addTask(task);
	}
	
	/*跳转到主页面去*/
	private void jumpToHomePage() {	
		SharedPreferencesUtil.savaLastestStatusId(LoginActivity.this, null);
		StatusService statusService = new StatusService(LoginActivity.this);
		statusService.emptyStatusDb();
		statusService.closeDBHelper();
		RankService rankService = new RankService(LoginActivity.this);
		rankService.emptyRankDb();
		rankService.closeDBHelper();
		
		Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
		startActivity(intent);
		finish();
	}
	
	/*
	 * 
	 */
	private void jumpToCompleteUserdata(String personSex) {
		String personNickname = null;
		if (personSex.equals(Person.MALE)) {
			personNickname = Person.MALE_NICKNAME;
		} else if (personSex.equals(Person.FEMALE)) {
			personNickname = Person.FEMALE_NICKNAME;
		}
		Intent intent = new Intent(LoginActivity.this, CompleteUserdataActivity.class);
		intent.putExtra(Person.PERSON_ID, user.getUserId());
		intent.putExtra(Person.PERSON_NICKNAME, personNickname);
		startActivity(intent);
		finish();
	}
	
	/*接收Intent返回的数据*/
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (IntentString.RequestCode.LOGIN_SELECTCOLLEGE == requestCode) {
			if (IntentString.ResultCode.SELECTCOLLEGE_LOGIN == resultCode) {
				collegeId = data.getStringExtra(College.COLLEGE_ID);
				collegeName = data.getStringExtra(College.COLLEGE_NAME);
				collegeCaptchaUrl = data.getStringExtra(College.COLLEGE_CAPTCHAURL);
				txt_college_name.setTextColor(Color.BLACK);
				txt_college_name.setText(collegeName);
			}
		}
	}
	
	/*不重写这个方法，在退出的时候杀死进程的话，
	 * 会导致没有完全杀死程序的，会残留哪些我也不太清楚
	 * 使得手机在没有清空缓存的时候，再一次打开软件，
	 * 会出现后台的MainService调用UI线程中的refresh函数不能更新UI的情况*/
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		System.exit(0);			
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MainService.removeActivity(this);
	}
	
}
