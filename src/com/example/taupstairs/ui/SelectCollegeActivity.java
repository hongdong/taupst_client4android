package com.example.taupstairs.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.example.taupstairs.R;

public class SelectCollegeActivity extends Activity {

	private SearchView search;
	private ListView list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_college);
		list = (ListView)findViewById(R.id.list_college);
		list.setTextFilterEnabled(true);
		search = (SearchView)findViewById(R.id.search_college);
		search.setIconifiedByDefault(false);
		search.setSubmitButtonEnabled(false);
		search.setQueryHint("请输入大学名称");
		search.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(newText)) {
					list.clearTextFilter();
				} else { 
					list.setFilterText(newText);
				}
				return true;
			}
		});
	}
}
