package com.example.taupstairs.ui.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taupstairs.R;
import com.example.taupstairs.adapter.InfoMessageAdapter;
import com.example.taupstairs.app.TaUpstairsApplication;
import com.example.taupstairs.bean.Info;
import com.example.taupstairs.bean.InfoMessage;
import com.example.taupstairs.bean.Message;
import com.example.taupstairs.bean.MessageContent;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Status;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.imageCache.SimpleImageLoader;
import com.example.taupstairs.listener.PersonDataListener;
import com.example.taupstairs.listener.ReplyInfoMessageListener;
import com.example.taupstairs.listener.ReplyListInfoMessageListener;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.services.PersonService;
import com.example.taupstairs.string.JsonString;
import com.example.taupstairs.util.HttpClientUtil;
import com.example.taupstairs.util.KeyBoardUtil;
import com.example.taupstairs.util.SharedPreferencesUtil;
import com.example.taupstairs.util.TimeUtil;

public class InfoMessageActivity extends Activity implements ItaActivity {

	private Button btn_back, btn_message;
	private Holder holder;
	private Info info;
	private InfoMessageAdapter adapter;
	private EditText edit_message;
	private String edit_text;
	private String messageId, replyId, replyNickname;
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_message);
		MainService.addActivity(this);
		init();
	}

	@Override
	public void init() {
		initHolder();
		initData();
		initView();
	}
	
	private void initData() {
		TaUpstairsApplication app = (TaUpstairsApplication) getApplication();
		info = app.getInfo();
		if (null == info.getInfoMessage()) {
			doGetInfoMessageTask();
		} else {
			displayMessage();
		}
	}
	
	private void initView() {
		btn_back = (Button) findViewById(R.id.btn_back_info_message_detail);
		edit_message = (EditText)findViewById(R.id.edit_task_detail_message);
		btn_message = (Button)findViewById(R.id.btn_task_detail_message);
		progressDialog = new ProgressDialog(this);
		
		SimpleImageLoader.showImage(holder.img_photo, 
				HttpClientUtil.PHOTO_BASE_URL + info.getPersonPhotoUrl());
		PersonDataListener personDataListener = new PersonDataListener(this, info.getPersonId());
		holder.img_photo.setOnClickListener(personDataListener);
		
		holder.txt_nickname.setText(info.getPersonNickname());
		
		String personSex = info.getPersonSex().trim();
		if (personSex.equals(Person.MALE)) {
			holder.img_sex.setImageResource(R.drawable.icon_male);
		} else if (personSex.equals(Person.FEMALE)) {
			holder.img_sex.setImageResource(R.drawable.icon_female);
		}
		
		String displayTime = TimeUtil.getDisplayTime(TimeUtil.getNow(Calendar.getInstance()), info.getInfoReleaseTime());
		holder.txt_releasetime.setText(displayTime);
		
		holder.txt_grade.setText(info.getPersonGrade());
		holder.txt_department.setText(info.getPersonDepartment());
		
		btn_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		btn_message.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (edit_message.getText().toString().trim().equals("")) {
					
				} else {
					progressDialog.setCancelable(false);
					progressDialog.setMessage("    稍等片刻...");
					progressDialog.show();
					doMessageTask();
				}
			}
		});
	}
	
	public void changeEditHint(String messageId, String replyId, String replyNickname) {
		this.messageId = messageId;
		this.replyId = replyId;
		this.replyNickname = replyNickname;
		edit_message.setHint("回复  " + replyNickname);
		edit_message.requestFocus();
		KeyBoardUtil.show(this, edit_message);
	}
	
	private void doGetInfoMessageTask() {
		HashMap<String, Object> taskParams = new HashMap<String, Object>();
		taskParams.put(Info.INFO_SOURCE, info.getInfoSource());
		taskParams.put(Info.INFO_TYPE, info.getInfoType());
		taskParams.put(Task.TA_GETINFO_DETAIL_ACTIVITY, Task.TA_GETINFO_DETAIL_MESSAGE);
		Task task = new Task(Task.TA_GETINFO_DETAIL, taskParams);
		MainService.addTask(task);
	}
	
	private void doMessageTask() {
		Map<String, Object> taskParams = new HashMap<String, Object>();
		taskParams.put(Task.TA_MESSAGE_ACTIVITY, Task.TA_MESSAGE_ACTIVITY_INFO);
		taskParams.put(Status.STATUS_ID, info.getInfoMessage().getStatusId());
		taskParams.put(MessageContent.CONTENT, edit_message.getText().toString().trim());
		taskParams.put(Task.TA_MESSAGE_MODE, Task.TA_MESSAGE_MODE_CHILD);
		taskParams.put(Message.MESSAGE_ID, messageId);
		taskParams.put(MessageContent.REPLY_ID, replyId);
		Task task = new Task(Task.TA_MESSAGE, taskParams);
		MainService.addTask(task);
	}

	@Override
	public void refresh(Object... params) {
		if (params[1] != null) {
			int taskId = (Integer) params[0];
			switch (taskId) {
			case Task.TA_GETINFO_DETAIL:
				InfoMessage infoMessage = (InfoMessage) params[1];
				info.setInfoMessage(infoMessage);
				displayMessage();
				break;
				
			case Task.TA_MESSAGE:
				progressDialog.dismiss();
				String message = (String) params[1];
				try {
					JSONObject jsonObject = new JSONObject(message);
					String state = jsonObject.getString(JsonString.Return.STATE).trim();
					if (state.equals(JsonString.Return.STATE_OK)) {
						edit_text = edit_message.getText().toString().trim();
						edit_message.setText("");
						postMessage();	
						KeyBoardUtil.dismiss(this, edit_message);
					} else {		
						Toast.makeText(this, "网络竟然出错了", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		}
	}
	
	private void displayMessage() {
		holder.txt_message.setText(info.getInfoMessage().getCurrentMessage());
		holder.txt_status_nickname.setText(info.getInfoMessage().getStatusPersonNickname());
		holder.txt_status_title.setText("  :  " + info.getInfoMessage().getStatusTitle());
		displayMessageList();
	}
	
	private void displayMessageList() {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<MessageContent> contents = info.getInfoMessage().getContents();
		for (int i = 0; i < contents.size(); i++) {
			MessageContent content = contents.get(i);		
			String t = content.getContent();				
			Map<String, Object> item = new HashMap<String, Object>();
			String replyNickname = content.getReplyNickname();
			String receiveNickname = content.getReceiveNickname();
			String text = replyNickname + MessageContent.REPLY_TEXT + 
					receiveNickname + MessageContent.REPLY_TEMP + t;
			item.put(MessageContent.CONTENT, text);			
			item.put(MessageContent.REPLY_START, 0);
			item.put(MessageContent.REPLY_END, replyNickname.length());
			item.put(MessageContent.RECEIVE_START, 
					replyNickname.length() + MessageContent.REPLY_TEXT_LENNTH);
			item.put(MessageContent.RECEIVEY_END, 
					replyNickname.length() + MessageContent.REPLY_TEXT_LENNTH + receiveNickname.length());
			list.add(item);
		}
		adapter = new InfoMessageAdapter(this, list);
		holder.list_message.setAdapter(adapter);
		
		String messageId = info.getInfoMessage().getMessageId();
		/*这两个在消息详情里面没传过来，要从外面拿
		 * 在点击回复按钮的时候，要回复这个人，需要用到*/
		String replyId = info.getPersonId();
		String replyNickname = info.getPersonNickname();
		ReplyInfoMessageListener replyInfoMessageListener = 
				new ReplyInfoMessageListener(this, messageId, replyId, replyNickname);
		holder.txt_message_reply.setOnClickListener(replyInfoMessageListener);
		
		ReplyListInfoMessageListener replyListInfoMessageListener = 
				new ReplyListInfoMessageListener(this, messageId, contents);
		holder.list_message.setOnItemClickListener(replyListInfoMessageListener);
	}
	
	/*头像，昵称，性别，发布时间，来自哪个院系、年级，
	 * 留言内容，任务发布人，任务标题*/
	private class Holder {
		public ImageView img_photo;
		public TextView txt_nickname;
		public ImageView img_sex;
		public TextView txt_releasetime;
		public TextView txt_grade;
		public TextView txt_department;
		
		public TextView txt_message;
		public TextView txt_message_reply;
		public TextView txt_status_nickname;
		public TextView txt_status_title;
		
		public ListView list_message;
	}
	
	private void initHolder() {
		holder = new Holder();
		holder.img_photo = (ImageView)findViewById(R.id.img_photo);
		holder.txt_nickname = (TextView)findViewById(R.id.txt_nickname);
		holder.img_sex = (ImageView)findViewById(R.id.img_sex);
		holder.txt_releasetime = (TextView)findViewById(R.id.txt_releasetime);
		holder.txt_grade = (TextView)findViewById(R.id.txt_grade);
		holder.txt_department = (TextView)findViewById(R.id.txt_department);	
		
		holder.txt_message = (TextView)findViewById(R.id.txt_info_message_message);
		holder.txt_message_reply = (TextView)findViewById(R.id.txt_info_message_reply);
		holder.txt_status_nickname = (TextView)findViewById(R.id.txt_info_message_nickname);
		holder.txt_status_title = (TextView)findViewById(R.id.txt_info_message_title);
		
		holder.list_message = (ListView)findViewById(R.id.list_info_message_message);
	}
	
	private void postMessage() {
		MessageContent content = new MessageContent();
		String personId = SharedPreferencesUtil.getDefaultUser(this).getUserId();
		content.setReplyId(personId);
		PersonService service = new PersonService(this);
		Person person = service.getPersonById(personId);	//这里读出来可能是空的。但基本上不会，先不管了
		content.setReplyNickname(person.getPersonNickname());
		content.setReceiveNickname(replyNickname);
		content.setContent(edit_text);
		info.getInfoMessage().getContents().add(content);
		displayMessageList();	//在这里用notify没用，这跟他内部机制有些关系。只好重新setAdapter了
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MainService.removeActivity(this);
	}
	
}
