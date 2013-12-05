package com.example.taupstairs.bean;

import java.util.Map;

public class Task {

	//����ID
	private int taskId;
	//�������
	private Map<String, Object> taskParams;
	
	//��¼
	public static final int TA_LOGIN = 1;
	public static final String TA_LOGIN_ACTIVITY = "LoginActivity";
	public static final String TA_LOGIN_SUCCESS = "��¼�ɹ�";
	
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
