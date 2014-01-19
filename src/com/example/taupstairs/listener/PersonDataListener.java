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
	
	public PersonDataListener(Context context, String personId) {
		this.context = context;
		this.personId = personId;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(context, PersonDataActivity.class);
		intent.putExtra(Person.PERSON_ID, personId);
		context.startActivity(intent);
	}

}
