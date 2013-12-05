package com.example.taupstairs.ui;

import com.example.taupstairs.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class LogoActivity extends Activity {

	private ImageView img_logo;
	private Animation animation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logo);
		img_logo = (ImageView)findViewById(R.id.img_logo);
		animation = AnimationUtils.loadAnimation(LogoActivity.this, R.anim.logo);
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LogoActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
		img_logo.setAnimation(animation);
	}
}
