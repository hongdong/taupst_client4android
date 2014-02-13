package com.example.taupstairs.ui.activity;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.taupstairs.R;
import com.example.taupstairs.app.TaUpstairsApplication;
import com.example.taupstairs.bean.InfoSignUp;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.string.IntentString;
import com.example.taupstairs.string.JsonString;

public class InfoSignUpExecActivity extends Activity implements ItaActivity {

	private Button btn_back, btn_ok;
	private EditText edit_string;
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
		
	}
	
	private void initView() {
		btn_back = (Button)findViewById(R.id.btn_back_info_signup_exec);
		btn_ok = (Button)findViewById(R.id.btn_ok_info_signup_exec);
		edit_string = (EditText)findViewById(R.id.edit_info_signup_exec);
		progressDialog = new ProgressDialog(this);

		
		btn_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		btn_ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				doExecTaskTask();
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
	
	private void doExecTaskTask() {
		showProgressDialog();
		Map<String, Object> taskParams = new HashMap<String, Object>();
		TaUpstairsApplication app = (TaUpstairsApplication) getApplication();
		String signUpId = app.getInfo().getInfoSignUp().getSignUpId();
		taskParams.put(InfoSignUp.SIGNUP_ID, signUpId);
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
}
