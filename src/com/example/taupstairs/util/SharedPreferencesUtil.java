package com.example.taupstairs.util;

import com.example.taupstairs.bean.User;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtil {
	
	private static final String DEFAULT_USER = "default_user";

	/*存储默认账户（如果先前存储过默认账户，则会覆盖）*/
	public static void saveDefaultUser(Context context, User user) {
		SharedPreferences sp = context.getSharedPreferences(DEFAULT_USER, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(User.USER_COLLEGEID, user.getUserCollegeId());
		editor.putString(User.USER_STUDENTID, user.getUserStudentId());
		editor.putString(User.USER_PASSWORD, user.getUserPassword());
		editor.commit();
	}
	
	/*从存储器里面找出默认账户*/
	public static User getDefaultUser(Context context) {
		SharedPreferences sp = context.getSharedPreferences(DEFAULT_USER, Context.MODE_PRIVATE);
		String userCollegeId = sp.getString(User.USER_COLLEGEID, null);
		if (null == userCollegeId) {
			return null;		//如果先前没有存储默认账户的话，就返回null
		}
		String userStudentId = sp.getString(User.USER_STUDENTID, null);
		String userPassword = sp.getString(User.USER_PASSWORD, null);
		return new User(userCollegeId, userStudentId, userPassword);
	}
}
