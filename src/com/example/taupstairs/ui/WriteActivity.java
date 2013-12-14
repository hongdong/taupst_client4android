package com.example.taupstairs.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.taupstairs.R;

public class WriteActivity extends Activity implements ItaActivity {

	private Button btn_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write);
		init();
	}
	@Override
	public void init() {
		btn_back = (Button)findViewById(R.id.btn_back_write);
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
