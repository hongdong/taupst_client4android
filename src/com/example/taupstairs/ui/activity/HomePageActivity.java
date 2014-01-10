package com.example.taupstairs.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.bean.User;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.ItaFragment;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.string.HomePageString;
import com.example.taupstairs.string.IntentString;
import com.example.taupstairs.ui.fragment.InfoFragment;
import com.example.taupstairs.ui.fragment.MeFragment;
import com.example.taupstairs.ui.fragment.RankFragment;
import com.example.taupstairs.ui.fragment.TaskFragment;
import com.example.taupstairs.util.SharedPreferencesUtil;

public class HomePageActivity extends Activity implements ItaActivity {

	private User defaultUser;
	private RadioGroup radioGroup;
	private Button btn_top_right;
	private int[] buttonIds = {R.id.btn_info, R.id.btn_task, R.id.btn_rank};
	private RadioButton currentButton;
	
	private boolean noNet = true;
	private boolean goCheck = false;
	private boolean displayNoNet = false;
	
	private InfoFragment infoFragment;
	private TaskFragment taskFragment;
	private RankFragment rankFragment;
	private MeFragment meFragment;
	private Fragment currentFragment;
	private int flag_me_write;
	private static final int ME = 0;
	private static final int WRITE = 1;
	
	private GestureDetector detector;
	private static final int GESTURE_DISTANCE = 80;
	private List<Fragment> listFragments;
	private int currentIndex;
	
