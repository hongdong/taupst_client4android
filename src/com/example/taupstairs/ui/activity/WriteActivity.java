package com.example.taupstairs.ui.activity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
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
	private TextView txt_12, txt_18, txt_21, txt_24, txt_day_1, txt_day_3, txt_day_10, txt_month_1;
	private TextView[] txtTimes = {txt_12, txt_18, txt_21, txt_24, 
			txt_day_1, txt_day_3, txt_day_10, txt_month_1};
	private int[] txtTimeIds = {R.id.txt_write_12, R.id.txt_write_18, R.id.txt_write_21, R.id.txt_write_24, 
			R.id.txt_write_day_1, R.id.txt_write_day_3, R.id.txt_write_day_10, R.id.txt_write_month_1};
	private Time now, endTime;
	private String timeString;
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		now = TimeUtil.getNow();
		progressDialog = new ProgressDialog(this);
		TxtEndTimeListener listener = new TxtEndTimeListener();
		for (int i = 0; i < txtTimes.length; i++) {
			txtTimes[i] = (TextView)findViewById(txtTimeIds[i]);
			txtTimes[i].setOnClickListener(listener);
		}
		
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
		Map<String, Object> taskParams = new HashMap<String, Object>();
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
						setResult(IntentString.ResultCode.WRITE_TASKACTIVITY, intent);
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
				endTime = new Time(year, month, day, hour, minute);
				displayEndTime();
				setTextColor("00000000");
			}
		}
	}
	
	/**
	 * 显示截止时间
	 * @param endTime
	 */
	private void displayEndTime() {
		String displayTime = TimeUtil.getDisplayTime(now, endTime);
		txt_write_display_endtime.setText(displayTime);
		timeString = TimeUtil.timeToOriginal(endTime);
	}
	
	/**
	 * 设置常用截止时间文字的颜色。不被选中时为灰色，选中时为黑色
	 * @param flag
	 */
	private void setTextColor(String flag) {
		char[] temp = flag.toCharArray();
		for (int i = 0; i < temp.length; i++) {
			if ('0' == temp[i]) {
				ColorStateList csl = (ColorStateList) getResources().getColorStateList(R.color.gray);
				txtTimes[i].setTextColor(csl);
			} else if ('1' == temp[i]) {
				ColorStateList csl = (ColorStateList) getResources().getColorStateList(R.color.red);
				txtTimes[i].setTextColor(csl);
			}
		}
	}
	
	/*
	 * 退出时要将它从链表中移除，不然下次再进来，可能不能更新UI
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MainService.removeActivity(WriteActivity.this);
	}
	
	/**
	 * 常用截至时间中几个TextView的监听器
	 * @author Administrator
	 *
	 */
	public class TxtEndTimeListener implements OnClickListener {
		public void onClick(View v) {
			now = TimeUtil.getNow();
			endTime = new Time(now);
			switch (v.getId()) {
			case R.id.txt_write_12:
				endTime.setHour(12);
				endTime.setMinute(0);
				if (TimeUtil.LARGE == TimeUtil.compare(now, endTime)) {
					Toast.makeText(WriteActivity.this, "额！已经过了这个时间了！", Toast.LENGTH_SHORT).show();
				} else {
					setTextColor("10000000");
					displayEndTime();
				}
				break;
				
			case R.id.txt_write_18:
				endTime.setHour(18);
				endTime.setMinute(0);
				if (TimeUtil.LARGE == TimeUtil.compare(now, endTime)) {
					Toast.makeText(WriteActivity.this, "额！已经过了这个时间了！", Toast.LENGTH_SHORT).show();
				} else {
					setTextColor("01000000");
					displayEndTime();
				}
				break;
				
			case R.id.txt_write_21:
				endTime.setHour(21);
				endTime.setMinute(0);
				if (TimeUtil.LARGE == TimeUtil.compare(now, endTime)) {
					Toast.makeText(WriteActivity.this, "额！已经过了这个时间了！", Toast.LENGTH_SHORT).show();
				} else {
					setTextColor("00100000");
					displayEndTime();
				}
				break;
	
			case R.id.txt_write_24:
				endTime.setHour(23);
				endTime.setMinute(59);
				setTextColor("00010000");
				displayEndTime();
				break;
				
			case R.id.txt_write_day_1:
				Calendar calendar1 = Calendar.getInstance();
				calendar1.add(Calendar.DATE, 1);
				endTime = TimeUtil.getTimeByCalendar(calendar1);
				setTextColor("00001000");
				displayEndTime();
				break;
				
			case R.id.txt_write_day_3:
				Calendar calendar3 = Calendar.getInstance();
				calendar3.add(Calendar.DATE, 3);
				endTime = TimeUtil.getTimeByCalendar(calendar3);
				setTextColor("00000100");
				displayEndTime();
				break;
				
			case R.id.txt_write_day_10:
				Calendar calendar10 = Calendar.getInstance();
				calendar10.add(Calendar.DATE, 10);
				endTime = TimeUtil.getTimeByCalendar(calendar10);
				setTextColor("00000010");
				displayEndTime();
				break;
				
			case R.id.txt_write_month_1:
				Calendar calendarM1 = Calendar.getInstance();
				calendarM1.add(Calendar.MONTH, 1);
				endTime = TimeUtil.getTimeByCalendar(calendarM1);
				setTextColor("00000001");
				displayEndTime();
				break;

			default:
				break;
			}
		}
	}
	
}
