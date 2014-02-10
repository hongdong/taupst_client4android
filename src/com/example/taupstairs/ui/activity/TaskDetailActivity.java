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
import android.content.Intent;
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
import com.example.taupstairs.adapter.MessageAdapter;
import com.example.taupstairs.app.TaUpstairsApplication;
import com.example.taupstairs.bean.Message;
import com.example.taupstairs.bean.MessageContent;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Status;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.bean.Time;
import com.example.taupstairs.imageCache.SimpleImageLoader;
import com.example.taupstairs.listener.PersonDataListener;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.services.PersonService;
import com.example.taupstairs.string.IntentString;
import com.example.taupstairs.string.JsonString;
import com.example.taupstairs.util.HttpClientUtil;
import com.example.taupstairs.util.KeyBoardUtil;
import com.example.taupstairs.util.SharedPreferencesUtil;
import com.example.taupstairs.util.TimeUtil;

public class TaskDetailActivity extends Activity implements ItaActivity {

	private Button btn_back, btn_refresh, btn_signup, btn_message;
	private TextView txt_multi;
	private Status status;
	private Holder holder;
	private Time now;
	private List<Message> messages;
	private MessageAdapter adapter;
	private EditText edit_message;
	private String edit_text;
	private String messageId, replyId, replyNickname;
	private ProgressDialog progressDialog;
	private boolean flag_message_or_signup;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_detail);
		MainService.addActivity(TaskDetailActivity.this);
		init();
	}
	@Override
	public void init() {
		initData();
		initView();
	}
	
	private void initData() {
		TaUpstairsApplication app = (TaUpstairsApplication) getApplication();
		status = app.getStatus();
		now = TimeUtil.getNow(Calendar.getInstance());
		String personId = SharedPreferencesUtil.getDefaultUser(this).getUserId();
		txt_multi = (TextView)findViewById(R.id.txt_task_detail_multi);
		if (personId.equals(status.getPersonId())) {
			txt_multi.setText("是我发布的");
			txt_multi.setVisibility(View.VISIBLE);
		} else {
			Time end = TimeUtil.originalToTime(status.getStatusEndTime());
			if (TimeUtil.LARGE == TimeUtil.compare(now, end)) {
				txt_multi.setText("已过期");
				txt_multi.setVisibility(View.VISIBLE);	
			} else {
				doCheckStatusTask();
			}
		}
		flag_message_or_signup = false;
	}
	
	/*头像，昵称，性别，发布时间，来自哪个院系、年级，
	 * 标题，内容，报酬，截止时间，报名人数*/
	private class Holder {
		public ImageView img_task_detail_photo;
		public TextView txt_task_detail_nickname;
		public ImageView img_task_detail_sex;
		public TextView txt_task_detail_releasetime;
		public TextView txt_task_detail_grade;
		public TextView txt_task_detail_department;
		
		public TextView txt_task_detail_title;
		public TextView txt_task_detail_content;
		public TextView txt_task_detail_rewards;
		public TextView txt_task_detail_endtime;
		public TextView txt_task_detail_signupcount;
		public TextView txt_task_detail_messagecount;
		public TextView txt_task_detail_no_message;
	}
	
	private void initView() {
		btn_back = (Button)findViewById(R.id.btn_back_task_detail);
		btn_refresh = (Button)findViewById(R.id.btn_refresh_task_detail);
		btn_signup = (Button)findViewById(R.id.btn_task_detail_signup);
		btn_message = (Button)findViewById(R.id.btn_task_detail_message);
		edit_message = (EditText)findViewById(R.id.edit_task_detail_message);
		progressDialog = new ProgressDialog(this);
		
		holder = new Holder();
		holder.img_task_detail_photo = (ImageView)findViewById(R.id.img_photo);
		holder.txt_task_detail_nickname = (TextView)findViewById(R.id.txt_nickname);
		holder.img_task_detail_sex = (ImageView)findViewById(R.id.img_sex);
		holder.txt_task_detail_releasetime = (TextView)findViewById(R.id.txt_releasetime);
		holder.txt_task_detail_grade = (TextView)findViewById(R.id.txt_grade);
		holder.txt_task_detail_department = (TextView)findViewById(R.id.txt_department);	
		holder.txt_task_detail_title = (TextView)findViewById(R.id.txt_task_detail_title);
		holder.txt_task_detail_content = (TextView)findViewById(R.id.txt_task_detail_content);
		holder.txt_task_detail_rewards = (TextView)findViewById(R.id.txt_task_detail_rewards);
		holder.txt_task_detail_endtime = (TextView)findViewById(R.id.txt_task_detail_endtime);
		holder.txt_task_detail_signupcount = (TextView)findViewById(R.id.txt_task_detail_signupcount);
		holder.txt_task_detail_messagecount = (TextView)findViewById(R.id.txt_task_detail_messagecount);
		holder.txt_task_detail_no_message = (TextView)findViewById(R.id.txt_task_detail_no_message);
		
		/*以下进行任务详情基本部分的显示*/
		SimpleImageLoader.showImage(holder.img_task_detail_photo, 
					HttpClientUtil.PHOTO_BASE_URL + status.getPersonPhotoUrl());
		PersonDataListener personDataListener = new PersonDataListener(this, status.getPersonId());
		holder.img_task_detail_photo.setOnClickListener(personDataListener);
		
		holder.txt_task_detail_nickname.setText(status.getPersonNickname());
		
		String personSex = status.getPersonSex().trim();
		if (personSex.equals(Person.MALE)) {
			holder.img_task_detail_sex.setImageResource(R.drawable.icon_male);
		} else if (personSex.equals(Person.FEMALE)) {
			holder.img_task_detail_sex.setImageResource(R.drawable.icon_female);
		}
		
		String displayTime = TimeUtil.getDisplayTime(now, status.getStatusReleaseTime());
		holder.txt_task_detail_releasetime.setText(displayTime);
		
		holder.txt_task_detail_grade.setText(status.getPersonGrade());
		holder.txt_task_detail_department.setText(status.getPersonDepartment());
		holder.txt_task_detail_title.setText(status.getStatusTitle());
		holder.txt_task_detail_content.setText(status.getStatusContent());
		holder.txt_task_detail_rewards.setText(status.getStatusRewards());
		
		String endTime = TimeUtil.getDisplayTime(now, status.getStatusEndTime());
		holder.txt_task_detail_endtime.setText(endTime);
		
		holder.txt_task_detail_signupcount.setText(status.getStatusSignUpCount());
		String messageCount = status.getStatusMessageCount();
		holder.txt_task_detail_messagecount.setText(messageCount);
		if (messageCount.equals("0")) {
			holder.txt_task_detail_no_message.setVisibility(View.VISIBLE);
		} else {
			doGetMessageTask();
		}
		
		btn_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (flag_message_or_signup) {
					Intent intent = new Intent();
					setResult(IntentString.ResultCode.TASKDETAIL_TASKFRAGMENT, intent);
				}
				finish();
			}
		});
		
		btn_refresh.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});
		
		btn_signup.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(TaskDetailActivity.this, SignupActivity.class);
				intent.putExtra(Status.STATUS_ID, status.getStatusId());
				intent.putExtra(Status.PERSON_ID, status.getPersonId());
				startActivityForResult(intent, IntentString.RequestCode.TASKDETAIL_SIGNUP);
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
	
	/**
	 * 检测任务
	 */
	private void doCheckStatusTask() {
		HashMap<String, Object> taskParams = new HashMap<String, Object>(1);
		taskParams.put(Status.STATUS_ID, status.getStatusId());
		Task task = new Task(Task.TA_CHECKSTATUS, taskParams);
		MainService.addTask(task);
	}
	
	/**
	 * 获取留言
	 */
	private void doGetMessageTask() {
		HashMap<String, Object> taskParams = new HashMap<String, Object>(1);
		taskParams.put(Status.STATUS_ID, status.getStatusId());
		taskParams.put(Task.TA_GETMESSAGE_ACTIVITY, Task.TA_GETMESSAGE_ACTIVITY);
		Task task = new Task(Task.TA_GETMESSAGE, taskParams);
		MainService.addTask(task);
	}
	
	/**
	 * 进行留言
	 */
	private void doMessageTask() {
		Map<String, Object> taskParams = new HashMap<String, Object>();
		taskParams.put(Task.TA_MESSAGE_ACTIVITY, Task.TA_MESSAGE_ACTIVITY_TASK);
		taskParams.put(Status.STATUS_ID, status.getStatusId());
		taskParams.put(MessageContent.CONTENT, edit_message.getText().toString().trim());
		if (replyId != null) {		//子留言
			taskParams.put(Task.TA_MESSAGE_MODE, Task.TA_MESSAGE_MODE_CHILD);
			taskParams.put(Message.MESSAGE_ID, messageId);
			taskParams.put(MessageContent.REPLY_ID, replyId);
		} else {					//根留言
			taskParams.put(Task.TA_MESSAGE_MODE, Task.TA_MESSAGE_MODE_ROOT);
			taskParams.put(MessageContent.REPLY_ID, status.getPersonId());
		}
		Task task = new Task(Task.TA_MESSAGE, taskParams);
		MainService.addTask(task);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void refresh(Object... params) {
		if (params[1] != null) {
			int taskId = (Integer) params[0];
			switch (taskId) {
			case Task.TA_CHECKSTATUS:
				String result = (String) params[1];
				try {
					JSONObject jsonObject = new JSONObject(result);
					int state = jsonObject.getInt(JsonString.Return.STATE);
					testState(state);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			
			case Task.TA_GETMESSAGE:
				messages = (List<Message>) params[1];
				ListView listView = (ListView) findViewById(R.id.list_task_detail_message);
				listView.setVisibility(View.VISIBLE);
				adapter = new MessageAdapter(this, messages);
				listView.setAdapter(adapter);
				break;
				
			case Task.TA_MESSAGE:
				String message = (String) params[1];
				try {
					JSONObject jsonObject = new JSONObject(message);
					String state = jsonObject.getString(JsonString.Return.STATE).trim();
					if (state.equals(JsonString.Return.STATE_OK)) {
						edit_text = edit_message.getText().toString().trim();
						edit_message.setText("");
						String newMessageId = jsonObject.getString(JsonString.Message.MESSAGE_ID);
						postMessage(newMessageId);	
						replyId = null;
						KeyBoardUtil.dismiss(this, edit_message);
					} else {		
						Toast.makeText(this, "网络竟然出错了", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				progressDialog.dismiss();

			default:
				break;
			}
		}
	}
	
	/**
	 * 检测任务后测试状态
	 * @param state	
	 */
	private void testState(int state) {
		switch (state) {
		case 0:	//正常
			btn_signup.setVisibility(View.VISIBLE);
			break;
		case 1:	
			txt_multi.setText("已报名");
			txt_multi.setVisibility(View.VISIBLE);
			break;
		case 2:
			Toast.makeText(this, "网络竟然出错了", Toast.LENGTH_SHORT).show();
			break;
		case 3:
			//是我发布的
			break;
		case 4:
			//已过期，我来处理
			break;
		case 5:
			txt_multi.setText("已完结");
			txt_multi.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}
	
	/**
	 * 留言之后更新列表与任务留言数的存储
	 */
	private void postMessage(String newMessageId) {
		String personId = SharedPreferencesUtil.getDefaultUser(this).getUserId();
		PersonService service = new PersonService(this);
		Person person = service.getPersonById(personId);
		if (replyId != null) {	//已有留言上添加子留言，需要遍历找出所回复的那条留言
			Message clickMessage = null;
			for (Message message : messages) {
				if (message.getMessageId().equals(messageId)) {
					clickMessage = message;
					break;
				}
			}
			MessageContent content = new MessageContent();
			content.setReplyId(personId);
			content.setReplyNickname(person.getPersonNickname());
			content.setReceiveNickname(replyNickname);
			content.setContent(edit_text);
			clickMessage.getMessageContents().add(content);
			adapter.notifyDataSetChanged();
		} else {	//一条新留言，有些东西没有创建时要先创建
			Message message = new Message();	
			message.setMessageId(newMessageId);
			message.setPersonId(personId);
			message.setPersonPhotoUrl(person.getPersonPhotoUrl());
			message.setPersonNickname(person.getPersonNickname());		
			Time now = TimeUtil.getNow(Calendar.getInstance());
			message.setMessageTime(TimeUtil.timeToOriginal(now));		
			List<MessageContent> contents = new ArrayList<MessageContent>();
			MessageContent content = new MessageContent();	
			content.setContent(edit_text);
			contents.add(0, content);	
			message.setMessageContents(contents);
			if (messages != null) {
				messages.add(0, message);
				adapter.notifyDataSetChanged();
			} else {
				holder.txt_task_detail_no_message.setVisibility(View.GONE);
				ListView listView = (ListView) findViewById(R.id.list_task_detail_message);	
				messages = new ArrayList<Message>();
				messages.add(0, message);
				adapter = new MessageAdapter(this, messages);
				listView.setAdapter(adapter);
				listView.setVisibility(View.VISIBLE);
			}
		}
		/*改变任务留言数*/
		int count = Integer.valueOf(status.getStatusMessageCount()) + 1;
		String messageCount = String.valueOf(count);
		holder.txt_task_detail_messagecount.setText(messageCount);
		status.setStatusMessageCount(messageCount);
		flag_message_or_signup = true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case IntentString.RequestCode.TASKDETAIL_SIGNUP:
			if (IntentString.ResultCode.SIGNUP_TASKDETAIL == resultCode) {
				Toast.makeText(this, "报名成功", Toast.LENGTH_SHORT).show();
				btn_signup.setVisibility(View.GONE);
				txt_multi.setText("已报名");
				txt_multi.setVisibility(View.VISIBLE);
				int count = Integer.valueOf(status.getStatusSignUpCount()) + 1;
				String signupCount = String.valueOf(count);
				holder.txt_task_detail_signupcount.setText(signupCount);
				status.setStatusSignUpCount(signupCount);
				flag_message_or_signup = true;
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		if (flag_message_or_signup) {
			Intent intent = new Intent();
			setResult(IntentString.ResultCode.TASKDETAIL_TASKFRAGMENT, intent);
		}
		super.onBackPressed();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MainService.removeActivity(TaskDetailActivity.this);
	}
	
}
