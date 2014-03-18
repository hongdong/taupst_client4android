package com.example.taupstairs.ui.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.example.taupstairs.R;
import com.example.taupstairs.adapter.ByIdMessageAdapter;
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

public class TaskByIdActivity extends Activity implements ItaActivity {

	private Button btn_back, btn_multi, btn_message;
	private ImageView img_expression, img_expression_delete;
	private GridView grid_expression;
	private LinearLayout layout_loading, expLayout;
	private TextView txt_expired, txt_end, txt_multi;
	private Holder holder;
	private Time now;
	private String statusId;
	private Status status;
	private List<Message> messages;
	private ByIdMessageAdapter adapter;
	private EditText edit_message;
	private String edit_text;
	private String messageId, replyId, replyNickname;
	private ProgressDialog progressDialog;
	private boolean flag_my_task, flag_end, flag_expired;
	private boolean flag_expression;
	private int[] imageIds = new int[105];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_detail);
		MainService.addActivity(this);
		init();
	}
	
	@Override
	public void init() {
		initProgressBar();
		initHolder();
		initData();
		initView();
	}
	
	private void initProgressBar() {
		layout_loading = (LinearLayout)findViewById(R.id.layout_loading);
		layout_loading.setVisibility(View.VISIBLE);
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
		public LinearLayout layout_loading_message;
	}
	
	private void initHolder() {
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
		holder.layout_loading_message = (LinearLayout)findViewById(R.id.layout_loading_message);
	}
	
	private void initData() {
		now = TimeUtil.getNow();
		statusId = getIntent().getStringExtra(Status.STATUS_ID);
		doGetTaskDetailTask();
	}
	
	private void initView() {
		btn_back = (Button)findViewById(R.id.btn_back_task_detail);
		btn_multi = (Button)findViewById(R.id.btn_task_detail_signup);
		txt_expired = (TextView)findViewById(R.id.txt_task_detail_expired);
		txt_end = (TextView)findViewById(R.id.txt_task_detail_end);
		txt_multi = (TextView)findViewById(R.id.txt_task_detail_multi);
		btn_message = (Button)findViewById(R.id.btn_task_detail_message);
		edit_message = (EditText)findViewById(R.id.edit_task_detail_message);
		img_expression = (ImageView)findViewById(R.id.img_task_detail_expression);
		img_expression_delete = (ImageView)findViewById(R.id.img_expression_delete);
		expLayout = (LinearLayout)findViewById(R.id.layout_expression);
		grid_expression = (GridView)findViewById(R.id.grid_expression);
		progressDialog = new ProgressDialog(this);
		
		createExpressionGridView();
		MyOnClickListener listener = new MyOnClickListener();
		btn_back.setOnClickListener(listener);
		btn_multi.setOnClickListener(listener);
		img_expression.setOnClickListener(listener);
		img_expression_delete.setOnClickListener(listener);
		edit_message.setOnClickListener(listener);
		btn_message.setOnClickListener(listener);	
		grid_expression.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageIds[arg2]);
				Bitmap smallBitmap = Bitmap.createScaledBitmap(bitmap, 40, 40, true);
				ImageSpan imageSpan = new ImageSpan(TaskByIdActivity.this, smallBitmap);
				String str;
				if (arg2 < 10){
					str = "[fac00" + arg2;
				} else if (arg2 < 100){
					str = "[fac0" + arg2;
				} else {
					str = "[fac" + arg2;
				}
				SpannableString spannableString = new SpannableString(str);
				spannableString.setSpan(imageSpan, 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				int selectionStart = edit_message.getSelectionStart();
				edit_message.getText().insert(selectionStart, spannableString);
			}
		});
	}
	
	/**
	 * 初始化表情
	 */
	private void createExpressionGridView() {
		List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
		for(int i = 0; i < 105; i++){
			try {
				Field field = R.drawable.class.getDeclaredField("smiley_" + i);
				int resourceId = Integer.parseInt(field.get(null).toString());
				imageIds[i] = resourceId;
			} catch (Exception e) {
				e.printStackTrace();
			}
	        Map<String,Object> listItem = new HashMap<String,Object>();
			listItem.put("image", imageIds[i]);
			listItems.add(listItem);
		}
		
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.expression_cell, 
				new String[]{"image"}, new int[] {R.id.img_expression_cell});
		grid_expression.setAdapter(simpleAdapter);
	}
	
	private void showProgressBar() {
		holder.layout_loading_message.setVisibility(View.VISIBLE);
	}
	
	private void hideProgressBar() {
		holder.layout_loading_message.setVisibility(View.GONE);
	}
	
	private void showProgressDialog() {
		progressDialog.setCancelable(false);
		progressDialog.setMessage("    稍等片刻...");
		progressDialog.show();
	}
	
	private void dismissProgressDialog() {
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
	
	public void changeEditHint(String messageId, String replyId, String replyNickname) {
		this.messageId = messageId;
		this.replyId = replyId;
		this.replyNickname = replyNickname;
		edit_message.setHint("回复  " + replyNickname);
		edit_message.requestFocus();
		KeyBoardUtil.show(this, edit_message); 
	}
	
	private void jumpToSignupList() {
		Intent intent = new Intent(TaskByIdActivity.this, SignUpListActivity.class);
		intent.putExtra(Status.STATUS_ID, status.getStatusId());
		intent.putExtra(Status.STATUS_STATE, flag_end);
		startActivityForResult(intent, IntentString.RequestCode.END_SIGNUPLIST);
	}
	
	private void jumpToSignup() {
		Intent intent = new Intent(TaskByIdActivity.this, SignupActivity.class);
		intent.putExtra(Task.TA_ACTIVITY, Task.TA_GETMESSAGE_ACTIVITY_BYID);
		intent.putExtra(Status.STATUS_ID, status.getStatusId());
		intent.putExtra(Status.PERSON_ID, status.getPersonId());
		startActivityForResult(intent, IntentString.RequestCode.TASKBYID_SIGNUP);
	}
	
	/**
	 * 刚进来要根据id获取任务详情
	 */
	private void doGetTaskDetailTask() {
		HashMap<String, Object> taskParams = new HashMap<String, Object>(2);
		taskParams.put(Task.TA_GET_TASK_DETAIL_ACTIVITY, Task.TA_GET_TASK_DETAIL_ACTIVITY);
		taskParams.put(Status.STATUS_ID, statusId);
		Task task = new Task(Task.TA_GET_TASK_DETAIL, taskParams);
		MainService.addTask(task);
	}
	
	/**
	 * 获取留言
	 */
	private void doGetMessageTask() {
		HashMap<String, Object> taskParams = new HashMap<String, Object>(1);
		taskParams.put(Status.STATUS_ID, status.getStatusId());
		taskParams.put(Task.TA_GETMESSAGE_ACTIVITY, Task.TA_GETMESSAGE_ACTIVITY_BYID);
		Task task = new Task(Task.TA_GETMESSAGE, taskParams);
		MainService.addTask(task);
	}
	
	/**
	 * 进行留言
	 */
	private void doMessageTask() {
		Map<String, Object> taskParams = new HashMap<String, Object>();
		taskParams.put(Task.TA_MESSAGE_ACTIVITY, Task.TA_MESSAGE_ACTIVITY_BYID);
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
		dismissProgressDialog();
		if (params[1] != null) {
			int taskId = (Integer) params[0];
			switch (taskId) {
			case Task.TA_GET_TASK_DETAIL:
				layout_loading.setVisibility(View.GONE);
				status = (Status) params[1];
				displayStatus();
				break;
				
			case Task.TA_GETMESSAGE:
				hideProgressBar();
				messages = (List<Message>) params[1];
				ListView listView = (ListView) findViewById(R.id.list_task_detail_message);
				listView.setVisibility(View.VISIBLE);
				adapter = new ByIdMessageAdapter(this, messages);
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
						expLayout.setVisibility(View.GONE);
					} else {		
						Toast.makeText(this, "网络竟然出错了", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			default:
				break;
			}
		}
	}
	
	/**
	 * 显示任务详情
	 */
	private void displayStatus() {
		setFlag();		//显示之前先设置标志
		displayStatusPerson();
		displayStatusContent();
	}
	
	/**
	 * 设置必要的标志（是否是我发布的，是否过期）
	 */
	private void setFlag() {
		String personId = SharedPreferencesUtil.getDefaultUser(this).getUserId();
		if (status.getPersonId().equals(personId)) {
			flag_my_task = true;
		} else {
			flag_my_task = false;
		}
		
		Time end = TimeUtil.originalToTime(status.getStatusEndTime());
		if (TimeUtil.LARGE == TimeUtil.compare(now, end)) {
			flag_expired = true;
			txt_expired.setText("已过期");
			txt_expired.setVisibility(View.VISIBLE);
		} else {
			flag_expired = false;
		}
	}
	
	private void displayStatusPerson() {
		SimpleImageLoader.showImage(holder.img_task_detail_photo, 
				HttpClientUtil.PHOTO_BASE_URL + status.getPersonPhotoUrl());
		PersonDataListener personDataListener = 
				new PersonDataListener(this, status.getPersonId(), Person.PERMISSION_HIDE);
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
	}
	
	private void displayStatusContent() {
		holder.txt_task_detail_title.setText(status.getStatusTitle());
		holder.txt_task_detail_content.setText(status.getStatusContent());
		holder.txt_task_detail_rewards.setText(status.getStatusRewards());	
		String endTime = TimeUtil.getDisplayTime(now, status.getStatusEndTime());
		holder.txt_task_detail_endtime.setText(endTime);	
		holder.txt_task_detail_signupcount.setText(status.getStatusSignUpCount());
		testState(Integer.parseInt(status.getStatusState()), Integer.parseInt(status.getIsSign()));
		String messageCount = status.getStatusMessageCount();
		holder.txt_task_detail_messagecount.setText(messageCount);
		if (messageCount.equals("0")) {
			holder.txt_task_detail_no_message.setVisibility(View.VISIBLE);
		} else {
			showProgressBar();	//显示加载留言的进度条
			doGetMessageTask();
		}
	}
	
	/**
	 * 检测任务后测试状态。相当麻烦的事情
	 * @param state	
	 */
	private void testState(int statusState, int isSign) {
		switch (statusState) {	//过期上面已经显示了，先判断是否已经完结
		case 1:	
			flag_end = false;
			txt_end.setText("未完结");
			break;
			
		case 3:
			flag_end = true;
			txt_end.setText("已完结");
			break;
			
		default:
			break;
		}
		if (flag_my_task) {		//是我发布的。这个时候按钮都可用
			btn_multi.setText("报名列表");
			btn_multi.setVisibility(View.VISIBLE);
		} else {	
			if (0 == isSign) {	//未报名，且未过期未完结的时候，才能猛戳报名。不然就是未报名
				if (!flag_expired && !flag_end) {
					btn_multi.setText("抢报酬");
					btn_multi.setVisibility(View.VISIBLE);
				} else {
					txt_multi.setText("未报名");
					txt_multi.setVisibility(View.VISIBLE);
				}
			} else {	//已经报名了
				txt_multi.setText("已报名");
				txt_multi.setVisibility(View.VISIBLE);
			}
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
			Time now = TimeUtil.getNow();
			message.setMessageTime(TimeUtil.timeToOriginal(now));		
			List<MessageContent> contents = new ArrayList<MessageContent>();
			MessageContent content = new MessageContent();	
			content.setContent(edit_text);
			contents.add(0, content);	
			message.setMessageContents(contents);
			if (messages != null) {
				messages.add(message);
				adapter.notifyDataSetChanged();
			} else {
				holder.txt_task_detail_no_message.setVisibility(View.GONE);
				ListView listView = (ListView) findViewById(R.id.list_task_detail_message);	
				messages = new ArrayList<Message>();
				messages.add(message);
				adapter = new ByIdMessageAdapter(this, messages);
				listView.setAdapter(adapter);
				listView.setVisibility(View.VISIBLE);
			}
		}
		/*改变任务留言数*/
		int count = Integer.valueOf(status.getStatusMessageCount()) + 1;
		String messageCount = String.valueOf(count);
		holder.txt_task_detail_messagecount.setText(messageCount);
		status.setStatusMessageCount(messageCount);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case IntentString.RequestCode.TASKBYID_SIGNUP:
			if (IntentString.ResultCode.SIGNUP_TASKBYID == resultCode) {
				Toast.makeText(this, "报名成功", Toast.LENGTH_SHORT).show();
				btn_multi.setVisibility(View.GONE);
				txt_multi.setText("已报名");
				txt_multi.setVisibility(View.VISIBLE);
				int count = Integer.valueOf(status.getStatusSignUpCount()) + 1;
				String signupCount = String.valueOf(count);
				holder.txt_task_detail_signupcount.setText(signupCount);
			}
			break;
			
		case IntentString.RequestCode.END_SIGNUPLIST:
			if (IntentString.ResultCode.SIGNUPLIST_END == resultCode) {
				flag_end = true;
				txt_end.setText("已完结");
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		if (expLayout.isShown()) {	//如果显示着表情的时候，先把表情布局取消掉
			expLayout.setVisibility(View.GONE);
		} else {
			if (flag_end) {
				Intent intent = new Intent();
				setResult(IntentString.ResultCode.TASKBYID_INFOSIGNUP, intent);
			}
			super.onBackPressed();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MainService.removeActivity(this);
	}
	
	class MyOnClickListener implements OnClickListener {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_back_task_detail:
				if (flag_end) {
					Intent intent = new Intent();
					setResult(IntentString.ResultCode.TASKBYID_INFOSIGNUP, intent);
				}
				finish();
				break;
				
			case R.id.btn_task_detail_signup:
				if (flag_my_task) {
					jumpToSignupList();
				} else {
					jumpToSignup();
				}
				break;
				
			case R.id.img_task_detail_expression:
				if (flag_expression) {
					img_expression.setImageResource(R.drawable.expression_p);
					KeyBoardUtil.dismiss(TaskByIdActivity.this, edit_message);
					expLayout.setVisibility(View.VISIBLE);
				} else {
					img_expression.setImageResource(R.drawable.expression_n);
					KeyBoardUtil.show(TaskByIdActivity.this, edit_message);
					expLayout.setVisibility(View.GONE);
				}
				flag_expression = !flag_expression;
				break;
				
			case R.id.edit_task_detail_message:
				img_expression.setImageResource(R.drawable.expression_n);
				expLayout.setVisibility(View.GONE);
				break;
				
			case R.id.btn_task_detail_message:
				if (!edit_message.getText().toString().trim().equals("")) {
					showProgressDialog();
					doMessageTask();
				}
				break;
				
			case R.id.img_expression_delete:
				int selectionStart = edit_message.getSelectionStart();// 获取光标的位置
				if (selectionStart > 0) {
				    String body = edit_message.getText().toString();
				    if (!TextUtils.isEmpty(body)) {
				    	String tempStr = body.substring(0, selectionStart);
				    	int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
				    	if (i != -1) {
				    		CharSequence cs = tempStr.subSequence(i, selectionStart - 3);
				    		if (cs.equals("[fac")) {// 判断是否是一个表情
				    			edit_message.getEditableText().delete(i, selectionStart);
				    			return;
				    		}
				    	}
				    	edit_message.getEditableText().delete(selectionStart - 1, selectionStart);
				    }
				}
				break;

			default:
				break;
			}
		}
	}
	
}
