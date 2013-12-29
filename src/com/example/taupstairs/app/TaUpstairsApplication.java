package com.example.taupstairs.app;

import com.example.taupstairs.imageCache.LazyImageLoader;

import android.app.Application;
import android.content.Context;

public class TaUpstairsApplication extends Application {

	public static Context context;
	public static LazyImageLoader lazyImageLoader;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = this.getApplicationContext();
		lazyImageLoader = new LazyImageLoader();
	}
}
