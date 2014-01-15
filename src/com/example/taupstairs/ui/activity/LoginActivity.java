package com.example.taupstairs.ui.activity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.College;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.bean.User;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.services.RankService;
import com.example.taupstairs.services.StatusService;
import com.example.taupstairs.string.IntentString;
import com.example.taupstairs.string.JsonString;
import com.example.taupstairs.util.HttpClientUtil;
import com.example.taupstairs.util.SharedPreferencesUtil;

public class LoginActivity extends Activity implements ItaActivity {

	private Button btn_login, btn_captcha;
	private String userId, studentId, password;
	private String collegeId, collegeName, collegeCaptchaUrl, collegeWeb, eduCode;
	private ProgressDialog progressDialog;
	private TextView txt_college_name, txt_about, txt_server;
	private EditText edit_studentid, edit_password, edit_captcha;
	private ImageView img_captcha;
	private boolean hasGetCaptcha = false;
	private boolean isExist = false;
	private boolean isRefresh = false;
	private boolean haGetEduCode = false;
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
		img_captcha = (ImageView)findViewById(R.id.img_college_captcha);
		btn_captcha = (Button)findViewById(R.id.btn_refresh_college_captcha);
		edit_captcha = (EditText)findViewById(R.id.edit_college_captcha);
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
					studentId = edit_studentid.getText().toString();
					password = edit_password.getText().toString();
					doCheckUserTask();
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
		btn_captcha.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				doGetCollegeCaptchaTask();
			}
		});
	}
	
	private void showProgressDialog() {
		progressDialog.setCancelable(false);
		progressDialog.setMessage("    稍等片刻...");
		progressDialog.show();
	}
	
	private void dismissProgressDialog() {
		progressDialog.dismiss();
	}
	
	private void doCheckUserTask() {
		if (!isRefresh) {
			isRefresh = true;
			showProgressDialog();
			HashMap<String, Object> taskParams = new HashMap<String, Object>(2);
			taskParams.put(College.COLLEGE_ID, collegeId);
			taskParams.put(User.USER_STUDENTID, studentId);
			Task task = new Task(Task.TA_CHECKUSER, taskParams);
			MainService.addTask(task);
		}
	}
	
	private void doGetEduCodeTask() {
		if (!isRefresh) {
			isRefresh = true;
			HashMap<String, Object> taskParams = new HashMap<String, Object>(1);
			taskParams.put(College.COLLEGE_WEB, collegeWeb);
			Task task = new Task(Task.TA_GETEDUCODE, taskParams);
			MainService.addTask(task);
		}
	}
	
	private void doGetCollegeCaptchaTask() {
		if (!isRefresh) {
			isRefresh = true;
			HashMap<String, Object> taskParams = new HashMap<String, Object>(1);
			taskParams.put(College.COLLEGE_CAPTCHAURL, collegeCaptchaUrl);
			Task task = new Task(Task.TA_GETCOLLEGECAPTCHA, taskParams);
			MainService.addTask(task);
		}
	}
	
	/*登录放到后台处理*/
	private void doLoginTask() {
		if (!isRefresh) {
			isRefresh = true;
			showProgressDialog();
			HashMap<String, Object> taskParams = new HashMap<String, Object>(1);
			taskParams.put(User.USER_COLLEGEID, collegeId);
			taskParams.put(User.USER_STUDENTID, studentId);
			taskParams.put(User.USER_PASSWORD, password);
			if (!isExist && hasGetCaptcha) {
				EditText editText = (EditText) findViewById(R.id.edit_college_captcha);
				String collegeCaptcha = editText.getText().toString().trim();
				if (collegeCaptcha.equals("")) {
					Toast.makeText(LoginActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
					return;
				}
				String cookieString = HttpClientUtil.cookieString.split(";")[0];
				taskParams.put(Task.TA_LOGIN_COLLEGECAPTCHA, collegeCaptcha);
				taskParams.put(Task.TA_LOGIN_COOKIE, cookieString);
				taskParams.put(Task.TA_LOGIN_EDUCODE, eduCode);
			}
			Task task = new Task(Task.TA_LOGIN, taskParams);
			MainService.addTask(task);
		}
	}

	public void refresh(Object... params) {	
		isRefresh = false;
		int taskId = (Integer) params[0];
		switch (taskId) {
		case Task.TA_CHECKUSER:
			dismissProgressDialog();
			String checkuser = ((String) params[1]).trim();
			refreshCheckUser(checkuser);
			break;
			
		case Task.TA_GETEDUCODE:
			String html = (String) params[1];
			Document d = Jsoup.parse(html);
			Elements es = d.select(College.VIEWSTATE);
			eduCode = es.val();
			doGetCollegeCaptchaTask();
			break;
			
		case Task.TA_GETCOLLEGECAPTCHA:
			Drawable drawable = (Drawable) params[1];
			refreshGetCollegeCaptcha(drawable);
			break;
			
		case Task.TA_LOGIN:
			dismissProgressDialog();
			String login = ((String) params[1]).trim();	//这里的字符串要去空格，不然很可能不会equals
			refreshLogin(login);
			break;

		default:
			break;
		}
	}
	
	private void refreshCheckUser(String checkuser) {
		if (checkuser != null) {
			try {
				JSONObject jsonObject = new JSONObject(checkuser);
				String state = jsonObject.getString(JsonString.Return.STATE).trim();
				if (state.equals(JsonString.Return.STATE_OK)) {
					isExist = true;
					doLoginTask();
				} else {
					isExist = false;
					if (collegeCaptchaUrl.equals("") || hasGetCaptcha) {
						doLoginTask();
					} else {
						Toast.makeText(LoginActivity.this, 
								"第一次登录需同步教务系统\n请填写验证码后再点击登录", 
								Toast.LENGTH_LONG).show();
						if (!haGetEduCode) {
							haGetEduCode = true;
							doGetEduCodeTask();
						} else {
							doGetCollegeCaptchaTask();
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(LoginActivity.this, "没网络啊！！！亲", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void refreshGetCollegeCaptcha(Drawable drawable) {
		if (drawable != null) {
			hasGetCaptcha = true;
			img_captcha = (ImageView) findViewById(R.id.img_college_captcha);
			edit_captcha = (EditText) findViewById(R.id.edit_college_captcha);
			edit_captcha.setText(null);
			img_captcha.setImageDrawable(drawable);
			img_captcha.setVisibility(View.VISIBLE);
			btn_captcha.setVisibility(View.VISIBLE);
			edit_captcha.setVisibility(View.VISIBLE);
		} else {
			Toast.makeText(LoginActivity.this, 
					"教务系统崩溃!!", 
					Toast.LENGTH_SHORT).show();
		}
	}
	
	private void refreshLogin(String result) {
		try {
			JSONObject loginJsonObject = new JSONObject(result);
			String isLogined = loginJsonObject.getString(JsonString.Login.IS_LOGINED).trim();
			if(isLogined.equals(Task.TA_FALSE)) {
				hasGetCaptcha = false;
				int state = Integer.parseInt(loginJsonObject.getString(JsonString.Login.STATE).trim());
				testState(state);
				doGetCollegeCaptchaTask();
			} else if (isLogined.equals(Task.TA_TRUE)) {
				userId = loginJsonObject.getString(JsonString.Login.USERS_ID);
				beforeJump();
				if (isExist) {
					jumpToHomePage();
				} else {
					jumpToCompleteUserdata();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void testState(int state) {
		switch (state) {
		case 1:
			Toast.makeText(LoginActivity.this, "请登录教务系统完成教师评价后在登录", Toast.LENGTH_SHORT).show();
			break;

		case 2:
			Toast.makeText(LoginActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
			break;
			
		case 3:
			Toast.makeText(LoginActivity.this, "验证码不正确", Toast.LENGTH_SHORT).show();
			break;
			
		case 4:
			Toast.makeText(LoginActivity.this, "用户名不存在或未按照要求参加教学活动", Toast.LENGTH_SHORT).show();
			break;
			
		case 5:
			Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
			break;
			
		case 6:
			Toast.makeText(LoginActivity.this, "信息填写错误或教务系统奔溃!!", Toast.LENGTH_SHORT).show();
			break;

		default:
			Toast.makeText(LoginActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
			break;
		}
	}
	
	private void beforeJump() {
		User user = new User();
		user.setUserId(userId);
		user.setUserStudentId(studentId);
		user.setUserPassword(password);
		user.setUserCollegeId(collegeId);
		user.setUserCollegeName(collegeName);
		/*至关重要的一步，保存后下次会自动跳到主页面*/
		SharedPreferencesUtil.saveDefaultUser(LoginActivity.this, user);
		SharedPreferencesUtil.savaLastestStatusId(LoginActivity.this, null);
		StatusService statusService = new StatusService(LoginActivity.this);
		statusService.emptyStatusDb();
		statusService.closeDBHelper();
		RankService rankService = new RankService(LoginActivity.this);
		rankService.emptyRankDb();
		rankService.closeDBHelper();
	}
	
	/*跳转到主页面去*/
	private void jumpToHomePage() {			
		Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
		startActivity(intent);
		finish();
	}
	
	private void jumpToCompleteUserdata() {
		Intent intent = new Intent(LoginActivity.this, CompleteUserdataActivity.class);
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
				collegeWeb = "http://jwgl.mju.edu.cn/default2.aspx";
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
