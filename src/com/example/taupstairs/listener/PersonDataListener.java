package com.example.taupstairs.listener;

import com.example.taupstairs.bean.Person;
import com.example.taupstairs.ui.activity.PersonDataActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class PersonDataListener implements OnClickListener {

	private Context context;
	private String personId;
	private String permission;
	
	public PersonDataListener(Context context, String personId, String permission) {
		this.context = context;
		this.personId = personId;
		this.permission = permission;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(context, PersonDataActivity.class);
		intent.putExtra(Person.PERSON_ID, personId);
		intent.putExtra(Person.PERMISSION, permission);
		context.startActivity(intent);
	}

}
