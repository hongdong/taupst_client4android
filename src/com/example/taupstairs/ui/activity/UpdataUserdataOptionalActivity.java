package com.example.taupstairs.ui.activity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.services.PersonService;
import com.example.taupstairs.string.JsonString;
import com.example.taupstairs.util.SharedPreferencesUtil;

public class UpdataUserdataOptionalActivity extends Activity implements ItaActivity {

	private Button btn_back, btn_ok;
	private EditText edit_qq, edit_email, edit_phone;
	private String qq, email, phone, qqString, emailString, phoneString;
	private ProgressDialog progressDialog;
	
	private String url;
	private String personId;
	private PersonService personService;
	private Person person;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updata_userdata_optional);
		MainService.addActivity(this);
		init();
	}

	@Override
	public void init() {
		initData();
		initView();
	}
	
	private void initData() {
		personId = SharedPreferencesUtil.getDefaultUser(this).getUserId();
		personService = new PersonService(this);
		person = personService.getPersonOptional(personId);
		qq = person.getPersonQq();
		email = person.getPersonEmail();
		phone = person.getPersonPhone();
	}
	
	private void initView() {
		btn_back = (Button)findViewById(R.id.btn_back_updata_userdata_optional);
		btn_ok = (Button)findViewById(R.id.btn_ok_updata_userdata_optional);
		edit_qq = (EditText)findViewById(R.id.edit_updata_userdata_qq);
		edit_email = (EditText)findViewById(R.id.edit_updata_userdata_email);
		edit_phone = (EditText)findViewById(R.id.edit_updata_userdata_phone);
		progressDialog = new ProgressDialog(this);
		
		edit_qq.setText(qq);
		edit_email.setText(email);
		edit_phone.setText(phone);
		
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getEditString();
				if (qqString.equals(qq) && emailString.equals(email) && phoneString.equals(phone)) {
					Toast.makeText(UpdataUserdataOptionalActivity.this, 
							"亲，你什么都没修改", Toast.LENGTH_SHORT).show();
				} else {
					beforeUpdata();
					doUpdataUserDataTask();
				}
			}
		});
	}
	
	private void getEditString() {
		qqString = edit_qq.getText().toString().trim();
		emailString = edit_email.getText().toString().trim();
		phoneString = edit_phone.getText().toString().trim();
	}
	
	private void beforeUpdata() {
		url = "users_id=" + personId;
		if (!qqString.equals(qq)) {
			url += "&qq=" + qqString;
		}
		if (!emailString.equals(email)) {
			url += "&email=" + emailString;
		}
		if (!phoneString.equals(phone)) {
			url += "&phone=" + phoneString;
		}
	}
	
	private void showProgressDialog() {
		progressDialog.setCancelable(false);
		progressDialog.setMessage("    稍等片刻...");
		progressDialog.show();
	}
	
	private void dismissProgressDialog() {
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
	
	/*
	 * 网络任务
	 */
	private void doUpdataUserDataTask() {	
		showProgressDialog();
		HashMap<String, Object> taskParams = new HashMap<String, Object>(2);
		taskParams.put(Task.TA_UPDATAUSERDATA_ACTIVITY, Task.TA_UPDATAUSERDATA_ACTIVITY_OPTIONAL);
		taskParams.put(Task.TA_UPDATAUSERDATA_URL, url);
		Task task = new Task(Task.TA_UPDATAUSERDATA, taskParams);
		MainService.addTask(task);
	}

	@Override
	public void refresh(Object... params) {
		dismissProgressDialog();
		int taskId = (Integer) params[0];
		switch (taskId) {
		case Task.TA_UPDATAUSERDATA:
			String result = (String) params[1];
			if (null == result) {
				Toast.makeText(this, "未连接网络", Toast.LENGTH_SHORT).show();
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					String state = jsonObject.getString(JsonString.Return.STATE).trim();
					if (state.equals(JsonString.Return.STATE_OK)) {
						Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
						personService.updataPersonInfo(personId, Person.PERSON_QQ, qqString);
						personService.updataPersonInfo(personId, Person.PERSON_EMAIL, emailString);
						personService.updataPersonInfo(personId, Person.PERSON_PHONE, phoneString);
					} else {
						Toast.makeText(this, "网络竟然出错了", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MainService.removeActivity(this);
		personService.closeDBHelper();
	}
	
}
