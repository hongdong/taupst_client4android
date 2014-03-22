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
	public static final String TA_ACTIVITY = "activity";
	public static final String TA_FRAGMENT = "fragment";
	
	
	//登录任务用到的常量值
	public static final int TA_LOGIN = 1;
	public static final String TA_LOGIN_TASKPARAMS = "login";
	public static final String TA_LOGIN_ACTIVITY = "LoginActivity";
	public static final String TA_LOGIN_CAPTCHA = "captcha";
	public static final String TA_LOGIN_ISEXIST = "isExist";
	
	//检测网络
	public static final int TA_CHECKNET = 2;
	public static final String TA_CHECKNET_TASKPARAMS = "check_net";
	public static final String TA_CHECKNET_ACTIVITY = "HomePageActivity";
	
	//获取用户信息
	public static final int TA_GETUSERDATA = 3;
	public static final String TA_GETUSERDATA_TASKPARAMS = "getuserdata";
	public static final String TA_GETUSERDATA_ACTIVITY = "activity";
	public static final String TA_GETUSERDATA_ACTIVITY_PERSONDATA = "PersonDataActivity";
	public static final String TA_GETUSERDATA_ACTIVITY_ME = "MeActivity";
	
	//获取任务信息
	public static final int TA_GETSTATUS = 4;
	public static final String TA_GETSTATUS_TYPE = "getstatus_type";
	public static final int TA_GETSTATUS_TYPE_ALL = 1;
	public static final int TA_GETSTATUS_TYPE_MY_RELEASE = 2;
	public static final int TA_GETSTATUS_TYPE_MY_SIGNUP = 3;
	public static final String TA_GETSTATUS_MODE = "getstatus_mode";
	public static final int TA_GETSTATUS_MODE_FIRSTTIME = 1;
	public static final int TA_GETSTATUS_MODE_LOADMORE = 3;
	public static final String TA_GETSTATUS_ACTIVITY = "activity";
	public static final String TA_GETSTATUS_ACTIVITY_TASK = "TaskActivity";
	public static final String TA_GETSTATUS_MYRELEASESTATUS = "MyReleaseStatusActivity";
	public static final String TA_GETSTATUS_MYSIGNUPSTATUS = "MySignUpStatusActivity";
	
	//发布任务
	public static final int TA_RELEASE = 5;
	public static final String TA_RELEASE_TASKPARAMS = "release";
	public static final String TA_RELEASE_ACTIVITY = "WriteActivity";
	
	//用户注销
	public static final int TA_USEREXIT = 6;
	public static final String TA_USEREXIT_TYPE = "type";
	public static final int TA_USEREXIT_TYPE_CHANGE = 0;
	public static final int TA_USEREXIT_TYPE_NORMAL = 1;
	public static final String TA_USEREXIT_TASKPARAMS = "userexit";
	public static final String TA_USEREXIT_ACTIVITY_HOMEPAGE = "HomePageActivity";
	public static final String TA_USEREXIT_ACTIVITY_SETTING = "SettingActivity";
	public static final String TA_USEREXIT_OK = "userexit ok";
	
	//更新用户资料
	public static final int TA_UPDATAUSERDATA = 7;
	public static final String TA_UPDATAUSERDATA_URL = "updatauserdata";
	public static final String TA_UPDATAUSERDATA_ACTIVITY = "activity";
	public static final String TA_UPDATAUSERDATA_ACTIVITY_COMPLETE = "CompleteUserdataActivity";
	public static final String TA_UPDATAUSERDATA_FRAGMENT_ME = "MeActivity";
	public static final String TA_UPDATAUSERDATA_ACTIVITY_BASE = "UpdataUserdataBaseActivity";
	public static final String TA_UPDATAUSERDATA_ACTIVITY_OPTIONAL = "UpdataUserdataOptionalActivity";
	public static final String TA_UPDATAUSERDATA_ACTIVITY_REAL = "UpdataUserdataRealActivity";
	
	//检查任务
	public static final int TA_CHECKSTATUS = 8;
	public static final String TA_CHECKSTATUS_ACTIVITY = "TaskDetailActivity";
	
	//获取留言
	public static final int TA_GETMESSAGE = 9;
	public static final String TA_GETMESSAGE_ACTIVITY = "activity";
	public static final String TA_GETMESSAGE_ACTIVITY_DETAIL = "TaskDetailActivity";
	public static final String TA_GETMESSAGE_ACTIVITY_BYID = "TaskByIdActivity";
	
	//发布留言
	public static final int TA_MESSAGE = 10;
	public static final String TA_MESSAGE_ACTIVITY = "activity";
	public static final String TA_MESSAGE_ACTIVITY_TASK = "TaskDetailActivity";
	public static final String TA_MESSAGE_ACTIVITY_INFO = "InfoMessageActivity";
	public static final String TA_MESSAGE_ACTIVITY_BYID = "TaskByIdActivity";
	public static final String TA_MESSAGE_MODE = "signup_mode";
	public static final String TA_MESSAGE_MODE_ROOT = "signup_mode_root";
	public static final String TA_MESSAGE_MODE_CHILD = "signup_mode_child";
	
	public static final int TA_SIGNUP = 11;
	public static final String TA_SIGNUP_ACTIVITY = "SignupActivity";
	public static final String TA_SIGNUP_CONTACT = "contact";
	public static final String TA_SIGNUP_MESSAGE = "message";
	
	public static final int TA_GETRANK = 12;
	public static final String TA_GETRANK_ACTIVITY = "RankActivity";
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
	public static final String TA_UPLOADPHOTO_ACTIVITY_ME = "MeActivity";
	public static final String TA_UPLOADPHOTO_BITMAP = "bitmap";
	
	//获取任务信息
	public static final int TA_GETINFO = 16;
	public static final String TA_GETINFO_MODE = "getinfo";
	public static final int TA_GETINFO_MODE_FIRSTTIME = 1;
	public static final int TA_GETINFO_MODE_PULLREFRESH = 2;
	public static final int TA_GETINFO_MODE_LOADMORE = 3;
	public static final String TA_GETINFO_INFOID = "infoid";
	public static final String TA_GETINFO_ACTIVITY = "InfoActivity";
	
	public static final int TA_GETINFO_DETAIL = 17;
	public static final String TA_GETINFO_DETAIL_ACTIVITY = "activity";
	public static final String TA_GETINFO_DETAIL_MESSAGE = "InfoMessageActivity";
	public static final String TA_GETINFO_DETAIL_EXECTASK = "InfoExecTaskActivity";
	public static final String TA_GETINFO_DETAIL_ENDTASK = "InfoEndTaskActivity";
	public static final String TA_GETINFO_DETAIL_SIGNUP = "InfoSignUpActivity";
	public static final String TA_GETINFO_DETAIL_PRIVATELETTER = "InfoPrivateLetterActivity";
	
	public static final int TA_EXEC_TASK = 18;
	public static final String TA_EXEC_TASK_ACTIVITY = "InfoSignUpExecActivity";
	
	public static final int TA_GET_TASK_DETAIL = 19;
	public static final String TA_GET_TASK_DETAIL_ACTIVITY = "TaskByIdActivity";
	
	public static final int TA_END_TASK = 20;
	public static final String TA_END_TASK_ACTIVITY = "SignUpListActivity";
	
	public static final int TA_PUSH = 21;
	public static final String TA_PUSH_CHANNEL_ID = "channel_id";
	public static final String TA_PUSH_USER_ID= "user_id";
	
	public static final int TA_GET_SIGNUP_LIST = 22;
	public static final String TA_GET_SIGNUP_LIST_ACTIVITY = "SignUpListActivity";
	
	public static final int TA_EVALUATE = 23;
	public static final String TA_EVALUATE_ACTIVITY = "EvaluateActivity";
	
	public static final int TA_FEEDBACK = 24;
	public static final String TA_FEEDBACK_STRING = "feedback_string";
	public static final String TA_FEEDBACK_ACTIVITY = "FeedbackActivity";
	
	public static final int TA_CHECKUPDATA = 25;
	public static final String TA_CHECKUPDATA_ACTIVITY = "HomePageActivity";
	
	public static final int TA_PRIVATE_LETTER = 26;
	public static final String TA_PRIVATE_LETTER_STRING = "private_letter_string";
	public static final String TA_PRIVATE_LETTER_ACTIVITY = "SendPrivateLetterActivity";
	
	public static final int TA_SIGNUP_INFODETAIL = 27;
	public static final String TA_SIGNUP_INFODETAIL_ACTIVITY = "SignUpListInfoDetailActivity";
	
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
