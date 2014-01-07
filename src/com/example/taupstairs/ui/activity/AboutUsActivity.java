package com.example.taupstairs.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.taupstairs.R;
import com.example.taupstairs.logic.ItaActivity;

public class AboutUsActivity extends Activity implements ItaActivity {

	private Button btn_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us);
		btn_back = (Button)findViewById(R.id.btn_back_aboutus);
		btn_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refresh(Object... params) {
		// TODO Auto-generated method stub

	}

}
