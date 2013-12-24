package com.example.taupstairs.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import android.app.Activity;
import android.app.Fragment;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.bean.User;
import com.example.taupstairs.ui.ItaActivity;
import com.example.taupstairs.ui.ItaFragment;
import com.example.taupstairs.util.HttpClientUtil;
import com.example.taupstairs.util.JsonUtil;

public class MainService extends Service implements Runnable {

	//主服务是否运行
	private boolean isRun;
	//任务队列
	private static Queue<Task> tasks = new LinkedList<Task>();
	//Activity链表
	private static ArrayList<Activity> activities = new ArrayList<Activity>();
	//Fragment链表
	private static ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Task.TA_LOGIN:
				ItaActivity activity_login = (ItaActivity) getActivityByName(Task.TA_LOGIN_ACTIVITY);
				activity_login.refresh(msg.obj);
				break;
				
			case Task.TA_CHECKNET:
				ItaActivity activity_checknet = (ItaActivity) getActivityByName(Task.TA_CHECKNET_ACTIVITY);
				activity_checknet.refresh(msg.obj);
				break;
				
			case Task.TA_GETUSERDATA:
				ItaFragment fragment_getuserdata = (ItaFragment) getFragmentByName(Task.TA_GETUSERDATA_FRAGMENT);
				fragment_getuserdata.refresh(msg.obj);
				break;

			default:
				break;
			}
		};
	};
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		isRun = true;
		Thread thread = new Thread(this);
		thread.start();
	}
	
	/*添加任务到任务队列*/
	public static void addTask(Task task) {
		tasks.add(task);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

		Task task;
		while (isRun) {
			if (!tasks.isEmpty()) {
				task = tasks.poll();	//ִ执行完任务后将它从队列中删除
				if (task != null) {
					doTask(task);
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void doTask(Task task) {
		Message msg = new Message();
		msg.what = task.getTaskId();
		switch (task.getTaskId()) {
		case Task.TA_LOGIN:
			msg.obj = doLoginTask(task, Task.TA_LOGIN_TASKPARAMS);
			break;
			
		case Task.TA_CHECKNET:		//检查网络的方式也是用login，看看是否能够成功返回登录数据
			msg.obj = doLoginTask(task, Task.TA_CHECKNET_TASKPARAMS);
			break;
			
		case Task.TA_GETUSERDATA:
			msg.obj = doGetUserDataTask(task);
			break;

		default:
			break;
		}
		handler.sendMessage(msg);
	}
	
	/*登录任务*/
	private String doLoginTask(Task task, String taskParamsString) {
		String result = Task.TA_NO;
		Map<String, Object> taskParams = task.getTaskParams();
		User user = (User) taskParams.get(taskParamsString);
		String login_url = HttpClientUtil.BASE_URL + 
				"user/login?student_id=" + user.getUserStudentId() + 
				"&pwd=" + user.getUserPassword() + 
				"&school=" + user.getUserCollegeId();
		try {
			result = HttpClientUtil.getRequest(login_url);
		} catch (Exception e) {
			e.printStackTrace();	//如果没有连接网络，就会抛出异常，result就会为初值TA_NO：no
		}
		return result;
	}
	
	/*获取Person信息*/
	private Person doGetUserDataTask(Task task) {
		Person person = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String personId = (String) taskParams.get(Task.TA_GETUSERDATA_TASKPARAMS);
		String getuserdata_url = HttpClientUtil.BASE_URL + "user/userInfo?users_id=" + personId;
		try {
			String jsonString = HttpClientUtil.getRequest(getuserdata_url);
			person = JsonUtil.getPerson(MainService.this, jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return person;		//此处如果未连接网络的话，返回的是null
	}
	
	/*将Activity添加到Activity链表中去*/
	public static void addActivity(Activity activity) {
		activities.add(activity);
	}
	
	/*根据Activity的name从Activity链表中找到它*/
	private Activity getActivityByName(String name) {
		if (!activities.isEmpty()) {
			for (Activity activity : activities) {
				if (activity != null) {
					if (activity.getClass().getName().indexOf(name) > 0) {
						return activity;
					}
				}
			}
		}
		return null;
	}
	
	/*将Fragment添加到Fragment链表中去*/
	public static void addFragment(Fragment fragment) {
		fragments.add(fragment);
	}
	
	/*根据Fragment的name从Fragment链表中找到它*/
	private Fragment getFragmentByName(String name) {
		if (!fragments.isEmpty()) {
			for (Fragment fragment : fragments) {
				if (fragment != null) {
					if (fragment.getClass().getName().indexOf(name) > 0) {
						return fragment;
					}
				}
			}
		}
		return null;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
