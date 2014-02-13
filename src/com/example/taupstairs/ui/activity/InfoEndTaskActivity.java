package com.example.taupstairs.ui.activity;

import com.example.taupstairs.R;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import android.app.Activity;
import android.os.Bundle;

public class InfoEndTaskActivity extends Activity implements ItaActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_end_task);
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
		
	}

	@Override
	public void refresh(Object... params) {
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MainService.removeActivity(this);
	}
}
