package com.example.taupstairs.bean;

import java.util.Map;

public class Task {

	//任务ID
	private int taskId;
	//任务参数
	private Map<String, Object> taskParams;
	
	//常用的常量值
	public static final String TA_NULL = "null";
	public static final String TA_TRUE = "true";
	public static final String TA_FALSE = "false";
	public static final String TA_OK = "ok";
	public static final String TA_NO = "no";
	public static final String TA_ERROR = "error";
	public static final String TA_SUCCESS = "success";
	
	
	//登录任务用到的常量值
	public static final int TA_LOGIN = 1;
	public static final String TA_LOGIN_TASKPARAMS = "login";
	public static final String TA_LOGIN_ACTIVITY = "LoginActivity";
	public static final String TA_LOGIN_CAPTCHA = "captcha";
	
	//检测网络
	public static final int TA_CHECKNET = 2;
	public static final String TA_CHECKNET_TASKPARAMS = "check_net";
	public static final String TA_CHECKNET_ACTIVITY = "HomePageActivity";
	
	//获取用户信息
	public static final int TA_GETUSERDATA = 3;
	public static final String TA_GETUSERDATA_TASKPARAMS = "getuserdata";
	public static final String TA_GETUSERDATA_ACTIVITY = "activity";
	public static final String TA_GETUSERDATA_ACTIVITY_PERSONDATA = "PersonDataActivity";
	public static final String TA_GETUSERDATA_ACTIVITY_ME = "MeFragment";
	
	//获取任务信息
	public static final int TA_GETSTATUS = 4;
	public static final String TA_GETSTATUS_MODE = "getstatus";
	public static final String TA_GETSTATUS_MODE_FIRSTTIME = "firsttime";
	public static final String TA_GETSTATUS_MODE_PULLREFRESH = "pullrefresh";
	public static final String TA_GETSTATUS_MODE_LOADMORE = "loadmore";
	public static final String TA_GETSTATUS_STATUSID = "statusid";
	public static final String TA_GETSTATUS_FRAGMENT = "TaskFragment";
	
	//发布任务
	public static final int TA_RELEASE = 5;
	public static final String TA_RELEASE_TASKPARAMS = "release";
	public static final String TA_RELEASE_ACTIVITY = "WriteActivity";
	
	//用户注销
	public static final int TA_USEREXIT = 6;
	public static final String TA_USEREXIT_TASKPARAMS = "userexit";
	public static final String TA_USEREXIT_ACTIVITY_HOMEPAGE = "HomePageActivity";
	public static final String TA_USEREXIT_ACTIVITY_SETTING = "SettingActivity";
	public static final String TA_USEREXIT_OK = "userexit ok";
	
	//更新用户资料
	public static final int TA_UPDATAUSERDATA = 7;
	public static final String TA_UPDATAUSERDATA_URL = "updatauserdata";
	public static final String TA_UPDATAUSERDATA_ACTIVITY = "activity";
	public static final String TA_UPDATAUSERDATA_ACTIVITY_COMPLETE = "CompleteUserdataActivity";
	public static final String TA_UPDATAUSERDATA_FRAGMENT_ME = "MeFragment";
	public static final String TA_UPDATAUSERDATA_ACTIVITY_UPDATAUSERDATABASE = "UpdataUserdataBaseActivity";
	public static final String TA_UPDATAUSERDATA_ACTIVITY_UPDATAUSERDATAOPTIONAL = "UpdataUserdataOptionalActivity";
	
	//检查任务
	public static final int TA_CHECKSTATUS = 8;
	public static final String TA_CHECKSTATUS_ACTIVITY = "TaskDetailActivity";
	
	//获取留言
	public static final int TA_GETMESSAGE = 9;
	public static final String TA_GETMESSAGE_ACTIVITY = "TaskDetailActivity";
	
	//发布留言
	public static final int TA_MESSAGE = 10;
	public static final String TA_MESSAGE_ACTIVITY = "TaskDetailActivity";
	public static final String TA_MESSAGE_MODE = "signup_mode";
	public static final String TA_MESSAGE_MODE_ROOT = "signup_mode_root";
	public static final String TA_MESSAGE_MODE_CHILD = "signup_mode_child";
	
	public static final int TA_SIGNUP = 11;
	public static final String TA_SIGNUP_ACTIVITY = "SignupActivity";
	public static final String TA_SIGNUP_CONTACT = "contact";
	public static final String TA_SIGNUP_MESSAGE = "message";
	
	public static final int TA_GETRANK = 12;
	public static final String TA_GETRANK_ACTIVITY = "RankFragment";
	public static final String TA_GETRANK_MODE = "getrank_mode";
	public static final String TA_GETRANK_MODE_OVERALL = "1";
	public static final String TA_GETRANK_MODE_MONTH = "2";
	
	public static final int TA_CHECKUSER = 13;
	public static final String TA_CHECKUSER_ACTIVITY = "LoginActivity";
	
	public static final int TA_GETCAPTCHA = 14;
	public static final String TA_GETCAPTCHA_ACTIVITY = "LoginActivity";
	public static final String TA_GETCAPTCHA_CAPTCHAURL = "captchaName";
	
	public static final int TA_UPLOADPHOTO = 15;
	public static final String TA_UPLOADPHOTO_ACTIVITY = "activity";
	public static final String TA_UPLOADPHOTO_ACTIVITY_COMPLETE = "CompleteUserdataActivity";
	public static final String TA_UPLOADPHOTO_ACTIVITY_ME = "MeFragment";
	public static final String TA_UPLOADPHOTO_BITMAP = "bitmap";
	
	public Task(int taskId, Map<String, Object> taskParams) {
		super();
		this.taskId = taskId;
		this.taskParams = taskParams;
	}
	

	public int getTaskId() {
		return taskId;
	}
	

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}


	public Map<String, Object> getTaskParams() {
		return taskParams;
	}


	public void setTaskParams(Map<String, Object> taskParams) {
		this.taskParams = taskParams;
	}
	
}
