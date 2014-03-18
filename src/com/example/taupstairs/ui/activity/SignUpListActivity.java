package com.example.taupstairs.ui.activity;

import java.util.HashMap;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.example.taupstairs.string.JsonString;

public class SignUpListActivity extends Activity implements ItaActivity {

	private Button btn_back, btn_setting;
	private LinearLayout layout_loading;
	private String statusId;
	private ListView list_signup_list;
	private List<SignUp> signUps;
	private SignUpListAdapter adapter;
	private ProgressDialog progressDialog;
	private boolean flag_end;
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
		flag_end = getIntent().getBooleanExtra(Status.STATUS_STATE, false);
		layout_loading = (LinearLayout)findViewById(R.id.layout_loading);
		layout_loading.setVisibility(View.VISIBLE);
		doGetSignUpListTask();
	}
	
	private void initView() {
		btn_back = (Button)findViewById(R.id.btn_back_signup_list);
		btn_setting = (Button)findViewById(R.id.btn_setting_signup_list);
		list_signup_list = (ListView)findViewById(R.id.list_signup_list);
		progressDialog = new ProgressDialog(this);
		
		btn_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (flag_end) {
					Intent intent = new Intent();
					setResult(IntentString.ResultCode.SIGNUPLIST_END, intent);
				}
				finish();
			}
		});
		btn_setting.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!flag_end) {
					//弹框提示是否真的要完结
					AlertDialog.Builder builder = new AlertDialog.Builder(SignUpListActivity.this);
					builder.setTitle("完结任务")
					.setMessage("任务完结后其它童鞋将不可报名\n确定要完结吗？")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							doEndTaskTask();
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							//销毁对话框，什么都不做
						}
					})
					.create()
					.show();
				} else {
					Toast.makeText(SignUpListActivity.this, "任务已完结", Toast.LENGTH_SHORT).show();
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
	
	private void doGetSignUpListTask() {
		HashMap<String, Object> taskParams = new HashMap<String, Object>(2);
		taskParams.put(Task.TA_GET_SIGNUP_LIST_ACTIVITY, Task.TA_GET_SIGNUP_LIST_ACTIVITY);
		taskParams.put(Status.STATUS_ID, statusId);
		Task task = new Task(Task.TA_GET_SIGNUP_LIST, taskParams);
		MainService.addTask(task);
	}
	
	private void doEndTaskTask() {
		showProgressDialog();
		HashMap<String, Object> taskParams = new HashMap<String, Object>(2);
		taskParams.put(Task.TA_END_TASK_ACTIVITY, Task.TA_END_TASK_ACTIVITY);
		taskParams.put(Status.STATUS_ID, statusId);
		Task task = new Task(Task.TA_END_TASK, taskParams);
		MainService.addTask(task);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void refresh(Object... params) {
		dismissProgressDialog();
		if (params[1] != null) {
			int taskId = (Integer) params[0];
			switch (taskId) {
			case Task.TA_GET_SIGNUP_LIST:
				layout_loading.setVisibility(View.GONE);
				signUps = (List<SignUp>) params[1];
				display();
				break;
				
			case Task.TA_END_TASK:
				String end = (String) params[1];
				try {
					JSONObject endObject = new JSONObject(end);
					String state = endObject.getString(JsonString.Return.STATE).trim();
					if (state.equals(JsonString.Return.STATE_OK)) {
						flag_end = true;
						Toast.makeText(this, "任务已完结", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(this, "网络竟然出错了", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
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
			adapter = new SignUpListAdapter(this, signUps, statusId);
			list_signup_list.setAdapter(adapter);
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
				int clickPosition = data.getIntExtra(SignUp.CLICK_POSITION, -1);
				signUps.add(clickPosition, signUp);
				signUps.remove(clickPosition + 1);
				adapter.notifyDataSetChanged();
			}
			break;
			
		case IntentString.RequestCode.SIGNUPLIST_INFODETAIL:
			if (IntentString.ResultCode.INFODETAIL_SIGNUPLIST == resultCode) {
				TaUpstairsApplication app = (TaUpstairsApplication) getApplication();
				SignUp signUp = app.getSignUp();
				int clickPosition = data.getIntExtra(SignUp.CLICK_POSITION, -1);
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
	public void onBackPressed() {
		if (flag_end) {
			Intent intent = new Intent();
			setResult(IntentString.ResultCode.SIGNUPLIST_END, intent);
		}
		super.onBackPressed();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MainService.removeActivity(this);
	}

}
