package com.example.taupstairs.ui;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.bean.User;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.util.SharedPreferencesUtil;

public class HomePageActivity extends Activity implements ItaActivity {

	private User defaultUser;
	private RadioGroup radioGroup;
	private Button btn_top_right;
	private int flag_setting_write;
	private static final int SETTING = 0;
	private static final int WRITE = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepage);
		MainService.addActivity(HomePageActivity.this);
		init();
	}
	
	@Override
	public void init() {
		initUiTask();
		initCheckNetTask();
		initSetListener();
	}
	
	/*初始化任务界面的UI*/
	private void initUiTask() {
		btn_top_right = (Button)findViewById(R.id.btn_setting);
		btn_top_right.setBackgroundResource(R.drawable.hp_bg_btn_setting);
		flag_setting_write = SETTING;
		getFragmentManager().beginTransaction().replace(R.id.hp_fm_content, new TaskFragment()).commit();
	}
	
	/*进入软件时检测网络*/
	private void initCheckNetTask() {
		defaultUser = SharedPreferencesUtil.getDefaultUser(HomePageActivity.this);
		Map<String, Object> taskParams = new HashMap<String, Object>();
		taskParams.put(Task.TA_CHECKNET_TASKPARAMS, defaultUser);
		Task task = new Task(Task.TA_CHECKNET, taskParams);
		MainService.addTask(task);
	}
	
	/*初始化控件的监听器*/
	private void initSetListener() {
		radioGroup = (RadioGroup)findViewById(R.id.rg_homepage);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
				switch (checkedId) {
				case R.id.btn_task:
					TaskFragment taskFragment = new TaskFragment();
					fragmentTransaction.replace(R.id.hp_fm_content, taskFragment);
					btn_top_right.setBackgroundResource(R.drawable.hp_bg_btn_setting);
					flag_setting_write = SETTING;
					break;
					
				case R.id.btn_info:
					RankFragment rankFragment = new RankFragment();
					fragmentTransaction.replace(R.id.hp_fm_content, rankFragment);
					btn_top_right.setBackgroundResource(R.drawable.hp_bg_btn_write);
					flag_setting_write = WRITE;
					break;
				
				case R.id.btn_rank:
					InfoFragment infoFragment = new InfoFragment();
					fragmentTransaction.replace(R.id.hp_fm_content, infoFragment);
					btn_top_right.setBackgroundResource(R.drawable.hp_bg_btn_setting);
					flag_setting_write = SETTING;
					break;

				default:
					break;
				}
				fragmentTransaction.commit();
			}
		});
		btn_top_right.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = null;
				if (SETTING == flag_setting_write) {
					intent = new Intent(HomePageActivity.this, SettingActivity.class);
				} else if (WRITE == flag_setting_write){
					intent = new Intent(HomePageActivity.this, WriteActivity.class);
				}
				startActivity(intent);
			}
		});
	}
	
	/*提示连接网络*/
	private void noNet() {
		Toast.makeText(HomePageActivity.this, "未连接网络", Toast.LENGTH_LONG).show();
/*		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			checkNetTask();			//隔三秒钟再检测
		}	*/
	}

	@Override
	public void refresh(Object... params) {
		String result = ((String) params[0]).trim();
		if (result.equals(Task.TA_NO)) {
			noNet();
		}
	}	

}
