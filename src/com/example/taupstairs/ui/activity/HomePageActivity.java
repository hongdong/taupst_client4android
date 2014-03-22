package com.example.taupstairs.ui.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import android.app.ActivityGroup;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.example.taupstairs.R;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.bean.User;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.manager.UpdataManager;
import com.example.taupstairs.string.NormalString;
import com.example.taupstairs.util.SharedPreferencesUtil;
import com.example.taupstairs.util.Utils;

@SuppressWarnings("deprecation")
public class HomePageActivity extends ActivityGroup implements ItaActivity {
	
	private User defaultUser;
	private boolean first_time_login = true;
	private boolean isExit = false;
	private static final String TAB_INFO = "info";
	private static final String TAB_TASK = "task";
	private static final String TAB_RANK = "rank";
	private static final String TAB_ME = "me";
	private String[] tabs = {TAB_INFO, TAB_TASK, TAB_RANK, TAB_ME};
	private int[] ids = {R.id.btn_info, R.id.btn_task, R.id.btn_rank, R.id.btn_me, };
	private TabHost tabHost;
	private RadioGroup rg;
	private HomePageReceiver receiver;
	@SuppressWarnings("rawtypes")
	private Class[] hpClasses = {InfoActivity.class, TaskActivity.class, 
			RankActivity.class, MeActivity.class};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepage);
		MainService.addActivity(this);
		init();
	}
	
	@Override
	public void init() {
		initData();
		initNet();
		initView();
		initReceiver();
	}
	
	private void initData() {
		defaultUser = SharedPreferencesUtil.getDefaultUser(HomePageActivity.this);
	}
	
	/*进入软件时检测网络*/
	private void initNet() {
		new Thread() {
			public void run() {
				while (true) {
					doCheckNetTask();
					try {
						sleep(600000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}
	
	private void initView() {
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup(getLocalActivityManager());
		for (int i = 0; i < hpClasses.length; i++) {
			TabSpec spec = tabHost.newTabSpec(tabs[i]).setIndicator(tabs[i])
					.setContent(new Intent(this, hpClasses[i]));
			tabHost.addTab(spec);
		}
		
		rg = (RadioGroup)findViewById(R.id.rg_homepage);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				for (int i = 0; i < ids.length; i++) {
					if (ids[i] == checkedId) {
						tabHost.setCurrentTabByTag(tabs[i]);
					}
				}
			}
		});
		rg.check(R.id.btn_task);
	}
	
	private void initReceiver() {
		receiver = new HomePageReceiver();
		IntentFilter filter = new IntentFilter();  
        filter.addAction(NormalString.Receiver.CHANGE_USER);
        registerReceiver(receiver, filter);  
	}
	
	private void doCheckNetTask() {
		Map<String, Object> taskParams = new HashMap<String, Object>();
		taskParams.put(User.USER_COLLEGEID, defaultUser.getUserCollegeId());
		taskParams.put(User.USER_STUDENTID, defaultUser.getUserStudentId());
		taskParams.put(User.USER_PASSWORD, defaultUser.getUserPassword());
		taskParams.put(Task.TA_LOGIN_ISEXIST, "3");		//3表示服务器数据库中已经存在
		Task task = new Task(Task.TA_CHECKNET, taskParams);
		MainService.addTask(task);
	}
	
	private void doCheckUpdataTask() {
		Task task = new Task(Task.TA_CHECKUPDATA, null);
		MainService.addTask(task);
	}
	
	private void doUsetExitTask() {
		finish();
		PushManager.stopWork(getApplicationContext());
		Map<String, Object> taskParams = new HashMap<String, Object>();
		taskParams.put(Task.TA_USEREXIT_TYPE, Task.TA_USEREXIT_TYPE_NORMAL);
		taskParams.put(Task.TA_USEREXIT_TASKPARAMS, Task.TA_USEREXIT_ACTIVITY_HOMEPAGE);
		Task task = new Task(Task.TA_USEREXIT, taskParams);
		MainService.addTask(task);
	}

	@Override
	public void refresh(Object... params) {
		int taskId = (Integer) params[0];
		switch (taskId) {
		case Task.TA_CHECKNET:
			String result = (String) params[1];
			if (null == result) {
				Toast.makeText(this, "没网络啊！！！亲", Toast.LENGTH_LONG).show();
			} else {
				if (first_time_login) {
					first_time_login = false;
					/*登录之后才能发id*/
					PushManager.startWork(getApplicationContext(),
							PushConstants.LOGIN_TYPE_API_KEY, 
							Utils.getMetaValue(this, "api_key"));
					doCheckUpdataTask();
				}
			}
			break;
			
		case Task.TA_CHECKUPDATA:
			String jsonString = (String) params[1];
			UpdataManager updataManager = new UpdataManager(this, jsonString);
			updataManager.checkUpdate();
			break;
			
		case Task.TA_USEREXIT:
			Intent intent = new Intent(HomePageActivity.this, MainService.class);
			stopService(intent);
			android.os.Process.killProcess(android.os.Process.myPid());
			break;

		default:
			break;
		}
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home_page, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.exit:
			doUsetExitTask();
			break;

		default:
			break;
		}
		return true;
	}
	
	@Override
	public void onBackPressed() {
		exitByTwoClick();
	}
	
	private void exitByTwoClick() {  
	    if (isExit == false) {  
	        isExit = true; // 准备退出  
	        Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();  
	        Timer tExit = new Timer();  
	        tExit.schedule(new TimerTask() {  
				public void run() {  
	                isExit = false; // 取消退出  
	            }  
	        }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务    
	    } else {  
	        doUsetExitTask();
	    }  
	}  
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		String action = intent.getAction();
		if (PushConstants.ACTION_RECEIVER_NOTIFICATION_CLICK.equals(action)) {
			rg.check(R.id.btn_info);
		}
	}
	
	public class HomePageReceiver extends BroadcastReceiver {
		public void onReceive(final Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(NormalString.Receiver.CHANGE_USER)) {
				finish();
				PushManager.stopWork(getApplicationContext());	
			}
		}
	}

}
