package com.example.taupstairs.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.taupstairs.R;
import com.example.taupstairs.logic.ItaActivity;

public class PersonDataActivity extends Activity implements ItaActivity {

	private Button btn_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_data);
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
		btn_back = (Button)findViewById(R.id.btn_back_person_data);
		btn_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	@Override
	public void refresh(Object... params) {
		// TODO Auto-generated method stub
		
	}
}
