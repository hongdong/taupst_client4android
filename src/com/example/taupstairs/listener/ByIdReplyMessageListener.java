package com.example.taupstairs.listener;

import android.view.View;
import android.view.View.OnClickListener;

import com.example.taupstairs.ui.activity.TaskByIdActivity;

public class ByIdReplyMessageListener implements OnClickListener {

	private TaskByIdActivity context;
	private String messageId;
	private String replyId;
	private String replyNickname;
	
	public ByIdReplyMessageListener(TaskByIdActivity context, String messageId, String replyId, String replyNickname) {
		super();
		this.context = context;
		this.messageId = messageId;
		this.replyId = replyId;
		this.replyNickname = replyNickname;
	}

	@Override
	public void onClick(View v) {
		context.changeEditHint(messageId, replyId, replyNickname);
	}

}
