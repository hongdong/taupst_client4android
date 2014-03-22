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
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Status;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.services.PersonService;
import com.example.taupstairs.string.IntentString;
import com.example.taupstairs.string.JsonString;
import com.example.taupstairs.util.SharedPreferencesUtil;
import com.example.taupstairs.view.KeyboardLayout;
import com.example.taupstairs.view.KeyboardLayout.onKybdsChangeListener;

public class SignupActivity extends Activity implements ItaActivity {

	private Button btn_back, btn_ok;
	private TextView txt_qq, txt_email, txt_phone, txt_setting;
	private CheckBox box_qq, box_email, box_phone;
	private EditText edit_signup;
	private KeyboardLayout keyboardLayout;
	private ScrollView scrollView;
	private Handler handler;
	private String personId;
	private PersonService personService;
	private Person person;
	private String qq, email, phone;
	private String statusId, statusPersonId;
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
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
		statusId = getIntent().getStringExtra(Status.STATUS_ID);
		statusPersonId = getIntent().getStringExtra(Status.PERSON_ID);
	} 
	
	private void initView() {
		btn_back = (Button)findViewById(R.id.btn_back_signup);
		btn_ok = (Button)findViewById(R.id.btn_ok_signup);
		txt_qq = (TextView)findViewById(R.id.txt_signup_qq);
		txt_email = (TextView)findViewById(R.id.txt_signup_email);
		txt_phone = (TextView)findViewById(R.id.txt_signup_phone);
		txt_setting = (TextView)findViewById(R.id.txt_signup_setting);
		box_qq = (CheckBox)findViewById(R.id.box_signup_qq);
		box_email = (CheckBox)findViewById(R.id.box_signup_email);
		box_phone = (CheckBox)findViewById(R.id.box_signup_phone);
		edit_signup = (EditText)findViewById(R.id.edit_signup);
		keyboardLayout = (KeyboardLayout)findViewById(R.id.layout_singup);
		scrollView = (ScrollView)findViewById(R.id.scroll_signup);
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
					Toast.makeText(SignupActivity.this, "至少提供一种联系方式", Toast.LENGTH_SHORT).show();
				} else {
					String message = edit_signup.getText().toString().trim();
					doSingupTask(contact, message);
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
				Intent intent = new Intent(SignupActivity.this, UpdataUserdataOptionalActivity.class);
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
	
	private void doSingupTask(String contact, String message) {
		showProgressDialog();
		Map<String, Object> taskParams = new HashMap<String, Object>();
		taskParams.put(Status.STATUS_ID, statusId);
		taskParams.put(Status.PERSON_ID, statusPersonId);
		taskParams.put(Task.TA_SIGNUP_CONTACT, contact);
		taskParams.put(Task.TA_SIGNUP_MESSAGE, message);
		Task task = new Task(Task.TA_SIGNUP, taskParams);
		MainService.addTask(task);
	}
	
	@Override
	public void refresh(Object... params) {
		dismissProgressDialog();
		int taskId = (Integer) params[0];
		switch (taskId) {
		case Task.TA_SIGNUP:
			String result = (String) params[1];
			if (result != null) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					String state = jsonObject.getString(JsonString.Return.STATE).trim();
					if (state.equals(JsonString.Return.STATE_OK)) {
						Intent intent = new Intent();
						setResult(IntentString.ResultCode.SIGNUP_TASK, intent);
						finish();
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