	private ChangeUserReceiver receiver;
	
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
		initView();
		initGesture();
		initCheckNetTask();
		initReceiver();
		initSetListener();
	}
	
	/*初始化全局变量*/
	private void initData() {
		defaultUser = SharedPreferencesUtil.getDefaultUser(HomePageActivity.this);
		flag_me_write = ME;
		currentIndex = 0;
	}
	
	/*初始化UI*/
	private void initView() {
		radioGroup = (RadioGroup)findViewById(R.id.rg_homepage);
		btn_top_right = (Button)findViewById(R.id.btn_me_write);
		btn_top_right.setBackgroundResource(R.drawable.hp_bg_btn_me);
		infoFragment = new InfoFragment();
		taskFragment = new TaskFragment(HomePageActivity.this);
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
	
	/*手势*/
	private void initGesture() {
		listFragments = new ArrayList<Fragment>();
		listFragments.add(infoFragment);
		listFragments.add(taskFragment);
		listFragments.add(rankFragment);
		listFragments.add(meFragment);
		detector = new GestureDetector(HomePageActivity.this, new OnGestureListener() {
			public boolean onSingleTapUp(MotionEvent e) {
				return false;
			}
			
			public void onShowPress(MotionEvent e) {
				
			}
			
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
					float distanceY) {
				return false;
			}
			
			public void onLongPress(MotionEvent e) {
				
			}
			
			/*处理一下将要选中的页面，丢给radioGroup和btn_top_right去处理*/
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				if (Math.abs(e1.getY() - e2.getY()) > GESTURE_DISTANCE) {
					return false;
				}
				if (e1.getX() - e2.getX() > GESTURE_DISTANCE) {			//从右向左滑，向右翻页
					if (currentIndex < 2) {								//只有前三页，才会翻，最后一页不能右翻
//						radioGroup.check(buttonIds[currentIndex + 1]);
						//这种方法不能用，会莫名其妙的让onCheckedChanged调用两三次
						//http://blog.csdn.net/piglovesula/article/details/9820521，这上面看的
						currentButton = (RadioButton)HomePageActivity.this.findViewById(buttonIds[currentIndex + 1]);
						currentButton.setChecked(true);
					} else if (2 == currentIndex) {
						jumpToMeFragment();
					}
					return true;
				} else if (e2.getX() - e1.getX() > GESTURE_DISTANCE) {	//从左向右滑，向左翻页
					if (currentIndex > 0) {								//只有后三页，才会翻，第一页不能左翻页
//						radioGroup.check(buttonIds[currentIndex - 1]);	//这种方法不能用
						currentButton = (RadioButton)HomePageActivity.this.findViewById(buttonIds[currentIndex - 1]);
						currentButton.setChecked(true);
					} 
					return true;
				}
				return false;
			}
			
			public boolean onDown(MotionEvent e) {
				return false;
			}
		});
	}
	
	/*进入软件时检测网络*/
	private void initCheckNetTask() {
		new Thread() {
			public void run() {
				while (noNet) {
					if (!goCheck) {
						goCheck = true;
						HashMap<String, Object> taskParams = new HashMap<String, Object>(1);
						taskParams.put(Task.TA_CHECKNET_TASKPARAMS, defaultUser);
						Task task = new Task(Task.TA_CHECKNET, taskParams);
						MainService.addTask(task);
						try {
							sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			};
		}.start();
	}
	
	/*初始化广播接收*/
	private void initReceiver() {
		receiver = new ChangeUserReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.example.taupstairs.CHANGE_USER");
		registerReceiver(receiver, filter);
	}
	
	/*初始化控件的监听器*/
	private void initSetListener() {
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				/*radiobutton不会有重复点击的问题，要check_change才会到这个监听器里面来
				 * 滑动手势的时候，也要改变check，手势相应函数不做页面改变，
				 * 这个时候也要到这里面来， 所以这里面的处理会比较麻烦*/
				switch (checkedId) {
				case R.id.btn_info:			
					setCurrent(0);
					break;
				case R.id.btn_task:
					setCurrent(1);
					break;
				case R.id.btn_rank:
					setCurrent(2);
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
						jumpToMeFragment();
					}
				} else if (WRITE == flag_me_write) {		//跳到发布任务的activity
					Intent intent = new Intent(HomePageActivity.this, WriteActivity.class);
					startActivityForResult(intent, IntentString.RequestCode.HOMEPAGE_WRITE);
				}
			}
		});
	}
	
	private void jumpToMeFragment() {
		radioGroup.clearCheck();			//如果跳到MeFragment，则radiobutton都不要check
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
		ft.hide(currentFragment).show(meFragment).commit();
		currentFragment = meFragment;
		currentIndex = 3;
	}
	
	/*设置当前状态*/
	private void setCurrent(int index) {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		if (currentIndex < index) {
			ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
		} else if (currentIndex > index) {
			ft.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
		}
		ft.hide(currentFragment).show(listFragments.get(index)).commit();
		fm.executePendingTransactions();	//让异步的commit函数立即执行，但界面切太快还是有bug
		currentIndex = index;
		currentFragment = listFragments.get(index);
		if (1 == index) {
			btn_top_right.setBackgroundResource(R.drawable.hp_bg_btn_write);
			flag_me_write = WRITE;
		} else {
			btn_top_right.setBackgroundResource(R.drawable.hp_bg_btn_me);
			flag_me_write = ME;
		}
	}

	@Override
	public void refresh(Object... params) {
		int taskId = (Integer) params[0];
		switch (taskId) {
		case Task.TA_CHECKNET:
			String ok = (String) params[1];
			if (ok.equals(Task.TA_NO)) {
				if (!displayNoNet) {
					displayNoNet = true;
					Toast.makeText(HomePageActivity.this, "没网络啊！！！亲", Toast.LENGTH_LONG).show();
				}
			} else {
				noNet = false;
			}
			goCheck = false;
			break;
			
		case Task.TA_USEREXIT:
			for (int i = 0; i < listFragments.size(); i++) {
				ItaFragment fragment = (ItaFragment) listFragments.get(i);
				fragment.exit();
			}
			System.exit(0);
			break;

		default:
			break;
		}
	}	
	
	/*
	 * 本地回调
	 */
	public void localRefresh(int id, Map<String, Object> params) {
		switch (id) {
		case HomePageString.UPDATA_PHOTO:
			taskFragment.localRefresh(id, params);
			break;
		case HomePageString.UPDATA_NICKNAME:
			taskFragment.localRefresh(id, params);
			break;

		default:
			break;
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		boolean ret = false;
		ret = detector.onTouchEvent(ev);
		/*如果滑动不成功，才分发事件。
		 * 想要响应list的click事件，要先down，再up的时候。
		 * 如果up的时候，已经被手势捕捉到了滑动，就返回true了，就不分发了*/
		if (!ret) {
			return super.dispatchTouchEvent(ev);
		}
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case IntentString.RequestCode.HOMEPAGE_WRITE:
			if (IntentString.ResultCode.WRITE_HOMEPAGE == resultCode) {
				taskFragment.releaseTaskSuccess();
			}
			break;

		default:
			break;
		}
	}
	
	/*不重写这个方法，在退出的时候杀死进程的话，
	 * 会导致没有完全杀死程序的，会残留哪些我也不太清楚
	 * 使得手机在没有清空缓存的时候，再一次打开软件，
	 * 会出现后台的MainService调用UI线程中的refresh函数不能更新UI的情况*/
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		HashMap<String, Object> taskParams = new HashMap<String, Object>(1);
		taskParams.put(Task.TA_USEREXIT_TASKPARAMS, Task.TA_USEREXIT_ACTIVITY_HOMEPAGE);
		Task task = new Task(Task.TA_USEREXIT, taskParams);
		MainService.addTask(task);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}
	
	/*接收切换用户时的广播消息，这个时候要结束本activity。
	 * 如果不结束的话，新用户登录的时候，这个activity还留着，也就是说有两个HomePageActivity
	 * 那样就会浪费内存，在更新UI方面，可能也会有麻烦
	 * 
	 * 还有一点就是：如果在清单文件中配置使用内部类广播，这里要用static的内部类才行，
	 * 可能是因为java语法的原因吧，别人内部的东西如果不是static的可能是不能调的。
	 * 但如果写成static内部类，HomePageActivity.this.finish();就会报错，不能用父类了。
	 * 那么只好不用static内部类。这时候注册广播就要在父类HomePageActivity里面去new一个
	 * 这个类的实体了，再通过代码注册广播的方法注册广播*/
	public class ChangeUserReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			HomePageActivity.this.finish();
		}
	}

}
