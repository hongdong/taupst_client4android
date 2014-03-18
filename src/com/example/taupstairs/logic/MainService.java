package com.example.taupstairs.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import com.example.taupstairs.bean.Task;

public class MainService extends Service implements Runnable {

	//主服务是否运行
	private boolean isRun;
	//任务队列
	private static Queue<Task> tasks = new LinkedList<Task>();
	//Activity链表
	private static ArrayList<Activity> activities = new ArrayList<Activity>();
	//Fragment链表
	private static ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	//执行各种任务的方法
	private DoTaskService doTaskService = new DoTaskService(MainService.this);
	
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {	
			case Task.TA_LOGIN:
				ItaActivity activity_login = (ItaActivity) getActivityByName(Task.TA_LOGIN_ACTIVITY);
				if (activity_login != null) {
					activity_login.refresh(Task.TA_LOGIN, msg.obj);
				}
				break;
				
			case Task.TA_CHECKNET:
				ItaActivity activity_checknet = (ItaActivity) getActivityByName(Task.TA_CHECKNET_ACTIVITY);
				if (activity_checknet != null) {
					activity_checknet.refresh(Task.TA_CHECKNET, msg.obj);
				}
				break;
				
			case Task.TA_CHECKUPDATA:
				ItaActivity activity_checkupdata = (ItaActivity) getActivityByName(Task.TA_CHECKUPDATA_ACTIVITY);
				if (activity_checkupdata != null) {
					activity_checkupdata.refresh(Task.TA_CHECKUPDATA, msg.obj);
				}
				break;
				
			case Task.TA_GETUSERDATA:
				Bundle data_getuserdata = msg.getData();
				String activity_getuserdata = data_getuserdata.getString(Task.TA_GETUSERDATA_ACTIVITY);
				if (activity_getuserdata.equals(Task.TA_GETUSERDATA_ACTIVITY_PERSONDATA)) {
					ItaActivity activity = (ItaActivity) getActivityByName(Task.TA_GETUSERDATA_ACTIVITY_PERSONDATA);
					if (activity != null) {
						activity.refresh(Task.TA_GETUSERDATA, msg.obj);
					}	
				} else if (activity_getuserdata.equals(Task.TA_GETUSERDATA_ACTIVITY_ME)) {
					ItaFragment fragment = (ItaFragment) getFragmentByName(Task.TA_GETUSERDATA_ACTIVITY_ME);
					fragment.refresh(Task.TA_GETUSERDATA, msg.obj);
				}
				break;
				
			case Task.TA_GETSTATUS:
				Bundle data_getstatus = msg.getData();
				String activity_getstatus = data_getstatus.getString(Task.TA_GETSTATUS_ACTIVITY);
				int mode = data_getstatus.getInt(Task.TA_GETSTATUS_MODE);
				if (activity_getstatus.equals(Task.TA_GETSTATUS_FRAGMENT)) {
					ItaFragment fragment_getstatus = (ItaFragment) getFragmentByName(Task.TA_GETSTATUS_FRAGMENT);
					fragment_getstatus.refresh(Task.TA_GETSTATUS, mode, msg.obj);
				} else if (activity_getstatus.equals(Task.TA_GETSTATUS_MYRELEASESTATUS)) {
					ItaActivity activity = (ItaActivity) getActivityByName(Task.TA_GETSTATUS_MYRELEASESTATUS);
					if (activity != null) {
						activity.refresh(Task.TA_GETSTATUS, mode, msg.obj);
					}
				} else if (activity_getstatus.equals(Task.TA_GETSTATUS_MYSIGNUPSTATUS)) {
					ItaActivity activity = (ItaActivity) getActivityByName(Task.TA_GETSTATUS_MYSIGNUPSTATUS);
					if (activity != null) {
						activity.refresh(Task.TA_GETSTATUS, mode, msg.obj);
					}
				}
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
				} else if (activity_updata_userdata.equals(Task.TA_UPDATAUSERDATA_ACTIVITY_BASE)) {
					ItaActivity activity = (ItaActivity) getActivityByName(
							Task.TA_UPDATAUSERDATA_ACTIVITY_BASE);
					activity.refresh(Task.TA_UPDATAUSERDATA, msg.obj);
				} else if (activity_updata_userdata.equals(Task.TA_UPDATAUSERDATA_ACTIVITY_OPTIONAL)) {
					ItaActivity activity = (ItaActivity) getActivityByName(
							Task.TA_UPDATAUSERDATA_ACTIVITY_OPTIONAL);
					activity.refresh(Task.TA_UPDATAUSERDATA, msg.obj);
				} else if (activity_updata_userdata.equals(Task.TA_UPDATAUSERDATA_ACTIVITY_REAL)) {
					ItaActivity activity = (ItaActivity) getActivityByName(
							Task.TA_UPDATAUSERDATA_ACTIVITY_REAL);
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
				Bundle data_getmessage = msg.getData();
				String string_getmessage = data_getmessage.getString(Task.TA_GETMESSAGE_ACTIVITY);
				ItaActivity activity_getmessage = null;
				if (string_getmessage.equals(Task.TA_GETMESSAGE_ACTIVITY_DETAIL)) {
					activity_getmessage = (ItaActivity) getActivityByName(Task.TA_GETMESSAGE_ACTIVITY_DETAIL);
				} else if (string_getmessage.equals(Task.TA_GETMESSAGE_ACTIVITY_BYID)) {
					activity_getmessage = (ItaActivity) getActivityByName(Task.TA_GETMESSAGE_ACTIVITY_BYID);
				}
				if (activity_getmessage != null) {
					activity_getmessage.refresh(Task.TA_GETMESSAGE, msg.obj);
				}
				break;
				
			case Task.TA_MESSAGE:
				Bundle data_message = msg.getData();
				String activity_message = data_message.getString(Task.TA_MESSAGE_ACTIVITY);
				if (activity_message.equals(Task.TA_MESSAGE_ACTIVITY_TASK)) {
					ItaActivity activity = (ItaActivity) getActivityByName(Task.TA_MESSAGE_ACTIVITY_TASK);
					activity.refresh(Task.TA_MESSAGE, msg.obj);
				} else if (activity_message.equals(Task.TA_MESSAGE_ACTIVITY_INFO)) {
					ItaActivity activity = (ItaActivity) getActivityByName(Task.TA_MESSAGE_ACTIVITY_INFO);
					activity.refresh(Task.TA_MESSAGE, msg.obj);
				} else if (activity_message.equals(Task.TA_MESSAGE_ACTIVITY_BYID)) {
					ItaActivity activity = (ItaActivity) getActivityByName(Task.TA_MESSAGE_ACTIVITY_BYID);
					activity.refresh(Task.TA_MESSAGE, msg.obj);
				}
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
				
			case Task.TA_GETCAPTCHA:
				ItaActivity activity_getcollegecaptcha = 
				(ItaActivity) getActivityByName(Task.TA_GETCAPTCHA_ACTIVITY);
				activity_getcollegecaptcha.refresh(Task.TA_GETCAPTCHA, msg.obj);
				break;
				
			case Task.TA_UPLOADPHOTO:
				Bundle data_upload_photo = msg.getData();
				String activity_upload_photo = data_upload_photo.getString(Task.TA_UPLOADPHOTO_ACTIVITY);
				if (activity_upload_photo.equals(Task.TA_UPLOADPHOTO_ACTIVITY_COMPLETE)) {
					ItaActivity activity_upload = (ItaActivity) getActivityByName(Task.TA_UPLOADPHOTO_ACTIVITY_COMPLETE);
					activity_upload.refresh(Task.TA_UPLOADPHOTO, msg.obj);
				} else if (activity_upload_photo.equals(Task.TA_UPLOADPHOTO_ACTIVITY_ME)) {
					ItaFragment activity_upload = (ItaFragment) getFragmentByName(Task.TA_UPLOADPHOTO_ACTIVITY_ME);
					activity_upload.refresh(Task.TA_UPLOADPHOTO, msg.obj);
				}
				break;
				
			case Task.TA_GETINFO:
				ItaFragment fragment_getinfo = (ItaFragment) getFragmentByName(Task.TA_GETINFO_FRAGMENT);
				Bundle data_getinfo = msg.getData();
				int mode_getinfo = data_getinfo.getInt(Task.TA_GETINFO_MODE);
				fragment_getinfo.refresh(Task.TA_GETINFO, mode_getinfo, msg.obj);
				break;
				
			case Task.TA_GETINFO_DETAIL:
				Bundle data_getinfo_detail = msg.getData();
				String activity_getinfo_detail = data_getinfo_detail.getString(Task.TA_GETINFO_DETAIL_ACTIVITY);
				ItaActivity activity_getinfo = null;
				if (activity_getinfo_detail.equals(Task.TA_GETINFO_DETAIL_MESSAGE)) {
					activity_getinfo = (ItaActivity) getActivityByName(Task.TA_GETINFO_DETAIL_MESSAGE);
				} else if (activity_getinfo_detail.equals(Task.TA_GETINFO_DETAIL_EXECTASK)) {
					activity_getinfo = (ItaActivity) getActivityByName(Task.TA_GETINFO_DETAIL_EXECTASK);
				} else if (activity_getinfo_detail.equals(Task.TA_GETINFO_DETAIL_SIGNUP)) {
					activity_getinfo = (ItaActivity) getActivityByName(Task.TA_GETINFO_DETAIL_SIGNUP);
				} else if (activity_getinfo_detail.equals(Task.TA_GETINFO_DETAIL_ENDTASK)) {
					activity_getinfo = (ItaActivity) getActivityByName(Task.TA_GETINFO_DETAIL_ENDTASK);
				} else if (activity_getinfo_detail.equals(Task.TA_GETINFO_DETAIL_PRIVATELETTER)) {
					activity_getinfo = (ItaActivity) getActivityByName(Task.TA_GETINFO_DETAIL_PRIVATELETTER);
				}
				if (activity_getinfo != null) {
					activity_getinfo.refresh(Task.TA_GETINFO_DETAIL, msg.obj);
				}
				break;
				
			case Task.TA_EXEC_TASK:
				ItaActivity activity_exec_task = (ItaActivity) getActivityByName(Task.TA_EXEC_TASK_ACTIVITY);
				activity_exec_task.refresh(Task.TA_EXEC_TASK, msg.obj);
				break;
				
			case Task.TA_GET_TASK_DETAIL:
				ItaActivity activity_get_task = (ItaActivity) getActivityByName(Task.TA_GET_TASK_DETAIL_ACTIVITY);
				if (activity_get_task != null) {
					activity_get_task.refresh(Task.TA_GET_TASK_DETAIL, msg.obj);
				}
				break;
				
			case Task.TA_END_TASK:
				ItaActivity itaActivity = (ItaActivity) getActivityByName(Task.TA_END_TASK_ACTIVITY);
				itaActivity.refresh(Task.TA_END_TASK, msg.obj);
				break;
				
			case Task.TA_GET_SIGNUP_LIST:
				ItaActivity activity_get_signup_list = 
					(ItaActivity) getActivityByName(Task.TA_GET_SIGNUP_LIST_ACTIVITY);
				if (activity_get_signup_list != null) {
					activity_get_signup_list.refresh(Task.TA_GET_SIGNUP_LIST, msg.obj);
				}
				break;
				
			case Task.TA_EVALUATE:
				ItaActivity activity_evaluate = (ItaActivity) getActivityByName(Task.TA_EVALUATE_ACTIVITY);
				activity_evaluate.refresh(Task.TA_EVALUATE, msg.obj);
				break;
				
			case Task.TA_FEEDBACK:
				ItaActivity activity_feedback = (ItaActivity) getActivityByName(Task.TA_FEEDBACK_ACTIVITY);
				activity_feedback.refresh(Task.TA_FEEDBACK, msg.obj);
				break;
				
			case Task.TA_PRIVATE_LETTER:
				ItaActivity activity_private_letter = (ItaActivity) getActivityByName(Task.TA_PRIVATE_LETTER_ACTIVITY);
				activity_private_letter.refresh(Task.TA_PRIVATE_LETTER, msg.obj);
				break;
				
			case Task.TA_SIGNUP_INFODETAIL:
				ItaActivity activity27 = (ItaActivity) getActivityByName(Task.TA_SIGNUP_INFODETAIL_ACTIVITY);
				if (activity27 != null) {
					activity27.refresh(Task.TA_SIGNUP_INFODETAIL, msg.obj);
				}
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
			msg.obj = doTaskService.doLoginTask(task);
			break;
			
		case Task.TA_PUSH:
			doTaskService.doPushTask(task);
			break;
			
		case Task.TA_CHECKNET:		//检查网络的方式也是用login，看看是否能够成功返回登录数据
			msg.obj = doTaskService.doLoginTask(task);
			break;
			
		case Task.TA_CHECKUPDATA:
			msg.obj = doTaskService.doCheckUpdataTask(task);
			break;
			
		case Task.TA_GETUSERDATA:
			msg.obj = doTaskService.doGetUserDataTask(task);
			String activity_getuserdata = (String) taskParams.get(Task.TA_GETUSERDATA_ACTIVITY);
			Bundle data_getuserdata = msg.getData();
			data_getuserdata.putString(Task.TA_GETUSERDATA_ACTIVITY, activity_getuserdata);
			break;
			
		case Task.TA_GETSTATUS:
			msg.obj = doTaskService.doGetStatusTask(task);
			String activity_getstatus = (String) taskParams.get(Task.TA_GETSTATUS_ACTIVITY);
			int mode = (Integer) taskParams.get(Task.TA_GETSTATUS_MODE);
			Bundle data_getstatus = msg.getData();
			data_getstatus.putInt(Task.TA_GETSTATUS_MODE, mode);
			data_getstatus.putString(Task.TA_GETSTATUS_ACTIVITY, activity_getstatus);
			break;
			
		case Task.TA_RELEASE:
			msg.obj = doTaskService.doReleaseTask(task);
			break;
			
		case Task.TA_UPDATAUSERDATA:
			msg.obj = doTaskService.doUpdataUserdata(task);
			String activity_updata_userdata = (String) taskParams.get(Task.TA_UPDATAUSERDATA_ACTIVITY);
			Bundle data_updata_userdata = msg.getData();
			data_updata_userdata.putString(Task.TA_UPDATAUSERDATA_ACTIVITY, activity_updata_userdata);
			break;
			
		case Task.TA_CHECKSTATUS:
			msg.obj = doTaskService.doCheckStatusTask(task);
			break;
			
		case Task.TA_GETMESSAGE:
			msg.obj = doTaskService.doGetMessageTask(task);
			String activity_getmessage = (String) taskParams.get(Task.TA_GETMESSAGE_ACTIVITY);
			Bundle data_getmessage = msg.getData();
			data_getmessage.putString(Task.TA_GETMESSAGE_ACTIVITY, activity_getmessage);
			break;
			
		case Task.TA_MESSAGE:
			msg.obj = doTaskService.doMessageTask(task);
			String activity_message = (String) taskParams.get(Task.TA_MESSAGE_ACTIVITY);
			Bundle data_message = msg.getData();
			data_message.putString(Task.TA_MESSAGE_ACTIVITY, activity_message);
			break;
			
		case Task.TA_SIGNUP:
			msg.obj = doTaskService.doSignupTask(task);
			break;
			
		case Task.TA_GETRANK:
			msg.obj = doTaskService.doGetRankTask(task);
			break;
		
		case Task.TA_USEREXIT:
			doTaskService.doUserExit(task); 
			String activity_userexit = (String) taskParams.get(Task.TA_USEREXIT_TASKPARAMS);
			msg.obj = activity_userexit;
			break;
			
		case Task.TA_CHECKUSER:
			msg.obj = doTaskService.doCheckUserTask(task);
			break;
			
		case Task.TA_GETCAPTCHA:
			msg.obj = doTaskService.doGetCaptchaTask(task);
			break;
			
		case Task.TA_UPLOADPHOTO:
			msg.obj = doTaskService.doUploadPhotoTask(task);
			String activity_upload_photo = (String) taskParams.get(Task.TA_UPLOADPHOTO_ACTIVITY);
			Bundle data_upload_photo = msg.getData();
			data_upload_photo.putString(Task.TA_UPDATAUSERDATA_ACTIVITY, activity_upload_photo);
			break;
			
		case Task.TA_GETINFO:
			msg.obj = doTaskService.doGetInfoTask(task);
			int mode_info = (Integer) taskParams.get(Task.TA_GETINFO_MODE);
			Bundle data_getinfo = msg.getData();
			data_getinfo.putInt(Task.TA_GETINFO_MODE, mode_info);
			break;
			
		case Task.TA_GETINFO_DETAIL:
			msg.obj = doTaskService.doGetInfoDetailTask(task);
			String activity_getinfo_detail = (String) taskParams.get(Task.TA_GETINFO_DETAIL_ACTIVITY);
			Bundle data_getinfo_detail = msg.getData();
			data_getinfo_detail.putString(Task.TA_GETINFO_DETAIL_ACTIVITY, activity_getinfo_detail);
			break;
			
		case Task.TA_EXEC_TASK:
			msg.obj = doTaskService.doExecTaskTask(task);
			break;
			
		case Task.TA_GET_TASK_DETAIL:
			msg.obj = doTaskService.doGetTaskDetail(task);
			break;
			
		case Task.TA_END_TASK:
			msg.obj = doTaskService.doEndTask(task);
			break;
			
		case Task.TA_GET_SIGNUP_LIST:
			msg.obj = doTaskService.doGetSignUpListTask(task);
			break;
			
		case Task.TA_EVALUATE:
			msg.obj = doTaskService.doEvaluateTask(task);
			break;
			
		case Task.TA_FEEDBACK:
			msg.obj = doTaskService.doFeedbackTask(task);
			break;
			
		case Task.TA_PRIVATE_LETTER:
			msg.obj = doTaskService.doSendPrivateLetterTask(task);
			break;
			
		case Task.TA_SIGNUP_INFODETAIL:
			msg.obj = doTaskService.doGetSignUpListDetailTask(task);
			break;

		default:
			break;
		}
		handler.sendMessage(msg);
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
