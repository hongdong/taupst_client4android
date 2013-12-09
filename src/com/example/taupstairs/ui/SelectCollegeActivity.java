package com.example.taupstairs.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.taupstairs.R;

public class SelectCollegeActivity extends Activity implements ItaActivity {

	private Button btn_back;
	private EditText edit;
	private ListView list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_college);
		init();
		btn_back = (Button)findViewById(R.id.btn_back_search);
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SelectCollegeActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
		edit = (EditText)findViewById(R.id.edit_college);
		edit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (edit.getText().toString().isEmpty()) {
					//list.clearTextFilter();
				} else {
					//list.setFilterText(edit.getText().toString());
				}
			}
		});
	}
	@Override
	public void init() {
		// TODO Auto-generated method stub
		String[] arr = new String[]{"福大", "师大", "工程",};
		list = (ListView)findViewById(R.id.list_college);
		list.setTextFilterEnabled(true);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(SelectCollegeActivity.this, 
				R.layout.college_item, arr);
		list.setAdapter(adapter);
	}
	@Override
	public void refresh(Object... params) {
		// TODO Auto-generated method stub
		
	}
}
