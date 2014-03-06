package com.example.taupstairs.ui.activity;

import java.util.List;

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
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.services.CollegeService;
import com.example.taupstairs.string.IntentString;

public class SelectCollegeActivity extends Activity implements ItaActivity {

	private Button btn_back;
	private EditText edit;
	private ListView list;
	private ArrayAdapter<String> adapter;
	private List<String> collegeNames;
	private CollegeService collegeService;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_college);
		init();
		
	}
	
	@Override
	public void init() {
		initData();
		initView();
	}
	
	private void initData() {
		collegeService = new CollegeService(SelectCollegeActivity.this);
		collegeNames = collegeService.getCollegeNames();
	}
	
	private void initView() {
		btn_back = (Button)findViewById(R.id.btn_back_search);
		edit = (EditText)findViewById(R.id.edit_college);
		list = (ListView)findViewById(R.id.list_college);
		
		adapter = new ArrayAdapter<String>(SelectCollegeActivity.this, 
				R.layout.common_txt_item, collegeNames);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String collegeName = ((TextView)arg1).getText().toString();
				College college = collegeService.getCollegeByName(collegeName);
				Intent intent = new Intent();
				intent.putExtra(College.COLLEGE_ID, college.getCollegeId());
				intent.putExtra(College.COLLEGE_NAME, collegeName);
				setResult(IntentString.ResultCode.SELECTCOLLEGE_LOGIN, intent);
				finish();
			}
		});
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				setResult(Activity.RESULT_CANCELED, intent);
				finish();
			}
		});
		edit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			@Override
			public void afterTextChanged(Editable s) {
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
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.common_txt_item, cursor, 
				new String[] {College.COLLEGE_NAME}, new int[] {R.id.txt_common}, 
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		list.setAdapter(adapter);
	}
	
	@Override
	public void refresh(Object... params) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		setResult(Activity.RESULT_CANCELED, intent);
		finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		collegeService.closeDBHelper();
	}
}
