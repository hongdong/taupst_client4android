package com.example.taupstairs.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taupstairs.R;
import com.example.taupstairs.logic.ItaFragment;
import com.example.taupstairs.ui.activity.HomePageActivity;

public class InfoFragment extends Fragment implements ItaFragment {

	private View view;
//	private HomePageActivity context;
	
	public InfoFragment() {
		super();
	}
	
	public InfoFragment(HomePageActivity context) {
		super();
//		this.context = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_info, container, false);
		initView();
		return view;
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(Object... params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub
		
	}

}
