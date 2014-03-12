package com.example.taupstairs.listener;

import com.example.taupstairs.bean.Person;
import com.example.taupstairs.ui.activity.LargePhotoActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class LargrPhotoListener implements OnClickListener {

	private Context context;
	private String photoUrl;
	
	public LargrPhotoListener(Context context, String photoUrl) {
		this.context = context;
		this.photoUrl = photoUrl;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(context, LargePhotoActivity.class);
		intent.putExtra(Person.PERSON_PHOTOURL, photoUrl);
		context.startActivity(intent);
	}

}
