package com.example.taupstairs.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HomePageFragmentPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments;
	
	public HomePageFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	public HomePageFragmentPagerAdapter(FragmentManager fm,
			List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}


	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragments.size();
	}

}
