package com.example.taupstairs.ui;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.Time;
import com.example.taupstairs.string.IntentString;
import com.example.taupstairs.util.TimeUtil;

public class SelectEndtimeActivity extends Activity implements ItaActivity {

	private Button btn_back, btn_ok;
	private DatePicker datePicker;
	private TimePicker timePicker;
	private Time now, endTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_endtime);
		init();
	}
	
	@Override
	public void init() {
		initData();
		initView();
	}
	
	private void initData() {
		now = TimeUtil.getNow(Calendar.getInstance());
		endTime = now;
	}
	
	private void initView() {
		btn_back = (Button)findViewById(R.id.btn_back_selectendtime);
		btn_ok = (Button)findViewById(R.id.btn_ok_selectendtime);
		datePicker = (DatePicker)findViewById(R.id.selectendtime_date);
		timePicker = (TimePicker)findViewById(R.id.selectendtime_time);
		
		datePicker.init(now.getYear(), now.getMonth(), now.getDay(), new OnDateChangedListener() {
			public void onDateChanged(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				endTime.setYear(year);
				endTime.setMonth(monthOfYear);
				endTime.setDay(dayOfMonth);
			}
		});
		
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(now.getHour());
		timePicker.setCurrentMinute(now.getMinute());
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				endTime.setHour(hourOfDay);
				endTime.setMinute(minute);
			}
		});
		
		
		btn_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		btn_ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				now = TimeUtil.getNow(Calendar.getInstance());
				if (TimeUtil.LARGE == TimeUtil.compare(endTime, now)) {
					Intent intent = new Intent();
					intent.putExtra(Time.YEAR, endTime.getYear());
					intent.putExtra(Time.MONTH, endTime.getMonth());
					intent.putExtra(Time.DAY, endTime.getDay());
					intent.putExtra(Time.HOUR, endTime.getHour());
					intent.putExtra(Time.MINUTE, endTime.getMinute());
					setResult(IntentString.ResultCode.SELECTENDTIME_WRITE, intent);
					finish();
				} else {
					Toast.makeText(SelectEndtimeActivity.this, 
							"截止时间必须大于当前时间哦！", 
							Toast.LENGTH_LONG)
							.show();
				}
			}
		});
	}
	
	@Override
	public void refresh(Object... params) {
		
	}
}
