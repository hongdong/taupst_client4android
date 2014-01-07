package com.example.taupstairs.util;

import com.example.taupstairs.bean.Status;
import com.example.taupstairs.bean.User;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtil {
	
	private static final String DEFAULT_USER = "default_user";
	private static final String LASTEST_STATUSID = "lastest_statusid";
	

	/*存储默认账户（如果先前存储过默认账户，则会覆盖）*/
	public static void saveDefaultUser(Context context, User user) {
		SharedPreferences sp = context.getSharedPreferences(DEFAULT_USER, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(User.USER_ID, user.getUserId());
		editor.putString(User.USER_COLLEGEID, user.getUserCollegeId());
		editor.putString(User.USER_STUDENTID, user.getUserStudentId());
		editor.putString(User.USER_PASSWORD, user.getUserPassword());
		editor.commit();
	}
	
	/*从存储器里面找出默认账户（用于登录）*/
	public static User getDefaultUser(Context context) {
		SharedPreferences sp = context.getSharedPreferences(DEFAULT_USER, Context.MODE_PRIVATE);
		String userId = sp.getString(User.USER_ID, null);
		if (null == userId) {
			return null;		//如果先前没有存储默认账户的话，就返回null
		}
		String userCollegeId = sp.getString(User.USER_COLLEGEID, null);
		String userStudentId = sp.getString(User.USER_STUDENTID, null);
		String userPassword = sp.getString(User.USER_PASSWORD, null);
		return new User(userId, userCollegeId, userStudentId, userPassword);
	}
	
	/*
	 * 储存最新任务ID
	 */
	public static void savaLastestStatusId(Context context, String lastestStatusId) {
		SharedPreferences sp = context.getSharedPreferences(LASTEST_STATUSID, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(Status.STATUS_ID, lastestStatusId);
		editor.commit();
	}
	
	/*
	 * 读取最新任务ID
	 */
	public static String getLastestStatusId(Context context) {
		SharedPreferences sp = context.getSharedPreferences(LASTEST_STATUSID, Context.MODE_PRIVATE);
		return sp.getString(Status.STATUS_ID, null);
	}
	
}
