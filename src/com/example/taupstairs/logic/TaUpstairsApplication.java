package com.example.taupstairs.logic;

import android.app.Application;
import android.content.Context;

import com.baidu.frontia.FrontiaApplication;
import com.example.taupstairs.bean.Info;
import com.example.taupstairs.bean.SignUp;
import com.example.taupstairs.bean.Status;
import com.example.taupstairs.imageCache.LazyImageLoader;

public class TaUpstairsApplication extends Application {

	public static Context context;
	public static LazyImageLoader lazyImageLoader;
	private Status status;
	private Info info;
	private SignUp signUp;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = this.getApplicationContext();
		FrontiaApplication.initFrontiaApplication(context);
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
	
	public Info getInfo() {
		return info;
	}
	
	public void setInfo(Info info) {
		this.info = info;
	}

	public SignUp getSignUp() {
		return signUp;
	}

	public void setSignUp(SignUp signUp) {
		this.signUp = signUp;
	}
	
}
