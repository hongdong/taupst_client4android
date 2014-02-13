package com.example.taupstairs.listener;

import com.example.taupstairs.ui.activity.InfoMessageActivity;

import android.view.View;
import android.view.View.OnClickListener;

public class ReplyInfoMessageListener implements OnClickListener {

	private InfoMessageActivity context;
	private String replyId;
	private String replyNickname;
	
	public ReplyInfoMessageListener(InfoMessageActivity context, String replyId, String replyNickname) {
		super();
		this.context = context;
		this.replyId = replyId;
		this.replyNickname = replyNickname;
	}

	@Override
	public void onClick(View v) {
		context.changeEditHint(replyId, replyNickname);
	}

}
