package com.example.taupstairs.ui.activity;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taupstairs.R;
import com.example.taupstairs.adapter.SignUpListAdapter;
import com.example.taupstairs.bean.SignUp;
import com.example.taupstairs.bean.Status;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.logic.TaUpstairsApplication;
import com.example.taupstairs.string.IntentString;

public class SignUpListActivity extends Activity implements ItaActivity {

	private Button btn_back;
	private LinearLayout layout_loading;
	private String statusId;
	private ListView list_signup_list;
	private List<SignUp> signUps;
	private SignUpListAdapter adapter;
	private int clickPosition;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_list);
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
		doGetSignUpListTask();
		layout_loading = (LinearLayout)findViewById(R.id.layout_loading);
		layout_loading.setVisibility(View.VISIBLE);
	}
	
	private void initView() {
		btn_back = (Button)findViewById(R.id.btn_back_signup_list);
		list_signup_list = (ListView)findViewById(R.id.list_signup_list);
		
		btn_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void doGetSignUpListTask() {
		HashMap<String, Object> taskParams = new HashMap<String, Object>(2);
		taskParams.put(Task.TA_GET_SIGNUP_LIST_ACTIVITY, Task.TA_GET_SIGNUP_LIST_ACTIVITY);
		taskParams.put(Status.STATUS_ID, statusId);
		Task task = new Task(Task.TA_GET_SIGNUP_LIST, taskParams);
		MainService.addTask(task);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void refresh(Object... params) {
		if (params[1] != null) {
			int taskId = (Integer) params[0];
			switch (taskId) {
			case Task.TA_GET_SIGNUP_LIST:
				layout_loading.setVisibility(View.GONE);
				signUps = (List<SignUp>) params[1];
				display();
				break;

			default:
				break;
			}
		}
	}
	
	private void display() {
		if (signUps.size() <= 0) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.layout_no_info);
			TextView txt_no_info = (TextView) findViewById(R.id.txt_no_info);
			txt_no_info.setText("这个任务没有人报名");
			layout.setVisibility(View.VISIBLE);	
		} else {
			adapter = new SignUpListAdapter(this, signUps);
			list_signup_list.setAdapter(adapter);
			list_signup_list.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					SignUp signUp = signUps.get(arg2);
					if (signUp.getIsExe().equals("0") && signUp.getSignUpPraise().equals("")) {
						clickPosition = arg2;
						TaUpstairsApplication app = (TaUpstairsApplication) getApplication();
						app.setSignUp(signUps.get(arg2));
						Intent intent = new Intent(SignUpListActivity.this, EvaluateActivity.class);
						intent.putExtra(Status.STATUS_ID, statusId);
						startActivityForResult(intent, IntentString.RequestCode.SIGNUPLIST_EVALUATE);
					} else {	
						//已经评价过了
					}
				}
			});
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case IntentString.RequestCode.SIGNUPLIST_EVALUATE:
			if (IntentString.ResultCode.EVALUATE_SIGNUPLIST == resultCode) {
				Toast.makeText(this, "评价成功", Toast.LENGTH_SHORT).show();
				TaUpstairsApplication app = (TaUpstairsApplication) getApplication();
				SignUp signUp = app.getSignUp();
				signUps.add(clickPosition, signUp);
				signUps.remove(clickPosition + 1);
				adapter.notifyDataSetChanged();
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
	}

}
