package com.example.taupstairs.listener;

import java.util.List;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.example.taupstairs.bean.MessageContent;
import com.example.taupstairs.ui.activity.TaskDetailActivity;

public class ReplyListMessageListener implements OnItemClickListener {

	private TaskDetailActivity context;
	private List<MessageContent> contents;
	private String messageId;

	public ReplyListMessageListener(TaskDetailActivity context,
			String messageId, List<MessageContent> contents) {
		super();
		this.context = context;
		this.messageId = messageId;
		this.contents = contents;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		String replyId = contents.get(arg2).getReplyId();
		String replyNickname = contents.get(arg2).getReplyNickname();
		context.changeEditHint(messageId, replyId, replyNickname);
	}

}
