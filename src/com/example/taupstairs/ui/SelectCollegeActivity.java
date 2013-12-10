package com.example.taupstairs.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.example.taupstairs.R;
import com.example.taupstairs.bean.College;
import com.example.taupstairs.intent.IntentInfo;
import com.example.taupstairs.services.CollegeService;

public class SelectCollegeActivity extends Activity implements ItaActivity {

	private Button btn_back;
	private EditText edit;
	private ListView list;
	private ArrayAdapter<String> adapter;
	private String[] allCollegeNames;
	private CollegeService collegeService;
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
				Intent intent = new Intent();
				setResult(IntentInfo.ResultCode.NO_SELECTCOLLEGE, intent);
				finish();
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
					showAllCollege();
				} else {
					showSearchCollege();
				}
			}
		});
	}
	
	private void showAllCollege() {
		list.setAdapter(adapter);
	}

	private void showSearchCollege() {
		Cursor cursor = collegeService.getCursorByKeyword(edit.getText().toString());
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.college_item, cursor, 
				new String[] {College.COLLEGE_NAME}, new int[] {R.id.txt_college_item}, 
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		list.setAdapter(adapter);
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		allCollegeNames = getResources().getStringArray(R.array.college_name);
		list = (ListView)findViewById(R.id.list_college);
		adapter = new ArrayAdapter<String>(SelectCollegeActivity.this, 
				R.layout.college_item, allCollegeNames);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String collegeName = ((TextView)arg1).getText().toString();
				Intent intent = new Intent();
				intent.putExtra("collegeName", collegeName);
				setResult(IntentInfo.ResultCode.SELECTCOLLEGE_LOGIN, intent);
				finish();
			}
		});
		collegeService = new CollegeService(SelectCollegeActivity.this);
	}
	@Override
	public void refresh(Object... params) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		setResult(IntentInfo.ResultCode.NO_SELECTCOLLEGE, intent);
		finish();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		collegeService.closeDBHelper();
	}
}
