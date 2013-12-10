package com.example.taupstairs.adapter;

import com.example.taupstairs.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class LoginBaseAdapter extends BaseAdapter {
	
	private int count;
	private Context context;
	
	public LoginBaseAdapter(Context context, int count) {
		this.context = context;
		this.count = count;
	}

	@Override
	public int getCount() {
		
		return count;
	}

	@Override
	public Object getItem(int position) {
		
		return null;
	}

	@Override
	public long getItemId(int position) {
		
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = null;
		switch (position) {
		case 0:
			view = LayoutInflater.from(context).inflate(R.layout.login_college, null);
			break;
		case 1:
			view = LayoutInflater.from(context).inflate(R.layout.login_studentid, null);
			break;
		case 2:
			view = LayoutInflater.from(context).inflate(R.layout.login_password, null);
			break;
		default:
			break;
		}
		return view;
	}

}
