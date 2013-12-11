package com.example.taupstairs.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.example.taupstairs.bean.Task;
import com.example.taupstairs.ui.ItaActivity;

public class MainService extends Service implements Runnable {

	//主服务是否运行
	private boolean isRun;
	//任务队列
	private static Queue<Task> tasks = new LinkedList<Task>();
	//Activity链表
	private static ArrayList<Activity> activities = new ArrayList<Activity>();
	
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Task.TA_LOGIN:
				ItaActivity activity = (ItaActivity) getActivityByName(Task.TA_LOGIN_ACTIVITY);
				activity.refresh(Task.TA_LOGIN_SUCCESS);
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
			System.out.println("doTask ----->> login");
			break;

		default:
			break;
		}
		handler.sendMessage(msg);
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

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
