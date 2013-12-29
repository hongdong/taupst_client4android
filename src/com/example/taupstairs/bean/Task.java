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
	
	//检测网络
	public static final int TA_CHECKNET = 2;
	public static final String TA_CHECKNET_TASKPARAMS = "check_net";
	public static final String TA_CHECKNET_ACTIVITY = "HomePageActivity";
	
	//获取用户信息
	public static final int TA_GETUSERDATA = 3;
	public static final String TA_GETUSERDATA_TASKPARAMS = "getuserdata";
	public static final String TA_GETUSERDATA_FRAGMENT = "MeFragment";
	
	//获取用户信息
	public static final int TA_GETSTATUS = 4;
	public static final String TA_GETSTATUS_TASKPARAMS = "getstatus";
	public static final String TA_GETSTATUS_FRAGMENT = "TaskFragment";
	
	
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
