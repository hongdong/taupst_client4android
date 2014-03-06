package com.example.taupstairs.ui.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.InfoSignUp;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.logic.TaUpstairsApplication;
import com.example.taupstairs.services.PersonService;
import com.example.taupstairs.string.IntentString;
import com.example.taupstairs.string.JsonString;
import com.example.taupstairs.util.SharedPreferencesUtil;
import com.example.taupstairs.view.KeyboardLayout;
import com.example.taupstairs.view.KeyboardLayout.onKybdsChangeListener;

public class InfoSignUpExecActivity extends Activity implements ItaActivity {

	private Button btn_back, btn_ok;
	private TextView txt_qq, txt_email, txt_phone, txt_setting;
	private CheckBox box_qq, box_email, box_phone;
	private EditText edit_string;
	private KeyboardLayout keyboardLayout;
	private ScrollView scrollView;
	private Handler handler;
	private String personId;
	private PersonService personService;
	private Person person;
	private String qq, email, phone;
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_signup_exec);
		MainService.addActivity(this);
		init();
	}

	@Override
	public void init() {
		initData();
		initView();
	}
	
	private void initData() {
		handler = new Handler(); 
		personId = SharedPreferencesUtil.getDefaultUser(this).getUserId();
		personService = new PersonService(this);
	}
	
	private void initView() {
		btn_back = (Button)findViewById(R.id.btn_back_info_signup_exec);
		btn_ok = (Button)findViewById(R.id.btn_ok_info_signup_exec);
		txt_qq = (TextView)findViewById(R.id.txt_info_signup_exec_qq);
		txt_email = (TextView)findViewById(R.id.txt_info_signup_exec_email);
		txt_phone = (TextView)findViewById(R.id.txt_info_signup_exec_phone);
		txt_setting = (TextView)findViewById(R.id.txt_info_signup_exec_setting);
		box_qq = (CheckBox)findViewById(R.id.box_info_signup_exec_qq);
		box_email = (CheckBox)findViewById(R.id.box_info_signup_exec_email);
		box_phone = (CheckBox)findViewById(R.id.box_info_signup_exec_phone);
		keyboardLayout = (KeyboardLayout)findViewById(R.id.layout_info_signup_exec);
		scrollView = (ScrollView)findViewById(R.id.scroll_info_signup_exec);
		edit_string = (EditText)findViewById(R.id.edit_info_signup_exec);
		progressDialog = new ProgressDialog(this);
		
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				char optional[] = {'0', '0', '0', }; 
				if (box_phone.isChecked()) {
					optional[0] = '1';
				}
				if (box_qq.isChecked()) {
					optional[1] = '1';
				}
				if (box_email.isChecked()) {
					optional[2] = '1';
				}
				String contact = new String(optional);		
				if (contact.equals("000")) {
					Toast.makeText(InfoSignUpExecActivity.this, "至少提供一种联系方式", Toast.LENGTH_SHORT).show();
				} else {
					String message = edit_string.getText().toString().trim();
					doExecTaskTask(contact, message);
				}
			}
		});
		
		keyboardLayout.setOnkbdStateListener(new onKybdsChangeListener() {         
            @Override
			public void onKeyBoardStateChange(int state) {
                switch (state) {
                case KeyboardLayout.KEYBOARD_STATE_HIDE:
                	handler.post(new Runnable() {
						@Override
						public void run() {
							scrollView.fullScroll(View.FOCUS_UP);
						}
					}); 
                break;
                case KeyboardLayout.KEYBOARD_STATE_SHOW:
                    handler.post(new Runnable() {
						@Override
						public void run() {
							scrollView.fullScroll(View.FOCUS_DOWN);
						}
					});  
                break;
                }
            }
		});
		
		box_qq.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					txt_qq.setTextColor(Color.BLACK);
				} else {
					txt_qq.setTextColor(Color.GRAY);
				}
			}
		});
		
		box_email.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					txt_email.setTextColor(Color.BLACK);
				} else {
					txt_email.setTextColor(Color.GRAY);
				}
			}
		});
		
		box_phone.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					txt_phone.setTextColor(Color.BLACK);
				} else {
					txt_phone.setTextColor(Color.GRAY);
				}
			}
		});
		
		txt_setting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(InfoSignUpExecActivity.this, UpdataUserdataOptionalActivity.class);
				startActivity(intent);
			}
		});
	}
	
	private void refreshView() {
		person = personService.getPersonOptional(personId);
		qq = person.getPersonQq();
		email = person.getPersonEmail();
		phone = person.getPersonPhone();
		txt_qq.setText(qq);
		txt_email.setText(email);
		txt_phone.setText(phone);
		txt_qq.setTextColor(Color.GRAY);
		txt_email.setTextColor(Color.GRAY);
		txt_phone.setTextColor(Color.BLACK);
		box_qq.setChecked(false);
		box_email.setChecked(false);
		box_phone.setChecked(true);
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
	
	private void doExecTaskTask(String contact, String message) {
		showProgressDialog();
		Map<String, Object> taskParams = new HashMap<String, Object>();
		TaUpstairsApplication app = (TaUpstairsApplication) getApplication();
		String signUpId = app.getInfo().getInfoSignUp().getSignUpId();
		taskParams.put(InfoSignUp.SIGNUP_ID, signUpId);
		taskParams.put(InfoSignUp.PERSON_CONTACT, contact);
		taskParams.put(InfoSignUp.SIGNUP_STRING, edit_string.getText().toString());
		Task task = new Task(Task.TA_EXEC_TASK, taskParams);
		MainService.addTask(task);
	}

	@Override
	public void refresh(Object... params) {
		dismissProgressDialog();
		if (params[1] != null) {
			int taskId = (Integer) params[0];
			switch (taskId) {
			case Task.TA_EXEC_TASK:
				String result = (String) params[1];
				try {
					JSONObject jsonObject = new JSONObject(result);
					String state = jsonObject.getString(JsonString.Return.STATE).trim();
					if (state.equals(JsonString.Return.STATE_OK)) {
						Intent intent = new Intent();
						setResult(IntentString.ResultCode.INFOSIGNUPEXEC_INFOSIGNUP, intent);
						finish();
					} else {
						Toast.makeText(this, "网络竟然出错了", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		refreshView();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MainService.removeActivity(this);
		personService.closeDBHelper();
	}
	
}
