package com.example.taupstairs.app;

import com.example.taupstairs.bean.Status;
import com.example.taupstairs.imageCache.LazyImageLoader;

import android.app.Application;
import android.content.Context;

public class TaUpstairsApplication extends Application {

	public static Context context;
	public static LazyImageLoader lazyImageLoader;
	private Status status;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = this.getApplicationContext();
		lazyImageLoader = new LazyImageLoader();
	}
	
	/*
	 * 这两个方法用来传递数据的，不然intent传递自定义的比较复杂的数据非常麻烦
	 */
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
}
