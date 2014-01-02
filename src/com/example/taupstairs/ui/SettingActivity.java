package com.example.taupstairs.ui;

import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.example.taupstairs.R;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.logic.MainService;

public class SettingActivity extends Activity implements ItaActivity {

	private Button btn_back, btn_change_user;
	private ListView list;
	private String[] setting = {"关于我们", "服务声名", "检查更新", "用户反馈"};
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		MainService.addActivity(SettingActivity.this);
		init();
	}

	@Override
	public void init() {
		btn_back = (Button)findViewById(R.id.btn_back_setting);
		list = (ListView)findViewById(R.id.list_setting);
		btn_change_user = (Button)findViewById(R.id.btn_change_user);
		
		btn_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(SettingActivity.this, 
				R.layout.common_txt_item, setting);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case 0:
					Intent intent1 = new Intent(SettingActivity.this, AboutUsActivity.class);
					startActivity(intent1);
					break;

				case 1:
					Intent intent2 = new Intent(SettingActivity.this, ServerDeclareActivity.class);
					startActivity(intent2);
					break;
					
				case 2:
					break;
					
				case 3:
					break;
					
				default:
					break;
				}
			}
		});
		btn_change_user.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				progressDialog = new ProgressDialog(SettingActivity.this);
				progressDialog.setCancelable(false);
				progressDialog.setMessage("    正在注销...");
				progressDialog.show();
				HashMap<String, Object> taskParams = new HashMap<String, Object>(1);
				taskParams.put(Task.TA_USEREXIT_TASKPARAMS, Task.TA_USEREXIT_ACTIVITY_SETTING);
				Task task = new Task(Task.TA_USEREXIT, taskParams);
				MainService.addTask(task);
			}
		});
	}

	@Override
	public void refresh(Object... params) {
		int taskId = (Integer) params[0];
		switch (taskId) {
		case Task.TA_USEREXIT:
			progressDialog.dismiss();
			String result = ((String) params[1]).trim();
			if (result.equals(Task.TA_USEREXIT_OK)) {
				MainService.emptyMainService();				//先清空MainService里面的链表
				Intent intent_receiver = new Intent();		//再发送主界面关闭的广播
				intent_receiver.setAction("com.example.taupstairs.CHANGE_USER");
				sendBroadcast(intent_receiver);
				Intent intent_login = new Intent(SettingActivity.this, LoginActivity.class);
				startActivity(intent_login);				//最后跳到登录界面
				finish();
			}
			break;

		default:
			break;
		}
	}
	
}
