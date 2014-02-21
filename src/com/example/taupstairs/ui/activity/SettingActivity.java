package com.example.taupstairs.ui.activity;

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
import android.widget.Toast;
import com.example.taupstairs.R;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.services.RankService;
import com.example.taupstairs.util.FileUtil;
import com.example.taupstairs.util.SharedPreferencesUtil;

public class SettingActivity extends Activity implements ItaActivity {

	private Button btn_back, btn_change_user;
	private ListView list_user, list_soft;
	private String[] setting_user = {"联系资料", "清除缓存", };
	private String[] setting_soft = {"关于我们", "服务声名", "版本特性", "用户反馈"};
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
		list_user = (ListView)findViewById(R.id.list_setting_user);
		list_soft = (ListView)findViewById(R.id.list_setting_soft);
		btn_change_user = (Button)findViewById(R.id.btn_change_user);
		progressDialog = new ProgressDialog(SettingActivity.this);
		
		btn_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		ArrayAdapter<String> adapter_user = new ArrayAdapter<String>(SettingActivity.this, 
				R.layout.common_txt_item, setting_user);
		list_user.setAdapter(adapter_user);
		list_user.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {			
				case 0:
					Intent intent = new Intent(SettingActivity.this, UpdataUserdataOptionalActivity.class);
					startActivity(intent);
					break;

				case 1:
					emptyCacheMemory();
					break;
					
				default:
					break;
				}
			}
		});
		
		ArrayAdapter<String> adapter_soft = new ArrayAdapter<String>(SettingActivity.this, 
				R.layout.common_txt_item, setting_soft);
		list_soft.setAdapter(adapter_soft);
		list_soft.setOnItemClickListener(new OnItemClickListener() {
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
				showProgressDialog();
				HashMap<String, Object> taskParams = new HashMap<String, Object>(1);
				taskParams.put(Task.TA_USEREXIT_TASKPARAMS, Task.TA_USEREXIT_ACTIVITY_SETTING);
				Task task = new Task(Task.TA_USEREXIT, taskParams);
				MainService.addTask(task);
			}
		});
	}
	
	private void showProgressDialog() {
		progressDialog.setCancelable(false);
		progressDialog.setMessage("    正在注销...");
		progressDialog.show();
	}
	
	private void dismissProgressDialog() {
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
	
	private void emptyCacheMemory() {
		progressDialog.setCancelable(false);
		progressDialog.setMessage("    正在清除...");
		progressDialog.show();
		SharedPreferencesUtil.savaLastestId(SettingActivity.this, SharedPreferencesUtil.LASTEST_STATUSID, null);
		FileUtil.deletePhoto(getFilesDir().getAbsolutePath());
//		PersonService personService = new PersonService(SettingActivity.this);
//		personService.emptyPersonDB();
//		personService.closeDBHelper();
		RankService rankService = new RankService(SettingActivity.this);
		rankService.emptyRankDb();
		rankService.closeDBHelper();
		progressDialog.dismiss();
		Toast.makeText(SettingActivity.this, "清除完成", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void refresh(Object... params) {
		dismissProgressDialog();
		int taskId = (Integer) params[0];
		switch (taskId) {
		case Task.TA_USEREXIT:
			MainService.emptyMainService();				//先清空MainService里面的链表
			Intent intent_receiver = new Intent();		//再发送主界面关闭的广播
			intent_receiver.setAction("com.example.taupstairs.CHANGE_USER");
			sendBroadcast(intent_receiver);
			Intent intent_login = new Intent(SettingActivity.this, LoginActivity.class);
			startActivity(intent_login);				//最后跳到登录界面
			finish();
			break;

		default:
			break;
		}
	}
	
}
