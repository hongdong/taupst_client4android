package com.example.taupstairs.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import com.example.taupstairs.bean.College;
import com.example.taupstairs.bean.MessageContent;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Rank;
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
				Bundle data_getuserdata = msg.getData();
				String activity_getuserdata = data_getuserdata.getString(Task.TA_GETUSERDATA_ACTIVITY);
				if (activity_getuserdata.equals(Task.TA_GETUSERDATA_ACTIVITY_LOGIN)) {
					ItaActivity activity = (ItaActivity) getActivityByName(Task.TA_GETUSERDATA_ACTIVITY_LOGIN);
					activity.refresh(Task.TA_GETUSERDATA, msg.obj);
				} else if (activity_getuserdata.equals(Task.TA_GETUSERDATA_ACTIVITY_ME)) {
					ItaFragment fragment = (ItaFragment) getFragmentByName(Task.TA_GETUSERDATA_ACTIVITY_ME);
					fragment.refresh(Task.TA_GETUSERDATA, msg.obj);
				}
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
				
			case Task.TA_CHECKSTATUS:
				ItaActivity activity_checkstatus = (ItaActivity) getActivityByName(Task.TA_CHECKSTATUS_ACTIVITY);
				if (activity_checkstatus != null) {
					activity_checkstatus.refresh(Task.TA_CHECKSTATUS, msg.obj);
				}
				break;
				
			case Task.TA_GETMESSAGE:
				ItaActivity activity_getmessage = (ItaActivity) getActivityByName(Task.TA_GETMESSAGE_ACTIVITY);
				if (activity_getmessage != null) {
					activity_getmessage.refresh(Task.TA_GETMESSAGE, msg.obj);
				}
				break;
				
			case Task.TA_MESSAGE:
				ItaActivity activity_message = (ItaActivity) getActivityByName(Task.TA_MESSAGE_ACTIVITY);
				activity_message.refresh(Task.TA_MESSAGE, msg.obj);
				break;
				
			case Task.TA_SIGNUP:
				ItaActivity activity_signup = (ItaActivity) getActivityByName(Task.TA_SIGNUP_ACTIVITY);
				activity_signup.refresh(Task.TA_SIGNUP, msg.obj);
				break;
				
			case Task.TA_GETRANK:
				ItaFragment fragment_getrank = (ItaFragment) getFragmentByName(Task.TA_GETRANK_ACTIVITY);
				fragment_getrank.refresh(Task.TA_GETRANK, msg.obj);
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
				break;
			
			case Task.TA_CHECKUSER:
				ItaActivity activity_checkuser = (ItaActivity) getActivityByName(Task.TA_CHECKUSER_ACTIVITY);
				activity_checkuser.refresh(Task.TA_CHECKUSER, msg.obj);
				break;
				
			case Task.TA_GETCOLLEGECAPTCHA:
				ItaActivity activity_getcollegecaptcha = 
				(ItaActivity) getActivityByName(Task.TA_GETCOLLEGECAPTCHA_ACTIVITY);
				activity_getcollegecaptcha.refresh(Task.TA_GETCOLLEGECAPTCHA, msg.obj);
				break;

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
			msg.obj = doLoginTask(task);
			break;
			
		case Task.TA_CHECKNET:		//检查网络的方式也是用login，看看是否能够成功返回登录数据
			msg.obj = doLoginTask(task);
			break;
			
		case Task.TA_GETUSERDATA:
			msg.obj = doGetUserDataTask(task);
			String activity_getuserdata = (String) taskParams.get(Task.TA_GETUSERDATA_ACTIVITY);
			Bundle data_getuserdata = msg.getData();
			data_getuserdata.putString(Task.TA_GETUSERDATA_ACTIVITY, activity_getuserdata);
			break;
			
		case Task.TA_GETSTATUS:
			msg.obj = doGetStatusTask(task);
			String mode = (String) taskParams.get(Task.TA_GETSTATUS_MODE);
			Bundle data_getstatus = msg.getData();
			data_getstatus.putString(Task.TA_GETSTATUS_MODE, mode);
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
			
		case Task.TA_CHECKSTATUS:
			msg.obj = doCheckStatusTask(task);
			break;
			
		case Task.TA_GETMESSAGE:
			msg.obj = doGetMessageTask(task);
			break;
			
		case Task.TA_MESSAGE:
			msg.obj = doMessageTask(task);
			break;
			
		case Task.TA_SIGNUP:
			msg.obj = doSignupTask(task);
			break;
			
		case Task.TA_GETRANK:
			msg.obj = doGetRankTask(task);
			break;
		
		case Task.TA_USEREXIT:
			doUserExit(); 
			String activity_userexit = (String) taskParams.get(Task.TA_USEREXIT_TASKPARAMS);
			msg.obj = activity_userexit;
			break;
			
		case Task.TA_CHECKUSER:
			msg.obj = doCheckUserTask(task);
			break;
			
		case Task.TA_GETCOLLEGECAPTCHA:
			msg.obj = doGetCollegeCaptchaTask(task);
			break;

		default:
			break;
		}
		handler.sendMessage(msg);
	}
	
	private String doCheckUserTask(Task task) {
		String result = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String collegeId = (String) taskParams.get(College.COLLEGE_ID);
		String studentId = (String) taskParams.get(User.USER_STUDENTID);
		String check_user_url = HttpClientUtil.BASE_URL + "data/user/issysn?school=" +
				collegeId + "&student_id=" + studentId;
		try {
			result = HttpClientUtil.getRequest(check_user_url);
		} catch (Exception e) {
			e.printStackTrace();	//如果没有连接网络，就会抛出异常，result就会为初值TA_NO：no
		}
		return result;
	}
	
	private Drawable doGetCollegeCaptchaTask(Task task) {
		Drawable drawable = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String collegeCaptchaUrl = (String) taskParams.get(College.COLLEGE_CAPTCHAURL);
		try {
			drawable = HttpClientUtil.getCollegeCaptcha(collegeCaptchaUrl);
		} catch (Exception e) {
			e.printStackTrace();	//如果没有连接网络，就会抛出异常，result就会为初值TA_NO：no
		}
		return drawable;
	}
	
	/*登录任务*/
	private String doLoginTask(Task task) {
		String result = Task.TA_NO;
		Map<String, Object> taskParams = task.getTaskParams();
		String collegeId = (String) taskParams.get(User.USER_COLLEGEID);
		String studentId = (String) taskParams.get(User.USER_STUDENTID);
		String password = (String) taskParams.get(User.USER_PASSWORD);
		String collegeCaptcha = (String) taskParams.get(Task.TA_LOGIN_COLLEGECAPTCHA);
		String cookie = (String) taskParams.get(Task.TA_LOGIN_COOKIE);
		String login_url = HttpClientUtil.BASE_URL + "data/user/login?student_id=" + studentId 
				+ "&pwd=" + password + "&school=" + collegeId;
		if (collegeCaptcha != null && cookie != null) {
			login_url += "&code=" + collegeCaptcha + "&ck=" + cookie;
		}
		try {
			login_url = StringUtil.replaceBlank(login_url);
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
		String release_url = HttpClientUtil.BASE_URL + "data/task/save?title=" + title
				+ "&content=" + content + "&rewards=" + rewards + "&end_of_time=" + endtime + "&task_level=1";
		try {
			release_url = StringUtil.replaceBlank(release_url);
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
	 * 检测任务状态
	 */
	private String doCheckStatusTask(Task task) {
		String result = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String statusId = (String) taskParams.get(Status.STATUS_ID);
		String check_status_url = HttpClientUtil.BASE_URL + "data/sign/issign?task_id=" + statusId;
		try {
			result = HttpClientUtil.getRequest(check_status_url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	} 
	
	/*
	 * 获取留言
	 */
	private List<com.example.taupstairs.bean.Message> doGetMessageTask(Task task) {
		List<com.example.taupstairs.bean.Message> messages = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String statusId = (String) taskParams.get(Status.STATUS_ID);
		String get_message_url = HttpClientUtil.BASE_URL + "data/taskmsg/taskMsgList2Down?task_id=" + statusId;
		try {
			get_message_url = StringUtil.replaceBlank(get_message_url);
			String jsonString = HttpClientUtil.getRequest(get_message_url);
			messages = JsonUtil.getMessages(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return messages;
	}
	
	/*
	 * 留言
	 */
	private String doMessageTask(Task task) {
		String result = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String statusId = (String) taskParams.get(Status.STATUS_ID);
		String content = (String) taskParams.get(MessageContent.CONTENT);
		String message_url = HttpClientUtil.BASE_URL + "data/taskmsg/save?task_id=" + statusId 
				+ "&message_content=" + content;
		String mode = (String) taskParams.get(Task.TA_MESSAGE_MODE);
		if (mode.equals(Task.TA_MESSAGE_MODE_ROOT)) {
			
		} else if (mode.equals(Task.TA_MESSAGE_MODE_CHILD)) {
			String messageId = (String) taskParams.get(com.example.taupstairs.bean.Message.MESSAGE_ID);
			String replyId = (String) taskParams.get(MessageContent.REPLY_ID);
			message_url += "&to_user=" + replyId + "&root_id=" + messageId;
		} 
		try {
			message_url = StringUtil.replaceBlank(message_url);
			result= HttpClientUtil.getRequest(message_url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	} 
	
	/*
	 * 报名
	 */
	private String doSignupTask(Task task) {
		String result = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String statusId = (String) taskParams.get(Status.STATUS_ID);
		String contact = (String) taskParams.get(Task.TA_SIGNUP_CONTACT);
		String message = (String) taskParams.get(Task.TA_SIGNUP_MESSAGE);
		String signup_url = HttpClientUtil.BASE_URL + "/data/sign/save?task_id=" + statusId + 
				"&open_mes=" + contact + "&message=" + message;
		try {
			signup_url = StringUtil.replaceBlank(signup_url);
			result = HttpClientUtil.getRequest(signup_url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private List<Rank> doGetRankTask(Task task) {
		List<Rank> ranks = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String mode = (String) taskParams.get(Task.TA_GETRANK_MODE);
		String get_rank_url = HttpClientUtil.BASE_URL + "data/ranking/list?type=" + mode;
		try {
			String jsonString = HttpClientUtil.getRequest(get_rank_url);
			ranks = JsonUtil.getRanks(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ranks;
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
