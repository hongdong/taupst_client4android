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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.SignUp;
import com.example.taupstairs.bean.Status;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.logic.TaUpstairsApplication;
import com.example.taupstairs.string.IntentString;
import com.example.taupstairs.string.JsonString;

public class EvaluateActivity extends Activity implements ItaActivity {

	private Button btn_back, btn_ok;
	private EditText edit_evaluate;
	private TextView txt_praise;
	private SeekBar sk_evaluate;
	private String statusId;
	private SignUp signUp;
	private String signUpPraise, signUpMessage;
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.evaluate);
		MainService.addActivity(this);
		init();
	}
	
	@Override
	public void init() {
		initData();
		initView();
	}
	
	private void initData() {
		statusId = getIntent().getStringExtra(Status.STATUS_ID);
		TaUpstairsApplication app = (TaUpstairsApplication) getApplication();
		signUp = app.getSignUp();
		signUpPraise = "0";
	}
	
	private void initView() {
		btn_back = (Button)findViewById(R.id.btn_back_evaluate);
		btn_ok = (Button)findViewById(R.id.btn_ok_evaluate);
		edit_evaluate = (EditText)findViewById(R.id.edit_evaluate);
		txt_praise = (TextView)findViewById(R.id.txt_evaluate_praise);
		sk_evaluate = (SeekBar)findViewById(R.id.sk_evaluate);
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
				signUpMessage = edit_evaluate.getText().toString();
				doEvaluateTask();
			}
		});
		
		txt_praise.setText("0");
		
		sk_evaluate.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				signUpPraise = String.valueOf(progress);
				txt_praise.setText(signUpPraise);
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
	
	private void doEvaluateTask() {
		showProgressDialog();
		Map<String, Object> taskParams = new HashMap<String, Object>();
		taskParams.put(Task.TA_EVALUATE_ACTIVITY, Task.TA_EVALUATE_ACTIVITY);
		taskParams.put(Status.STATUS_ID, statusId);
		taskParams.put(SignUp.SIGNUP_ID, signUp.getSignUpId());
		taskParams.put(SignUp.PERSON_ID, signUp.getPersonId());
		taskParams.put(SignUp.SIGNUP_PRAISE, signUpPraise);
		taskParams.put(SignUp.SIGNUP_MESSAGE, signUpMessage);
		Task task = new Task(Task.TA_EVALUATE, taskParams);
		MainService.addTask(task);
	}

	@Override
	public void refresh(Object... params) {
		dismissProgressDialog();
		if (params[1] != null) {
			int taskId = (Integer) params[0];
			switch (taskId) {
			case Task.TA_EVALUATE:
				String result = (String) params[1];
				System.out.println(result);
				try {
					JSONObject jsonObject = new JSONObject(result);
					String state = jsonObject.getString(JsonString.Return.STATE).trim();
					if (state.equals(JsonString.Return.STATE_OK)) {
						signUp.setSignUpPraise(signUpPraise);
						signUp.setSignUpMessage(signUpMessage);
						TaUpstairsApplication app = (TaUpstairsApplication) getApplication();
						app.setSignUp(signUp);
						Intent intent = new Intent();
						setResult(IntentString.ResultCode.EVALUATE_SIGNUPLIST, intent);
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
	protected void onDestroy() {
		super.onDestroy();
		MainService.removeActivity(this);
	}

}
