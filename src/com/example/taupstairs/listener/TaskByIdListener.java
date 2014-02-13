package com.example.taupstairs.listener;

import com.example.taupstairs.bean.Status;
import com.example.taupstairs.ui.activity.TaskByIdActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class TaskByIdListener implements OnClickListener {

	private Context context;
	private String statusId;
	
	public TaskByIdListener(Context context, String statusId) {
		this.context = context;
		this.statusId = statusId;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(context, TaskByIdActivity.class);
		intent.putExtra(Status.STATUS_ID, statusId);
		context.startActivity(intent);
	}

}
