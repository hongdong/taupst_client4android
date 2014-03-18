package com.example.taupstairs.ui.activity;

import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.taupstairs.R;
import com.example.taupstairs.bean.Info;
import com.example.taupstairs.bean.InfoSignUp;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.SignUp;
import com.example.taupstairs.bean.SignUpListTaskDetail;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.bean.Time;
import com.example.taupstairs.imageCache.SimpleImageLoader;
import com.example.taupstairs.listener.PersonDataListener;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.logic.TaUpstairsApplication;
import com.example.taupstairs.string.IntentString;
import com.example.taupstairs.util.HttpClientUtil;
import com.example.taupstairs.util.TimeUtil;

public class SignUpListInfoDetailActivity extends Activity implements ItaActivity {

	private Button btn_back;
	private Holder holder;
	private int clickPosition;
	private SignUp signUp;
	private String signUpId;
	private SignUpListTaskDetail st;
	private boolean to_select;		//是否可以选Ta执行（就是那个按钮是否显示了，还是显示“已选Ta执行”）
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_signup);
		MainService.addActivity(this);
		init();
	}
	
	@Override
	public void init() {
		initHolder();
		initData();
		initView();
	}
	
	/*头像，昵称，性别，发布时间，来自哪个院系、年级，
	 * 留言内容，任务发布人，任务标题*/
	private class Holder {
		public LinearLayout layout_loading;
		public ImageView img_photo;
		public TextView txt_nickname;
		public ImageView img_sex;
		public TextView txt_releasetime;
		public TextView txt_grade;
		public TextView txt_department;
		
		public TextView txt_signup_string;
		public View view;
		public TextView txt_status_nickname;
		public TextView txt_status_title;
		public Button btn_exec;
		public TextView txt_multi;
		public TextView txt_expired;
		public TextView txt_end;
		public TextView txt_person_phone;
		public TextView txt_person_qq;
		public TextView txt_person_email;
	}
	
	private void initHolder() {
		holder = new Holder();
		holder.layout_loading = (LinearLayout)findViewById(R.id.layout_loading);
		
		holder.img_photo = (ImageView)findViewById(R.id.img_photo);
		holder.txt_nickname = (TextView)findViewById(R.id.txt_nickname);
		holder.img_sex = (ImageView)findViewById(R.id.img_sex);
		holder.txt_releasetime = (TextView)findViewById(R.id.txt_releasetime);
		holder.txt_grade = (TextView)findViewById(R.id.txt_grade);
		holder.txt_department = (TextView)findViewById(R.id.txt_department);	
		
		holder.txt_signup_string = (TextView)findViewById(R.id.txt_info_signup_string);
		holder.view = findViewById(R.id.layout_info_signup_task);
		holder.txt_status_nickname = (TextView)findViewById(R.id.txt_info_signup_nickname);
		holder.txt_status_title = (TextView)findViewById(R.id.txt_info_signup_title);
		holder.btn_exec = (Button)findViewById(R.id.btn_info_signup_exec);
		holder.txt_multi = (TextView)findViewById(R.id.txt_info_signup_multi);
		holder.txt_expired = (TextView)findViewById(R.id.txt_info_signup_expired);
		holder.txt_end = (TextView)findViewById(R.id.txt_info_signup_end);
		holder.txt_person_phone = (TextView)findViewById(R.id.txt_info_signup_phone);
		holder.txt_person_qq = (TextView)findViewById(R.id.txt_info_signup_qq);
		holder.txt_person_email = (TextView)findViewById(R.id.txt_info_signup_email);
	}
	
	private void initData() {
		showProgressBar();
		clickPosition = getIntent().getIntExtra(SignUp.CLICK_POSITION, 0);
		TaUpstairsApplication app = (TaUpstairsApplication) getApplication();
		signUp = app.getSignUp();
		signUpId = signUp.getSignUpId();
		to_select = true;
		doGetInfoSignUpTask();
	}
	
	private void initView() {
		btn_back = (Button)findViewById(R.id.btn_back_info_singup_detail);	
		btn_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (signUp.getIsExe().equals("0")) {
					Intent intent = new Intent();
					intent.putExtra(SignUp.CLICK_POSITION, clickPosition);
					setResult(IntentString.ResultCode.INFODETAIL_SIGNUPLIST, intent);
				}
				finish();
			}
		});
	}
	
	private void showProgressBar() {
		holder.layout_loading.setVisibility(View.VISIBLE);
	}
	
	private void hideProgressBar() {
		holder.layout_loading.setVisibility(View.GONE);
	}
	
	private void doGetInfoSignUpTask() {
		Map<String, Object> taskParams = new HashMap<String, Object>();
		taskParams.put(SignUp.SIGNUP_ID, signUpId);
		Task task = new Task(Task.TA_SIGNUP_INFODETAIL, taskParams);
		MainService.addTask(task);
	}

	@Override
	public void refresh(Object... params) {
		if (params[1] != null) {
			int taskId = (Integer) params[0];
			switch (taskId) {
			case Task.TA_SIGNUP_INFODETAIL:
				hideProgressBar();
				st = (SignUpListTaskDetail) params[1];
				displaySignUp();
				break;

			default:
				break;
			}
		}
	}
	
	/**
	 * 显示被报名消息详情
	 */
	private void displaySignUp() {
		SimpleImageLoader.showImage(holder.img_photo, 
				HttpClientUtil.PHOTO_BASE_URL + st.getPersonPhotoUrl());
		PersonDataListener personDataListener = 
				new PersonDataListener(this, st.getPersonId(), Person.PERMISSION_PUBLIC);
		holder.img_photo.setOnClickListener(personDataListener);
		holder.txt_nickname.setText(st.getPersonNickname());
		String personSex = st.getPersonSex().trim();
		if (personSex.equals(Person.MALE)) {
			holder.img_sex.setImageResource(R.drawable.icon_male);
		} else if (personSex.equals(Person.FEMALE)) {
			holder.img_sex.setImageResource(R.drawable.icon_female);
		}
		String displayTime = TimeUtil.getDisplayTime(TimeUtil.getNow(), st.getInfoReleaseTime());
		holder.txt_releasetime.setText(displayTime);
		holder.txt_grade.setText(st.getPersonGrade());
		holder.txt_department.setText(st.getPersonDepartment());
		
		holder.txt_signup_string.setText(st.getSignUpString());
		holder.view.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				//这里不要跳转了，不然一直嵌套
			}
		});
		holder.txt_status_nickname.setText(st.getStatusPersonNickname());
		holder.txt_status_title.setText("  :  " + st.getStatusTitle());
		String contact = st.getPersonContact();
		/*以下为联系方式，可能对方没有全部提供*/
		char[] optional = contact.toCharArray();
		if (optional[0] != '0') {
			holder.txt_person_phone.setText(st.getPersonPhone());
		} else {
			holder.txt_person_phone.setText("Ta没有向您提供手机号");
		}
		if (optional[1] != '0') {
			holder.txt_person_qq.setText(st.getPersonQq());
		} else {
			holder.txt_person_qq.setText("Ta没有向您提供qq号");
		}
		if (optional[2] != '0') {
			holder.txt_person_email.setText(st.getPersonEmail());
		} else {
			holder.txt_person_email.setText("Ta没有向您提供email");
		}
		
		Time now = TimeUtil.getNow();
		Time end = TimeUtil.originalToTime(st.getStatusEndTime());
		if (TimeUtil.LARGE == TimeUtil.compare(now, end)) {
			to_select = false;
			holder.txt_expired.setVisibility(View.VISIBLE);
		}
	
		if (st.getStatusState().trim().equals("3")) {
			to_select = false;
			holder.txt_end.setVisibility(View.VISIBLE);
		}
		
		String hasExec = st.getHasExec();
		if (hasExec.equals("1")) {
			if (to_select) {
				holder.btn_exec.setVisibility(View.VISIBLE);
				holder.btn_exec.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(SignUpListInfoDetailActivity.this, InfoSignUpExecActivity.class);
						//这个没传过去，选择执行页面获取不到报名id，就会异常
						TaUpstairsApplication app = (TaUpstairsApplication) getApplication();
						InfoSignUp infoSignUp = new InfoSignUp();
						infoSignUp.setSignUpId(signUpId);
						Info info = new Info();
						info.setInfoSignUp(infoSignUp);
						app.setInfo(info);
						
						startActivityForResult(intent, IntentString.RequestCode.INFOSIGNUP_INFOSIGNUPEXEC);
					}
				});
			}
		} else if (hasExec.equals("0")) {
			holder.txt_multi.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case IntentString.RequestCode.INFOSIGNUP_INFOSIGNUPEXEC:
			if (IntentString.ResultCode.INFOSIGNUPEXEC_INFOSIGNUP == resultCode) {
				Toast.makeText(this, "选择成功", Toast.LENGTH_SHORT).show();
				holder.btn_exec.setVisibility(View.GONE);
				holder.txt_multi.setVisibility(View.VISIBLE);
				signUp.setIsExe("0");
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		if (signUp.getIsExe().equals("0")) {
			Intent intent = new Intent();
			intent.putExtra(SignUp.CLICK_POSITION, clickPosition);
			setResult(IntentString.ResultCode.INFODETAIL_SIGNUPLIST, intent);
		}
		super.onBackPressed();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MainService.removeActivity(this);
	}

}
