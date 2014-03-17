package com.example.taupstairs.ui.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.string.JsonString;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendPrivateLetterActivity extends Activity implements ItaActivity {

	private Button btn_back, btn_ok;
	private String personId;
	private EditText edit_string;
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.private_letter);
		MainService.addActivity(this);
		init();
	}
	
	@Override
	public void init() {
		initData();
		initView();
	}
	
	private void initData() {
		personId = getIntent().getStringExtra(Person.PERSON_ID);
	}
	
	private void initView() {
		btn_back = (Button)findViewById(R.id.btn_back_private_letter);
		btn_ok = (Button)findViewById(R.id.btn_ok_private_letter);
		edit_string = (EditText)findViewById(R.id.edit_private_letter);
		progressDialog = new ProgressDialog(this);
		
		btn_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		btn_ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (edit_string.getText().toString().trim().equals("")) {
					Toast.makeText(SendPrivateLetterActivity.this, "您还没有输入内容", Toast.LENGTH_SHORT).show();
				} else {
					doSendPrivateLetterTask();
				}
			}
		});
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
	
	private void doSendPrivateLetterTask() {
		showProgressDialog();
		Map<String, Object> taskParams = new HashMap<String, Object>();
		taskParams.put(Task.TA_PRIVATE_LETTER_STRING, edit_string.getText().toString());
		taskParams.put(Person.PERSON_ID, personId);
		Task task = new Task(Task.TA_PRIVATE_LETTER, taskParams);
		MainService.addTask(task);
	}

	@Override
	public void refresh(Object... params) {
		dismissProgressDialog();
		if (params[1] != null) {
			int taskId = (Integer) params[0];
			switch (taskId) {
			case Task.TA_PRIVATE_LETTER:
				String result = (String) params[1];
				try {
					JSONObject jsonObject = new JSONObject(result);
					String state = jsonObject.getString(JsonString.Return.STATE).trim();
					if (state.equals(JsonString.Return.STATE_OK)) {
						Toast.makeText(this, "发送成功", Toast.LENGTH_SHORT).show();
						edit_string.setText("");
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
	
	public void pl_refresh(Object... params) {
		dismissProgressDialog();
		if (params[1] != null) {
			int taskId = (Integer) params[0];
			switch (taskId) {
			case Task.TA_PRIVATE_LETTER:
				String result = (String) params[1];
				try {
					JSONObject jsonObject = new JSONObject(result);
					String state = jsonObject.getString(JsonString.Return.STATE).trim();
					if (state.equals(JsonString.Return.STATE_OK)) {
						Toast.makeText(this, "发送成功", Toast.LENGTH_SHORT).show();
						edit_string.setText("");
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
	protected void onDestroy() {
		super.onDestroy();
		MainService.removeActivity(this);
	}

}
