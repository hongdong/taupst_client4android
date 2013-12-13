package com.example.taupstairs.bean;

import java.util.Map;

public class Task {

	//任务ID
	private int taskId;
	//任务参数
	private Map<String, Object> taskParams;
	
	//登录任务用到的常量值
	public static final int TA_LOGIN = 1;
	public static final String TA_LOGIN_TASKPARAMS = "login";
	public static final String TA_LOGIN_ACTIVITY = "LoginActivity";
	public static final String TA_LOGIN_TRUE = "true";
	public static final String TA_LOGIN_FALSE = "false";
	public static final String TA_LOGIN_SUCCESS = "login success";
	public static final String TA_LOGIN_ERROR = "login error";
	
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
