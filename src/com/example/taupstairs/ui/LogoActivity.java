package com.example.taupstairs.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.example.taupstairs.R;
import com.example.taupstairs.bean.User;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.util.SharedPreferencesUtil;

public class LogoActivity extends Activity implements ItaActivity {

	private User defaultUser;
	private ImageView img_logo;
	private Animation animation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logo);
		Intent intent = new Intent(LogoActivity.this, MainService.class);
		startService(intent);
		img_logo = (ImageView)findViewById(R.id.img_logo);
		animation = AnimationUtils.loadAnimation(LogoActivity.this, R.anim.logo);
		animation.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
				
			}
			
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			public void onAnimationEnd(Animation animation) {
				defaultUser = SharedPreferencesUtil.getDefaultUser(LogoActivity.this);
				if (null != defaultUser) {				
					jumpToHomePage();			//如果已经存在默认登录账户，就直接登录	，跳转到主页面	
				} else {
					jumpToLogin();				//只有第一次使用软件才会跳转到登录页面手动登录
				}
			}
		});
		img_logo.setAnimation(animation);
	}
	
	/*跳转到主页面去*/
	private void jumpToHomePage() {
		Intent intent = new Intent(LogoActivity.this, HomePageActivity.class);
		startActivity(intent);
		finish();
	}
	
	/*跳转到登录页面去*/
	private void jumpToLogin() {
		Intent intent = new Intent(LogoActivity.this, LoginActivity.class);
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		finish();
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void refresh(Object... params) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBackPressed() {
		// 页面显示logo的时候，不响应手机的back键（物理按键，每个安卓手机都有）

	}
}
