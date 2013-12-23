package com.example.taupstairs.ui;

import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.app.Fragment;
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
	private TaskFragment taskFragment;
	private InfoFragment infoFragment;
	private RankFragment rankFragment;
	private MeFragment meFragment;
	private Fragment currentFragment;
	private int flag_me_write;
	private static final int ME = 0;
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
		initData();
		initUiTask();
		initCheckNetTask();
		initSetListener();
	}
	
	/*初始化全局变量*/
	private void initData() {
		defaultUser = SharedPreferencesUtil.getDefaultUser(HomePageActivity.this);
		flag_me_write = ME;
	}
	
	/*初始化UI*/
	private void initUiTask() {
		btn_top_right = (Button)findViewById(R.id.btn_me_write);
		btn_top_right.setBackgroundResource(R.drawable.hp_bg_btn_me);
		taskFragment = new TaskFragment();
		infoFragment = new InfoFragment();
		rankFragment = new RankFragment();
		meFragment = new MeFragment(HomePageActivity.this);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.hp_fm_content, infoFragment);
		ft.add(R.id.hp_fm_content, taskFragment).hide(taskFragment);
		ft.add(R.id.hp_fm_content, rankFragment).hide(rankFragment);
		ft.add(R.id.hp_fm_content, meFragment).hide(meFragment);
		ft.commit();
		currentFragment = infoFragment;
	}
	
	/*进入软件时检测网络*/
	private void initCheckNetTask() {
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
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				switch (checkedId) {
				case R.id.btn_info:			//radiobutton不会有重复点击的问题，要check_change才会到这个监听器里面来
					ft.hide(currentFragment).show(infoFragment).commit();
					currentFragment = infoFragment;
					btn_top_right.setBackgroundResource(R.drawable.hp_bg_btn_me);
					flag_me_write = ME;
					break;
				case R.id.btn_task:
					ft.hide(currentFragment).show(taskFragment).commit();
					currentFragment = taskFragment;
					btn_top_right.setBackgroundResource(R.drawable.hp_bg_btn_write);
					flag_me_write = WRITE;
					break;
				case R.id.btn_rank:
					ft.hide(currentFragment).show(rankFragment).commit();
					currentFragment = rankFragment;
					btn_top_right.setBackgroundResource(R.drawable.hp_bg_btn_me);
					flag_me_write = ME;
					break;

				default:
					break;
				}
			}
		});
		/*初始化右上角按键的监听器*/
		btn_top_right.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (ME == flag_me_write) {
					if (currentFragment != meFragment) {	//避免重复点击时候一直运行下面的代码
						radioGroup.clearCheck();			//如果跳到MeFragment，则radiobutton都不要check
						getFragmentManager().beginTransaction()
						.hide(currentFragment).show(meFragment).commit();
						currentFragment = meFragment;
					}
				} else if (WRITE == flag_me_write) {
					Intent intent = new Intent(HomePageActivity.this, WriteActivity.class);
					startActivity(intent);
				}
			}
		});
	}

	@Override
	public void refresh(Object... params) {
		String result = ((String) params[0]).trim();
		if (result.equals(Task.TA_NO)) {
			noNet();
		}
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
	
	/*不重写这个方法，在退出的时候杀死进程的话，
	 * 会导致没有完全杀死程序的，会残留哪些我也不太清楚
	 * 使得手机在没有清空缓存的时候，再一次打开软件，
	 * 会出现后台的MainService调用UI线程中的refresh函数不能更新UI的情况*/
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		System.exit(0);
//		android.os.Process.killProcess(android.os.Process.myPid());
//		ActivityManager manager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE); 
//		manager.killBackgroundProcesses(getPackageName());
	}

}
