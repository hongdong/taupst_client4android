package com.example.taupstairs.ui.activity;

import java.util.Calendar;
import java.util.HashMap;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.Status;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.bean.Time;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.string.IntentString;
import com.example.taupstairs.string.JsonString;
import com.example.taupstairs.util.TimeUtil;

public class WriteActivity extends Activity implements ItaActivity {

	private Button btn_back, btn_ok, btn_write_endtime;
	private EditText edit_write_title, edit_write_content, edit_write_rewards;
	private TextView txt_write_display_endtime;
	private String timeString;
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write);
		MainService.addActivity(WriteActivity.this);
		init();
	}
	
	@Override
	public void init() {
		btn_back = (Button)findViewById(R.id.btn_back_write);
		btn_ok = (Button)findViewById(R.id.btn_ok_write);
		btn_write_endtime = (Button)findViewById(R.id.btn_write_endtime);
		edit_write_title = (EditText)findViewById(R.id.edit_write_title);
		edit_write_content = (EditText)findViewById(R.id.edit_write_content);
		edit_write_rewards = (EditText)findViewById(R.id.edit_write_rewards);
		txt_write_display_endtime = (TextView)findViewById(R.id.txt_write_display_endtime);
		progressDialog = new ProgressDialog(this);
		
		btn_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		btn_ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (edit_write_title.getText().toString().equals("")) {
					Toast.makeText(WriteActivity.this, "标题不能为空", Toast.LENGTH_SHORT).show();
				} else if (edit_write_content.getText().toString().equals("")) {
					Toast.makeText(WriteActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
				} else if (edit_write_rewards.getText().toString().equals("")) {
					Toast.makeText(WriteActivity.this, "报酬不能为空", Toast.LENGTH_SHORT).show();
				} else if (txt_write_display_endtime.getText().toString().equals("")) {
					Toast.makeText(WriteActivity.this, "选个截止时间吧", Toast.LENGTH_SHORT).show();
				} else {
					doReleaseTask();
				}	
			}
		});
		
		btn_write_endtime.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(WriteActivity.this, SelectEndtimeActivity.class);
				startActivityForResult(intent, IntentString.RequestCode.WRITE_SELECTENDTIME);
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
	
	/*
	 * 发布任务
	 */
	private void doReleaseTask() {
		showProgressDialog();
		HashMap<String, Object> taskParams = new HashMap<String, Object>(4);
		taskParams.put(Status.STATUS_TITLE, edit_write_title.getText().toString());
		taskParams.put(Status.STATUS_CONTENT, edit_write_content.getText().toString());
		taskParams.put(Status.STATUS_REWARDS, edit_write_rewards.getText().toString());
		taskParams.put(Status.STATUS_ENDTIME, timeString);
		Task task = new Task(Task.TA_RELEASE, taskParams);
		MainService.addTask(task);
	}
	
	@Override
	public void refresh(Object... params) {
		dismissProgressDialog();
		int taskId = (Integer) params[0];
		switch (taskId) {
		case Task.TA_RELEASE:
			String result = ((String) params[1]);
			if (null == result) {
				Toast.makeText(WriteActivity.this, "没网络啊！！！亲", Toast.LENGTH_SHORT).show();
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					String state = jsonObject.getString(JsonString.Return.STATE).trim();
					if (state.equals(JsonString.Return.STATE_OK)) {
						Intent intent = new Intent();
						setResult(IntentString.ResultCode.WRITE_HOMEPAGE, intent);
						finish();
					} else {
						Toast.makeText(WriteActivity.this, "惊呆了，网络出错了", Toast.LENGTH_SHORT).show();
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (IntentString.RequestCode.WRITE_SELECTENDTIME == requestCode) {
			if (IntentString.ResultCode.SELECTENDTIME_WRITE == resultCode) {
				int year = data.getIntExtra(Time.YEAR, 0);
				int month = data.getIntExtra(Time.MONTH, 0);
				int day = data.getIntExtra(Time.DAY, 0);
				int hour = data.getIntExtra(Time.HOUR, 0);
				int minute = data.getIntExtra(Time.MINUTE, 0);
				Time endTime = new Time(year, month, day, hour, minute);
				txt_write_display_endtime.setText(TimeUtil.getDisplayTime(TimeUtil.getNow(Calendar.getInstance()), 
						endTime));
				timeString = TimeUtil.timeToOriginal(endTime);
			}
		}
	}
	
	/*
	 * 退出时要将它从链表中移除，不然下次再进来，可能不能更新UI
	 */
	protected void onDestroy() {
		super.onDestroy();
		MainService.removeActivity(WriteActivity.this);
	}
}
