package com.example.taupstairs.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import android.app.Activity;
import android.app.Fragment;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Status;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.bean.User;
import com.example.taupstairs.util.HttpClientUtil;
import com.example.taupstairs.util.JsonUtil;
import com.example.taupstairs.util.StringUtil;

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
				activity_login.refresh(Task.TA_LOGIN, msg.obj);
				break;
				
			case Task.TA_CHECKNET:
				ItaActivity activity_checknet = (ItaActivity) getActivityByName(Task.TA_CHECKNET_ACTIVITY);
				activity_checknet.refresh(Task.TA_CHECKNET, msg.obj);
				break;
				
			case Task.TA_GETUSERDATA:
				ItaFragment fragment_getuserdata = (ItaFragment) getFragmentByName(Task.TA_GETUSERDATA_FRAGMENT);
				fragment_getuserdata.refresh(Task.TA_GETUSERDATA, msg.obj);
				break;
				
			case Task.TA_GETSTATUS:
				ItaFragment fragment_getstatus = (ItaFragment) getFragmentByName(Task.TA_GETSTATUS_FRAGMENT);
				Bundle data_getstatus = msg.getData();
				String mode = data_getstatus.getString(Task.TA_GETSTATUS_MODE);
				fragment_getstatus.refresh(Task.TA_GETSTATUS, mode, msg.obj);
				break;
				
			case Task.TA_RELEASE:
				ItaActivity activity_release = (ItaActivity) getActivityByName(Task.TA_RELEASE_ACTIVITY);
				activity_release.refresh(Task.TA_RELEASE, msg.obj);
				break;
			
			case Task.TA_UPDATAUSERDATA:
				Bundle data_updata_userdata = msg.getData();
				String activity_updata_userdata = data_updata_userdata.getString(Task.TA_UPDATAUSERDATA_ACTIVITY);
				if (activity_updata_userdata.equals(Task.TA_UPDATAUSERDATA_ACTIVITY_COMPLETE)) {
					ItaActivity activity = (ItaActivity) getActivityByName(Task.TA_UPDATAUSERDATA_ACTIVITY_COMPLETE);
					activity.refresh(Task.TA_UPDATAUSERDATA, msg.obj);
				} else if (activity_updata_userdata.equals(Task.TA_UPDATAUSERDATA_FRAGMENT_ME)) {
					ItaFragment fragment = (ItaFragment) getFragmentByName(Task.TA_UPDATAUSERDATA_FRAGMENT_ME);
					fragment.refresh(Task.TA_UPDATAUSERDATA, msg.obj);
				} else if (activity_updata_userdata.equals(Task.TA_UPDATAUSERDATA_ACTIVITY_UPDATAUSERDATABASE)) {
					ItaActivity activity = (ItaActivity) getActivityByName(
							Task.TA_UPDATAUSERDATA_ACTIVITY_UPDATAUSERDATABASE);
					activity.refresh(Task.TA_UPDATAUSERDATA, msg.obj);
				} else if (activity_updata_userdata.equals(Task.TA_UPDATAUSERDATA_ACTIVITY_UPDATAUSERDATAOPTIONAL)) {
					ItaActivity activity = (ItaActivity) getActivityByName(
							Task.TA_UPDATAUSERDATA_ACTIVITY_UPDATAUSERDATAOPTIONAL);
					activity.refresh(Task.TA_UPDATAUSERDATA, msg.obj);
				}
				break;
				
			case Task.TA_USEREXIT:
				String activity = (String) msg.obj;
				if (activity.equals(Task.TA_USEREXIT_ACTIVITY_HOMEPAGE)) {
					ItaActivity activity_userexit = (ItaActivity) getActivityByName(Task.TA_USEREXIT_ACTIVITY_HOMEPAGE);
					activity_userexit.refresh(Task.TA_USEREXIT, Task.TA_USEREXIT_OK);
				} else if (activity.equals(Task.TA_USEREXIT_ACTIVITY_SETTING)) {
					ItaActivity activity_userexit = (ItaActivity) getActivityByName(Task.TA_USEREXIT_ACTIVITY_SETTING);
					activity_userexit.refresh(Task.TA_USEREXIT, Task.TA_USEREXIT_OK);
				}

			default:
				break;
			}
		};
	};
	
	@Override
	public void onCreate() {
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
				e.printStackTrace();
			}
		}
	}
	
	private void doTask(Task task) {
		Message msg = new Message();
		msg.what = task.getTaskId();
		Map<String, Object> taskParams = task.getTaskParams();
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
			
		case Task.TA_GETSTATUS:
			msg.obj = doGetStatusTask(task);
			String mode = (String) taskParams.get(Task.TA_GETSTATUS_MODE);
			Bundle data_getuserdata = msg.getData();
			data_getuserdata.putString(Task.TA_GETSTATUS_MODE, mode);
			break;
			
		case Task.TA_RELEASE:
			msg.obj = doReleaseTask(task);
			break;
			
		case Task.TA_UPDATAUSERDATA:
			msg.obj = doUpdataUserdata(task);
			String activity_updata_userdata = (String) taskParams.get(Task.TA_UPDATAUSERDATA_ACTIVITY);
			Bundle data_updata_userdata = msg.getData();
			data_updata_userdata.putString(Task.TA_UPDATAUSERDATA_ACTIVITY, activity_updata_userdata);
			break;
		
		case Task.TA_USEREXIT:
			doUserExit(); 
			String activity_userexit = (String) taskParams.get(Task.TA_USEREXIT_TASKPARAMS);
			msg.obj = activity_userexit;
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
				"data/user/login?student_id=" + user.getUserStudentId() + 
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
		String getuserdata_url = HttpClientUtil.BASE_URL + "data/user/userInfo?users_id=" + personId;
		try {
			String jsonString = HttpClientUtil.getRequest(getuserdata_url);
			person = JsonUtil.getPerson(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return person;		//此处如果未连接网络的话，返回的是null
	}
	
	/*
	 * 获取List<Status>（任务）
	 * 分为三种，分别是第一次加载，下拉刷新，上拉加载更多
	 */
	private List<Status> doGetStatusTask(Task task) {
		List<Status> listStatus = null;
		String getstatus_url = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String mode = (String) taskParams.get(Task.TA_GETSTATUS_MODE);
		if (mode.equals(Task.TA_GETSTATUS_MODE_FIRSTTIME)) {
			getstatus_url = HttpClientUtil.BASE_URL + "data/task/taskList2Down";
		} else if (mode.equals(Task.TA_GETSTATUS_MODE_PULLREFRESH)) {
			String statusId = (String) taskParams.get(Task.TA_GETSTATUS_STATUSID);
			getstatus_url = HttpClientUtil.BASE_URL + "data/task/taskList2Down?task_id=" + statusId;
		} else if (mode.equals(Task.TA_GETSTATUS_MODE_LOADMORE)) {
			String statusId = (String) taskParams.get(Task.TA_GETSTATUS_STATUSID);
			getstatus_url = HttpClientUtil.BASE_URL + "data/task/taskList2Up?task_id=" + statusId;
		}
		try {
			String jsonString = HttpClientUtil.getRequest(getstatus_url);
			/*如果数组长度为0，则链表长度为0，但他不为空，因为在里面已经new了，
			 * 所以有联网状态下，没有更新与没有更多的时候，0 == listStatus.size() 成立，但listStatus不为空*/
			listStatus = JsonUtil.getListStatus(jsonString);
		} catch (Exception e) {
			/*没网络，会返回null*/
			e.printStackTrace();
		}
		return listStatus;
	}
	
	/*
	 * 发布任务
	 */
	private String doReleaseTask(Task task) {
		String result = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String title = (String) taskParams.get(Status.STATUS_TITLE);
		String content = (String) taskParams.get(Status.STATUS_CONTENT);
		String rewards = (String) taskParams.get(Status.STATUS_REWARDS);
		String endtime = (String) taskParams.get(Status.STATUS_ENDTIME);	
		String release_url = HttpClientUtil.BASE_URL 
				+ "data/task/save?title=" + title
				+ "&content=" + content
				+ "&rewards=" + rewards 
				+ "&end_of_time=" + endtime 
				+ "&task_level=1";
		release_url = StringUtil.replaceBlank(release_url);
		try {
			result = HttpClientUtil.getRequest(release_url);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return result;

	}
	
	/*
	 * 更新用户数据
	 */
	private String doUpdataUserdata(Task task) {
		String result = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String url = (String) taskParams.get(Task.TA_UPDATAUSERDATA_URL);
		String updata_userdata_url = HttpClientUtil.BASE_URL + "data/user/update?" + url;
		updata_userdata_url = StringUtil.replaceBlank(updata_userdata_url);
		try {
			result = HttpClientUtil.getRequest(updata_userdata_url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/*
	 * 用户注销
	 */
	private void doUserExit() {
		String userexit_url = HttpClientUtil.BASE_URL + "data/user/exit";
		try {
			HttpClientUtil.getRequest(userexit_url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*清空activitys和fragments链表*/
	public static void emptyMainService() {
		activities.clear();
		fragments.clear();
	}
	
	/*将Activity添加到Activity链表中去*/
	public static void addActivity(Activity activity) {
		activities.add(activity);
	}
	
	public static void removeActivity(Activity activity) {
		activities.remove(activity);
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
	
	public static void removeFragment(Fragment fragment) {
		fragments.remove(fragment);
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
